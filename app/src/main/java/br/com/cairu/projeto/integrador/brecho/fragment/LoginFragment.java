package br.com.cairu.projeto.integrador.brecho.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import br.com.cairu.projeto.integrador.brecho.R;
import br.com.cairu.projeto.integrador.brecho.config.ApiClient;
import br.com.cairu.projeto.integrador.brecho.dtos.ErrorResponse;
import br.com.cairu.projeto.integrador.brecho.dtos.LoginRequestDTO;
import br.com.cairu.projeto.integrador.brecho.dtos.LoginResponseDTO;
import br.com.cairu.projeto.integrador.brecho.services.LoginService;
import br.com.cairu.projeto.integrador.brecho.utils.Generic;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    public EditText email;
    public EditText password;
    public BottomNavigationView bottomNavigationView;
    public Button buttonLogin;

    public Generic generic;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.GONE);
        return inflater.inflate(R.layout.fragment_login, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        generic = new Generic(getActivity());

        buttonLogin = view.findViewById(R.id.buttonLogin);
        email = view.findViewById(R.id.editEmail);
        password = view.findViewById(R.id.editPassword);
        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<EditText> testando = new ArrayList<>();

                testando.add(email);
                testando.add(password);

                boolean verify = generic.empty(testando);

                if (verify) {
                    login(email.getText().toString(), password.getText().toString());
                }
            }
        });
    }

    public void login(String email, String password) {
        LoginRequestDTO loginRequest = new LoginRequestDTO(email, password);

        LoginService loginService = new ApiClient().getClient().create(LoginService.class);

        Call<LoginResponseDTO> call = loginService.login(loginRequest);
        call.enqueue(new Callback<LoginResponseDTO>() {
            @Override
            public void onResponse(Call<LoginResponseDTO> call, Response<LoginResponseDTO> response) {
                if (response.isSuccessful()) {
                    LoginResponseDTO loginResponseDTO = response.body();

                    generic.saveToken(loginResponseDTO.getToken());
                    generic.saveUsername(loginResponseDTO.getName());

                    if (!loginResponseDTO.isAdmin()) {
                        Menu menu = bottomNavigationView.getMenu();
                        menu.removeItem(R.id.user);
                    }

                    changeFrament();
                } else {
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        Toast.makeText(getActivity(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponseDTO> call, Throwable throwable) {
                Toast.makeText(getActivity(), "Network error.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void changeFrament() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, new HomeFragment())
                .addToBackStack(null)
                .commit();

        bottomNavigationView.setVisibility(View.VISIBLE);
    }
}