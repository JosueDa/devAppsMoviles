package com.example.aplicacionesmoviles.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.aplicacionesmoviles.fragments.PlacesFragment;
import com.example.aplicacionesmoviles.fragments.RestaurantsFragment;


public class ViewFavPageAdapter extends FragmentStateAdapter {

    public ViewFavPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position==0) return new PlacesFragment(true);
        else return new RestaurantsFragment(true);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
