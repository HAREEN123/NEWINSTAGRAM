package com.example.aq_instagramclone2;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("yyWvyQcbSTG2JX4hzyhFZzDMKO3HyOfceP9kqGo6")
                // if defined
                .clientKey("jcLuCtQrplQGT4ozXZN7NVfWnM6YQ5udlIGe5mrs")
                .server("https://parseapi.back4app.com/")
                .build()
        );

    }
}


