package com.example.appdoan.API;

import okhttp3.Interceptor;
import androidx.annotation.NonNull;
import com.example.appdoan.Helper.MySharedPreferences;
import java.io.IOException;


import okhttp3.Request;
import okhttp3.Response;
public class MyInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Lấy token từ shared preferences
        MySharedPreferences mySharedPreferences = MySharedPreferences.getInstance();
        String token = mySharedPreferences.getToken();

        // Kiểm tra nếu token không rỗng thì thêm vào header
        if (token != null && !token.isEmpty()) {
            // Thêm "Authorization" header vào request
            Request request = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer " + token) // Thêm khoảng trắng giữa "Bearer" và token
                    .build();
            return chain.proceed(request);
        }

        // Nếu không có token, tiếp tục request gốc mà không thêm header
        return chain.proceed(originalRequest);
    }
}
