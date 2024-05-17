package br.com.cairu.projeto.integrador.brecho.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

import java.util.ArrayList;

public class Generic {

    private SharedPreferences sharedPreferences;
    private static final String TOKEN_KEY = "jwt_token";
    private static final String USERNAME_KEY = "userName";

    public Generic(Context context) {
       sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(TOKEN_KEY, null);
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void saveUsername(String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME_KEY, username);
        editor.apply();
    }

    public String getUsername() {
        return sharedPreferences.getString(USERNAME_KEY, null);
    }

    public boolean empty(ArrayList<EditText> editTexts){
        boolean teste = false;

        editTexts.forEach((n) -> {
            if (n.getText().toString().trim().isEmpty()){
                n.setError("PREENCHA TODOS OS CAMPOS!");
            }
        });

        return  teste;
    }
}
