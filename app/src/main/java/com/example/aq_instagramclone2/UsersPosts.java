package com.example.aq_instagramclone2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class UsersPosts extends AppCompatActivity {

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_posts);

        linearLayout = findViewById(R.id.linearLayout);

        Intent receivedIntentObject = getIntent();
        final String receivedUserName = receivedIntentObject.getStringExtra("username");// it is going to automatically identify the intent object that is passed through this activity.
        FancyToast.makeText(this,receivedUserName, Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();

        setTitle(receivedUserName + "'s posts");

        ParseQuery<ParseUser> parseQuery = new ParseQuery<>("Photo");
        parseQuery.whereEqualTo("username",receivedUserName);// This condition is very important.
        parseQuery.orderByDescending("createdAt");

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(objects.size()> 0 && e==null){
                    for(ParseObject post : objects){
                        final TextView postDescription = new TextView(UsersPosts.this);
                        postDescription.setText(post.get("image_des") + ""); // This how we can create an object in code..
                        ParseFile postPicture = (ParseFile) post.get("picture");
                        postPicture.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {

                                if(data!=null && e == null){ // valid object from the server..

                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                    ImageView postImageView = new ImageView(UsersPosts.this);
                                    LinearLayout.LayoutParams imageView_params  = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                    imageView_params.setMargins(5,5,5,5);
                                    postImageView.setLayoutParams(imageView_params);
                                    postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    postImageView.setImageBitmap(bitmap);

                                    //also need to specify the params for the text view as well.

                                    LinearLayout.LayoutParams des_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                    des_params.setMargins(5,5,5,15);
                                    postDescription.setLayoutParams(des_params);
                                    postDescription.setGravity(Gravity.CENTER);
                                    postDescription.setBackgroundColor(Color.RED);
                                    postDescription.setTextColor(Color.WHITE);
                                    postDescription.setTextSize(30f);

                                    linearLayout.addView(postImageView);
                                    linearLayout.addView(postDescription);



                                }

                            }


                        });
                    }
                }else{
                    FancyToast.makeText(UsersPosts.this,receivedUserName + " does not have any posts..", Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
                    finish(); // we do not need to show an empty activity to the user.

                }

                dialog.dismiss();
            }

        });



    }
}