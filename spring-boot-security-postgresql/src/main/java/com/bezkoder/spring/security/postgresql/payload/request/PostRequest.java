package com.bezkoder.spring.security.postgresql.payload.request;

import com.bezkoder.spring.security.postgresql.dto.UserDTO;
import com.bezkoder.spring.security.postgresql.dto.WeddingDataDTO;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.models.WeddingData;

public class PostRequest {
    private String content;
    private String mediaUrl;
    private String mediaFolder;
    private String mediaType;
    private User user;
    private WeddingData weddingData;

    // Construtor
    public PostRequest(String content, String mediaUrl, String mediaFolder, String mediaType, User user, WeddingData weddingData) {
        this.content = content;
        this.mediaUrl = mediaUrl;
        this.mediaFolder = mediaFolder;
        this.mediaType = mediaType;
        this.user = user;
        this.weddingData = weddingData;
    }

    // Getters e Setters
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

    public String getMediaFolder() {
        return mediaFolder;
    }

    public void setMediaFolder(String mediaFolder) {
        this.mediaFolder = mediaFolder;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public WeddingData getWeddingData() {
        return weddingData;
    }

    public void setWeddingData(WeddingData weddingData) {
        this.weddingData = weddingData;
    }
}
