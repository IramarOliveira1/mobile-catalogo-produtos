package br.com.cairu.projeto.integrador.brecho.fragment;

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
import android.widget.Toast;

import java.util.List;

import br.com.cairu.projeto.integrador.brecho.R;
import br.com.cairu.projeto.integrador.brecho.adapter.CategoryAdapter;
import br.com.cairu.projeto.integrador.brecho.config.ApiClient;
import br.com.cairu.projeto.integrador.brecho.dtos.CategoryResponseDTO;
import br.com.cairu.projeto.integrador.brecho.services.CategoryService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoryFragment extends Fragment {

    private RecyclerView recyclerView;

    private CategoryAdapter categoryAdapter;

    public CategoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.all(view);
        this.createOrUpdate(view);
    }

    public void all(View view) {
        CategoryService categoryService = new ApiClient().getClient(getActivity()).create(CategoryService.class);

        Call<List<CategoryResponseDTO>> call = categoryService.categoryResponseDTO();

        recyclerView = view.findViewById(R.id.recycleViewCategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        call.enqueue(new Callback<List<CategoryResponseDTO>>() {
            @Override
            public void onResponse(Call<List<CategoryResponseDTO>> call, Response<List<CategoryResponseDTO>> response) {
                List<CategoryResponseDTO> categoryResponseDTO = response.body();

                categoryAdapter = new CategoryAdapter(categoryResponseDTO);

                recyclerView.setAdapter(categoryAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<CategoryResponseDTO>> call, @NonNull Throwable throwable) {
                Toast.makeText(getActivity(), "Network error.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createOrUpdate(View view) {

        Button buttonTextView = view.findViewById(R.id.createCategory);

        buttonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new CreateOrUpdateFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

}