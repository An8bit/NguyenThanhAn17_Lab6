package com.example.nguyenthanhan17_lab6;

import android.app.Application;

import java.text.ParseException;
import java.util.ArrayList;

public class App extends Application { // Singleton Pattern
   DBHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
         dbHelper= new DBHelper(this);
         dbHelper.CopyDatabaseFromAssets();
         dbHelper.getInfo();
    }
}
