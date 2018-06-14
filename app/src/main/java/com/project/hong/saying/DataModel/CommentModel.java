package com.project.hong.saying.DataModel;

import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hong on 2018-06-11.
 */

public class CommentModel {
    private CircleImageView profileUrl;
    private String name;
    private String comment;

    public CommentModel() {
    }

    public CommentModel(CircleImageView profileUrl, String name, String comment) {
        this.profileUrl = profileUrl;
        this.name = name;
        this.comment = comment;
    }

    public CircleImageView getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(CircleImageView profileUrl) {
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
}
