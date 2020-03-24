package com.ghawk1ns.perspective;

import org.junit.Assert;
import org.junit.Test;

public class PerspectiveAPITest {

    @Test
    public void noAPIKey() {
        PerspectiveAPIBuilder builder = new PerspectiveAPIBuilder()
            .setClientToken("ct")
            .setApiVersion("v1")
            .setSessionId("sid");
        try {
            // Intentionally build without setting the api key
            builder.build();
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // All good
        }
    }

    @Test
    public void allGood() {
        PerspectiveAPIBuilder builder = new PerspectiveAPIBuilder()
            .setApiKey("apiKey")
            .setClientToken("ct")
            .setApiVersion("v1")
            .setSessionId("sid");
        try {
            builder.build();
        } catch (IllegalArgumentException e) {
            Assert.fail();
        }
    }
}
