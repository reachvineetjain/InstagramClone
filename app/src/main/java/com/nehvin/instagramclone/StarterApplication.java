package com.nehvin.instagramclone;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;

/**
 * Created by Neha on 27-Nov-17.
 */

public class StarterApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("c514c647deea3aa77e35027658cd076adb5bb969")
                .clientKey("531704387af1b5fd08907bc6a3b8b945fe4db705")
                .server("http://18.221.222.228:80/parse/")
                .build()
        );

//        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

    }
}