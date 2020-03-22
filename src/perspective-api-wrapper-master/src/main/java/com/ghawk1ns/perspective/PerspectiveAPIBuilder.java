package com.ghawk1ns.perspective;

public class PerspectiveAPIBuilder {

    String sessionId;
    String clientToken;
    String apiKey;
    String apiVersion = "v1alpha1";

    public PerspectiveAPIBuilder setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public PerspectiveAPIBuilder setClientToken(String clientToken) {
        this.clientToken = clientToken;
        return this;
    }

    public PerspectiveAPIBuilder setApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public PerspectiveAPIBuilder setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
        return this;
    }

    public PerspectiveAPI build() {
        if (apiKey == null) {
            throw new IllegalArgumentException("You must provide an API Key");
        }
        return new PerspectiveAPI(this);
    }
}