package com.example.aq_instagramclone2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class UsersTab extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView listView;
    private ArrayList<String>arrayList;
    private ArrayAdapter arrayAdapter;

    public UsersTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_users_tab, container, false);

        listView = view.findViewById(R.id.listView);// here i need to populate the list view the parse users that we get from the server.
        // This will automatically update based on the data from the array adapter.
        // here we need to ged the all users if not, objects from the server.
        arrayList = new ArrayList(); // to take a array list
        arrayAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,arrayList); // let the list view we have an array adapter.
        listView.setOnItemClickListener(UsersTab.this);// Whenever the user taps on one of the items of our list view ,users tab will be notified.then we can do something about that.
        listView.setOnItemLongClickListener(UsersTab.this);
        final TextView txtLoadingUsers = view.findViewById(R.id.txtLoadingUsers);

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();

        parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername()); // we do not need the current user from the query as he is already the user.

        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) { // users is a list.

                if(e==null){
                    if(users.size()>0){

                        for(ParseUser user: users){

                            arrayList.add(user.getUsername()); // in order to populate our list view.
                            // We need to have an Array adapter and this will update the list view. otherwise, we can not interact with the list view directly.
                        }

                        listView.setAdapter(arrayAdapter);
                        txtLoadingUsers.animate().alpha(0).setDuration(2000);
                        listView.setVisibility(View.VISIBLE);


                    }
                }

            }
        });

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(getContext(),UsersPosts.class);
        intent.putExtra("username",arrayList.get(position));// put extra is used to send data from one activity or a fragment to another.
        // array list we created earlier holds the all user names of the parse users. IF IT IS AN OBJECT, IT WILL SHOW AN ERROR.
        // the position is going to be the index if we pass the index to this array list,it is going to give us the value that is related to that position. it means the user name in the emulator. Jhon will be the index0
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("username",arrayList.get(position));
        parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(user != null && e == null){
                    //FancyToast.makeText(getContext(),user.get("profileProfession") + "", Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                    final PrettyDialog prettyDialog = new PrettyDialog(getContext());
                    prettyDialog.setTitle(user.getUsername() + " 's Info").setMessage(user.get("profileBio") + "\n"
                    + user.get("profileProfession") + "\n" + user.get("profileHobbies") + "\n" +
                            user.get("profileFavSport")).setIcon(R.drawable.person).addButton("OK", R.color.pdlg_color_white, R.color.pdlg_color_green, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {

                            prettyDialog.dismiss();

                        }


                    })
                    .show();
                }
            }
        });


        return true;
    }
}