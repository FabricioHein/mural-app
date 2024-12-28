package com.bezkoder.spring.security.postgresql.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String mediaUrl; // URL ou caminho do arquivo
    private String mediaFolder; // Pasta onde o arquivo foi salvo
    private String mediaType; // Tipo da mídia

    @ElementCollection
    @CollectionTable(name = "post_reactions", joinColumns = @JoinColumn(name = "post_id"))
    @MapKeyColumn(name = "reaction_type")
    @Column(name = "reaction_count")
    @JsonManagedReference
    private Map<String, Integer> reactions = new HashMap<>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "wedding_data_id", nullable = false)
    @JsonBackReference
    private WeddingData weddingData;



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

    public WeddingData getWeddingData() {
        return weddingData;
    }

    public void setWeddingData(WeddingData weddingData) {
        this.weddingData = weddingData;
    }


}
