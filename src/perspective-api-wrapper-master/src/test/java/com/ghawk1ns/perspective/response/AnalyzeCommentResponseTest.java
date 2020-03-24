package com.ghawk1ns.perspective.response;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class AnalyzeCommentResponseTest {

    private static final String responseJSON = "{\n"
        + "  \"attributeScores\": {\n"
        + "    \"ATTACK_ON_COMMENTER\": {\n"
        + "      \"summaryScore\": {\n"
        + "        \"value\": 0.10010548,\n"
        + "        \"type\": \"PROBABILITY\"\n"
        + "      }\n"
        + "    },\n"
        + "    \"LIKELY_TO_REJECT\": {\n"
        + "      \"summaryScore\": {\n"
        + "        \"value\": 0.99959844,\n"
        + "        \"type\": \"PROBABILITY\"\n"
        + "      }\n"
        + "    },\n"
        + "    \"ATTACK_ON_AUTHOR\": {\n"
        + "      \"summaryScore\": {\n"
        + "        \"value\": 0.022719337,\n"
        + "        \"type\": \"PROBABILITY\"\n"
        + "      }\n"
        + "    },\n"
        + "    \"TOXICITY\": {\n"
        + "      \"spanScores\": [\n"
        + "        {\n"
        + "          \"begin\": 0,\n"
        + "          \"end\": 17,\n"
        + "          \"score\": {\n"
        + "            \"value\": 0.97946084,\n"
        + "            \"type\": \"PROBABILITY\"\n"
        + "          }\n"
        + "        }\n"
        + "      ],\n"
        + "      \"summaryScore\": {\n"
        + "        \"value\": 0.97946084,\n"
        + "        \"type\": \"PROBABILITY\"\n"
        + "      }\n"
        + "    }\n"
        + "  },\n"
        + "  \"languages\": [\n"
        + "    \"en\"\n"
        + "  ]\n"
        + "}";



    private ObjectMapper mapper;

    @Before
    public void init() {
        mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(Include.NON_DEFAULT);
    }

    @Test
    public void testResponse() throws IOException {
        AnalyzeCommentResponse response = mapper
            .readValue(AnalyzeCommentResponseTest.responseJSON, AnalyzeCommentResponse.class);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.attributeScores);
        Assert.assertNotNull(response.languages);
        Assert.assertEquals(1, response.languages.size());
        Assert.assertEquals(0.10010548f, response.getAttributeSummaryScore("ATTACK_ON_COMMENTER"), 0);
        Assert.assertEquals(0.99959844f, response.getAttributeSummaryScore("LIKELY_TO_REJECT"), 0);
        Assert.assertEquals(0.022719337f, response.getAttributeSummaryScore("ATTACK_ON_AUTHOR"), 0);
        Assert.assertEquals(0.97946084f, response.getAttributeSummaryScore("TOXICITY"), 0);
        Assert.assertEquals(0f, response.getAttributeSummaryScore("fakeAttribute"), 0);
    }
}
