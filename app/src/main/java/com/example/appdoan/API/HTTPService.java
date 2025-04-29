package com.example.appdoan.API;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HTTPService {
    private static final String BASE_URL = "http://192.168.1.3:8080/";
    private static Retrofit retrofit = null; // Đảm bảo chỉ tạo một instance
    private static OkHttpClient client;

    // Hàm khởi tạo OkHttpClient với interceptor
    private static OkHttpClient getClient() {
        if (client == null) {
            synchronized (HTTPService.class) {  // Đảm bảo thread-safe khi tạo client
                if (client == null) {
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
                    client = new OkHttpClient.Builder()
                            .addInterceptor(interceptor)
                            .addInterceptor(new MyInterceptor())
                            .build();
                }
            }
        }
        return client;
    }

    // Hàm tạo Retrofit instance
    public static Retrofit getInstance() {
        if (retrofit == null) {
            synchronized (HTTPService.class) {  // Đảm bảo thread-safe khi tạo Retrofit
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .client(getClient())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }
}

