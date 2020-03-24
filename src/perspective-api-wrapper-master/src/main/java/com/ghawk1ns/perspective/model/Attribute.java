package com.ghawk1ns.perspective.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * https://github.com/conversationai/perspectiveapi/blob/master/api_reference.md#models
 */
public class Attribute {

    /**
     *  rude, disrespectful, or unreasonable comment that is likely to make people leave a discussion.
     */
    public static final String TOXICITY = "TOXICITY";

    /**
     * Similar to the TOXICITY model, but has lower latency and lower accuracy.
     */
    public static final String TOXICITY_FAST = "TOXICITY_FAST";

    /**
     * Attack on author of original article or post.
     */
    public static final String ATTACK_ON_AUTHOR = "ATTACK_ON_AUTHOR";

    /**
     * Attack on fellow commenter.
     */
    public static final String ATTACK_ON_COMMENTER = "ATTACK_ON_COMMENTER";

    /**
     * Difficult to understand, nonsensical.
     */
    public static final String INCOHERENT = "INCOHERENT";

    /**
     *  Intending to provoke or inflame.
     */
    public static final String INFLAMMATORY = "INFLAMMATORY";

    /**
     * Obscene or vulgar language such as cursing.
     */
    public static final String OBSCENE = "OBSCENE";

    /**
     * Irrelevant and unsolicited commercial content.
     */
    public static final String SPAM = "SPAM";

    /**
     * Trivial or short comments.
     */
    public static final String UNSUBSTANTIAL = "UNSUBSTANTIAL";

    /**
     *  Overall measure of the likelihood for the comment to be rejected according by the NYT's moderation.
     */
    public static final String LIKELY_TO_REJECT = "LIKELY_TO_REJECT";

    @JsonIgnore
    public final String type;

    @JsonProperty("scoreType")
    public String scoreType;

    @JsonProperty("scoreThreshold")
    public float scoreThreshold;

    private Attribute(String type) {
        this.type = type;
    }

    public static Attribute ofType(String type) {
        return new Attribute(type);
    }

    public Attribute setScoreType(String scoreType) {
        this.scoreType = scoreType;
        return this;
    }

    public Attribute setScoreThreshold(float scoreThreshold) {
        this.scoreThreshold = scoreThreshold;
        return this;
    }
}
