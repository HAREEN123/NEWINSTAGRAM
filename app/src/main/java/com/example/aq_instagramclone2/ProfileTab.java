package com.example.aq_instagramclone2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;
// in here, we need to update the user info, when he is tapping the update info.
// and also when the user enter in to the app that information should be dragged from the server.

public class ProfileTab extends Fragment {

    private EditText edtProfileName,edtProfileBio,edtProfileProfession,edtProfileHobbies,edtProfileFavSport;
    private Button btnUpdateInfo;

    public ProfileTab() { // because we are inheriting from the fragments.
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, // container is the activity.// this must return something finally.
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);  // this is similar to the set content method.

        edtProfileName = view.findViewById(R.id.edtProfileName); // in here you sll put the view to initialize.
        // you must know the difference between the activity and the fragment.
        edtProfileProfession = view.findViewById(R.id.edtProfileProfession);
        edtProfileBio = view.findViewById(R.id.edtProfileBio);
        edtProfileHobbies = view.findViewById(R.id.edtProfileHobbies);
        edtProfileFavSport = view.findViewById(R.id.edtProfileFavoriteSport);

        btnUpdateInfo = view.findViewById(R.id.btnUpdateInfo);

        final ParseUser parseUser = ParseUser.getCurrentUser(); // now we have a reference to the current user.

        if(parseUser.get("profileName") == null){
            edtProfileName.setText("");
        }else{
            edtProfileName.setText(parseUser.get("profileName").toString());// to string as there is no null
        }// Think about. what happens when there are nun values. that is a problem.
        // u can not call a null value.So to avoid that delete the to String method. an put "". it puts the null type automatically.

        if (parseUser.get("profileBio")==null){
            edtProfileBio.setText("");
        }else {
            edtProfileBio.setText(parseUser.get("profileBio").toString());
        }

        if (parseUser.get("profileProfession")==null){
            edtProfileProfession.setText("");
        }else {
            edtProfileProfession.setText(parseUser.get("profileProfession").toString());
        }

        if (parseUser.get("profileHobbies")==null){
            edtProfileHobbies.setText("");
        }else {
            edtProfileHobbies.setText(parseUser.get("profileHobbies").toString());
        }

        if (parseUser.get("profileFavSport")==null){
            edtProfileFavSport.setText("");
        }else {
            edtProfileFavSport.setText(parseUser.get("profileFavSport").toString());
        }



        btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                parseUser.put("profileName",edtProfileName.getText().toString());
                parseUser.put("profileBio",edtProfileBio.getText().toString());
                parseUser.put("profileProfession",edtProfileProfession.getText().toString());
                parseUser.put("profileHobbies",edtProfileHobbies.getText().toString());
                parseUser.put("profileFavSport",edtProfileFavSport.getText().toString());

                parseUser.saveInBackground(new SaveCallback() { // you have to use the get text() as this is a fragment.
                    @Override
                    public void done(ParseException e) {

                        if(e == null){
                            FancyToast.makeText(getContext(), "Info Updated", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();
                        } else {
                            FancyToast.makeText(getContext(),e.getMessage(),FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();

                        }



                    }
                });


            }
        });

        return view;

    }
}