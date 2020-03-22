package com.ghawk1ns.perspective.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyzeCommentResponse extends BaseResponse {

    @JsonProperty("languages")
    public List<String> languages;

    @JsonProperty("attributeScores")
    public Map<String, AttributeScores> attributeScores;

    @JsonIgnore
    private Map<String, Float> attributeSummaryScores;

    /**
     * @param attr Attribute Name
     * @return the summary score as a probability
     */
    public float getAttributeSummaryScore(String attr) {
        return getAttributeSummaryScores().getOrDefault(attr, 0f);
    }

    /**
     *
     * @return a mapping of Attribute names to the summary score as a probability
     */
    public Map<String, Float> getAttributeSummaryScores() {
        if (attributeSummaryScores == null) {
            if (attributeScores == null) {
                attributeSummaryScores = Collections.emptyMap();
            } else {
                attributeSummaryScores = new HashMap<>(attributeScores.size());
                attributeScores.forEach((k, v) -> {
                    if (v != null && v.summaryScore != null) {
                        attributeSummaryScores.put(k, v.summaryScore.score);
                    }
                });
            }
        }
        return attributeSummaryScores;
    }
}
