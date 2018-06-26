package com.project.hong.saying.AccountPackage.EditPackage;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.hong.saying.DataModel.CommentModel;
import com.project.hong.saying.DataModel.QuestionModel;
import com.project.hong.saying.R;
import com.project.hong.saying.Util.SharedPreference;

/**
 * Created by hong on 2018-06-08.
 */

public class SuggestActivity extends Dialog implements View.OnClickListener, OnSuccessListener<Void>, OnFailureListener {

    private EditText editEmail, editSuggest;
    private Button cancelBt, okBt;
    private String userNameSt;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference qnaData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom);

        initView();

    }

    private void initView() {
        editEmail = findViewById(R.id.edit_reply);
        editSuggest = findViewById(R.id.edit_suggest);
        cancelBt = findViewById(R.id.cancel_bt);
        okBt = findViewById(R.id.ok_bt);

        okBt.setOnClickListener(this);
        cancelBt.setOnClickListener(this);
    }

    public SuggestActivity(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_bt:
                if (editSuggest.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "내용을 입력해주세요", Toast.LENGTH_LONG).show();
                } else {
                    if (editEmail.getText().toString().isEmpty()) {
                        editEmail.setText("");
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

        String uid = firebaseAuth.getCurrentUser().getUid();
        SharedPreference sharedPreference = new SharedPreference();
        userNameSt = sharedPreference.getValue(getContext(), "userName", "");
        QuestionModel questionModel = new QuestionModel(uid, userNameSt, editEmail.getText().toString(), editSuggest.getText().toString());
        qnaData = database.getReference().child("question");
        qnaData.push().setValue(questionModel).addOnSuccessListener(this).addOnFailureListener(this);

    }

    @Override
    public void onSuccess(Void aVoid) {
        dismiss();
        Toast.makeText(getContext(), "문의하였습니다", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        dismiss();
        Toast.makeText(getContext(), "실패하였습니다", Toast.LENGTH_SHORT).show();
    }
}
