package org.keycloak.testsuite.vbee.forms;

import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.authentication.FormAction;
import org.keycloak.authentication.FormActionFactory;
import org.keycloak.authentication.FormContext;
import org.keycloak.authentication.ValidationContext;
import org.keycloak.events.Details;
import org.keycloak.events.Errors;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.*;
import org.keycloak.models.utils.FormMessage;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.testsuite.vbee.utils.UserUtils;

import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.List;

public class RegistrationPhoneNumber implements FormAction, FormActionFactory {

    private static final Logger logger = Logger.getLogger(RegistrationPhoneNumber.class);

    private  static final String PROVIDER_ID = "registration-phone";

    private static final String FIELD_PHONE_NUMBER = "user.attributes.phoneNumber";
    private static final String MISSING_PHONE_NUMBER = "requiredPhoneNumber";
    private static final String PHONE_EXISTS = "phoneNumberExists";

    private static AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED, AuthenticationExecutionModel.Requirement.DISABLED };

    @Override
    public String getDisplayType() {
        return "Phone validation";
    }

    @Override
    public String getReferenceCategory() {
        return null;
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return true;
    }

    @Override
    public void buildPage(FormContext formContext, LoginFormsProvider loginFormsProvider) {
        loginFormsProvider.setAttribute("phoneNumberRequired", true);
    }

    @Override
    public void validate(ValidationContext context) {
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
       List<FormMessage> errors = new ArrayList<>();

       context.getEvent().detail(Details.REGISTER_METHOD, "form");

        KeycloakSession session = context.getSession();

       String phoneNumber = formData.getFirst(FIELD_PHONE_NUMBER);
       context.getEvent().detail(FIELD_PHONE_NUMBER, phoneNumber);

       if(phoneNumber == null || phoneNumber.trim().length() == 0) {
           context.error(Errors.INVALID_REGISTRATION);
           errors.add(new FormMessage(FIELD_PHONE_NUMBER, MISSING_PHONE_NUMBER));
           context.validationError(formData, errors);
           return;
       }

       // check phone number unique
        if (UserUtils.findUserByPhone(session.users(), context.getRealm(), phoneNumber) != null) {
            formData.remove(FIELD_PHONE_NUMBER);
            context.getEvent().detail(FIELD_PHONE_NUMBER, phoneNumber);
            errors.add(new FormMessage(FIELD_PHONE_NUMBER, PHONE_EXISTS));
            context.error(Errors.INVALID_REGISTRATION);
            context.validationError(formData, errors);
            return;
        }

        if (errors.size() > 0) {
           context.validationError(formData, errors);
           return;
       } else {
           context.success();
       }
    }

    @Override
    public void success(FormContext context) {
        UserModel user = context.getUser();

        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        String phoneNumber = formData.getFirst(FIELD_PHONE_NUMBER);

        logger.info(String.format("registration user %s phone success", user.getId()));
    }

    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    public boolean configuredFor(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {

    }

    @Override
    public String getHelpText() {
        return "valid phone number";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return null;
    }

    @Override
    public FormAction create(KeycloakSession keycloakSession) {
        return this;
    }

    @Override
    public void init(Config.Scope scope) {

    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}
