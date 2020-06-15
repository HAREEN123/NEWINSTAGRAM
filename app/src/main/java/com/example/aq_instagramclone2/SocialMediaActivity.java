package com.example.aq_instagramclone2;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;

public class SocialMediaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAdapter tabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        setTitle("SocIal Media APP!!!");

        toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPager);
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager,false);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.postImageItem){
            if(Build.VERSION.SDK_INT >=23 && checkSelfPermission(// Android can not access the dangerous operations
                    // without asking the permission firstly.it is for API 23 and above versions.
                    Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

                requestPermissions(new String[]
                        {Manifest.permission.READ_EXTERNAL_STORAGE},3000);
            }else{
                captureImage();
            }


        }else if(item.getItemId()==R.id.logoutUserItem){

            ParseUser.getCurrentUser().logOut();
            finish(); // Social media activity will be eliminated from the stack.
            Intent intent = new Intent(SocialMediaActivity.this,MainActivity.class); // this must be put in oder to move in to the next activity window...
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 3000){
            if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                captureImage();
            }
        }

    }

    private void captureImage() {
        Intent intent = new Intent(Intent.ACTION_PICK // we need to capture an image
                , MediaStore.Images.Media.EXTERNAL_CONTENT_URI); // So as to access external images , we have to use this code.
        // it might be the camera application,gallery application
        // we need to create an intent because the images application of the user's device is another intent.
        startActivityForResult(intent,4000); // we need to get an image as a result.Result bight be a image or no image.
        //request code shall be unique. it has not been already used.
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {// nullable means that data or image will be null. nothing.
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==4000 && resultCode==RESULT_OK && data!=null){//REQUEST_IMAGE_CAPTURE_FRAGMENT
                try{
                    Uri capturedImage = data.getData();// ASSIGNING AN IMAGE TO THE VARIABLE.
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),capturedImage);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();

                    ParseFile parseFile = new ParseFile("img.png",bytes);
                    ParseObject parseObject = new ParseObject("Photo");// we are going to have a new class in the parse dashboard.
                    parseObject.put("picture",parseFile);// parse file means our image.
                    parseObject.put("username", ParseUser.getCurrentUser().getUsername());// who has uploaded the image to the server.
                    // that is why we pass the user name.also we have a user name column in the photo class.
                    // the reason creating this column is that later we need to get this image from this specific user so that we need this user name
                    // so that we can parse this image from the server.
                    final ProgressDialog dialog = new ProgressDialog(this);
                    dialog.setMessage("Loading...");
                    dialog.show();

                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                            if(e==null){

                                FancyToast.makeText(SocialMediaActivity.this, "Done!!!", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,true).show();

                            }else {

                                FancyToast.makeText(SocialMediaActivity.this,"Unknown Error: " + e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR,true).show();
                            }

                            dialog.dismiss();

                        }
                    });




                }catch (Exception e){

                    e.printStackTrace();
                }
            }
        }



    }