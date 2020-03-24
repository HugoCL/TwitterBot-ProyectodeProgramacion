package com.ghawk1ns.perspective.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseResponse {

    @JsonProperty("error")
    public Error error;

    @JsonProperty("clientToken")
    public String clientToken;
}
