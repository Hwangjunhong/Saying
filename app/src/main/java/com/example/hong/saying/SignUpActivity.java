package com.example.hong.saying;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.hong.saying.DataBase.FirebaseData;
import com.example.hong.saying.DataModel.UserModel;
import com.example.hong.saying.Util.SharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    EditText editEmail, editPw, editName;
    Button signUpBt;
    TextInputLayout edit1, edit2, edit3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initView();

    }

    private void initView() {
        editEmail = findViewById(R.id.edit_email);
        editPw = findViewById(R.id.edit_pw);
        signUpBt = findViewById(R.id.sign_up_bt);

        signUpBt.setOnClickListener(this);
    }


    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            UserModel userModel = new UserModel();
                            SharedPreference sharedPreference = new SharedPreference();

                            String name = editName.getText().toString();
                            sharedPreference.getValue(SignUpActivity.this, "name", name);

                            Toast.makeText(SignUpActivity.this, "회원가입 성공", Toast.LENGTH_LONG).show();


                        } else {

                        }

                        // ...
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up_bt:
                createUser(editEmail.getText().toString(), editPw.getText().toString());
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
