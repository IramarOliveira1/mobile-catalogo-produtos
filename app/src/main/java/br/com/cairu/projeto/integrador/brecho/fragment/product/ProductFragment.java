package br.com.cairu.projeto.integrador.brecho.fragment.product;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import br.com.cairu.projeto.integrador.brecho.R;
import br.com.cairu.projeto.integrador.brecho.adapter.FilterCategoryAdapter;
import br.com.cairu.projeto.integrador.brecho.adapter.ProductAdapter;

public class ProductFragment extends Fragment {

    private ProductAdapter productAdapter;
    private RecyclerView productRecyclerView;
    private RecyclerView categoryFilterRecyclerView;
    private FilterCategoryAdapter filterCategoryAdapter;
    private List<String> categories = Arrays.asList("Todos", "Camisas", "Calças", "Joias", "Jaquetas", "Vestidos", "Cama mesa e banho");

    public ProductFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productRecyclerView = view.findViewById(R.id.recyclerViewProduct);

        List<String> imageUrls = Arrays.asList(
                "http://127.0.0.1:8080/public/images/1714138911150.png"
        );

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        productRecyclerView.setLayoutManager(layoutManager);

        productAdapter = new ProductAdapter(imageUrls, new ProductAdapter.OnItemDeleteListener() {
            @Override
            public void onUpdateItem(int position) {
            }

            @Override
            public void onItemDelete(int position) {
            }

            @Override
            public void onItemClick(String imageUrl, int position) {
            }
        });

        productRecyclerView.setAdapter(productAdapter);

        recyclerViewFilterCategory(view);
    }

    public void recyclerViewFilterCategory(View view) {
        categoryFilterRecyclerView = view.findViewById(R.id.recyclerViewFilterCategory);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        categoryFilterRecyclerView.setLayoutManager(layoutManager);

        filterCategoryAdapter = new FilterCategoryAdapter(categories, new FilterCategoryAdapter.onItemClickListener() {
            @Override
            public void onItemClick(String category, int position) {
                System.out.println(category);
                System.out.println(position);
            }
        });
        categoryFilterRecyclerView.setAdapter(filterCategoryAdapter);
    }
}