package com.example.moviesearcher.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.moviesearcher.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        NavController navController = Navigation.findNavController(this, R.id.host_fragment);
        NavigationUI.setupWithNavController(toolbar, navController, drawerLayout);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.categories:
                Log.d("Drawer selection", "onNavigationItemSelected: CATEGORIES");
                drawerLayout.closeDrawers();
                return true;

            case R.id.filter:
                Log.d("Drawer selection", "onNavigationItemSelected: FILTER");
                drawerLayout.closeDrawers();
                return true;
        }
        return false;
    }
}
