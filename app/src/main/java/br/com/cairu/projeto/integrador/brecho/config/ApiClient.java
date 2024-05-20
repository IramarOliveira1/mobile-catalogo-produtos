package br.com.cairu.projeto.integrador.brecho.config;

import android.content.Context;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public Retrofit getClient(Context context){

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new ApiInterceptor(context))
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        
        return new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }
}
