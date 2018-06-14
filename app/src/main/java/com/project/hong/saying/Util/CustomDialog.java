package com.project.hong.saying.Util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.hong.saying.R;

/**
 * Created by hong on 2018-06-08.
 */

public class CustomDialog extends Dialog implements View.OnClickListener {

    private EditText editName, editReply, editSuggest;
    private Button cancelBt, okBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom);

        initView();

    }

    private void initView() {
        editName = findViewById(R.id.edit_name);
        editReply = findViewById(R.id.edit_reply);
        editSuggest = findViewById(R.id.edit_suggest);
        cancelBt = findViewById(R.id.cancel_bt);
        okBt = findViewById(R.id.ok_bt);

        okBt.setOnClickListener(this);
        cancelBt.setOnClickListener(this);
    }

    public CustomDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_bt:
                if (editName.getText().toString().isEmpty() | editSuggest.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "내용을 입력해주세요", Toast.LENGTH_LONG).show();
                } else {
                    if (editReply.getText().toString().isEmpty()) {
                        editReply.setText("");
                        sendEmail();
                    } else {
                        sendEmail();
                    }
                }
                break;

            case R.id.cancel_bt:
                cancel();
                break;
        }

    }

    private void sendEmail() {

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"hwang-junhong@naver.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, editName.getText().toString());
        intent.putExtra(Intent.EXTRA_TEXT, editReply.getText().toString() + "\n" + editSuggest.getText().toString());
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            getContext().startActivity(intent);
            Toast.makeText(getContext(), "완료하였습니다", Toast.LENGTH_SHORT).show();
            dismiss();
        }

    }

}
