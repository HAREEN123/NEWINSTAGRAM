package com.example.aq_instagramclone2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SpinnerAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail,edtPassword,edtUserName;
    private Button btnSignUp,btnLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Sign Up");// to change the name of the action bar.
        edtEmail = findViewById(R.id.edtEmail);  // this is for the Main Activity..
        edtPassword = findViewById(R.id.edtPassword);
        edtUserName = findViewById(R.id.edtUserName);
        btnLogIn = findViewById(R.id.btnLogIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(this);
        btnLogIn.setOnClickListener(this); //Remember THis Must be Within the On create Method..


    }

    @Override
    public void onClick(View buttonView) {

        switch(buttonView.getId()){



        }

    }
}