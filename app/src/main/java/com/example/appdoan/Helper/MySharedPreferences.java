package com.example.appdoan.Helper;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    public static final String KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN";

    private static MySharedPreferences instance;
    private SharedPreferences mySharedPreferences;

    // Khởi tạo đối tượng SharedPreferences chỉ một lần
    public static void init(Context context) {
        if (instance == null) {
            instance = new MySharedPreferences();
            instance.mySharedPreferences = context.getSharedPreferences("MyApp", Context.MODE_PRIVATE);
        }
    }

    // Trả về đối tượng instance của MySharedPreferences
    public static MySharedPreferences getInstance() {
        return instance;
    }

    // Lưu token vào SharedPreferences
    public void putToken(String token) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN, token);
        editor.apply();  // Thay vì commit(), sử dụng apply() để tối ưu hóa.
    }

    // Lấy token từ SharedPreferences
    public String getToken() {
        return mySharedPreferences.getString(KEY_ACCESS_TOKEN, "");
    }
}

