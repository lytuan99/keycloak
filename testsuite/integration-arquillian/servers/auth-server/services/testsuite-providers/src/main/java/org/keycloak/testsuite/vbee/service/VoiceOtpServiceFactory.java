package org.keycloak.testsuite.vbee.service;

public class VoiceOtpServiceFactory {
    public static OtpService get() {
        return new VoiceOtpService();
    }
}
