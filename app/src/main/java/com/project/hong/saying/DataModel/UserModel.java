package com.project.hong.saying.DataModel;

import java.util.ArrayList;

/**
 * Created by hong on 2018-05-22.
 */

public class UserModel {
    private String profileUrl;
    private String name;
    private String password;
    private ArrayList<String> scrapList;


    public UserModel() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
