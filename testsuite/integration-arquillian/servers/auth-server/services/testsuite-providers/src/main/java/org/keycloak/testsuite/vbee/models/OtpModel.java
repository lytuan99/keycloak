package org.keycloak.testsuite.vbee.models;

/**
 * instance of this class will be injected to otp-code.ftl template
 */
public class OtpModel {
    private String method;
    private String targetUsername;
    private String resendDuration;

    public OtpModel(String method, String targetUsername, String resendDuration) {
        this.method = method;
        this.targetUsername = targetUsername;
        this.resendDuration = resendDuration;
    }

    public String getResendDuration() {
        return resendDuration;
    }

    public void setResendDuration(String resendDuration) {
        this.resendDuration = resendDuration;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTargetUsername() {
        return targetUsername;
    }

    public void setTargetUsername(String targetUsername) {
        this.targetUsername = targetUsername;
    }
}
