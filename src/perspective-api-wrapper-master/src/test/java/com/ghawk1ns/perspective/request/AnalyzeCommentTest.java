package com.ghawk1ns.perspective.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ghawk1ns.perspective.model.Attribute;
import com.sun.javaws.exceptions.InvalidArgumentException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AnalyzeCommentTest {

    private  Client client;

    @Before
    public void init() {
        client = new Client("apik", "apiV");
    }

    @Test
    public void testGoodBody() throws JsonProcessingException, InvalidArgumentException {
        final String expected = "{\"comment\":{\"text\":\"I am a comment\"},"
            + "\"context\":{"
                + "\"entries\":["
                    + "{\"text\":\"I am the first context\"},"
                    + "{\"text\":\"I am the second context\"}]"
            + "},"
            + "\"requestedAttributes\":{"
                + "\"TOXICITY\":{\"scoreThreshold\":0.5},"
                + "\"SPAM\":{\"scoreThreshold\":0.75}"
            + "},"
            + "\"languages\":[\"en\",\"fr\"],"
            + "\"doNotStore\":true,"
            + "\"clientToken\":\"ct\","
            + "\"sessionId\":\"sid\"}";

        AnalyzeCommentRequest request = new AnalyzeCommentRequest(client, "sid", "ct");
        request.setComment("I am a comment")
            .addContext("I am the first context")
            .addContext("I am the second context")
            .addLanguage("en")
            .addLanguage("fr")
            .addAttribute(Attribute.ofType(Attribute.TOXICITY).setScoreThreshold(.5f))
            .addAttribute(Attribute.ofType(Attribute.SPAM).setScoreThreshold(.75f))
            .setDoNotStore(true);

        Assert.assertEquals("request body was not as expected", expected, request.bodyJSON());
    }

    @Test
    public void testBadBody() throws JsonProcessingException {
        AnalyzeCommentRequest requestNoComment = new AnalyzeCommentRequest(client, "sid", "ct");
        requestNoComment.addContext("I am the first context")
            .addAttribute(Attribute.ofType(Attribute.TOXICITY));
        try {
            requestNoComment.bodyJSON();
            Assert.fail("Expected IllegalArgumentException because a comment wasn't provided");
        } catch (IllegalArgumentException e) {
            // pass
        }

        AnalyzeCommentRequest requestNoAttribute = new AnalyzeCommentRequest(client, "sid", "ct");
        requestNoComment.addContext("I am context").setComment("I am a comment");
        try {
            requestNoAttribute.bodyJSON();
            Assert.fail("Expected IllegalArgumentException because an attribute wasn't provided");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
}
