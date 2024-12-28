package com.bezkoder.spring.security.postgresql.interfaces;

import java.util.List;

public interface PostProjection {
    Long getId();
    String getContent();
    String getMediaUrl();
    String getMediaType();
    String getUsername();
    List<ReactionProjection> getReactions();

    interface ReactionProjection {
        String getReactionType();
        Integer getReactionCount();
    }
}
