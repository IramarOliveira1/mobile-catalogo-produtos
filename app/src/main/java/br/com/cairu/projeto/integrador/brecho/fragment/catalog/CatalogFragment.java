package br.com.cairu.projeto.integrador.brecho.fragment.catalog;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import br.com.cairu.projeto.integrador.brecho.R;
import br.com.cairu.projeto.integrador.brecho.adapter.FilterCategoryAdapter;
import br.com.cairu.projeto.integrador.brecho.adapter.ProductAdapter;
import br.com.cairu.projeto.integrador.brecho.config.ApiClient;
import br.com.cairu.projeto.integrador.brecho.dtos.category.CategoryResponseDTO;
import br.com.cairu.projeto.integrador.brecho.dtos.product.ProductAndCategory;
import br.com.cairu.projeto.integrador.brecho.dtos.product.ProductResponseDTO;
import br.com.cairu.projeto.integrador.brecho.fragment.category.CategoryFragment;
import br.com.cairu.projeto.integrador.brecho.fragment.home.HomeFragment;
import br.com.cairu.projeto.integrador.brecho.fragment.login.LoginFragment;
import br.com.cairu.projeto.integrador.brecho.fragment.product.ProductFragment;
import br.com.cairu.projeto.integrador.brecho.fragment.user.UserFragment;
import br.com.cairu.projeto.integrador.brecho.services.ProductService;
import br.com.cairu.projeto.integrador.brecho.utils.Generic;
import br.com.cairu.projeto.integrador.brecho.utils.InitToolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogFragment extends Fragment implements ProductAdapter.OnItemDeleteListener, FilterCategoryAdapter.onItemClickListener {
    private ProductAdapter productAdapter;
    private FilterCategoryAdapter filterCategoryAdapter;
    private ProductService productService;
    private ProgressBar progressBar;
    private List<ProductResponseDTO> itemList;
    private List<CategoryResponseDTO> itemListCategory;
    private List<CategoryResponseDTO> categories;
    private TextView categoryEmpty;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Generic generic;

    private EditText search;

    public CatalogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        BottomNavigationView bottom = getActivity().findViewById(R.id.bottomNavigationView);
        bottom.setVisibility(View.GONE);
        return inflater.inflate(R.layout.fragment_catalog, container, false);
    }

    @SuppressLint("CutPasteId")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);

        new InitToolbar().toolbar((AppCompatActivity) requireActivity(), toolbar, getActivity(), true);

        productService = new ApiClient().getClient(getActivity()).create(ProductService.class);

        generic = new Generic(requireContext());

        itemList = new ArrayList<>();

        categories = new ArrayList<>();

        itemListCategory = new ArrayList<>();

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);

        categoryEmpty = view.findViewById(R.id.notfoundCategoryProduct);

        search = view.findViewById(R.id.searchProductCatalog);

        RecyclerView productRecyclerView = view.findViewById(R.id.recyclerViewProductCatalog);

        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
        productRecyclerView.setLayoutManager(layoutManager);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        productAdapter = new ProductAdapter(itemList, this, true);
        productRecyclerView.setAdapter(productAdapter);

        EditText searchProduct = view.findViewById(R.id.searchProductCatalog);

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

        TextView menuButton = view.findViewById(R.id.sidebar);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                all();
            }
        });

        all();
        recyclerViewFilterCategory(view);
    }

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(getActivity(), view);
        MenuInflater inflater = popup.getMenuInflater();

        if (generic.getToken() != null) {
            inflater.inflate(R.menu.menu_catalog_logged, popup.getMenu());
        } else {
            inflater.inflate(R.menu.menu_catalog_not_logged, popup.getMenu());
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
                bottomNavigationView.setVisibility(View.VISIBLE);

                if (itemId == R.id.home_menu) {
                    loadFragment(new HomeFragment());
                    bottomNavigationView.setSelectedItemId(R.id.home);
                } else if (itemId == R.id.product_menu) {
                    loadFragment(new ProductFragment());
                    bottomNavigationView.setSelectedItemId(R.id.product);
                } else if (itemId == R.id.category_menu) {
                    loadFragment(new CategoryFragment());
                    bottomNavigationView.setSelectedItemId(R.id.category);
                } else if (itemId == R.id.user_menu) {
                    loadFragment(new UserFragment());
                    bottomNavigationView.setSelectedItemId(R.id.user);
                } else {
                    loadFragment(new LoginFragment());
                    bottomNavigationView.setVisibility(View.GONE);
                }

                return true;
            }
        });
        popup.show();
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frameLayout, fragment);

        fragmentTransaction.commit();
    }

    public void recyclerViewFilterCategory(View view) {
        RecyclerView categoryFilterRecyclerView = view.findViewById(R.id.recyclerViewFilterCategory);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        categoryFilterRecyclerView.setLayoutManager(layoutManager);

        filterCategoryAdapter = new FilterCategoryAdapter(itemListCategory, this);
        categoryFilterRecyclerView.setAdapter(filterCategoryAdapter);
    }

    public void all() {
        productService.all("catalog").enqueue(new Callback<ProductAndCategory>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ProductAndCategory> call, @NonNull Response<ProductAndCategory> response) {

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
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<ProductAndCategory> call, @NonNull Throwable throwable) {
                Toast.makeText(getActivity(), "Network error.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void filterCategory(Long id) {
        productService.filterPerCategory(id, "catalog").enqueue(new Callback<List<ProductResponseDTO>>() {
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

    public void index(Long id) {
        productService.index(id, "detail").enqueue(new Callback<ProductResponseDTO>() {
            @Override
            public void onResponse(Call<ProductResponseDTO> call, Response<ProductResponseDTO> response) {
                if (response.isSuccessful()) {
                    search.setText("");

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new DetailFragment(response.body())).addToBackStack(null).commit();
                }
            }

            @Override
            public void onFailure(Call<ProductResponseDTO> call, Throwable throwable) {
                Toast.makeText(getActivity(), "Network error.", Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    public void onUpdateItem(int position) {
        ProductResponseDTO item = itemList.get(position);

        this.index(item.getId());
    }

    @Override
    public void onItemDelete(int position) {

    }

    @Override
    public void onItemClick(String category, int position) {

    }
}