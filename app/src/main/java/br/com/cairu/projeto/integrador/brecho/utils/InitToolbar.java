package br.com.cairu.projeto.integrador.brecho.utils;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import br.com.cairu.projeto.integrador.brecho.R;

public class InitToolbar {

    public void toolbar(AppCompatActivity activity, Toolbar toolbar, FragmentActivity fragmentActivity, boolean catalog) {

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("");

        if (activity.getSupportActionBar() != null && !catalog) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
            activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.getOnBackPressedDispatcher().onBackPressed();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(toolbar, new androidx.core.view.OnApplyWindowInsetsListener() {
            @NonNull
            @Override
            public WindowInsetsCompat onApplyWindowInsets(@NonNull View v, @NonNull WindowInsetsCompat insets) {
                int insetTop = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;

                toolbar.setPadding(toolbar.getPaddingLeft(), insetTop, toolbar.getPaddingRight(), toolbar.getPaddingBottom());

                return insets;
            }
        });


        BottomNavigationView bottomNavigationView = fragmentActivity.findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.GONE);
    }
}
