package com.project.hong.saying.DataModel;

import java.io.Serializable;

/**
 * Created by hong on 2018-06-15.
 */

public class QuestionModel {
    String uid;
    String userName;
    String userEmail;
    String contents;

    public QuestionModel(String uid, String userName, String userEmail, String contents) {
        this.uid = uid;
        this.userName = userName;
        this.userEmail = userEmail;
        this.contents = contents;
    }
}
