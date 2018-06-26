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
    private ArrayList<String> like;

    public CommentModel() {
    }

    public CommentModel(String profileUrl, String name, String comment) {
        this.profileUrl = profileUrl;
        this.name = name;
        this.comment = comment;
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

    public ArrayList<String> getLike() {
        return like;
    }

    public void setLike(ArrayList<String> like) {
        this.like = like;
    }
}
