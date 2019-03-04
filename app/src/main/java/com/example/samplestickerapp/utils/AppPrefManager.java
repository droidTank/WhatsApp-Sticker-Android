package com.example.samplestickerapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPrefManager {
    static AppPrefManager instance;
    private final SharedPreferences sharedPref;
    public static  final  String JSON_FILE_PATH="file_path";

    private AppPrefManager(Context context) {
        sharedPref = context.getSharedPreferences("app_data", Context.MODE_PRIVATE);
    }


    public static AppPrefManager getInstance(Context context) {
        if (instance == null)
            instance = new AppPrefManager(context.getApplicationContext());
        return instance;
    }

    public String getString(String key) {
        return sharedPref.getString(key, "");
    }

    public void putString(String key, String value) {
        sharedPref.edit().putString(key, value).apply();
    }
}
