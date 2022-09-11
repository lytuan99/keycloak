package org.keycloak.testsuite.vbee.service;

import org.apache.sshd.common.config.keys.loader.openssh.kdf.BCrypt;
import org.keycloak.services.ServicesLogger;
import org.keycloak.testsuite.vbee.constants.Env;
import org.keycloak.testsuite.vbee.http.Http;
import org.keycloak.testsuite.vbee.models.UserSyncRequest;
import org.keycloak.testsuite.vbee.utils.Utils;

import java.io.IOException;

public class SyncV3Service {
    protected static ServicesLogger log = ServicesLogger.LOGGER;
    public static final String HASHED_PASSWORD_V3 = "hashedPasswordV3";
    public static final String VBEE_TTS_CLIENT = System.getenv(Env.VBEE_TTS_CLIENT);

    private static final String VBEE_SYNC_USER_URL = System.getenv(Env.VBEE_ACCOUNT_URL) + "/api/v1/users/sync";
    private static final String VBEE_ACCOUNT_KEY = System.getenv(Env.VBEE_ACCOUNT_KEY);


    public static boolean syncUser(UserSyncRequest userSyncRequest) {
        String jsonData = Utils.convertObjectToJson(userSyncRequest);
        try {
                String response = Http.executePostRequest(VBEE_SYNC_USER_URL, jsonData, VBEE_ACCOUNT_KEY);
                log.info("Response from " + VBEE_SYNC_USER_URL + ": " + response);
                return true;
            } catch (IOException e) {
                log.warn("Send request failed from " + VBEE_SYNC_USER_URL + ": " + jsonData);
                return false;
            }
    }

    public static boolean verifyV3Password(String passwordInput, String hashedPassword) {
        return BCrypt.checkpw(passwordInput, hashedPassword);
    }

}
