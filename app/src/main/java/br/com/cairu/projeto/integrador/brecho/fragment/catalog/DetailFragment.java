package br.com.cairu.projeto.integrador.brecho.fragment.catalog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import br.com.cairu.projeto.integrador.brecho.R;
import br.com.cairu.projeto.integrador.brecho.adapter.ProductAdapter;
import br.com.cairu.projeto.integrador.brecho.config.ApiClient;
import br.com.cairu.projeto.integrador.brecho.dtos.product.ProductResponseDTO;
import br.com.cairu.projeto.integrador.brecho.models.File;
import br.com.cairu.projeto.integrador.brecho.services.ProductService;
import br.com.cairu.projeto.integrador.brecho.utils.Generic;
import br.com.cairu.projeto.integrador.brecho.utils.InitToolbar;

public class DetailFragment extends Fragment {

    private ProductResponseDTO productResponseDTO;

    private TextView name, price, description;

    public DetailFragment() {
    }

    public DetailFragment(ProductResponseDTO productResponseDTO) {
        this.productResponseDTO = productResponseDTO;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        BottomNavigationView bottom = getActivity().findViewById(R.id.bottomNavigationView);
        bottom.setVisibility(View.GONE);


        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);

        new InitToolbar().toolbar((AppCompatActivity) requireActivity(), toolbar, getActivity(), false);

        ArrayList<SlideModel> images = new ArrayList<>();

        for (File file : productResponseDTO.getFiles()) {
            images.add(new SlideModel("http://10.0.2.2:8080/" + file.getUrl(), ScaleTypes.CENTER_CROP));
        }

        ImageSlider imageSlider = view.findViewById(R.id.image_slider);
        imageSlider.setImageList(images);

        name = view.findViewById(R.id.nameDetail);
        name.setText(productResponseDTO.getName().toUpperCase());

        Locale locale = new Locale("pt", "BR");

        String convertMoney = NumberFormat.getCurrencyInstance(locale).format(Double.parseDouble(productResponseDTO.getPrice()));

        price = view.findViewById(R.id.priceDetail);
        price.setText(convertMoney);

        description = view.findViewById(R.id.descriptionDetail);
        description.setText(productResponseDTO.getDescription());
    }

}