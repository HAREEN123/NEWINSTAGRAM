package com.example.aq_instagramclone2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtLoginEmail,edtLoginPassword;
    private Button btnLoginActivity,btnSignUpLoginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Log In");

        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        btnLoginActivity = findViewById(R.id.btnLoginLogin);
        btnSignUpLoginActivity= findViewById(R.id.btnLoginSignUp);

        btnLoginActivity.setOnClickListener(this);
        btnSignUpLoginActivity.setOnClickListener(this);

        if(ParseUser.getCurrentUser() != null){
            ParseUser.getCurrentUser().logOut();// this let the user to sign up whenever the app runs.so as to log out the user that already signed up or Logged in.
        }



    }

    @Override
    public void onClick(View buttonView) {
        switch (buttonView.getId()){

            case R.id.btnLoginLogin:
                ParseUser.logInInBackground(edtLoginEmail.getText().toString(), edtLoginPassword.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {

                        if(user != null && e == null){

                            FancyToast.makeText(LoginActivity.this, user.getUsername() + " is logged in successfully.", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();



                        }else{

                            FancyToast.makeText(LoginActivity.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();

                        }

                    }
                });


                break;

            case R.id.btnLoginSignUp:

                break;

        }

    }
}