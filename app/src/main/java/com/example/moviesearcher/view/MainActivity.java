package com.example.moviesearcher.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;

import com.example.moviesearcher.R;
import com.example.moviesearcher.util.BundleUtil;
import com.example.moviesearcher.util.ConverterUtil;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private NavController navController;
    private NavigationView navigationView;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        navController = Navigation.findNavController(this, R.id.host_fragment);
        NavigationUI.setupWithNavController(toolbar, navController, drawerLayout);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() != R.id.moviesList)
                toolbar.setTitle(destination.getLabel());
            else{
                if (arguments != null)
                    toolbar.setTitle(ConverterUtil.bundleKeyToToolbarTitle(arguments.getString(BundleUtil.KEY_MOVIE_LIST_TYPE)));
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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Bundle bundle = new Bundle();
        switch (menuItem.getItemId()){
            case R.id.menu_categories:
                navController.navigate(R.id.categoriesFragment);
                drawerLayout.closeDrawers();
                return true;

            case R.id.menu_popular:
                bundle.putString(BundleUtil.KEY_MOVIE_LIST_TYPE, BundleUtil.KEY_POPULAR);
                navController.navigate(R.id.moviesList, bundle);
                drawerLayout.closeDrawers();
                return true;

            case R.id.menu_top_rated:
                bundle.putString(BundleUtil.KEY_MOVIE_LIST_TYPE, BundleUtil.KEY_TOP_RATED);
                navController.navigate(R.id.moviesList, bundle);
                drawerLayout.closeDrawers();
                return true;

            case R.id.menu_now_playing:
                bundle.putString(BundleUtil.KEY_MOVIE_LIST_TYPE, BundleUtil.KEY_NOW_PLAYING);
                navController.navigate(R.id.moviesList, bundle);
                drawerLayout.closeDrawers();
                return true;

            case R.id.menu_upcoming:
                bundle.putString(BundleUtil.KEY_MOVIE_LIST_TYPE, BundleUtil.KEY_UPCOMING);
                navController.navigate(R.id.moviesList, bundle);
                drawerLayout.closeDrawers();
                return true;

            case R.id.menu_about:
                navController.navigate(R.id.aboutFragment);
                drawerLayout.closeDrawers();
                return true;
        }
        return false;
    }
}
