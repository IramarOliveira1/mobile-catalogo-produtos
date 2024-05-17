package br.com.cairu.projeto.integrador.brecho.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import br.com.cairu.projeto.integrador.brecho.R;
import br.com.cairu.projeto.integrador.brecho.adapter.HomeAdapter;
import br.com.cairu.projeto.integrador.brecho.config.ApiClient;
import br.com.cairu.projeto.integrador.brecho.dtos.HomeResponseDTO;
import br.com.cairu.projeto.integrador.brecho.services.HomeService;
import br.com.cairu.projeto.integrador.brecho.utils.Dialog;
import br.com.cairu.projeto.integrador.brecho.utils.Generic;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements Dialog {

    private BottomNavigationView bottomNavigationView;

    private TextView buttonTextView;

    private TextView logout;

    private Generic generic;

    private RecyclerView recycleView;

    private HomeAdapter homeAdapter;

    public HomeFragment() {
    }


    public void changeScreenProduct(View view) {
        buttonTextView = view.findViewById(R.id.homeViewAllProduct);

        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);

        buttonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new ProductFragment())
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
                        .replace(R.id.frameLayout, new CategoryFragment())
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

        logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void all(View view) {
        generic = new Generic(getActivity());

        TextView username = view.findViewById(R.id.userHome);
        username.setText("OLÁ, " + generic.getUsername().toUpperCase());

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
                totalProduct.setText(homeResponseDTO.isEmpty() ? "0" : Long.toString(homeResponseDTO.get(0).getTotalProduct()));

                TextView totalCategory = view.findViewById(R.id.quantityCategory);
                totalCategory.setText(homeResponseDTO.isEmpty() ? "0" : Long.toString(homeResponseDTO.get(0).getTotalCategory()));
            }

            @Override
            public void onFailure(@NonNull Call<List<HomeResponseDTO>> call, @NonNull Throwable throwable) {
                Toast.makeText(getActivity(), "Network error.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void logout() {
        this.showDialog(
                "Você deseja realmente sair ?",
                "Sair",
                "Cancelar"
        );
    }

    @Override
    public void showDialog(String message, String positiveButton, String negativeButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                generic.clear();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new LoginFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
        builder.setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}