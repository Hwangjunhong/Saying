package com.project.hong.saying.DataBase;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

/**
 * Created by hong on 2018-05-08.
 */

public class FireBaseFile implements OnCompleteListener<UploadTask.TaskSnapshot> {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference("feedImage");
    FileCallback fileCallback;

    public FireBaseFile(FileCallback fileCallback) {
        this.fileCallback = fileCallback;
    }

    public void fileUpload(String bucket, File file) {
        Uri uri = Uri.fromFile(file);
        storageReference = storage.getReference(bucket + "/" + System.currentTimeMillis() + uri.getLastPathSegment());
        storageReference.putFile(uri).addOnCompleteListener(this);

    }

    @Override
    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
        if (task.isSuccessful()) {
            String url = task.getResult().getDownloadUrl().toString();
            fileCallback.completeFileUpload(url);
        } else {
            fileCallback.completeFileUpload(null);
        }
    }
}
