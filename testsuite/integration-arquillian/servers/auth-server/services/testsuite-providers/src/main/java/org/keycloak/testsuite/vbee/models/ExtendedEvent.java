package org.keycloak.testsuite.vbee.models;

import org.keycloak.events.Event;
import org.keycloak.events.EventType;
import org.keycloak.testsuite.vbee.constants.Env;
import org.keycloak.testsuite.vbee.http.Http;
import org.keycloak.testsuite.vbee.utils.Utils;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ExtendedEvent {
    private final String VBEE_WEBHOOK_URL = System.getenv(Env.VBEE_ACCOUNT_URL) + "/api/v1/users/webhooks";
    private final String VBEE_ACCOUNT_KEY = System.getenv(Env.VBEE_ACCOUNT_KEY);
    private final String AICC_WEBHOOK_URL = System.getenv(Env.AICC_WEBHOOK_URL);
    private final String AICC_ACCOUNT_KEY = System.getenv(Env.AICC_ACCOUNT_KEY);

    private String id;

    private long time;

    private EventType type;

    private String realmId;

    private String clientId;

    private String userId;

    private String sessionId;

    private String ipAddress;

    private String error;

    private Map<String, String> details;

    public ExtendedEvent(Event event) {
        this.id = event.getId();
        this.time = event.getTime();
        this.type = event.getType();
        this.realmId = event.getRealmId();
        this.clientId = event.getClientId();
        this.userId = event.getUserId();
        this.sessionId = event.getSessionId();
        this.ipAddress = event.getIpAddress();
        this.error = event.getError();
        this.details = new HashMap<String, String>();
    }

    public void mapValuesToDetails(Map<String, List<String>> attributes, String identityProvider) {
        if (identityProvider != null)
            this.details.put("identity_provider", identityProvider);
        Map<String, String> userAttributes = Utils.mapListMapToStringMap(attributes);
        this.details.putAll(userAttributes);
    }

    public void handleRegisterEvent(Map<String, List<String>> attributes, String identityProvider) {
        mapValuesToDetails(attributes, identityProvider);
    }

    public void handleUpdateUserEvent(Map<String, String> details) {
        for (Map.Entry<String, String> entry : details.entrySet()) {
            String key = entry.getKey();
            if(key.contains("updated")) {
                int length = key.length();
                String updatedKey = Utils.stringToSnakeCase(key.substring(8, length));
                this.details.put(updatedKey, entry.getValue());
            }
        }
    }

    public void handleExchangeTokenEvent(Map<String, List<String>> attributes, String userId, String identityProvider) {
        this.type = EventType.LOGIN;
        this.userId = userId;
        mapValuesToDetails(attributes, identityProvider);
    }

    public void executeSendingWebhook() {
        CompletableFuture.supplyAsync(() -> {
            handleSendingEvent(VBEE_WEBHOOK_URL, VBEE_ACCOUNT_KEY);
            return 0;
        });
        CompletableFuture.supplyAsync(() -> {
            handleSendingEvent(AICC_WEBHOOK_URL, AICC_ACCOUNT_KEY);
            return 0;
        });
    }

    private void handleSendingEvent(String webhookUrl, String accountKey) {
        try {
            String jsonData = Utils.convertObjectToJson(this);
            String response = Http.executePostRequest(webhookUrl, jsonData, accountKey );
            System.out.println("response: " + response);
        } catch (IOException e) {
            System.out.println("Send request failed!");
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getRealmId() {
        return realmId;
    }

    public void setRealmId(String realmId) {
        this.realmId = realmId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }
}
