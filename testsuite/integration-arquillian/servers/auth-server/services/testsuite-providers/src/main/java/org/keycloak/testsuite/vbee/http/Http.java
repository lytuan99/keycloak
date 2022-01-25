package org.keycloak.testsuite.vbee.http;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jboss.logging.Logger;

import java.io.IOException;

public class Http {

    private static final Logger LOG = Logger.getLogger(Http.class);

    public static String executePostRequest(String urlString, String jsonData, String accessToken) throws IOException {
        HttpPost post = new HttpPost(urlString);
        post.setHeader("Content-Type", "application/json");
        post.setHeader("Accept", "application/json");
        post.setHeader("Authorization", "Bearer " + accessToken);
        post.setEntity(new StringEntity(jsonData, "UTF-8"));

        String jsonResponse = "";
        try (
                CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(post)
        ) {
            LOG.info("Send POST request successfully!");
            jsonResponse = EntityUtils.toString(response.getEntity());
        }
        return jsonResponse;
    }

}

