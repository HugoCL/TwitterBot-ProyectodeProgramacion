package com.ghawk1ns.perspective.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Comment {

    @JsonProperty("text")
    public String text;

    @JsonProperty("type")
    public String type;

    public Comment(String text, String type) {
        this.text = text;
        this.type = type;
    }
}
