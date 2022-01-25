package org.keycloak.testsuite.vbee.voiceOtp;


import org.keycloak.testsuite.vbee.http.Http;
import org.keycloak.testsuite.vbee.utils.Utils;
import org.keycloak.testsuite.vbee.voiceOtp.models.Contact;
import org.keycloak.testsuite.vbee.voiceOtp.models.RequestVoiceModel;

import java.io.IOException;
import java.util.ArrayList;

public class VoiceOtpService implements OtpService {
    @Override
    public void send(String url, String accessToken, String phoneNumber, String message) throws IOException {
        ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(phoneNumber, message));

        RequestVoiceModel requestVoiceModel = new RequestVoiceModel(accessToken, contacts);

        String response = Http.executePostRequest(url, Utils.convertObjectToJson(requestVoiceModel), null);
        if(response != null)
            System.out.println(response);

    }
}
