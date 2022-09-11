package org.keycloak.testsuite.vbee.models;

public class Contact {
    private String phone_number;
    private String otp_code;

    public Contact(String phoneNumber, String otpCode) {
        this.phone_number = phoneNumber;
        this.otp_code = otpCode;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getOtp_code() {
        return otp_code;
    }

    public void setOtp_code(String otp_code) {
        this.otp_code = otp_code;
    }
}