package org.keycloak.testsuite.vbee.broker;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.authenticators.broker.AbstractIdpAuthenticator;
import org.keycloak.authentication.authenticators.broker.util.SerializedBrokeredIdentityContext;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.testsuite.vbee.utils.UserUtils;

import java.util.ArrayList;
import java.util.List;

public class FacebookReviewProfileAuthenticator extends AbstractIdpAuthenticator {
    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    protected void authenticateImpl(AuthenticationFlowContext context, SerializedBrokeredIdentityContext userCtx, BrokeredIdentityContext brokerContext) {
        String email = userCtx.getEmail();
        String userId = userCtx.getFirstAttribute("id");
        if (email != null) {
            UserModel user = context.getSession().users().getUserByEmail(context.getRealm(), email);
            if (user != null) {
                
            }
        }
        if (email == null) {
            while(true) {
                String newEmail = UserUtils.randomVbeeEmail();
                UserModel existingUser = context.getSession().users().getUserByEmail(context.getRealm(), newEmail);
                if (existingUser == null) {
                    userCtx.setEmail(newEmail);
                    break;
                }
            }
        }

        String avatar = userCtx.getFirstAttribute("avatar");
        if (avatar != null) {
            List<String> defaultAttributes = new ArrayList<>();
            defaultAttributes.add("https://vbee.s3.ap-southeast-1.amazonaws.com/images/logos/avatar.png");
            userCtx.setAttribute("avatar", defaultAttributes);
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
