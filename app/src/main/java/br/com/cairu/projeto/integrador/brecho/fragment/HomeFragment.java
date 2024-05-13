package br.com.cairu.projeto.integrador.brecho.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import br.com.cairu.projeto.integrador.brecho.R;

public class HomeFragment extends Fragment {

    public BottomNavigationView bottomNavigationView;

    public TextView buttonTextView;

    public HomeFragment(){

    }

    public void changeScreenProduct(View view){
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}