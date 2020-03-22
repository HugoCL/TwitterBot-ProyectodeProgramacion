package com.ghawk1ns.perspective.request;


import com.ghawk1ns.perspective.model.AnalyzeCommentBody;
import com.ghawk1ns.perspective.model.Attribute;
import com.ghawk1ns.perspective.model.Comment;
import com.ghawk1ns.perspective.response.AnalyzeCommentResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.asynchttpclient.Response;

import java.io.IOException;

/**
 * Requests a comment to be analyzed. You must set a comment and at least 1 Attribute.
 *
 * https://github.com/conversationai/perspectiveapi/blob/master/api_reference.md
 */
public class AnalyzeCommentRequest extends BaseRequest<AnalyzeCommentResponse> {

    private AnalyzeCommentBody requestBody;

    public AnalyzeCommentRequest(Client client, String sessionId, String clientToken) {
        super(client);
        requestBody = new AnalyzeCommentBody();
        requestBody.sessionId = sessionId;
        requestBody.clientToken = clientToken;
    }

    @Override
    String bodyJSON() throws JsonProcessingException {
        if (requestBody.comment == null) {
            throw new IllegalArgumentException("A comment must be provided");
        }

        if (requestBody.requestedAttributes.isEmpty()) {
            throw new IllegalArgumentException("At least 1 attribute must be provided");
        }

        return client.mapper.writeValueAsString(requestBody);
    }

    @Override
    String getPath() {
        return client.getEndpoint("comments:analyze");
    }

    @Override
    AnalyzeCommentResponse transform(Response response) {
        try {
            return client.mapper.readValue(response.getResponseBodyAsStream(), AnalyzeCommentResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AnalyzeCommentRequest setComment(String comment, String type) {
        requestBody.comment = new Comment(comment, type);
        return this;
    }

    public AnalyzeCommentRequest setComment(String comment) {
        return setComment(comment, null);
    }

    public AnalyzeCommentRequest addLanguage(String lang) {
        requestBody.languages.add(lang);
        return this;
    }

    /**
     * Optionally add as many contexts as you need
     */
    public AnalyzeCommentRequest addContext(String context, String type) {
        requestBody.context.entries.add(new Comment(context, type));
        return this;
    }

    public AnalyzeCommentRequest addContext(String context) {
        return addContext(context, null);
    }

    /**
     * You must set at least 1 attribute but you may add as many as you need
     */
    public AnalyzeCommentRequest addAttribute(Attribute attr) {
        requestBody.requestedAttributes.put(attr.type, attr);
        return this;
    }

    /**
     *
     * @param doNotStore whether the API is permitted to store comment and context from this request.
     * default=false
     *
     *  Important note: This should be set to true if data being submitted is private
     *      (i.e. not publicly accessible), or if the data submitted contains content written
     *      by someone under 13 years old.
     */
    public AnalyzeCommentRequest setDoNotStore(boolean doNotStore) {
        requestBody.doNotStore = doNotStore;
        return this;
    }
}
