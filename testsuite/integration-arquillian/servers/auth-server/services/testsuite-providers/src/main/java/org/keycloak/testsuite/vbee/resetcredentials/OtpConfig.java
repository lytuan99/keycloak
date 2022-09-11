package org.keycloak.testsuite.vbee.resetcredentials;

public class OtpConfig {
    public static final int DEFAULT_OTP_CODE_LENGTH = 6;
    public static final int DEFAULT_OTP_CODE_TTL = 120;
    public static final int DEFAULT_OTP_CODE_RESEND_DURATION = 60;

    public static final String LENGTH = "length";
    public static final String TTL = "ttl";
    public static final String RESEND_DURATION = "resend_duration";
    public static final String URL = "url";
    public static final String ACCESS_TOKEN = "access_token";
}
