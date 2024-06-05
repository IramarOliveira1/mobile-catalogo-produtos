package br.com.cairu.projeto.integrador.brecho.fragment.category;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.cairu.projeto.integrador.brecho.R;
import br.com.cairu.projeto.integrador.brecho.adapter.CategoryAdapter;
import br.com.cairu.projeto.integrador.brecho.config.ApiClient;
import br.com.cairu.projeto.integrador.brecho.dtos.category.CategoryResponseDTO;
import br.com.cairu.projeto.integrador.brecho.dtos.generic.MessageResponse;
import br.com.cairu.projeto.integrador.brecho.fragment.login.LoginFragment;
import br.com.cairu.projeto.integrador.brecho.services.CategoryService;
import br.com.cairu.projeto.integrador.brecho.utils.Generic;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoryFragment extends Fragment implements CategoryAdapter.OnItemDeleteListener {

    private  TextView notFound;
    private Generic generic;
    private ProgressBar progressBar;
    private CategoryAdapter categoryAdapter;
    private CategoryService categoryService;
    private List<CategoryResponseDTO> itemList;

    public CategoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycleViewCategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        generic = new Generic(requireContext());

        notFound = view.findViewById(R.id.notFoundCategory);

        itemList = new ArrayList<>();

        categoryService = new ApiClient().getClient(getActivity()).create(CategoryService.class);

        categoryAdapter = new CategoryAdapter(itemList, this);
        recyclerView.setAdapter(categoryAdapter);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        this.all();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.createOrUpdate(view);
    }

    public void all() {
        categoryService.all().enqueue(new Callback<List<CategoryResponseDTO>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<CategoryResponseDTO>> call, @NonNull Response<List<CategoryResponseDTO>> response) {
                if (response.isSuccessful()) {
                    List<CategoryResponseDTO> categoryResponseDTO = response.body();
                    itemList.clear();
                    itemList.addAll(response.body());
                    categoryAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);

                    if (response.body().isEmpty()){
                        notFound.setText("Nenhuma categoria encontrada.");
                        notFound.setVisibility(View.VISIBLE);
                    }
                }

                if (response.code() == 403) {
                    generic.clear();
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), "Token expirado faça login novamente.", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameLayout, new LoginFragment())
                                .addToBackStack(null)
                                .commit();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CategoryResponseDTO>> call, @NonNull Throwable throwable) {
                Toast.makeText(getActivity(), "Network error.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    public void createOrUpdate(View view) {
        Button buttonTextView = view.findViewById(R.id.createCategory);

        buttonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, new CreateOrUpdateCategoryFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    public void delete(Long id, int position) {
        categoryService.delete(id).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(@NonNull Call<MessageResponse> call, @NonNull Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    MessageResponse messageResponse = response.body();

                    itemList.remove(position);
                    categoryAdapter.notifyItemRemoved(position);

                    if (itemList.isEmpty()){
                        notFound.setText("Nenhuma categoria encontrada.");
                        notFound.setVisibility(View.VISIBLE);
                    }

                    Toast.makeText(getActivity(), messageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        MessageResponse errorResponse = new Gson().fromJson(response.errorBody().string(), MessageResponse.class);
                        Toast.makeText(getActivity(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable throwable) {
                Toast.makeText(getActivity(), "Network error.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void index(Long id) {
        categoryService.index(id).enqueue(new Callback<CategoryResponseDTO>() {
            @Override
            public void onResponse(@NonNull Call<CategoryResponseDTO> call, @NonNull Response<CategoryResponseDTO> response) {
                if (response.isSuccessful()) {
                    CategoryResponseDTO categoryResponseDTO = response.body();

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, new CreateOrUpdateCategoryFragment(categoryResponseDTO, true))
                            .addToBackStack(null)
                            .commit();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryResponseDTO> call, @NonNull Throwable throwable) {
                Toast.makeText(getActivity(), "Network error.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onUpdateItem(int position) {
        CategoryResponseDTO item = itemList.get(position);

        this.index(item.getId());
    }

    @Override
    public void onItemDelete(int position) {
        CategoryResponseDTO item = itemList.get(position);
        this.confirmDelete(item.getId(), position);
    }

    public void confirmDelete(Long id, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Você deseja realmente apagar essa categoria ?");
        builder.setPositiveButton("Apagar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete(id, position);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}