package br.com.cairu.projeto.integrador.brecho.config;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.io.IOException;

import br.com.cairu.projeto.integrador.brecho.utils.Generic;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiInterceptor implements Interceptor {

    private Context context;

    public ApiInterceptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

//        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);

        Request originalRequest = chain.request();
        Request.Builder builder = originalRequest.newBuilder()
                .header("Authorization", new Generic(context).getToken())
                .header("Content-Type", "application/json");

        Request newRequest = builder.build();
        return chain.proceed(newRequest);
    }
}
