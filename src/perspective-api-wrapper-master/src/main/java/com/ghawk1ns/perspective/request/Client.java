package com.ghawk1ns.perspective.request;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;

public class Client {

    private static final String BASE_FORMAT = "https://commentanalyzer.googleapis.com/%s/%%s?key=%s";
    private final String BASE_PATH;

    final AsyncHttpClient http;
    final ObjectMapper mapper;

    public Client(String apiKey, String apiVersion) {
        http = new DefaultAsyncHttpClient();
        mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(Include.NON_DEFAULT);
        BASE_PATH = String.format(BASE_FORMAT, apiVersion, apiKey);
    }

    /**
     *
     * @return the path for a given endpoint: BASE_PATH / API_VERSION / endpoint
     */
    String getEndpoint(String endpoint) {
        return String.format(BASE_PATH, endpoint);
    }
}