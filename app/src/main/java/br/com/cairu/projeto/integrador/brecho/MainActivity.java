package br.com.cairu.projeto.integrador.brecho;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import br.com.cairu.projeto.integrador.brecho.fragment.CatalogFragment;
import br.com.cairu.projeto.integrador.brecho.fragment.CategoryFragment;
import br.com.cairu.projeto.integrador.brecho.fragment.HomeFragment;
import br.com.cairu.projeto.integrador.brecho.fragment.LoginFragment;
import br.com.cairu.projeto.integrador.brecho.fragment.ProductFragment;
import br.com.cairu.projeto.integrador.brecho.fragment.UserFragment;

public class MainActivity extends AppCompatActivity {

    public BottomNavigationView bottomNavigationView;
    public FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        frameLayout = findViewById(R.id.frameLayout);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if (itemId == R.id.home){
                    loadFragment(new HomeFragment(), false);
                } else if (itemId == R.id.product){
                    loadFragment(new ProductFragment(), false);
                } else if (itemId == R.id.category){
                    loadFragment(new CategoryFragment(), false);
                } else if(itemId == R.id.catalog) {
                    loadFragment(new CatalogFragment(), false);
                } else {
                    loadFragment(new UserFragment(), false);

                }
                return true;
            }
        });

        loadFragment(new LoginFragment(), true);
    }

    public void loadFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isAppInitialized){
            fragmentTransaction.add(R.id.frameLayout, fragment);
        } else {
            fragmentTransaction.replace(R.id.frameLayout, fragment);
        }
        fragmentTransaction.commit();
    }
}