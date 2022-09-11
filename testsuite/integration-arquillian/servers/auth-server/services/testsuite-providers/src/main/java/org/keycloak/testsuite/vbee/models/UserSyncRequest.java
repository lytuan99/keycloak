package org.keycloak.testsuite.vbee.models;

public class UserSyncRequest {
    private String realmId;
    private String clientId;
    private String username;
    private String password;
    private String provider;
    private String providerUserId;
    private String userId;

    public UserSyncRequest(String realmId, String clientId) {
        this.realmId = realmId;
        this.clientId = clientId;
    }

    public static UserSyncRequest build(String realmId, String clientId) {
        return new UserSyncRequest(realmId, clientId);
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

    public String getProvider() { return provider; };

    public UserSyncRequest provider(String provider) {
        this.provider = provider;
        return this;
    }

    public String getProviderUserId() { return providerUserId; }

    public UserSyncRequest providerUserId(String providerUserId) {
        this.providerUserId = providerUserId;
        return this;
    }

    public UserSyncRequest userId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUserId() {
        return userId;
    }
}
