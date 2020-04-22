package com.msd.finalproject.model;

public class CurrentCoordinates {
    private Integer id;

    private Integer activityId;

    private Double longitude;

    private Double latitude;

    public CurrentCoordinates() {
        id = 0;
        activityId = 0;
        longitude = 0d;
        latitude = 0d;
    }

    public CurrentCoordinates(Integer id, Integer activityId, Double longitude, Double latitude) {
        this.id = id;
        this.activityId = activityId;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
