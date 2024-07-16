package br.com.cairu.projeto.integrador.brecho;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import br.com.cairu.projeto.integrador.brecho.fragment.catalog.CatalogFragment;
import br.com.cairu.projeto.integrador.brecho.fragment.category.CategoryFragment;
import br.com.cairu.projeto.integrador.brecho.fragment.home.HomeFragment;
import br.com.cairu.projeto.integrador.brecho.fragment.product.ProductFragment;
import br.com.cairu.projeto.integrador.brecho.fragment.user.UserFragment;
import br.com.cairu.projeto.integrador.brecho.utils.Generic;

public class MainActivity extends AppCompatActivity {

    public BottomNavigationView bottomNavigationView;
    public FrameLayout frameLayout;
    public Generic generic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        generic = new Generic(getApplicationContext());

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        frameLayout = findViewById(R.id.frameLayout);

        if (!generic.getIsAdmin()) {
            Menu menu = bottomNavigationView.getMenu();
            MenuItem menuItem = menu.findItem(R.id.user);
            menuItem.setVisible(false);
        }

        Window window = getWindow();
        View decorView = window.getDecorView();
        WindowInsetsController insetsController = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            insetsController = decorView.getWindowInsetsController();
        }

        if (insetsController != null) {
            // Ensure light status bar (dark icons)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                insetsController.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                );
            }
        }

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if (itemId == R.id.home) {
                    loadFragment(new HomeFragment(), false);
                } else if (itemId == R.id.product) {
                    loadFragment(new ProductFragment(), false);
                } else if (itemId == R.id.category) {
                    loadFragment(new CategoryFragment(), false);
                } else if (itemId == R.id.catalog) {
                    loadFragment(new CatalogFragment(), false);
                } else {
                    loadFragment(new UserFragment(), false);
                }

                return true;
            }
        });

        if (generic.getToken() != null) {
            loadFragment(new HomeFragment(), true);
        } else {
            loadFragment(new CatalogFragment(), true);
        }
    }

    public void loadFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isAppInitialized) {
            fragmentTransaction.add(R.id.frameLayout, fragment);
        } else {
            fragmentTransaction.replace(R.id.frameLayout, fragment);
        }

        fragmentTransaction.commit();
    }
}