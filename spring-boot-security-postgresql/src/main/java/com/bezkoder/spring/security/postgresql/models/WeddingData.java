package com.bezkoder.spring.security.postgresql.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "wedding_data")
public class WeddingData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;

    private String brideAndGroomName;
    private String imageUrl;
    private String leaveMessageUrl;
    private String viewMuralUrl;
    private String loginUrl;
    private LocalDate weddingDate;
    private String color;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    public WeddingData() {
    }

    public WeddingData(UUID uuid, String brideAndGroomName, String imageUrl, String leaveMessageUrl, String viewMuralUrl, String loginUrl, LocalDate weddingDate, User user) {
        this.uuid = uuid;
        this.brideAndGroomName = brideAndGroomName;
        this.imageUrl = imageUrl;
        this.leaveMessageUrl = leaveMessageUrl;
        this.viewMuralUrl = viewMuralUrl;
        this.loginUrl = loginUrl;
        this.weddingDate = weddingDate;
        this.user = user;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getBrideAndGroomName() {
        return brideAndGroomName;
    }

    public void setBrideAndGroomName(String brideAndGroomName) {
        this.brideAndGroomName = brideAndGroomName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLeaveMessageUrl() {
        return leaveMessageUrl;
    }

    public void setLeaveMessageUrl(String leaveMessageUrl) {
        this.leaveMessageUrl = leaveMessageUrl;
    }

    public String getViewMuralUrl() {
        return viewMuralUrl;
    }

    public void setViewMuralUrl(String viewMuralUrl) {
        this.viewMuralUrl = viewMuralUrl;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public LocalDate getWeddingDate() {
        return weddingDate;
    }

    public void setWeddingDate(LocalDate weddingDate) {
        this.weddingDate = weddingDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
