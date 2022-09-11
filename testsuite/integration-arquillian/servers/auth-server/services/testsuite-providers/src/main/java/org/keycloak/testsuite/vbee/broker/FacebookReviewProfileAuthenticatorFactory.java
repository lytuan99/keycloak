package org.keycloak.testsuite.vbee.broker;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.List;
public class FacebookReviewProfileAuthenticatorFactory implements AuthenticatorFactory {
    public static final String PROVIDER_ID = "facebook-review-profile";
    public static final String UPDATE_PROFILE_ON_FIRST_LOGIN = "update.profile.on.first.login";

    @Override
    public Authenticator create(KeycloakSession session) {
        return new FacebookReviewProfileAuthenticator();
    }

    @Override
    public void init(Config.Scope config) {

    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getReferenceCategory() {
        return null;
    }

    @Override
    public boolean isConfigurable() {
        return false;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public String getDisplayType() {
        return "Facebook Review Profile";
    }

    @Override
    public String getHelpText() {
        return "User reviews and updates profile data retrieved from Identity Provider in the displayed form";
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return null;
    }
}
