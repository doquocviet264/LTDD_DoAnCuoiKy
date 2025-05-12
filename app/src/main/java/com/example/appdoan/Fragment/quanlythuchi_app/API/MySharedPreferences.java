package com.example.appdoan.Fragment.quanlythuchi_app.API;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    public static final String KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN";

    private static MySharedPreferences instance;
    private SharedPreferences mySharedPreferences;

    public static void init(Context context)
    {
        if(instance == null)
        {
            instance = new MySharedPreferences();
        }
        instance.mySharedPreferences = context.getSharedPreferences("MyApp", context.MODE_PRIVATE);
    }

    public static MySharedPreferences getInstance()
    {
        return instance;
    }

    public void putToken(String token)
    {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN, token);
        editor.commit();
    }

    public String getToken()
    {
        return mySharedPreferences.getString(KEY_ACCESS_TOKEN, "");
    }
}
