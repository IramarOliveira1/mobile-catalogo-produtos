package br.com.cairu.projeto.integrador.brecho.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.santalu.maskara.widget.MaskEditText;

import java.util.ArrayList;

import br.com.cairu.projeto.integrador.brecho.R;

public class Generic {

    private final SharedPreferences sharedPreferences;
    private static final String TOKEN_KEY = "jwt_token";
    private static final String USERNAME_KEY = "userName";
    private static final String USERID_KEY = "userId";
    private static final String ISADMIN_KEY = "isAdmin";

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

    public void setUserId(Long id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(USERID_KEY, id);
        editor.apply();
    }

    public Long getUserId() {
        return sharedPreferences.getLong(USERID_KEY, 0L);
    }

    public void setIsAdmin(boolean isAdmin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ISADMIN_KEY, isAdmin);
        editor.apply();
    }

    public Boolean getIsAdmin() {
        return sharedPreferences.getBoolean(ISADMIN_KEY, false);
    }


    public boolean empty(ArrayList<EditText> editTexts, @Nullable ArrayList<MaskEditText> maskEditTexts, @Nullable ArrayList<CurrencyEditText> currencyEditTexts) {
        boolean validate = false;

        for (EditText editText : editTexts) {
            if (editText.getText().toString().trim().isEmpty()) {
                editText.setError("Preencha este campo.");
                validate = true;
            }
        }

        if (maskEditTexts != null) {
            for (MaskEditText maskEditText : maskEditTexts) {
                if (maskEditText.getText().toString().trim().isEmpty()) {
                    maskEditText.setError("Preencha este campo.");
                    validate = true;
                }
            }
        }
        if (currencyEditTexts != null) {
            for (CurrencyEditText currencyEditText : currencyEditTexts) {
                if (currencyEditText.getText().toString().trim().isEmpty()) {
                    currencyEditText.setError("Preencha este campo.");
                    validate = true;
                }
            }
        }

        return validate;
    }
}
