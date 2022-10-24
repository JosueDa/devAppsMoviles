package com.example.aplicacionesmoviles;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.aplicacionesmoviles.adapter.ViewFavPageAdapter;
import com.example.aplicacionesmoviles.adapter.ViewPageAdapter;
import com.example.aplicacionesmoviles.databinding.ActivityFavPlacesBinding;
import com.example.aplicacionesmoviles.databinding.ActivityHomeBinding;
import com.example.aplicacionesmoviles.fragments.PlacesFragment;
import com.example.aplicacionesmoviles.fragments.RestaurantsFragment;
import com.example.aplicacionesmoviles.utils.SessionManagement;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class FavActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_places);
        ActivityFavPlacesBinding binding = ActivityFavPlacesBinding.inflate(getLayoutInflater());
        View favViewBinding = binding.getRoot();
        setContentView(favViewBinding);

        ViewFavPageAdapter viewPageAdapter = new ViewFavPageAdapter(getSupportFragmentManager(), getLifecycle());

        TabLayout tableLayout= binding.tabFavBar;
        ViewPager2 viewPager= binding.viewFavPagerPlace;
        viewPager.setAdapter(viewPageAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tableLayout.selectTab(tableLayout.getTabAt(position));
            }
        });

        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
}