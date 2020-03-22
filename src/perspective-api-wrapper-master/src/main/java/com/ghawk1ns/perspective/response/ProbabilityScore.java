package com.ghawk1ns.perspective.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProbabilityScore {

    @JsonProperty("value")
    public float score;

    @JsonProperty("type")
    public String type;
}