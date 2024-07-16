package br.com.cairu.projeto.integrador.brecho.fragment.category;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import br.com.cairu.projeto.integrador.brecho.R;
import br.com.cairu.projeto.integrador.brecho.config.ApiClient;
import br.com.cairu.projeto.integrador.brecho.dtos.category.CategoryRequestDTO;
import br.com.cairu.projeto.integrador.brecho.dtos.category.CategoryResponseDTO;
import br.com.cairu.projeto.integrador.brecho.dtos.generic.MessageResponse;
import br.com.cairu.projeto.integrador.brecho.services.CategoryService;
import br.com.cairu.projeto.integrador.brecho.utils.Generic;
import br.com.cairu.projeto.integrador.brecho.utils.InitToolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateOrUpdateCategoryFragment extends Fragment {

    private Generic generic;
    private ProgressBar progressBar;
    private Button saveCategory;
    private CategoryService categoryService;
    private CategoryResponseDTO categoryResponseDTO;
    private EditText inputCategoryName;
    private boolean isUpdate;

    public CreateOrUpdateCategoryFragment() {
    }

    public CreateOrUpdateCategoryFragment(CategoryResponseDTO categoryFragment, boolean isUpdate) {
        this.categoryResponseDTO = categoryFragment;
        this.isUpdate = isUpdate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_or_update_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryService = new ApiClient().getClient(getActivity()).create(CategoryService.class);

        Toolbar toolbar = view.findViewById(R.id.toolbar);

        new InitToolbar().toolbar((AppCompatActivity) requireActivity(), toolbar, getActivity(),false);

        saveCategory = view.findViewById(R.id.btnSaveCategory);
        inputCategoryName = view.findViewById(R.id.inputCategoryName);
        TextView titleCategory = view.findViewById(R.id.titleCategory);

        progressBar = view.findViewById(R.id.progressBar);
        generic = new Generic(requireContext());

        if (this.isUpdate) {
            inputCategoryName.setText(this.categoryResponseDTO.getName());
            saveCategory.setText("ATUALIZAR");
            titleCategory.setText("Atualizar Categoria");
        }

        saveCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<EditText> editTexts = new ArrayList<>();

                editTexts.add(inputCategoryName);

                boolean verify = generic.empty(editTexts, null, null);

                if (!verify) {
                    progressBar.setVisibility(View.VISIBLE);
                    saveCategory.setEnabled(false);
                    createdOrUpdate(inputCategoryName.getText().toString());
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
        if (this.isUpdate) {
            this.update(name);
        } else {
            this.register(name);
        }
    }

    public void register(String name) {
        CategoryRequestDTO categoryRequest = new CategoryRequestDTO(name);
        categoryService.register(categoryRequest).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    MessageResponse messageResponse = response.body();

                    Toast.makeText(getActivity(), messageResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, new CategoryFragment())
                            .addToBackStack(null)
                            .commit();
                } else {
                    try {
                        MessageResponse messageResponse = new Gson().fromJson(response.errorBody().string(), MessageResponse.class);
                        Toast.makeText(getActivity(), messageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                progressBar.setVisibility(View.GONE);
                saveCategory.setEnabled(true);
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable throwable) {
                Toast.makeText(getActivity(), "Network error.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                saveCategory.setEnabled(true);
            }
        });
    }

    public void update(String name) {
        CategoryRequestDTO categoryRequest = new CategoryRequestDTO(name);
        categoryService.update(this.categoryResponseDTO.getId(), categoryRequest).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    MessageResponse messageResponse = response.body();

                    Toast.makeText(getActivity(), messageResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, new CategoryFragment())
                            .addToBackStack(null)
                            .commit();
                } else {
                    try {
                        MessageResponse messageResponse = new Gson().fromJson(response.errorBody().string(), MessageResponse.class);
                        Toast.makeText(getActivity(), messageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                progressBar.setVisibility(View.GONE);
                saveCategory.setEnabled(true);
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable throwable) {
                Toast.makeText(getActivity(), "Network error.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                saveCategory.setEnabled(true);
            }
        });
    }
}