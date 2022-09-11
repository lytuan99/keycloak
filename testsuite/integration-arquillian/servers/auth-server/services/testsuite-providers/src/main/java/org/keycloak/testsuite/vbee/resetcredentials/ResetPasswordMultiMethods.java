package org.keycloak.testsuite.vbee.resetcredentials;

import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.authentication.actiontoken.DefaultActionTokenKey;
import org.keycloak.authentication.actiontoken.resetcred.ResetCredentialsActionToken;
import org.keycloak.authentication.authenticators.browser.AbstractUsernameFormAuthenticator;
import org.keycloak.common.util.Time;
import org.keycloak.email.EmailException;
import org.keycloak.email.EmailTemplateProvider;
import org.keycloak.events.Details;
import org.keycloak.events.Errors;
import org.keycloak.events.EventBuilder;
import org.keycloak.events.EventType;
import org.keycloak.models.*;
import org.keycloak.models.utils.FormMessage;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.services.ServicesLogger;
import org.keycloak.services.messages.Messages;
import org.keycloak.sessions.AuthenticationSessionCompoundId;
import org.keycloak.sessions.AuthenticationSessionModel;
import org.keycloak.testsuite.vbee.models.OtpModel;
import org.keycloak.testsuite.vbee.utils.Utils;
import org.keycloak.theme.Theme;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ResetPasswordMultiMethods implements Authenticator, AuthenticatorFactory {
    private static final Logger logger = Logger.getLogger(ResetPasswordMultiMethods.class);

    public static final String PROVIDER_ID = "vbee-otp-or-magic-link";
    public static final String OTP_CODE_TPL = "otp-code.ftl";
    public static final String EMAIL_RESET_PASSWORD_CODE_TPL = "email-verification-with-code.ftl";
    public static final String PHONE_NUMBER_FIELD = "phoneNumber";

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        UserModel user = context.getUser();
        AuthenticationSessionModel authenticationSession = context.getAuthenticationSession();

        //When user clicks the link that be sent to email of user
        String actionTokenUserId = authenticationSession.getAuthNote(DefaultActionTokenKey.ACTION_TOKEN_USER_ID);
        if (actionTokenUserId != null && Objects.equals(user.getId(), actionTokenUserId)) {
            logger.debugf("Forget-password triggered when reauthenticating user after authentication via action token. Skipping " + PROVIDER_ID + " screen and using user '%s' ", user.getUsername());
            context.success();
            return;
        }

        String method = context.getAuthenticationSession().getAuthNote(ResetPasswordMethod.METHOD);
        switch (method) {
            case ResetPasswordMethod.PHONE_OTP:
            case ResetPasswordMethod.EMAIL_OTP:
                authenticateWithOTP(context);
                break;
            case ResetPasswordMethod.MAGIC_LINK:
                authenticateWithMagicLink(context);
                break;
            default:
                break;
        }
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        String method = context.getAuthenticationSession().getAuthNote(ResetPasswordMethod.METHOD);
        switch (method) {
            case ResetPasswordMethod.PHONE_OTP:
            case ResetPasswordMethod.EMAIL_OTP:
                actionWithOTP(context);
                break;
            case ResetPasswordMethod.MAGIC_LINK:
                actionWithMagicLink(context);
                break;
            default:
                break;
        }
    }

    private void sendOtpCodeThroughMobilePhone(AuthenticationFlowContext context, String code, int ttl) throws IOException {
        KeycloakSession session = context.getSession();
        UserModel user = context.getUser();
        Theme theme = session.theme().getTheme(Theme.Type.LOGIN);
        Locale locale = session.getContext().resolveLocale(user);
        String smsAuthText = theme.getMessages(locale).getProperty("smsAuthText");
        String smsText = String.format(smsAuthText, code, Math.floorDiv(ttl, 60));
        String phoneNumber = user.getFirstAttribute(PHONE_NUMBER_FIELD);
        logger.info("OTP with phone number: " + smsText);

//        AuthenticatorConfigModel config = context.getAuthenticatorConfig();
//        if (config == null) {
//            logger.error("OTP config is null");
//            throw new IOException();
//        }
//        String urlCallCenter = config.getConfig().get(OtpConfig.URL);
//        String accessToken = config.getConfig().get(OtpConfig.ACCESS_TOKEN);
//        if (urlCallCenter == null || accessToken == null){
//            logger.error("OTP config invalid: url and accessToken is required");
//            throw new IOException();
//        }
//        VoiceOtpServiceFactory.get().send(urlCallCenter, accessToken, phoneNumber, smsText);
    }


    private void sendOtpCodeThroughEmail(AuthenticationFlowContext context, String code, int ttl) throws EmailException {
        KeycloakSession session = context.getSession();
        UserModel user = context.getUser();
        AuthenticationSessionModel authSession = context.getAuthenticationSession();
        EventBuilder event = context.getEvent().clone().event(EventType.SEND_RESET_PASSWORD).detail(Details.EMAIL, user.getEmail());

        RealmModel realm = session.getContext().getRealm();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("code", code);
        attributes.put("user", user);
        attributes.put("realmName", realm.getName());
        attributes.put("ttl", Math.floorDiv(ttl, 60));
        try {
            session
                .getProvider(EmailTemplateProvider.class)
                .setAuthenticationSession(authSession)
                .setRealm(realm)
                .setUser(user)
                .send("passwordResetSubject", EMAIL_RESET_PASSWORD_CODE_TPL, attributes);
            event.success();
        } catch(EmailException e) {
            event.error(Errors.EMAIL_SEND_FAILED);
            logger.error("Failed to send otp email", e);
            Response challenge = context.form()
                    .setError(Messages.EMAIL_SENT_ERROR)
                    .createErrorPage(Response.Status.INTERNAL_SERVER_ERROR);
            context.failure(AuthenticationFlowError.INTERNAL_ERROR, challenge);
        }
    }

    private void challengeOtpForm(AuthenticationFlowContext context) {
        AuthenticationSessionModel authSession = context.getAuthenticationSession();
        String resetCredentialMethod = authSession.getAuthNote(ResetPasswordMethod.METHOD);
        String targetUsername = authSession.getAuthNote(ResetPasswordMethod.TARGET_USERNAME_INFO);
        String oldResendDuration = authSession.getAuthNote(ResetPasswordMethod.RESEND_DURATION);

        try {
            OtpModel otpModel = new OtpModel(resetCredentialMethod, targetUsername, oldResendDuration);
            context.challenge(context.form().setAttribute(ResetPasswordMethod.OTP_MODEL, otpModel).createForm(OTP_CODE_TPL));
        } catch (Exception e) {
            context.failureChallenge(AuthenticationFlowError.INTERNAL_ERROR,
                    context.form().setError(Messages.INTERNAL_SERVER_ERROR, e.getMessage())
                            .createErrorPage(Response.Status.INTERNAL_SERVER_ERROR));
        }
    }

    private void authenticateWithOTP(AuthenticationFlowContext context) {
        AuthenticationSessionModel authSession = context.getAuthenticationSession();
        String oldResendDuration = authSession.getAuthNote(ResetPasswordMethod.RESEND_DURATION);

        // Check spam send OTP
        if (oldResendDuration != null && Long.parseLong(oldResendDuration) > System.currentTimeMillis()) {
            challengeOtpForm(context);
            return;
        }

        int length = OtpConfig.DEFAULT_OTP_CODE_LENGTH;
        int ttl = OtpConfig.DEFAULT_OTP_CODE_TTL;
        int resendDuration = OtpConfig.DEFAULT_OTP_CODE_RESEND_DURATION;
        AuthenticatorConfigModel config = context.getAuthenticatorConfig();
        if (config != null) {
            length = Integer.parseInt(config.getConfig().get(OtpConfig.LENGTH));
            ttl = Integer.parseInt(config.getConfig().get(OtpConfig.TTL));
            resendDuration = Integer.parseInt(config.getConfig().get(OtpConfig.RESEND_DURATION));
        }

        String code = Utils.randomOtpCode(length);

        authSession.setAuthNote(ResetPasswordMethod.CODE, code);
        authSession.setAuthNote(ResetPasswordMethod.TTL, Long.toString(System.currentTimeMillis() + (ttl * 1000L)));
        authSession.setAuthNote(ResetPasswordMethod.RESEND_DURATION, Long.toString(System.currentTimeMillis() + (resendDuration * 1000L)));

        try {
            String resetCredentialMethod = authSession.getAuthNote(ResetPasswordMethod.METHOD);
            if(resetCredentialMethod.equals(ResetPasswordMethod.EMAIL_OTP))
                sendOtpCodeThroughEmail(context, code, ttl);
            else
                sendOtpCodeThroughMobilePhone(context, code, ttl);
        } catch (EmailException | IOException e) {
            context.failureChallenge(AuthenticationFlowError.INTERNAL_ERROR,
                    context.form().setError("smsAuthSmsNotSent", e.getMessage())
                            .createErrorPage(Response.Status.INTERNAL_SERVER_ERROR));
            return;
        }

        challengeOtpForm(context);
    }

    private void actionWithOTP(AuthenticationFlowContext context) {
        String enteredCode = context.getHttpRequest().getDecodedFormParameters().getFirst("code");
        AuthenticationSessionModel authSession = context.getAuthenticationSession();
        String code = authSession.getAuthNote(ResetPasswordMethod.CODE);
        String ttl = authSession.getAuthNote(ResetPasswordMethod.TTL);
        String resetPasswordMethod = authSession.getAuthNote(ResetPasswordMethod.METHOD);
        String targetUsername = authSession.getAuthNote(ResetPasswordMethod.TARGET_USERNAME_INFO);
        String oldResendDuration = authSession.getAuthNote(ResetPasswordMethod.RESEND_DURATION);

        if (code == null || ttl == null) {
            context.failureChallenge(AuthenticationFlowError.INTERNAL_ERROR,
                    context.form().createErrorPage(Response.Status.INTERNAL_SERVER_ERROR));
            return;
        }

        boolean isValid = enteredCode.equals(code);
        if (isValid) {
            if (Long.parseLong(ttl) < System.currentTimeMillis()) {
                // expired
                context.failureChallenge(AuthenticationFlowError.EXPIRED_CODE,
                        context.form().setError("smsAuthCodeExpired").createErrorPage(Response.Status.BAD_REQUEST));
            } else {
                // valid
                context.success();
            }
        } else {
            // invalid
            AuthenticationExecutionModel execution = context.getExecution();
            if (execution.isRequired()) {
                OtpModel otpModel = new OtpModel(resetPasswordMethod, targetUsername, oldResendDuration);
                context.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS,
                        context.form().setAttribute(ResetPasswordMethod.OTP_MODEL, otpModel)
                                .setError("smsAuthCodeInvalid").createForm(OTP_CODE_TPL));
            } else if (execution.isConditional() || execution.isAlternative()) {
                context.attempted();
            }
        }
    }

    private void authenticateWithMagicLink(AuthenticationFlowContext context) {
        UserModel user = context.getUser();
        AuthenticationSessionModel authenticationSession = context.getAuthenticationSession();
        String username = authenticationSession.getAuthNote(AbstractUsernameFormAuthenticator.ATTEMPTED_USERNAME);

        // we don't want people guessing usernames, so if there was a problem obtaining the user, the user will be null.
        // just reset login for with a success message
        if (user == null) {
            context.forkWithSuccessMessage(new FormMessage(Messages.EMAIL_SENT));
            return;
        }

        String actionTokenUserId = authenticationSession.getAuthNote(DefaultActionTokenKey.ACTION_TOKEN_USER_ID);
        if (actionTokenUserId != null && Objects.equals(user.getId(), actionTokenUserId)) {
            logger.debugf("Forget-password triggered when reauthenticating user after authentication via action token. Skipping " + PROVIDER_ID + " screen and using user '%s' ", user.getUsername());
            context.success();
            return;
        }

        EventBuilder event = context.getEvent();
        if (user.getEmail() == null || user.getEmail().trim().length() == 0) {
            event.user(user)
                    .detail(Details.USERNAME, username)
                    .error(Errors.INVALID_EMAIL);

            context.forkWithSuccessMessage(new FormMessage(Messages.EMAIL_SENT));
            return;
        }

        int validityInSecs = context.getRealm().getActionTokenGeneratedByUserLifespan(ResetCredentialsActionToken.TOKEN_TYPE);
        int absoluteExpirationInSecs = Time.currentTime() + validityInSecs;

        // We send the secret in the email in a link as a query param.
        String authSessionEncodedId = AuthenticationSessionCompoundId.fromAuthSession(authenticationSession).getEncodedId();
        ResetCredentialsActionToken token = new ResetCredentialsActionToken(user.getId(), user.getEmail(), absoluteExpirationInSecs, authSessionEncodedId, authenticationSession.getClient().getClientId());
        String link = UriBuilder
                .fromUri(context.getActionTokenUrl(token.serialize(context.getSession(), context.getRealm(), context.getUriInfo())))
                .build()
                .toString();
        long expirationInMinutes = TimeUnit.SECONDS.toMinutes(validityInSecs);
        try {
            context.getSession().getProvider(EmailTemplateProvider.class).setRealm(context.getRealm()).setUser(user).setAuthenticationSession(authenticationSession).sendPasswordReset(link, expirationInMinutes);

            event.clone().event(EventType.SEND_RESET_PASSWORD)
                    .user(user)
                    .detail(Details.USERNAME, username)
                    .detail(Details.EMAIL, user.getEmail()).detail(Details.CODE_ID, authenticationSession.getParentSession().getId()).success();
            context.forkWithSuccessMessage(new FormMessage(Messages.EMAIL_SENT));
        } catch (EmailException e) {
            event.clone().event(EventType.SEND_RESET_PASSWORD)
                    .detail(Details.USERNAME, username)
                    .user(user)
                    .error(Errors.EMAIL_SEND_FAILED);
            ServicesLogger.LOGGER.failedToSendPwdResetEmail(e);
            Response challenge = context.form()
                    .setError(Messages.EMAIL_SENT_ERROR)
                    .createErrorPage(Response.Status.INTERNAL_SERVER_ERROR);
            context.failure(AuthenticationFlowError.INTERNAL_ERROR, challenge);
        }
    }

    private void actionWithMagicLink(AuthenticationFlowContext context) {
        context.getUser().setEmailVerified(true);
        context.success();
    }

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {

    }

    @Override
    public String getDisplayType() {
        return "Vbee Reset Password With OTP";
    }

    @Override
    public String getReferenceCategory() {
        return null;
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    public static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED
    };

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return "Execute sending OTP through phone number or Magic link through email";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return Arrays.asList(
                new ProviderConfigProperty(OtpConfig.LENGTH, "Code length", "The number of digits of the generated code.", ProviderConfigProperty.STRING_TYPE, OtpConfig.DEFAULT_OTP_CODE_LENGTH),
                new ProviderConfigProperty(OtpConfig.TTL, "Time-to-live", "The time to live in seconds for the code to be valid.", ProviderConfigProperty.STRING_TYPE, OtpConfig.DEFAULT_OTP_CODE_TTL),
                new ProviderConfigProperty(OtpConfig.RESEND_DURATION, "Resend Duration", "The limit duration to user can request to system after seconds to get new code.", ProviderConfigProperty.STRING_TYPE, OtpConfig.DEFAULT_OTP_CODE_RESEND_DURATION),
                new ProviderConfigProperty(OtpConfig.URL, "Call center server url", "The url to call the center server.", ProviderConfigProperty.STRING_TYPE, ""),
                new ProviderConfigProperty(OtpConfig.ACCESS_TOKEN, "Access token", "The access token used to access the call center server.", ProviderConfigProperty.STRING_TYPE, "")
        );
    }

    @Override
    public void close() {

    }

    @Override
    public Authenticator create(KeycloakSession session) {
        return this;
    }

    @Override
    public void init(Config.Scope config) {

    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}
