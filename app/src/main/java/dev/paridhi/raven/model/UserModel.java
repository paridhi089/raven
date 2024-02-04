package dev.paridhi.raven.model;


import java.util.Date;

public class UserModel {

    private String displayName,email,photoUrl;


    private long created_at;

    public UserModel(String displayName, String email, String photoUrl, long created_at) {
        this.displayName = displayName;
        this.email = email;
        this.photoUrl = photoUrl;
        this.created_at = created_at;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }
}
