package com.example.aplicacionesmoviles.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.aplicacionesmoviles.fragments.CommentsFragment;
import com.example.aplicacionesmoviles.fragments.PlaceDescriptionFragment;


public class ViewPageAdapter extends FragmentStateAdapter {

    public ViewPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position==0) return new PlaceDescriptionFragment();
        else return new CommentsFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
