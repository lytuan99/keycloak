package org.keycloak.testsuite.vbee.voiceOtp;

import org.jboss.logging.Logger;

import java.util.Map;

public class VoiceOtpServiceFactory {
    private static final Logger LOG = Logger.getLogger(VoiceOtpServiceFactory.class);

    public static OtpService get(Map<String, String> config) {
        if (Boolean.parseBoolean(config.getOrDefault("simulation", "false"))) {
            return (url, accessToken, phoneNumber, message) ->
                    LOG.warn(String.format("***** SIMULATION MODE ***** Would send Voice OTP to %s with text: %s", phoneNumber, message));
        } else {
            return new VoiceOtpService();
        }
    }
}
