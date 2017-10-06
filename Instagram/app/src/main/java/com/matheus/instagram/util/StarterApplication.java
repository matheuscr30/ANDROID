package com.matheus.instagram.util;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;

/**
 * Created by matheus on 07/09/17.
 */

public class StarterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Habilite armazenamento local.
        Parse.enableLocalDatastore(this);

        // Codigo de configuração do App
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("matheus2230")
                .clientKey(null)
                .server("http://instagram2285.herokuapp.com/parse/")
                .build()
        );

        //ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        defaultACL.setPublicReadAccess(true);
        //ParseACL.setDefaultACL(defaultACL, true);
    }
}