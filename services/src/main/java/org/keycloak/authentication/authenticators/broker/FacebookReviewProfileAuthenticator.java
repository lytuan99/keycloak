package org.keycloak.authentication.authenticators.broker;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.authenticators.broker.util.SerializedBrokeredIdentityContext;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import java.util.Date;
public class FacebookReviewProfileAuthenticator extends AbstractIdpAuthenticator{
    @Override
    public boolean requiresUser() {
        return false;
    }

    private String randomEmail() {
        Date date = new Date();
        long timeMilli = date.getTime();
        return timeMilli + "@gmail.com";
    }

    @Override
    protected void authenticateImpl(AuthenticationFlowContext context, SerializedBrokeredIdentityContext userCtx, BrokeredIdentityContext brokerContext) {
        String email = userCtx.getEmail();
        if (email == null) {
            String newEmail = randomEmail();
            userCtx.setEmail(newEmail);
        }

        userCtx.saveToAuthenticationSession(context.getAuthenticationSession(), BROKERED_CONTEXT_NOTE);
        context.getAuthenticationSession().setAuthNote(ENFORCE_UPDATE_PROFILE, "true");

        context.success();
    }

    @Override
    protected void actionImpl(AuthenticationFlowContext context, SerializedBrokeredIdentityContext userCtx, BrokeredIdentityContext brokerContext) {

    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return true;
    }

    @Override
    public void close() {
    }

    @Override
    public void action(AuthenticationFlowContext context) {
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
    }
}
