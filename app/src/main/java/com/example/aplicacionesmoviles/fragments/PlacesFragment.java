package com.example.aplicacionesmoviles.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;

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
import com.example.aplicacionesmoviles.utils.ErrorModal;
import com.example.aplicacionesmoviles.utils.LoadingBar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlacesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlacesFragment extends Fragment implements SearchView.OnQueryTextListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    boolean fav=false;

    RecyclerView placesRecyclerView;
    PlacesApi placesApi;
    SearchView searchPlace;
    PlaceAdapter placesAdapter;
    ExtendedFloatingActionButton filterButton, cityAZFilter,cityZAFilter, rateDESCFilter,rateASCFilter,filterAZ,filterZA,dateDESCFilter,dateASCFilter;
    Boolean filtersVisible;
    int userId;
    private ScoreApi scoreApi;
    private Score score=new Score();
    LoadingBar loadingDialog;

    public PlacesFragment() {
        // Required empty public constructor
    }

    public PlacesFragment(boolean fav) {
        this.fav=fav;
    }

    public static PlacesFragment newInstance(String param1, String param2) {
        PlacesFragment fragment = new PlacesFragment();
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
        visibilityGone();

        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loadingDialog= new LoadingBar(getContext());
        loadingDialog.startDialog();

        SharedPreferences sharedPreferences= this.requireContext().getSharedPreferences("session", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("id",-1);

        placesApi= ApiClient.getInstanceRetrofit().create(PlacesApi.class);

        View placesView = inflater.inflate(R.layout.fragment_places, container, false);
        placesRecyclerView=placesView.findViewById(R.id.placesList);
        placesAdapter= new PlaceAdapter( new ArrayList<>(),userId);
        placesRecyclerView.setAdapter(placesAdapter);
        placesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        instanceButtons(placesView);
        visibilityGone();

        Call<List<Place>> placeCall;
        if (fav) placeCall=placesApi.getFavPlacesById(userId);
        else placeCall=placesApi.getTouristicPlaces();
        data(placeCall);

        placesAdapter.setOnClickListener(v ->navToIndividualView(userId,v));

        searchPlace.setOnQueryTextListener(this);

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
        searchPlace= placesView.findViewById(R.id.places_search_bar);
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
                ErrorModal.createErrorDialog(v.getContext(),t.getMessage());
            }
        });
    }

    public  void data(Call<List<Place>> placeCall){
        placeCall.enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                placesAdapter.reloadData(response.body());
                loadingDialog.hideDialog();
            }
            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {
                loadingDialog.hideDialog();
                ErrorModal.createErrorDialog(getContext(),getString(R.string.genericErrorText));
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