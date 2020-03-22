package com.ghawk1ns.perspective.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyzeCommentBody {

    @JsonProperty("comment")
    public Comment comment;

    @JsonProperty("context")
    public Context context = new Context();

    @JsonProperty("requestedAttributes")
    public Map<String, Attribute> requestedAttributes = new HashMap<>();

    @JsonProperty("languages")
    public List<String> languages = new ArrayList<>();

    @JsonProperty("doNotStore")
    public boolean doNotStore;

    @JsonProperty("clientToken")
    public String clientToken;

    @JsonProperty("sessionId")
    public String sessionId;

    public static class Context {
        @JsonProperty("entries")
        public List<Comment> entries = new ArrayList<>();
    }
}
