package com.project.hong.saying.DataBase;

import com.project.hong.saying.DataModel.FeedModel;

/**
 * Created by hong on 2018-05-08.
 */

public interface FeedDataCallback {
    void getFeedData(FeedModel feedModel, String key);
}
