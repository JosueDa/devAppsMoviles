package com.example.aplicacionesmoviles.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aplicacionesmoviles.R;
import com.example.aplicacionesmoviles.model.Place;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    private List<Place> mPlaces;
    private List<Place> originalsPlaces=new ArrayList<Place>();
    private Context context;

    public PlaceAdapter(List<Place> mPlaces) {
        this.mPlaces = mPlaces;
        this.originalsPlaces.addAll(mPlaces);
    }

    public void reloadData(List<Place> places){
        this.mPlaces=places;
        originalsPlaces.addAll(mPlaces);
        notifyDataSetChanged();
    }

    public void filter(String text){
        if (text.length()==0){
            mPlaces.clear();
            mPlaces.addAll(originalsPlaces);
        }else {
            List<Place> queryResult = originalsPlaces.stream().filter(i->i.name.toLowerCase().contains(text.toLowerCase()))
                    .collect(Collectors.toList());
            mPlaces.clear();
            mPlaces.addAll(queryResult);
        }
        notifyDataSetChanged();
    }

    public void filterCityAZ(){
        mPlaces.sort(new Comparator<Place>() {
            @Override
            public int compare(Place p1, Place p2) {
                return p2.city.compareTo(p1.city);
            }
        });
        notifyDataSetChanged();
    }

    public void filterCityZA(){
        mPlaces.sort(new Comparator<Place>() {
            @Override
            public int compare(Place p1, Place p2) {
                return p1.city.compareTo(p2.city);
            }
        });
        notifyDataSetChanged();
    }

    public void filterRateASC(){
        mPlaces.sort(new Comparator<Place>() {
            @Override
            public int compare(Place p1, Place p2) {
                return p1.score.compareTo(p2.score);
            }
        });
        notifyDataSetChanged();
    }

    public void filterRateDESC(){
        mPlaces.sort(new Comparator<Place>() {
            @Override
            public int compare(Place p2, Place p1) {
                return p1.score.compareTo(p2.score);
            }
        });
        notifyDataSetChanged();
    }

    public void filterAZ(){
        mPlaces.sort(new Comparator<Place>() {
            @Override
            public int compare(Place p1, Place p2) {
                return p1.name.compareTo(p2.name);
            }
        });
        notifyDataSetChanged();
    }

    public void filterZA(){
        mPlaces.sort(new Comparator<Place>() {
            @Override
            public int compare(Place p2, Place p1) {
                return p1.name.compareTo(p2.name);
            }
        });
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlaceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context= parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());

        View contactView=inflater.inflate(R.layout.item_place,parent,false);

        ViewHolder viewHolder=new ViewHolder(contactView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceAdapter.ViewHolder holder, int position) {
        Place place =mPlaces.get(position);

        TextView placeNameTextView= holder.mPlaceName;
        placeNameTextView.setText(place.name);

        TextView placeCityTextView= holder.mPlaceCity;
        placeCityTextView.setText(place.city);

        ImageView placeImage=holder.mPlaceImage;
        Glide.with(this.context).load(place.imageUrl).into(placeImage);

        RatingBar ratingPlace=holder.mPlaceRate;
        ratingPlace.setRating(Float.parseFloat(place.score));
    }

    @Override
    public int getItemCount() {
        return mPlaces.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView mPlaceImage;
        private TextView mPlaceName;
        private TextView mPlaceCity;
        private RatingBar mPlaceRate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mPlaceImage=(ImageView)itemView.findViewById(R.id.place_image);
            mPlaceName=(TextView) itemView.findViewById(R.id.place_name);
            mPlaceCity=(TextView)itemView.findViewById(R.id.place_city);
            mPlaceRate=(RatingBar) itemView.findViewById(R.id.ratingBar);
        }
    }
}

