package com.project.hong.saying.DataModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hong on 2018-06-11.
 */

public class CommentModel implements Serializable{
    private String profileUrl;
    private String name;
    private String comment;
    private String userKey;
    private long time;

    public CommentModel() {
    }

    public CommentModel(String profileUrl, String name, String comment, String userKey, long time) {
        this.profileUrl = profileUrl;
        this.name = name;
        this.comment = comment;
        this.userKey = userKey;
        this.time = time;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
