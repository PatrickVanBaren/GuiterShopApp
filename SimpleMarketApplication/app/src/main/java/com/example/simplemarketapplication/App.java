package com.example.simplemarketapplication;

import android.app.Application;

import com.example.simplemarketapplication.db.Database;
import com.example.simplemarketapplication.posts.PostsModule;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Database.createInstance(this);
        AuthModule.createInstance();
        PostsModule.createInstance();
    }
}
