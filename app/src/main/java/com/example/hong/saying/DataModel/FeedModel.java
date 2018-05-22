package com.example.hong.saying.DataModel;

import java.io.Serializable;

/**
 * Created by hong on 2018-04-03.
 */

public class FeedModel implements Serializable{
    String imageUrl;
    String userName;
    String profileUrl;
    int gravity;
    String textColor;
    String contents;
    long time;
    String userKey;

    public FeedModel(){}

    public FeedModel(String imageUrl, String userName, String profileUrl, int gravity, String textColor, String contents, long time, String userKey) {
        this.imageUrl = imageUrl;
        this.userName = userName;
        this.profileUrl = profileUrl;
        this.gravity = gravity;
        this.textColor = textColor;
        this.contents = contents;
        this.time = time;
        this.userKey = userKey;
    }

    public long getTime() {
        return time;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public int getGravity() {
        return gravity;
    }

    public String getTextColor() {
        return textColor;
    }

    public String getContents() {
        return contents;
    }

    public String getUserKey() {
        return userKey;
    }
}
