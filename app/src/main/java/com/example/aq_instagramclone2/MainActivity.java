package com.example.aq_instagramclone2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SpinnerAdapter;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

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

        if(ParseUser.getCurrentUser() != null){
            ParseUser.getCurrentUser().logOut();// this let the user to sign up whenever the app runs.so as to log out the user that already signed up or Logged in.
        }


    }

    @Override
    public void onClick(View buttonView) {

        switch(buttonView.getId()){

            case R.id.btnSignUp:
                final ParseUser appUser = new ParseUser();
                appUser.setEmail(edtEmail.getText().toString());
                appUser.setUsername(edtUserName.getText().toString());
                appUser.setPassword(edtPassword.getText().toString());

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Signing Up " + edtUserName.getText().toString());
                progressDialog.show();

                appUser.signUpInBackground(new SignUpCallback() {// Signing up to the server.
                    @Override
                    public void done(ParseException e) {
                        if(e==null){

                            FancyToast.makeText(MainActivity.this, appUser.get("username") + " is signed up successfully.", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();


                        } else {
                            FancyToast.makeText(MainActivity.this,"There was an Error "+ e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                        }

                        progressDialog.dismiss(); // we do not need to show the progress bar when signed up.

                    }
                });

                break;
            case R.id.btnLogIn:
                Intent intent = new Intent(MainActivity.this,LoginActivity.class); // this must be put in oder to move in to the next activity window...
                startActivity(intent);

                break;


        }

    }
}