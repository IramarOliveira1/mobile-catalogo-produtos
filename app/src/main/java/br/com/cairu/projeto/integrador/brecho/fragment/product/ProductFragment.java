package br.com.cairu.projeto.integrador.brecho.fragment.product;

import android.annotation.SuppressLint;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.cairu.projeto.integrador.brecho.R;
import br.com.cairu.projeto.integrador.brecho.adapter.FilterCategoryAdapter;
import br.com.cairu.projeto.integrador.brecho.adapter.ProductAdapter;
import br.com.cairu.projeto.integrador.brecho.config.ApiClient;
import br.com.cairu.projeto.integrador.brecho.dtos.product.ProductAndCategory;
import br.com.cairu.projeto.integrador.brecho.dtos.product.ProductResponseDTO;
import br.com.cairu.projeto.integrador.brecho.models.Category;
import br.com.cairu.projeto.integrador.brecho.services.ProductService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment implements ProductAdapter.OnItemDeleteListener, FilterCategoryAdapter.onItemClickListener {

    private ProductAdapter productAdapter;
    private FilterCategoryAdapter filterCategoryAdapter;
    private ProductService productService;
    private ProgressBar progressBar;
    private List<ProductResponseDTO> itemList;
    private List<Category> itemListCategory;
    private TextView categoryEmpty;

    private TextView searchProduct;

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

        itemList = new ArrayList<>();

        itemListCategory = new ArrayList<>();

        categoryEmpty = view.findViewById(R.id.notfoundCategoryProduct);

        RecyclerView productRecyclerView = view.findViewById(R.id.recyclerViewProduct);

        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
        productRecyclerView.setLayoutManager(layoutManager);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        productAdapter = new ProductAdapter(itemList, this);
        productRecyclerView.setAdapter(productAdapter);

        searchProduct = view.findViewById(R.id.searchProductList);

        searchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                productAdapter.filter(s.toString());
                System.out.println(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        all();
        recyclerViewFilterCategory(view);
    }

    public void recyclerViewFilterCategory(View view) {
        RecyclerView categoryFilterRecyclerView = view.findViewById(R.id.recyclerViewFilterCategory);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        categoryFilterRecyclerView.setLayoutManager(layoutManager);

        filterCategoryAdapter = new FilterCategoryAdapter(itemListCategory, this);
        categoryFilterRecyclerView.setAdapter(filterCategoryAdapter);
    }

    public void all() {
        productService.all().enqueue(new Callback<ProductAndCategory>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<ProductAndCategory> call, Response<ProductAndCategory> response) {
                itemList.clear();
                itemList.addAll(response.body().getProducts());
                productAdapter.notifyDataSetChanged();

                itemListCategory.clear();
                Category category = new Category();
                category.setName("Todos");
                itemListCategory.add(category);
                itemListCategory.addAll(response.body().getCategories());
                filterCategoryAdapter.notifyDataSetChanged();

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<ProductAndCategory> call, @NonNull Throwable throwable) {
                System.out.println(throwable.getMessage());
                Toast.makeText(getActivity(), "Network error.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void filterCategory(Long id) {
        productService.filterPerCategory(id).enqueue(new Callback<List<ProductResponseDTO>>() {
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
                System.out.println(throwable.getMessage());
                Toast.makeText(getActivity(), "Network error.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onUpdateItem(int position) {
    }

    @Override
    public void onItemDelete(int position) {
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