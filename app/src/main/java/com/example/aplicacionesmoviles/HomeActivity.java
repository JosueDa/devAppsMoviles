package com.example.aplicacionesmoviles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.aplicacionesmoviles.databinding.ActivityHomeBinding;
import com.example.aplicacionesmoviles.fragments.PlacesFragment;
import com.example.aplicacionesmoviles.fragments.RestaurantsFragment;
import com.example.aplicacionesmoviles.utils.SessionManagement;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    PlacesFragment placesFragment= new PlacesFragment();
    RestaurantsFragment restaurantsFragment = new RestaurantsFragment();
    BottomNavigationView navigationView;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View homeViewBinding = binding.getRoot();
        setContentView(homeViewBinding);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentManager.beginTransaction().add(binding.frameContainer.getId(),restaurantsFragment).hide(restaurantsFragment).commit();
        fragmentManager.beginTransaction().add(binding.frameContainer.getId(), placesFragment).show(placesFragment).commit();

        navigationView=binding.navigationBar;
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.touristic_places:
                        fragmentManager.beginTransaction().show(placesFragment).commit();
                        fragmentManager.beginTransaction().hide(restaurantsFragment).commit();
                        return true;
                    case R.id.restaurants:
                        fragmentManager.beginTransaction().show(restaurantsFragment).commit();
                        fragmentManager.beginTransaction().hide(placesFragment).commit();
                        return true;
                    case R.id.logOutMenu:
                        logOut();
                        return true;
                }
                return false;
            }
        });
    }

    public void logOut(){
        SessionManagement sessionManagement= new SessionManagement(HomeActivity.this);
        sessionManagement.removeSession();
        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivity);
    }

}