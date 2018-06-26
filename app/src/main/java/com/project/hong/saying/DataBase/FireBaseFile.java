package com.project.hong.saying.DataBase;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

/**
 * Created by hong on 2018-05-08.
 */

public class FireBaseFile implements OnSuccessListener<UploadTask.TaskSnapshot>, OnFailureListener {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference("feedImage");
    FileCallback fileCallback;

    public FireBaseFile(FileCallback fileCallback) {
        this.fileCallback = fileCallback;
    }

    public void fileUpload(final String bucket, String path, Context context) {
        Glide.with(context).downloadOnly().load(path)
                .into(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                        Uri uri = Uri.fromFile(resource);
                        Log.d("uriuri", uri.toString());
                        storageReference = storage.getReference(bucket + "/" + System.currentTimeMillis() + uri.getLastPathSegment());
                        storageReference.putFile(uri).addOnSuccessListener(FireBaseFile.this).addOnFailureListener(FireBaseFile.this);
                    }
                });


    }


//    @Override
//    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//        if (task.isSuccessful()) {
//            String url = task.getResult().getDownloadUrl().toString();
//            fileCallback.completeFileUpload(url);
//        } else {
//            fileCallback.completeFileUpload(null);
//        }
//    }

    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        String url = taskSnapshot.getDownloadUrl().toString();
        fileCallback.completeFileUpload(url);
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        fileCallback.completeFileUpload(null);
        Log.d("asdasdasd", e.getMessage());
    }



}
