package org.keycloak.testsuite.vbee.voiceOtp;

import java.io.IOException;

public interface OtpService {
    void send(String url, String accessToken, String phoneNumber, String message) throws IOException;
}
