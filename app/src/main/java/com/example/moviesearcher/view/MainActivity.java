package com.example.moviesearcher.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.example.moviesearcher.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private NavController navController;
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        navController = Navigation.findNavController(this, R.id.host_fragment);
        NavigationUI.setupWithNavController(toolbar, navController, drawerLayout);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == controller.getGraph().getStartDestination()){
                if (navigationView.getCheckedItem() != null)
                    navigationView.getCheckedItem().setChecked(false);
            }
        });

        prepareMenuItemCategoryStyle();
    }

    private void prepareMenuItemCategoryStyle(){
        new Thread(()-> {
            Menu menu = navigationView.getMenu();
            for (int i = 0; i < menu.size(); i++){
                MenuItem tools= menu.getItem(i);
                SpannableString s = new SpannableString(tools.getTitle());
                s.setSpan(new TextAppearanceSpan(this, R.style.ItemCategoryTextAppearance), 0, s.length(), 0);
                tools.setTitle(s);
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        if (navigationView.getCheckedItem() != null)
            navigationView.getCheckedItem().setChecked(false);
        if (drawerLayout.isDrawerOpen(navigationView))
            drawerLayout.closeDrawers();
        else super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.menu_categories:
                Log.d("Drawer selection", "onNavigationItemSelected: CATEGORIES");
                navController.navigate(R.id.categoriesFragment);
                drawerLayout.closeDrawers();
                menuItem.setChecked(true);
                return true;

            case R.id.menu_about:
                navController.navigate(R.id.aboutFragment);
                drawerLayout.closeDrawers();
                menuItem.setChecked(true);
                return true;

            case R.id.menu_popular:
                navController.navigate(R.id.moviesList);
                drawerLayout.closeDrawers();
                menuItem.setChecked(true);
                return true;
        }
        return false;
    }
}
