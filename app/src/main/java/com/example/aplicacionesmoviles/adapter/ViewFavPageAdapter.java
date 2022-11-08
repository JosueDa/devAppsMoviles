package com.example.aplicacionesmoviles.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.aplicacionesmoviles.fragments.PlacesFragment;
import com.example.aplicacionesmoviles.fragments.RestaurantsFragment;


public class ViewFavPageAdapter extends FragmentStateAdapter {

    boolean restaurantFav;
    boolean placeFav;
    public ViewFavPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,boolean restaurantFav, boolean placeFav) {
        super(fragmentManager, lifecycle);
        this.restaurantFav =restaurantFav;
        this.placeFav=placeFav;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position==0) return new PlacesFragment(placeFav);
        else return new RestaurantsFragment(restaurantFav);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
