package org.keycloak.testsuite.vbee.voiceOtp.models;

import java.util.ArrayList;

public class RequestVoiceModel {
    private String access_token;
    private ArrayList<Contact> contacts;

    public RequestVoiceModel(String access_token, ArrayList<Contact> contacts) {
        this.access_token = access_token;
        this.contacts = contacts;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }
}
