package com.project.hong.saying.DataModel;

import java.io.Serializable;
import java.util.ArrayList;

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

    ArrayList<String> comment;
    ArrayList<String> scrap;

    public FeedModel() {
    }

    public FeedModel(String imageUrl, String userName, String profileUrl, int gravity, String textColor, String contents,
                     long time, String userKey) {
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

    public ArrayList<String> getScrap() {
        return scrap;
    }

    public void setScrap(ArrayList<String> scrap) {
        this.scrap = scrap;
    }

    public ArrayList<String> getComment() {
        return comment;
    }

    public void setComment(ArrayList<String> comment) {
        this.comment = comment;
    }
}
