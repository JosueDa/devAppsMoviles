package com.example.aplicacionesmoviles.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.aplicacionesmoviles.PlaceDescription;
import com.example.aplicacionesmoviles.R;
import com.example.aplicacionesmoviles.adapter.PlaceAdapter;
import com.example.aplicacionesmoviles.api.ApiClient;
import com.example.aplicacionesmoviles.api.PlacesApi;
import com.example.aplicacionesmoviles.api.ScoreApi;
import com.example.aplicacionesmoviles.model.Place;
import com.example.aplicacionesmoviles.model.Score;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RestaurantsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantsFragment extends Fragment implements SearchView.OnQueryTextListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    boolean fav=false;

    RecyclerView placesRecyclerView;
    PlacesApi placesApi;
    PlaceAdapter placesAdapter;
    ExtendedFloatingActionButton filterButton, cityAZFilter,cityZAFilter, rateDESCFilter,rateASCFilter,filterAZ,filterZA,dateDESCFilter,dateASCFilter;
    Boolean filtersVisible;
    SearchView searchPlace;
    int userId;
    private ScoreApi scoreApi;
    private Score score=new Score();


    public RestaurantsFragment() {
        // Required empty public constructor
    }

    public RestaurantsFragment(boolean fav) {
        this.fav=fav;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RestaurantsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RestaurantsFragment newInstance(String param1, String param2) {
        RestaurantsFragment fragment = new RestaurantsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        placesAdapter.getFavorites(userId);
        placesAdapter.getVisits(userId);

        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sharedPreferences= this.requireContext().getSharedPreferences("session", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("id",-1);

        placesApi= ApiClient.getInstanceRetrofit().create(PlacesApi.class);

        View placesView = inflater.inflate(R.layout.fragment_restaurants, container, false);
        placesRecyclerView=placesView.findViewById(R.id.restaurantsList);
        placesAdapter=new PlaceAdapter( new ArrayList<>(),userId);
        placesRecyclerView.setAdapter(placesAdapter);
        placesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        instanceButtons(placesView);
        visibilityGone();

        Call<List<Place>> placeCall;
        if (fav) placeCall=placesApi.getFavRestaurantsById(userId);
        else placeCall=placesApi.getRestaurantsPlaces();
        data(placeCall);

        searchPlace.setOnQueryTextListener(this);

        placesAdapter.setOnClickListener(v ->navToIndividualView(userId,v));

        filterButton.setOnClickListener(v -> {
            if (!filtersVisible) visibilityVisible();
            else visibilityGone();
        });

        filters();

        placesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (filtersVisible) visibilityGone();
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        return placesView;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        placesAdapter.filter(newText);
        return false;
    }

    public void filters(){
        cityZAFilter.setOnClickListener(v -> placesAdapter.filterCityAZ());
        cityAZFilter.setOnClickListener(v -> placesAdapter.filterCityZA());
        rateASCFilter.setOnClickListener(v -> placesAdapter.filterRateASC());
        rateDESCFilter.setOnClickListener(v -> placesAdapter.filterRateDESC());
        filterAZ.setOnClickListener(v -> placesAdapter.filterAZ());
        filterZA.setOnClickListener(v -> placesAdapter.filterZA());
        dateASCFilter.setOnClickListener(v -> placesAdapter.dateASC());
        dateDESCFilter.setOnClickListener(v -> placesAdapter.dateDESC());
    }

    public void visibilityGone(){
        filtersVisible=false;
        filterAZ.hide();
        filterZA.hide();
        cityAZFilter.hide();
        cityZAFilter.hide();
        rateASCFilter.hide();
        rateDESCFilter.hide();
        dateASCFilter.hide();
        dateDESCFilter.hide();
    }

    public void visibilityVisible(){
        filtersVisible=true;
        filterAZ.show();
        filterZA.show();
        cityAZFilter.show();
        cityZAFilter.show();;
        rateASCFilter.show();
        rateDESCFilter.show();
        dateDESCFilter.show();
        dateASCFilter.show();
    }

    public void instanceButtons(View placesView){
        searchPlace= placesView.findViewById(R.id.search_bar);
        filterButton= placesView.findViewById(R.id.filterBtn);
        filterAZ= placesView.findViewById(R.id.AZFilter);
        filterZA= placesView.findViewById(R.id.ZAFilter);
        cityAZFilter= placesView.findViewById(R.id.cityAZFilter);
        cityZAFilter= placesView.findViewById(R.id.cityZAFilter);
        rateASCFilter= placesView.findViewById(R.id.rateASCFilter);
        rateDESCFilter= placesView.findViewById(R.id.rateDESCFilter);
        dateDESCFilter=placesView.findViewById(R.id.dateDESCFilter);
        dateASCFilter=placesView.findViewById(R.id.dateASCFilter);
    }

    public void navToIndividualView(int userId, View v){
        Place place =placesAdapter.getPlaceByIndex(placesRecyclerView.getChildAdapterPosition(v));
        scoreApi= ApiClient.getInstanceRetrofit().create(ScoreApi.class);
        Call<Score> scores=scoreApi.findByUserAndPlace(userId,place.id);
        scores.enqueue(new Callback<Score>() {
            @Override
            public void onResponse(Call<Score> call, Response<Score> response) {
                score = response.body();
                intentToIndividualPlace(place,v);
            }

            @Override
            public void onFailure(Call<Score> call, Throwable t) {
                intentToIndividualPlace(place,v);
            }
        });
    }

    public  void data(Call<List<Place>> placeCall){
        placeCall.enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                placesAdapter.reloadData(response.body());
            }
            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void intentToIndividualPlace(Place place, View v){
        Intent intent = new Intent(v.getContext(), PlaceDescription.class);
        intent.putExtra("place", place);
        intent.putExtra("score", score);
        v.getContext().startActivity(intent);
    }
}