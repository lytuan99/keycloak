package org.keycloak.testsuite.vbee.models;

public class UserSyncRequest {
    private String realmId;
    private String clientId;
    private String username;
    private String password;

    public static UserSyncRequest build() {
        return new UserSyncRequest();
    }

    public String getUsername() {
        return username;
    }

    public UserSyncRequest username(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserSyncRequest password(String password) {
        this.password = password;
        return this;
    }

    public String getRealmId() {
        return realmId;
    }

    public UserSyncRequest realmId(String realmId) {
        this.realmId = realmId;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public UserSyncRequest clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }
}
