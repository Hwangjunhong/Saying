package com.project.hong.saying.Upload.SettingPackage;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.project.hong.saying.R;
import com.rtugeek.android.colorseekbar.ColorSeekBar;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hong on 2018-04-19.
 */

public class ColorPickerFragment extends Fragment implements View.OnClickListener, ColorSeekBar.OnColorChangeListener {


    ColorCallback colorCallback;
    ButtonClick buttonClick;
    ColorSeekBar colorSeekBar;
    RelativeLayout cancel, complete;
    CircleImageView white, black;

    boolean isFirst = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.upload_colorpicker, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        colorSeekBar = view.findViewById(R.id.colorSeekBar);
        cancel = view.findViewById(R.id.cancel);
        complete = view.findViewById(R.id.complete);
        white = view.findViewById(R.id.white);
        black = view.findViewById(R.id.black);

        cancel.setOnClickListener(this);
        complete.setOnClickListener(this);
        colorSeekBar.setOnColorChangeListener(this);
        white.setOnClickListener(this);
        black.setOnClickListener(this);
    }

    public void setColorCallback(ColorCallback colorCallback) {
        this.colorCallback = colorCallback;
    }

    public void setButtonClick(com.project.hong.saying.Upload.SettingPackage.ButtonClick buttonClick) {
        this.buttonClick = buttonClick;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.white:
                colorCallback.getTextColor(Color.WHITE);
                break;
            case R.id.black:
                colorCallback.getTextColor(Color.BLACK);
                break;
            default:
                buttonClick.onClickEvent(v);
                break;

        }

    }

    @Override
    public void onColorChangeListener(int i, int i1, int color) {
        if(!isFirst){
            colorCallback.getTextColor(color);
        }else{
            isFirst = false;
        }
    }
}
