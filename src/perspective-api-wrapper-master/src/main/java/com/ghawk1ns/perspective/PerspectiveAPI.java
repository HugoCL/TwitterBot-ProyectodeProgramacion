package com.ghawk1ns.perspective;

import com.ghawk1ns.perspective.request.AnalyzeCommentRequest;
import com.ghawk1ns.perspective.request.Client;

public class PerspectiveAPI {

    private final String clientToken;
    private final String sessionId;

    private final Client client;

    PerspectiveAPI(PerspectiveAPIBuilder builder) {
        this.sessionId = builder.sessionId;
        this.clientToken = builder.clientToken;
        client = new Client(builder.apiKey, builder.apiVersion);
    }

    public AnalyzeCommentRequest analyze() {
        return new AnalyzeCommentRequest(client, sessionId, clientToken);
    }
}
