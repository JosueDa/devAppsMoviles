package com.example.aplicacionesmoviles.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.aplicacionesmoviles.fragments.CommentsFragment;
import com.example.aplicacionesmoviles.fragments.PlaceDescriptionFragment;
import com.example.aplicacionesmoviles.model.Score;


public class ViewPageAdapter extends FragmentStateAdapter {

    Score score;
    public ViewPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,Score score) {
        super(fragmentManager, lifecycle);
        this.score=score;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position==0) return new PlaceDescriptionFragment();
        else return new CommentsFragment(score);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
