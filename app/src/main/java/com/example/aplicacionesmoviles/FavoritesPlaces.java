package com.example.aplicacionesmoviles;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.aplicacionesmoviles.adapter.ViewPageAdapter;
import com.example.aplicacionesmoviles.databinding.ActivityFavPlacesBinding;
import com.example.aplicacionesmoviles.model.Place;
import com.google.android.material.tabs.TabLayout;

public class FavoritesPlaces extends AppCompatActivity {

    Place place;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_places);
        ActivityFavPlacesBinding binding = ActivityFavPlacesBinding.inflate(getLayoutInflater());
        View favPlacesBinding = binding.getRoot();
        setContentView(favPlacesBinding);

        place =(Place) getIntent().getSerializableExtra("place");

        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(), getLifecycle());

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
