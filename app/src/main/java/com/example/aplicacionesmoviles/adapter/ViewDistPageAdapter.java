package com.example.aplicacionesmoviles.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.aplicacionesmoviles.fragments.PlaceDistanceFragment;
import com.example.aplicacionesmoviles.fragments.PlacesFragment;
import com.example.aplicacionesmoviles.fragments.RestaurantsFragment;


public class ViewDistPageAdapter extends FragmentStateAdapter {

    public ViewDistPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position==0) return new PlaceDistanceFragment(true);
        else return new PlaceDistanceFragment(false);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
