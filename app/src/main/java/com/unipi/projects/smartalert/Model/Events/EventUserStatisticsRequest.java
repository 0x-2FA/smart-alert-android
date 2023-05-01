package com.unipi.projects.smartalert.Model.Events;

import com.google.gson.annotations.SerializedName;

public class EventUserStatisticsRequest {
    @SerializedName("id")
    private String id;

    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
    }
}
