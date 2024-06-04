package br.com.cairu.projeto.integrador.brecho.fragment.product;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.cairu.projeto.integrador.brecho.R;
import br.com.cairu.projeto.integrador.brecho.adapter.FilterCategoryAdapter;
import br.com.cairu.projeto.integrador.brecho.adapter.ProductAdapter;
import br.com.cairu.projeto.integrador.brecho.config.ApiClient;
import br.com.cairu.projeto.integrador.brecho.dtos.category.CategoryResponseDTO;
import br.com.cairu.projeto.integrador.brecho.dtos.generic.MessageResponse;
import br.com.cairu.projeto.integrador.brecho.dtos.product.ProductAndCategory;
import br.com.cairu.projeto.integrador.brecho.dtos.product.ProductResponseDTO;
import br.com.cairu.projeto.integrador.brecho.fragment.login.LoginFragment;
import br.com.cairu.projeto.integrador.brecho.models.Category;
import br.com.cairu.projeto.integrador.brecho.services.CategoryService;
import br.com.cairu.projeto.integrador.brecho.services.ProductService;
import br.com.cairu.projeto.integrador.brecho.utils.Generic;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment implements ProductAdapter.OnItemDeleteListener, FilterCategoryAdapter.onItemClickListener {
    private Generic generic;
    private ProductAdapter productAdapter;
    private FilterCategoryAdapter filterCategoryAdapter;
    private ProductService productService;
    private ProgressBar progressBar;
    private List<ProductResponseDTO> itemList;
    private List<CategoryResponseDTO> itemListCategory;
    private List<CategoryResponseDTO> categories;
    private TextView categoryEmpty;

    public ProductFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productService = new ApiClient().getClient(getActivity()).create(ProductService.class);

        generic = new Generic(requireActivity());

        itemList = new ArrayList<>();

        categories = new ArrayList<>();

        itemListCategory = new ArrayList<>();

        categoryEmpty = view.findViewById(R.id.notfoundCategoryProduct);

        RecyclerView productRecyclerView = view.findViewById(R.id.recyclerViewProduct);

        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
        productRecyclerView.setLayoutManager(layoutManager);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        productAdapter = new ProductAdapter(itemList, this, false);
        productRecyclerView.setAdapter(productAdapter);

        EditText searchProduct = view.findViewById(R.id.searchProductList);

        searchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                productAdapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        all();
        recyclerViewFilterCategory(view);
        createOrUpdate(view);
    }

    public void recyclerViewFilterCategory(View view) {
        RecyclerView categoryFilterRecyclerView = view.findViewById(R.id.recyclerViewFilterCategory);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        categoryFilterRecyclerView.setLayoutManager(layoutManager);

        filterCategoryAdapter = new FilterCategoryAdapter(itemListCategory, this);
        categoryFilterRecyclerView.setAdapter(filterCategoryAdapter);
    }

    public void all() {
        productService.all("").enqueue(new Callback<ProductAndCategory>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ProductAndCategory> call, @NonNull Response<ProductAndCategory> response) {
                if (response.isSuccessful()) {
                    itemList.clear();
                    itemList.addAll(response.body().getProducts());
                    productAdapter.notifyDataSetChanged();

                    itemListCategory.clear();
                    CategoryResponseDTO category = new CategoryResponseDTO();
                    category.setName("Todos");
                    itemListCategory.add(category);
                    itemListCategory.addAll(response.body().getCategories());
                    filterCategoryAdapter.notifyDataSetChanged();

                    CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();

                    categoryResponseDTO.setId(0L);
                    categoryResponseDTO.setName("Selecione");
                    categories.add(categoryResponseDTO);
                    categories.addAll(response.body().getCategories());

                    progressBar.setVisibility(View.GONE);
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
            public void onFailure(@NonNull Call<ProductAndCategory> call, @NonNull Throwable throwable) {
                Toast.makeText(getActivity(), "Network error.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void filterCategory(Long id) {
        productService.filterPerCategory(id, "").enqueue(new Callback<List<ProductResponseDTO>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<ProductResponseDTO>> call, Response<List<ProductResponseDTO>> response) {
                itemList.clear();
                itemList.addAll(response.body());
                productAdapter.notifyDataSetChanged();

                if (response.body().isEmpty()) {
                    categoryEmpty.setText("Nenhum produto encontrado para essa categoria.");
                    categoryEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<ProductResponseDTO>> call, Throwable throwable) {
                Toast.makeText(getActivity(), "Network error.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void createOrUpdate(View view) {
        Button buttonTextView = view.findViewById(R.id.createProduct);
        buttonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new CreateOrUpdateProductFragment(null, false, categories)).addToBackStack(null).commit();
            }
        });
    }

    public void delete(Long id, int position) {
        productService.delete(id).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    MessageResponse messageResponse = response.body();

                    itemList.remove(position);
                    productAdapter.notifyItemRemoved(position);

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
        productService.index(id, "").enqueue(new Callback<ProductResponseDTO>() {
            @Override
            public void onResponse(Call<ProductResponseDTO> call, Response<ProductResponseDTO> response) {
                if (response.isSuccessful()) {

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new CreateOrUpdateProductFragment(response.body(), true, categories)).addToBackStack(null).commit();
                }
            }

            @Override
            public void onFailure(Call<ProductResponseDTO> call, Throwable throwable) {
                Toast.makeText(getActivity(), "Network error.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void confirmDelete(Long id, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Você deseja realmente apagar esse produto ?");
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

    @Override
    public void onUpdateItem(int position) {
        ProductResponseDTO item = itemList.get(position);

        this.index(item.getId());
    }

    @Override
    public void onItemDelete(int position) {
        ProductResponseDTO item = itemList.get(position);
        this.confirmDelete(item.getId(), position);
    }

    @Override
    public void onItemClick(String category, int position) {
    }

    @Override
    public void onItemClick(String category, int position, Long id) {
        categoryEmpty.setVisibility(View.GONE);
        if (id == null) {
            this.all();
            return;
        }

        this.filterCategory(id);
    }
}