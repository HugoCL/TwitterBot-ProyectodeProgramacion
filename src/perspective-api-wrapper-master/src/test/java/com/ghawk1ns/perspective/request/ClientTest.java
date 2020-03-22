package com.ghawk1ns.perspective.request;

import org.junit.Assert;
import org.junit.Test;

public class ClientTest {

    private Client client = new Client("k", "v");

    @Test
    public void pathTest() {
        String path = client.getEndpoint("e");
        String expected = "https://commentanalyzer.googleapis.com/v/e?key=k";
        Assert.assertEquals(expected, path);

        String path2 = client.getEndpoint("9ads8fad7fasd");
        String expected2 = "https://commentanalyzer.googleapis.com/v/9ads8fad7fasd?key=k";
        Assert.assertEquals(expected2, path2);
    }
}
