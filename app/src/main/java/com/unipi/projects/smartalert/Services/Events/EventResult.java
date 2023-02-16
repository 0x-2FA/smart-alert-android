package com.unipi.projects.smartalert.Services.Events;

public class EventResult {
    private String type;
    private String latitude;
    private String longitude;
    private String photo;
    private String comment;
    private String userId;

    public String getType() { return type; }

    public void setType(String type) {
        this.type = type;
    }

    public String getLatitude() { return latitude; }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() { return longitude; }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPhoto() { return photo; }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getComment() { return comment; }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserId() { return userId; }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
