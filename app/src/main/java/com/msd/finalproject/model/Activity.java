package com.msd.finalproject.model;

import java.util.Date;

/**
 * Activity model to Initiate activity
 * it stores captured image, user's id
 * and time when activity started
 */
public class Activity {
    private Integer id;
    private Integer userId;
    private String imgUrl;
    private Date createdAt;

    public Activity() {
        id = 0;
        userId = 0;
        imgUrl = null;
        createdAt = null;
    }

    public Activity(Integer userId, String imgUrl, Date createdAt) {
        this.userId = userId;
        this.imgUrl = imgUrl;
        this.createdAt = createdAt;
    }

    public Activity(Integer id, Integer userId, String imgUrl, Date createdAt) {
        this.id = id;
        this.userId = userId;
        this.imgUrl = imgUrl;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
