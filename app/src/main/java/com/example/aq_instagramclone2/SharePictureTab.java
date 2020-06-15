package com.example.aq_instagramclone2;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;


public class SharePictureTab extends Fragment implements View.OnClickListener {

    private ImageView imgShare;
    private EditText edtDescription;
    private Button btnShareImage;

    Bitmap receivedImageBitmap;


    public SharePictureTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_picture_tab, container, false);

        imgShare = view.findViewById(R.id.imgShare);
        edtDescription = view.findViewById(R.id.edtDes);
        btnShareImage = view.findViewById(R.id.btnShareImage);

        imgShare.setOnClickListener(SharePictureTab.this);
        btnShareImage.setOnClickListener(SharePictureTab.this);

        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.imgShare:
                if(Build.VERSION.SDK_INT >=23 && ActivityCompat.checkSelfPermission(getContext(),// Android can not access the dangerous operations
                        // without asking the permission firstly.it is for API 23 and above versions.
                        Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]
                            {Manifest.permission.READ_EXTERNAL_STORAGE},1000);
                }else{
                    getChosenImage();
                }
                break;
            case R.id.btnShareImage:

                if (receivedImageBitmap != null){
                    // in this case, we allow the user to share or upload to the server and later we can get that from the server.
                    if(edtDescription.getText().toString().equals("")){

                        FancyToast.makeText(getContext(), "ERROR: You must enter an description", FancyToast.LENGTH_SHORT, FancyToast.ERROR,true).show();

                    }else {

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();// because we need to upload an image to the server,we need to covert into an array of bytes.
                        //when getting it back,it will convert into the image itself.
                        receivedImageBitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        ParseFile parseFile = new ParseFile("img.png",bytes);
                        ParseObject parseObject = new ParseObject("Photo");// we are going to have a new class in the parse dashboard.
                        parseObject.put("picture",parseFile);// parse file means our image.
                        parseObject.put("image_des",edtDescription.getText().toString());// this is a new column to the class naming the Description.
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());// who has uploaded the image to the server.
                        // that is why we pass the user name.also we have a user name column in the photo class.
                        // the reason creating this column is that later we need to get this image from this specific user so that we need this user name
                        // so that we can parse this image from the server.
                        final ProgressDialog dialog = new ProgressDialog(getContext());
                        dialog.setMessage("Loading...");
                        dialog.show();

                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                if(e==null){

                                    FancyToast.makeText(getContext(), "Done!!!", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,true).show();

                                }else {

                                    FancyToast.makeText(getContext(),"Unknown Error: " + e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR,true).show();
                                }

                                dialog.dismiss();

                            }
                        });


                    }
                }else {
                    FancyToast.makeText(getContext(), "ERROR: You must select an image", FancyToast.LENGTH_SHORT, FancyToast.ERROR,true).show();
                }

                break;






        }

    }

    private void getChosenImage() {

        //FancyToast.makeText(getContext(), " Now you can access the images", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();

        Intent intent = new Intent(Intent.ACTION_PICK // we need to capture an image
                , MediaStore.Images.Media.EXTERNAL_CONTENT_URI); // So as to access external images , we have to use this code.
        // it might be the camera application,gallery application
        // we need to create an intent because the images application of the user's device is another intent.
        startActivityForResult(intent,2000); // we need to get an image as a result.Result bight be a image or no image.



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000){
            if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getChosenImage();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {// intent data contain your image.
        super.onActivityResult(requestCode, resultCode, data);
        // accessing an image from the fragment is completely different from the activities.

        if(requestCode==2000){//REQUEST_IMAGE_CAPTURE_FRAGMENT
            if(resultCode== Activity.RESULT_OK){// OK MEANS WE CAN GET AN IMAGE SUCCESSFULLY.
                // dO SOMETHING WITH YOUR CAPTURED IMAGE
                try{
                    Uri selectedImage = data.getData();// ASSIGNING AN IMAGE TO THE VARIABLE.
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};// THIS ARRAY STORES THE IMAGE. ONLY ONE.
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,filePathColumn,null,null,null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);// the path of our picture.
                    cursor.close();
                    receivedImageBitmap = BitmapFactory.decodeFile(picturePath);
                    imgShare.setImageBitmap(receivedImageBitmap);

                }catch (Exception e){

                    e.printStackTrace();
                }
            }
        }

        // if you are using a fragment so as to access the images and get an image from the user's device.
        // These are the Codes that you need to write in order to access the images and then put it on the image view.
        // This process is completely different from an activity.we learn about that in the next tutorials.




    }
}