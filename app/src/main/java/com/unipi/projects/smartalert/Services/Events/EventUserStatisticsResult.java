package com.unipi.projects.smartalert.Services.Events;

public class EventUserStatisticsResult {
    int earthquakeEventsNum;
    int fireEventsNum;
    int floodEventsNum;

    public int getEarthquakeEventsNum() {
        return earthquakeEventsNum;
    }

    public void setEarthquakeEventsNum(int earthquakeEventsNum) {
        this.earthquakeEventsNum = earthquakeEventsNum;
    }

    public int getFireEventsNum() {
        return fireEventsNum;
    }

    public void setFireEventsNum(int fireEventsNum) {
        this.fireEventsNum = fireEventsNum;
    }

    public int getFloodEventsNum() {
        return floodEventsNum;
    }

    public void setFloodEventsNum(int floodEventsNum) {
        this.floodEventsNum = floodEventsNum;
    }
}
