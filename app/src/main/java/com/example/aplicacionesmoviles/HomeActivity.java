package com.example.aplicacionesmoviles;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.example.aplicacionesmoviles.databinding.ActivityHomeBinding;
import com.example.aplicacionesmoviles.fragments.PlacesFragment;
import com.example.aplicacionesmoviles.fragments.RestaurantsFragment;
import com.example.aplicacionesmoviles.utils.SessionManagement;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
        navigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.touristic_places:
                    fragmentManager.beginTransaction().show(placesFragment).commit();
                    fragmentManager.beginTransaction().hide(restaurantsFragment).commit();
                    return true;
                case R.id.restaurants:
                    fragmentManager.beginTransaction().show(restaurantsFragment).commit();
                    fragmentManager.beginTransaction().hide(placesFragment).commit();
                    return true;
                case R.id.profileMenu:
                    Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(profileIntent);
                    return true;
                case R.id.FavMenuBtn:
                    Intent favIntent = new Intent(getApplicationContext(), FavActivity.class);
                    startActivity(favIntent);
                    return true;
                case R.id.NearMeMenuBtn:
                    Intent nearMeIntent = new Intent(getApplicationContext(), NearMeActivity.class);
                    startActivity(nearMeIntent);
                    return true;
            }
            return false;
        });
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Estás seguro de salir?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Ok",
                    (dialog, id) -> finishAffinity());

            builder1.setNegativeButton(
                    "Cancel",
                    (dialog, id) -> dialog.cancel());
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }

        this.doubleBackToExitPressedOnce = true;

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 1000);
    }

}