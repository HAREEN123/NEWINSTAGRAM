package com.example.aq_instagramclone2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {


    public TabAdapter(@NonNull FragmentManager fm) {
        super(fm);

    }

    @NonNull
    @Override
    public Fragment getItem(int tabPosition) {
        switch (tabPosition){
            case 0:
                ProfileTab profileTab = new ProfileTab();
                return profileTab;// return statement is like the break statement.
            case 1:
                UsersTab usersTab = new UsersTab();  // this is also can be written in this manner..return new UserTab();
                return usersTab;
            case 2:
                SharePictureTab sharePictureTab =new SharePictureTab(); // if none of cases are used,u just create a default statement and just return null.
                return sharePictureTab;

                default:
                return null;

        }
    }

    @Override
    public int getCount() { // how many tabs do we have inside the tab lay out.
        // whenever u need to add more tabs to your tab layout, implement and update these tab adapter class.

        return 3;
    } // WE CAN NOT CREATE AN OBJECT DIRECTLY FROM AN ABSTRACT CLASS.

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Profile";
            case 1:
                return "Users";
            case 2:
                return "Share Picture";
            default:
                return null; // because of the Char sequence, we must indicate the default value
        }
    }
}

