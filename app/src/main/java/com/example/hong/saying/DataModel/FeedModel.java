package com.example.hong.saying.DataModel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hong on 2018-04-03.
 */

public class FeedModel implements Serializable {
    private String imageUrl;
    private String userName;
    private String profileUrl;
    private int gravity;
    private String textColor;
    private String contents;
    private long time;
    private String userKey;
    private String hashTag;

    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public FeedModel() {
    }

    public FeedModel(String imageUrl, String userName, String profileUrl, int gravity, String textColor, String contents,
                     long time, String userKey, String hashTag) {
        this.imageUrl = imageUrl;
        this.userName = userName;
        this.profileUrl = profileUrl;
        this.gravity = gravity;
        this.textColor = textColor;
        this.contents = contents;
        this.time = time;
        this.userKey = userKey;
        this.hashTag = hashTag;
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

    public String getHashTag() {
        return hashTag;
    }
}
