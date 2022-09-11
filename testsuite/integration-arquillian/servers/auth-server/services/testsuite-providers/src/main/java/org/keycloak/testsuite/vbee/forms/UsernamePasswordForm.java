/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.keycloak.testsuite.vbee.forms;

import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.authenticators.browser.AbstractUsernameFormAuthenticator;
import org.keycloak.credential.CredentialModel;
import org.keycloak.events.Details;
import org.keycloak.events.Errors;
import org.keycloak.events.EventBuilder;
import org.keycloak.events.EventType;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.*;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.protocol.oidc.OIDCLoginProtocol;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.services.ServicesLogger;
import org.keycloak.services.managers.AuthenticationManager;
import org.keycloak.sessions.AuthenticationSessionModel;
import org.keycloak.testsuite.vbee.models.UserSyncRequest;
import org.keycloak.testsuite.vbee.service.SyncV3Service;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.keycloak.services.validation.Validation.FIELD_PASSWORD;
import static org.keycloak.services.validation.Validation.FIELD_USERNAME;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class UsernamePasswordForm extends AbstractUsernameFormAuthenticator implements Authenticator {
    protected static ServicesLogger log = ServicesLogger.LOGGER;


    @Override
    public void action(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        if (formData.containsKey("cancel")) {
            context.cancelLogin();
            return;
        }
        // Handle Vbee LOGIN
        RealmModel realm = context.getRealm();
        String clientId = context.getAuthenticationSession().getClient().getClientId();
        String username = formData.getFirst(AuthenticationManager.FORM_USERNAME);
        String password = formData.getFirst(CredentialRepresentation.PASSWORD);

        if (clientId.equals(SyncV3Service.VBEE_TTS_CLIENT)) {
            if (username == null || username.isEmpty()) {
                Response challengeResponse = challenge(context, getDefaultChallengeMessage(context), FIELD_USERNAME);
                context.failureChallenge(AuthenticationFlowError.INVALID_USER, challengeResponse);
                return;
            }
            if(password == null || password.isEmpty()) {
                Response challengeResponse = challenge(context, getDefaultChallengeMessage(context), FIELD_PASSWORD);
                context.forceChallenge(challengeResponse);
                context.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS, challengeResponse);
                return;
            }
            UserSyncRequest userSyncRequest = UserSyncRequest.build(realm.getName(), clientId).username(username).password(password);

//            SyncV3Service.syncUser(userSyncRequest);

            UserModel user = context.getSession().users().getUserByUsername(realm, username);

            if (user == null)
                user = context.getSession().users().getUserByEmail(realm, username);

            // Nếu User có password thì tiếp tục thực hiện đăng nhập mà không cần check gì cả
            if (user != null && !hasUserHadPassword(context.getSession(), realm, user)) {
                String providerName = findIdentityProviderOfUser(context.getSession(), realm, user);
                if (providerName != null) {
                    Response challengeResponse = challenge(context, "existingIdentityProvider" + providerName, "identityProvider");
                    context.failureChallenge(AuthenticationFlowError.IDENTITY_PROVIDER_ERROR, challengeResponse);
                    return;
                }
                // Nếu không có thì thực hiện check password Vbee V3
                String hashedPasswordV3 = user.getFirstAttribute(SyncV3Service.HASHED_PASSWORD_V3);
                if (hashedPasswordV3 == null) {
                    log.error("Error Login Sync Flow with userId: " + user.getId());
                    Response challenge = context.form()
                            .setError("errorLoginSyncFlow")
                            .createErrorPage(Response.Status.INTERNAL_SERVER_ERROR);
                    context.failure(AuthenticationFlowError.INTERNAL_ERROR, challenge);
                    return;
                }

                // Check password Vbee V3 xong mà KHỚP thì update password đó vào user keycloak, đồng thời xóa bỏ hashed-password-v3
                if (SyncV3Service.verifyV3Password(password, hashedPasswordV3)) {
                    boolean result = createNewPassword(context, realm, user, password);
                    if(!result) return;
                    user.removeAttribute(SyncV3Service.HASHED_PASSWORD_V3);
//
//                    context.success();
//                    return;
                }
            }
        }

        if (!validateForm(context, formData)) {
            return;
        }
        context.success();
    }

    private String findIdentityProviderOfUser(KeycloakSession session, RealmModel realm, UserModel user) {
        Stream<FederatedIdentityModel> providers = session.users().getFederatedIdentitiesStream(realm, user);
        List<FederatedIdentityModel> userList = providers.collect(Collectors.toList());
        if (userList.size() == 0)
            return null;
        if (userList.size() == 1)
            return userList.get(0).getIdentityProvider();

        String provider1 = userList.get(0).getIdentityProvider();
        String provider2 = userList.get(1).getIdentityProvider();
        return provider1 + "-" + provider2;
    }

    /**
     * Kiểm tra user trên keycloak đã có password hay chưa
     * Có trả về true, không có trả về false
     */
    private boolean hasUserHadPassword(KeycloakSession session, RealmModel realm, UserModel user) {
        Stream<CredentialModel> credentialModelStream = session.userCredentialManager().getStoredCredentialsByTypeStream(realm, user, PasswordCredentialModel.TYPE);
        List<CredentialModel> credentialModels = credentialModelStream.collect(Collectors.toList());
        for (CredentialModel credentialModel: credentialModels) {
            if(credentialModel.getType().equals(PasswordCredentialModel.TYPE))
                return true;
        }
        return false;
    }

    private boolean createNewPassword(AuthenticationFlowContext context, RealmModel realm, UserModel user, String newPassword) {
        AuthenticationSessionModel authSession = context.getAuthenticationSession();
        EventBuilder event = context.getEvent();
        EventBuilder errorEvent = event.clone().event(EventType.UPDATE_PASSWORD_ERROR)
                .client(authSession.getClient())
                .user(authSession.getAuthenticatedUser());
        try {
            context.getSession().userCredentialManager().updateCredential(realm, user, UserCredentialModel.password(newPassword, false));
            return true;
        } catch (ModelException me) {
            errorEvent.detail(Details.REASON, me.getMessage()).error(Errors.PASSWORD_REJECTED);
            Response challenge = context.form()
                    .setAttribute("username", authSession.getAuthenticatedUser().getUsername())
                    .setError(me.getMessage(), me.getParameters())
                    .createResponse(UserModel.RequiredAction.UPDATE_PASSWORD);
            context.challenge(challenge);
            return false;
        } catch (Exception ape) {
            errorEvent.detail(Details.REASON, ape.getMessage()).error(Errors.PASSWORD_REJECTED);
            Response challenge = context.form()
                    .setAttribute("username", authSession.getAuthenticatedUser().getUsername())
                    .setError(ape.getMessage())
                    .createResponse(UserModel.RequiredAction.UPDATE_PASSWORD);
            context.challenge(challenge);
            return false;
        }
    }

    protected boolean validateForm(AuthenticationFlowContext context, MultivaluedMap<String, String> formData) {
        return validateUserAndPassword(context, formData);
    }

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> formData = new MultivaluedMapImpl<>();
        String loginHint = context.getAuthenticationSession().getClientNote(OIDCLoginProtocol.LOGIN_HINT_PARAM);

        String rememberMeUsername = AuthenticationManager.getRememberMeUsername(context.getRealm(), context.getHttpRequest().getHttpHeaders());

        if (loginHint != null || rememberMeUsername != null) {
            if (loginHint != null) {
                formData.add(AuthenticationManager.FORM_USERNAME, loginHint);
            } else {
                formData.add(AuthenticationManager.FORM_USERNAME, rememberMeUsername);
                formData.add("rememberMe", "on");
            }
        }
        Response challengeResponse = challenge(context, formData);
        context.challenge(challengeResponse);
    }

    @Override
    public boolean requiresUser() {
        return false;
    }

    protected Response challenge(AuthenticationFlowContext context, MultivaluedMap<String, String> formData) {
        LoginFormsProvider forms = context.form();

        if (formData.size() > 0) forms.setFormData(formData);

        return forms.createLoginUsernamePassword();
    }


    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        // never called
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
        // never called
    }

    @Override
    public void close() {

    }

}
