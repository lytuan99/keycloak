package org.keycloak.testsuite.vbee.resetcredentials;

import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.authentication.actiontoken.DefaultActionTokenKey;
import org.keycloak.authentication.authenticators.broker.AbstractIdpAuthenticator;
import org.keycloak.events.Details;
import org.keycloak.events.Errors;
import org.keycloak.events.EventBuilder;
import org.keycloak.models.*;
import org.keycloak.models.utils.FormMessage;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.services.messages.Messages;
import org.keycloak.services.validation.Validation;
import org.keycloak.testsuite.vbee.models.UserSyncRequest;
import org.keycloak.testsuite.vbee.service.SyncV3Service;
import org.keycloak.testsuite.vbee.utils.UserUtils;
import org.keycloak.testsuite.vbee.utils.Utils;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.List;

public class ResetPasswordChooseUser implements Authenticator, AuthenticatorFactory {
    private static final Logger logger = Logger.getLogger(ResetPasswordChooseUser.class);

    public static final String PROVIDER_ID = "vbee-reset-credentials-choose-user";

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        String existingUserId = context.getAuthenticationSession().getAuthNote(AbstractIdpAuthenticator.EXISTING_USER_INFO);
        if (existingUserId != null) {
            UserModel existingUser = AbstractIdpAuthenticator.getExistingUser(context.getSession(), context.getRealm(), context.getAuthenticationSession());

            logger.debugf("Forget-password triggered when reauthenticating user after first broker login. Prefilling reset-credential-choose-user screen with user '%s' ", existingUser.getUsername());
            context.setUser(existingUser);
            Response challenge = context.form().createPasswordReset();
            context.challenge(challenge);
            return;
        }

        String actionTokenUserId = context.getAuthenticationSession().getAuthNote(DefaultActionTokenKey.ACTION_TOKEN_USER_ID);
        if (actionTokenUserId != null) {
            UserModel existingUser = context.getSession().users().getUserById(context.getRealm(), actionTokenUserId);

            // Action token logics handles checks for user ID validity and user being enabled
            logger.debugf("Forget-password triggered when reauthenticating user after authentication via action token. Skipping reset-credential-choose-user screen and using user '%s' ", existingUser.getUsername());
            context.setUser(existingUser);
            context.success();
            return;
        }

        Response challenge = context.form().createPasswordReset();
        context.challenge(challenge);
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        EventBuilder event = context.getEvent();
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        String username = formData.getFirst("username");

        if (username == null || username.isEmpty()) {
            event.error(Errors.USERNAME_MISSING);
            Response challenge = context.form()
                    .addError(new FormMessage(Validation.FIELD_USERNAME, Messages.MISSING_USERNAME))
                    .createPasswordReset();
            context.failureChallenge(AuthenticationFlowError.INVALID_USER, challenge);
            return;
        }
        username = username.trim();
        RealmModel realm = context.getRealm();
        UserModel user = null;

        // Validate Reset Credential Method and find user

        if (username.contains("@")) {
            user =  context.getSession().users().getUserByEmail(realm, username);
            context.getAuthenticationSession().setAuthNote(ResetPasswordMethod.METHOD, ResetPasswordMethod.EMAIL_OTP);
        } else {
            user = UserUtils.findUserByPhone(context.getSession().users(), realm, username);
            context.getAuthenticationSession().setAuthNote(ResetPasswordMethod.METHOD, ResetPasswordMethod.PHONE_OTP);
        }
//        context.getAuthenticationSession().setAuthNote(AbstractUsernameFormAuthenticator.ATTEMPTED_USERNAME, username);
        if (user == null) {
            // Nếu user không tồn tại thì thực hiện Sync User từ V3
            boolean status = handleSyncV3(context, realm, username);
            if (status) {
                // Sau khi đồng bộ thành công, find User và đưa vào context cho các phase sau sử dụng
                user = findUser(context, realm, username);
            }
            if (!status || user == null) {
                // status false: user không tồn tại hoặc có lỗi xảy ra khi đồng bộ || hoặc status = true nhưng vẫn không đồng bộ được user
                event.clone()
                        .detail(Details.USERNAME, username)
                        .error(Errors.USER_NOT_FOUND);
                Response challenge = context.form()
                        .addError(new FormMessage(Validation.FIELD_USERNAME, "userNotFoundError"))
                        .createPasswordReset();
                context.failureChallenge(AuthenticationFlowError.INVALID_USER, challenge);
                context.clearUser();
                return;
            }
            context.setUser(user);
        } else if (!user.isEnabled()) {
            event.clone()
                    .detail(Details.USERNAME, username)
                    .user(user).error(Errors.USER_DISABLED);
            Response challenge = context.form()
                    .addError(new FormMessage(Validation.FIELD_USERNAME, "disabledUserError"))
                    .createPasswordReset();
            context.failureChallenge(AuthenticationFlowError.USER_DISABLED, challenge);
            context.clearUser();
            return;
        } else {
            context.setUser(user);
            }

        context.getAuthenticationSession().setAuthNote(ResetPasswordMethod.TARGET_USERNAME_INFO, username);

        context.success();
    }

    private UserModel findUser(AuthenticationFlowContext context, RealmModel realm, String username) {
        UserModel user;
        if (Utils.isPhoneNumberValid(username))
            user = UserUtils.findUserByPhone(context.getSession().users(), realm, username);
        else user = context.getSession().users().getUserByEmail(realm, username);
        return user;
    }

    private boolean handleSyncV3(AuthenticationFlowContext context, RealmModel realm, String username) {
        String clientId = context.getAuthenticationSession().getClient().getClientId();
        if (!clientId.equals(SyncV3Service.VBEE_TTS_CLIENT))
            return false;
        UserSyncRequest userSyncRequest = UserSyncRequest.build(realm.getName(), clientId).username(username);
        return SyncV3Service.syncUser(userSyncRequest);
    }

    @Override
    public boolean requiresUser() {
        return false;
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
        return "Vbee Choose User And Reset Credential Method";
    }

    @Override
    public String getReferenceCategory() {
        return null;
    }

    @Override
    public boolean isConfigurable() {
        return false;
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
        return "Choose a user to reset credentials for, Choose a method to reset, it can be OTP through phone number or Magic link through email";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return null;
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
