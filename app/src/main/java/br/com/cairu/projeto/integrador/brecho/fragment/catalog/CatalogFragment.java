package br.com.cairu.projeto.integrador.brecho.fragment.catalog;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import br.com.cairu.projeto.integrador.brecho.R;

public class CatalogFragment extends Fragment {
    public CatalogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            BottomNavigationView bottom = getActivity().findViewById(R.id.bottomNavigationView);
            bottom.setVisibility(View.GONE);
            return inflater.inflate(R.layout.fragment_catalog, container, false);
    }
}