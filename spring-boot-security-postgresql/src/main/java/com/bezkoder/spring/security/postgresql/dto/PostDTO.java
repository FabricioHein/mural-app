package com.bezkoder.spring.security.postgresql.dto;

import com.bezkoder.spring.security.postgresql.models.User;

import java.util.HashMap;
import java.util.Map;

import java.util.List;

public class PostDTO {
    private Long id;
    private String content;
    private String mediaUrl;
    private Map<String, Integer> reactions = new HashMap<>();
    private User user;

    // Construtor
    public PostDTO(Long id, String content, String mediaUrl, Map<String, Integer> reactions, User user) {
        this.id = id;
        this.content = content;
        this.mediaUrl = mediaUrl;
        this.reactions = reactions;
        this.user = user;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }


    public Map<String, Integer> getReactions() {
        return reactions;
    }

    public void setReactions(Map<String, Integer> reactions) {
        this.reactions = reactions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
