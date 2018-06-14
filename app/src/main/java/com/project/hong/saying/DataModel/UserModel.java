package com.project.hong.saying.DataModel;

import java.util.ArrayList;

/**
 * Created by hong on 2018-05-22.
 */

public class UserModel {
    private String profileUrl;
    private String name;
    private ArrayList<String> scrapList;
    private ArrayList<String> commentList;


    public UserModel() {
    }

    public UserModel(String name) {
        this.name = name;
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

    public ArrayList<String> getScrapList() {
        return scrapList;
    }

    public void setScrapList(ArrayList<String> scrapList) {
        this.scrapList = scrapList;
    }

    public ArrayList<String> getCommentList() {
        return commentList;
    }

    public void setCommentList(ArrayList<String> commentList) {
        this.commentList = commentList;
    }
}
