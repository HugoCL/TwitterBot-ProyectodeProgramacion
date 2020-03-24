package com.ghawk1ns.perspective.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AttributeScores {

    // TODO: spanScores

    @JsonProperty("summaryScore")
    public ProbabilityScore summaryScore;
}
