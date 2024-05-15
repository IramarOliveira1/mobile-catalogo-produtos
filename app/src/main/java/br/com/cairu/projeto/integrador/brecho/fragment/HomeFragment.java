package br.com.cairu.projeto.integrador.brecho.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import br.com.cairu.projeto.integrador.brecho.R;
import br.com.cairu.projeto.integrador.brecho.adapter.HomeAdapter;
import br.com.cairu.projeto.integrador.brecho.config.ApiClient;
import br.com.cairu.projeto.integrador.brecho.databinding.ActivityMainBinding;
import br.com.cairu.projeto.integrador.brecho.dtos.HomeResponseDTO;
import br.com.cairu.projeto.integrador.brecho.services.HomeService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    public BottomNavigationView bottomNavigationView;

    public TextView buttonTextView;

    public RecyclerView recycleView;

    public HomeAdapter homeAdapter;

    public HomeFragment() {

    }

    public void changeScreenProduct(View view) {
        buttonTextView = view.findViewById(R.id.homeViewAllProduct);

        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);

        buttonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.frameLayout, new ProductFragment())
                        .addToBackStack(null)
                        .commit();

                bottomNavigationView.setSelectedItemId(R.id.product);
            }
        });
    }

    public void changeScreenCategory(View view) {

        buttonTextView = view.findViewById(R.id.homeViewAllCategory);

        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);

        buttonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.frameLayout, new CategoryFragment())
                        .addToBackStack(null)
                        .commit();

                bottomNavigationView.setSelectedItemId(R.id.category);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.changeScreenProduct(view);
        this.changeScreenCategory(view);
        this.all(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void all(View view) {
        HomeService homeService = new ApiClient().getClient().create(HomeService.class);

        Call<List<HomeResponseDTO>> call = homeService.homeResponseDto();

        recycleView = view.findViewById(R.id.recycleView);
        call.enqueue(new Callback<List<HomeResponseDTO>>() {
            @Override
            public void onResponse(Call<List<HomeResponseDTO>> call, Response<List<HomeResponseDTO>> response) {
                List<HomeResponseDTO> homeResponseDTO = response.body();

                homeAdapter = new HomeAdapter(homeResponseDTO);

                recycleView.setAdapter(homeAdapter);

                TextView totalProduct = view.findViewById(R.id.quantityProduct);
                totalProduct.setText(homeResponseDTO.isEmpty()  ? "0" :  Long.toString(homeResponseDTO.get(0).getTotalProduct()));

                TextView totalCategory = view.findViewById(R.id.quantityCategory);
                totalCategory.setText(homeResponseDTO.isEmpty()  ? "0" :  Long.toString(homeResponseDTO.get(0).getTotalCategory()));
            }

            @Override
            public void onFailure(@NonNull Call<List<HomeResponseDTO>> call, @NonNull Throwable throwable) {
                Log.e("HomeFragment", "Erro ao carregar dados da API", throwable);
            }
        });
    }
}