package com.example.aplicacionesmoviles.adapter;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aplicacionesmoviles.MainActivity;
import com.example.aplicacionesmoviles.PlaceDescription;
import com.example.aplicacionesmoviles.R;
import com.example.aplicacionesmoviles.RegisterActivity;
import com.example.aplicacionesmoviles.api.ApiClient;
import com.example.aplicacionesmoviles.api.FavoriteApi;
import com.example.aplicacionesmoviles.api.VisitsApi;
import com.example.aplicacionesmoviles.model.Comment;
import com.example.aplicacionesmoviles.model.Favorite;
import com.example.aplicacionesmoviles.model.Place;
import com.example.aplicacionesmoviles.model.Visit;
import com.example.aplicacionesmoviles.utils.ErrorModal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> implements View.OnClickListener {

    private List<Place> mPlaces;
    private final List<Place> originalsPlaces=new ArrayList<>();
    private Context context;
    private View.OnClickListener listener;
    FavoriteApi favoriteApi = ApiClient.getInstanceRetrofit().create(FavoriteApi.class);
    VisitsApi visitsApi= ApiClient.getInstanceRetrofit().create(VisitsApi.class);
    List<Favorite> favorites=new ArrayList<>();
    List<Visit> visits=new ArrayList<>();

    int userId;
    public PlaceAdapter(List<Place> mPlaces, int userId) {
        this.mPlaces = mPlaces;
        this.originalsPlaces.addAll(mPlaces);
        this.userId=userId;
        getFavorites(userId);
        getVisits(userId);
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
        mPlaces.sort((p1, p2) -> p2.city.compareTo(p1.city));
        notifyDataSetChanged();
    }

    public void filterCityZA(){
        mPlaces.sort(Comparator.comparing(p -> p.city));
        notifyDataSetChanged();
    }

    public void filterRateASC(){
        mPlaces.sort(Comparator.comparing(p -> p.score));
        notifyDataSetChanged();
    }

    public void filterRateDESC(){
        mPlaces.sort((p2, p1) -> p1.score.compareTo(p2.score));
        notifyDataSetChanged();
    }

    public void filterAZ(){
        mPlaces.sort(Comparator.comparing(p -> p.name));
        notifyDataSetChanged();
    }

    public void filterZA(){
        mPlaces.sort((p2, p1) -> p1.name.compareTo(p2.name));
        notifyDataSetChanged();
    }

    public void dateDESC(){
        mPlaces.sort((p2, p1) ->  p1.visit.compareTo(p2.visit));
        notifyDataSetChanged();
    }

    public void dateASC(){
        mPlaces.sort(Comparator.comparing(p -> p.visit));
        rotatePlacesByVisits();
        notifyDataSetChanged();
    }

    public void placeDistance(double lat, double longitude, double km){
        mPlaces.clear();
        mPlaces.addAll(originalsPlaces);
        List<Place> newPlaces=new ArrayList<>();
        int size =mPlaces.size();
        for (int i=0; i<size;i++){
            double distance =mPlaces.get(i).distance=getDistance(lat,longitude,Double.parseDouble(mPlaces.get(i).latitude),Double.parseDouble(mPlaces.get(i).longitude));
            if (distance<km){
                newPlaces.add(mPlaces.get(i));
            };
        }

        mPlaces=newPlaces;
        mPlaces.sort(Comparator.comparing(p -> p.distance));

        notifyDataSetChanged();
    }

    public double getDistance(double lat, double longitude, double latPlace, double longitudePlace){
        Location startPoint=new Location("UserLocation");
        startPoint.setLatitude(lat);
        startPoint.setLongitude(longitude);

        Location endPoint=new Location("PlaceLocation");
        endPoint.setLatitude(latPlace);
        endPoint.setLongitude(longitudePlace);

        return startPoint.distanceTo(endPoint)/1000;
    }
    public void rotatePlacesByVisits(){
        int iterations=0;
        for (int i=0; i<mPlaces.size();i++){
            if (mPlaces.get(i).visit.equals("")) {
               iterations++;
            }
        }
        for (int i=0;i<iterations;i++){
            Collections.rotate(mPlaces,mPlaces.size()-1);
        }
    }
    @NonNull
    @Override
    public PlaceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context= parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());

        View contactView=inflater.inflate(R.layout.item_place,parent,false);
        contactView.setOnClickListener(this);


        setVisits();
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder (@NonNull PlaceAdapter.ViewHolder holder, int position) {

        Place place =getPlaceByIndex(position);

        int visitIndex=getVisitIndex(place.id);
        TextView lastVisit= holder.mLastVisit;

        if (visitIndex==-1){
            place.visit="";
            lastVisit.setText(R.string.WithoutVisit);
        }
        else {
            place.visit= visits.get(visitIndex).visit;
            place.visitId=visits.get(visitIndex).id;
            String date= "Última visita: "+place.getVisitDate();
            lastVisit.setText(date);
        }

        TextView placeNameTextView= holder.mPlaceName;
        placeNameTextView.setText(place.name);

        TextView placeCityTextView= holder.mPlaceCity;
        String city="Departamento: "+place.city;
        placeCityTextView.setText(city);

        ImageView placeImage=holder.mPlaceImage;
        Glide.with(this.context).load(place.imageUrl).into(placeImage);

        RatingBar ratingPlace=holder.mPlaceRate;
        ratingPlace.setRating(Float.parseFloat(place.score));

        ToggleButton favBtn= holder.favBtn;

        favBtn.setChecked(isFavorite(favorites, place.id));

        favBtn.setOnClickListener(v ->{
           if (!favBtn.isChecked()) deleteFav(place.id);
           else addFav(place.id,userId);
        });
    }

    @Override
    public int getItemCount() {
        return mPlaces.size();
    }

    public Place getPlaceByIndex(int index) {
        return mPlaces.get(index);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View v) {
        if (listener!=null) listener.onClick(v);
    }

    public void addFav(int placeId, int user){
        Favorite favorite=new Favorite();
        favorite.place=placeId;
        favorite.user=user;

        Call<Favorite> favoriteCall=favoriteApi.createFavorite(favorite);
        favoriteCall.enqueue(new Callback<Favorite>() {
            @Override
            public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                getFavorites(favorite.user);
            }

            @Override
            public void onFailure(Call<Favorite> call, Throwable t) {
                ErrorModal.createErrorDialog(context,context.getString(R.string.genericErrorText));
            }
        });
    }

    public void deleteFav(int placeId){
        int id=getFavIndex(favorites,placeId);
        Favorite favorite= favorites.get(id);

        Call<Void> favoriteCall=favoriteApi.deleteFav(favorite.id);
        favoriteCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                favorites.remove(id);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                ErrorModal.createErrorDialog(context,context.getString(R.string.genericErrorText));
            }
        });

    }

    public void getFavorites(int userId){
        Call<List<Favorite>> favoritesCall= favoriteApi.getFavoritesByUserId(userId);
        favoritesCall.enqueue(new Callback<List<Favorite>>() {
            @Override
            public void onResponse(Call<List<Favorite>> call, Response<List<Favorite>> response) {
                favorites=response.body();
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Favorite>> call, Throwable t) {
                System.out.println(t.getMessage());

            }
        });
    }

    public void getVisits(int userId){
        Call<List<Visit>> visitsCalls= visitsApi.findVisitsByUserId(userId);
        visitsCalls.enqueue(new Callback<List<Visit>>() {
            @Override
            public void onResponse(Call<List<Visit>> call, Response<List<Visit>> response) {
                visits=response.body();
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Visit>> call, Throwable t) {

            }
        });
    }

    public boolean isFavorite(List<Favorite> favorites, int placeId){
        boolean res=false;
        for (int i=0; i<favorites.size();i++){
            if (favorites.get(i).place==placeId) res=true;
        }
        return res;
    }

    public void setVisits(){
        for (int i=0; i<mPlaces.size();i++){
            int index =getVisitIndex(mPlaces.get(i).id);
            if (index==-1)mPlaces.get(i).visit="";
            else {
                mPlaces.get(i).visit=visits.get(index).visit;
                mPlaces.get(i).visitId=visits.get(index).id;
            }
        }
    }

    public int getFavIndex(List<Favorite> favorites, int placeId){
        for (int i=0; i<favorites.size();i++){
            if (favorites.get(i).place==placeId) return i;
        }
        return -1;
    }

    public int getVisitIndex( int placeId){
        for (int i=0; i<visits.size();i++){
            if (visits.get(i).place==placeId) return i;
        }
        return -1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView mPlaceImage;
        private final TextView mPlaceName;
        private final TextView mPlaceCity;
        private final RatingBar mPlaceRate;
        private final ToggleButton favBtn;
        private final TextView mLastVisit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mPlaceImage=(ImageView)itemView.findViewById(R.id.place_image);
            mPlaceName=(TextView) itemView.findViewById(R.id.place_name);
            mPlaceCity=(TextView)itemView.findViewById(R.id.place_city);
            mPlaceRate=(RatingBar) itemView.findViewById(R.id.ratingBar);
            favBtn=(ToggleButton) itemView.findViewById(R.id.favBtn);
            mLastVisit=(TextView) itemView.findViewById(R.id.lastVisitedItem);

        }
    }
}

