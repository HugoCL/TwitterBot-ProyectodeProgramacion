package com.ghawk1ns.perspective.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {

    @JsonProperty("code")
    public int code;

    @JsonProperty("message")
    public String message;

    @JsonProperty("status")
    public String status;
}
