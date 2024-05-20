package br.com.cairu.projeto.integrador.brecho.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
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
import br.com.cairu.projeto.integrador.brecho.dtos.CategoryRequestDTO;
import br.com.cairu.projeto.integrador.brecho.dtos.MessageResponse;
import br.com.cairu.projeto.integrador.brecho.services.CategoryService;
import br.com.cairu.projeto.integrador.brecho.utils.Generic;
import br.com.cairu.projeto.integrador.brecho.utils.InitToolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateOrUpdateFragment extends Fragment {

    private Generic generic;

    public CreateOrUpdateFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_or_update, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        new InitToolbar().toolbar((AppCompatActivity) requireActivity(), toolbar, getActivity());

        Button saveCategory = view.findViewById(R.id.buttonLogin);
        EditText name = view.findViewById(R.id.inputCategoryName);
        generic = new Generic(getActivity());

        saveCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<EditText> editTexts = new ArrayList<>();

                editTexts.add(name);

                boolean verify = generic.empty(editTexts);

                if (!verify) {
//                    progressBar.setVisibility(View.VISIBLE);
//                    buttonLogin.setEnabled(false);
                    createdOrUpdate(name.getText().toString());
                }
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    public void createdOrUpdate(String name) {
        CategoryRequestDTO categoryRequest = new CategoryRequestDTO(name);

        CategoryService categoryService = new ApiClient().getClient(getActivity()).create(CategoryService.class);

        Call<MessageResponse> call = categoryService.register(categoryRequest);

        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    MessageResponse errorResponse = response.body();

                    Toast.makeText(getActivity(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();

                } else {
                    try {
                        MessageResponse errorResponse = new Gson().fromJson(response.errorBody().string(), MessageResponse.class);
                        Toast.makeText(getActivity(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }

//                progressBar.setVisibility(View.GONE);
//                buttonLogin.setEnabled(true);
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable throwable) {
                Toast.makeText(getActivity(), "Network error.", Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
//                buttonLogin.setEnabled(true);
            }
        });
    }
}