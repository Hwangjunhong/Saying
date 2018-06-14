package com.project.hong.saying.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import com.project.hong.saying.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by hong on 2018-05-17.
 */

public class LayoutToImage {

    public interface SaveImageCallback{
        void imageResult(int status, Uri uri);
    }

    SaveImageCallback saveImageCallback;

    public void setSaveImageCallback(SaveImageCallback saveImageCallback) {
        this.saveImageCallback = saveImageCallback;
    }

    public void saveBitMap(Context context, View drawView) {
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), context.getString(R.string.app_name));
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated) {
                if(saveImageCallback != null){
                    saveImageCallback.imageResult(400, null);
                }
                return;
            }
        }
        String filename = pictureFileDir.getPath() + File.separator + System.currentTimeMillis() + ".jpg";
        File pictureFile = new File(filename);
        Bitmap bitmap = getBitmapFromView(drawView);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            if(saveImageCallback != null){
                saveImageCallback.imageResult(400, null);
            }
        }
        scanGallery(context, pictureFile.getAbsolutePath());
    }

    //create bitmap from view and returns it
    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    // used for scanning gallery
    private void scanGallery(Context context, String path) {
        try {
            MediaScannerConnection.scanFile(context, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    if(saveImageCallback != null){
                        saveImageCallback.imageResult(200, uri);
                    }

                }
            });
        } catch (Exception e) {
            if(saveImageCallback != null){
                saveImageCallback.imageResult(400, null);
            }
        }
    }
}
