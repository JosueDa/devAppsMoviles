package com.example.aplicacionesmoviles.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;

import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.aplicacionesmoviles.R;
import com.example.aplicacionesmoviles.adapter.PlaceAdapter;
import com.example.aplicacionesmoviles.api.ApiClient;
import com.example.aplicacionesmoviles.api.PlacesApi;
import com.example.aplicacionesmoviles.model.Place;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView placesRecyclerView;
    PlacesApi placesApi;
    SearchView searchPlace;
    PlaceAdapter placesAdapter= new PlaceAdapter( new ArrayList<>());
    ExtendedFloatingActionButton filterButton, cityAZFilter,cityZAFilter, rateDESCFilter,rateASCFilter,filterAZ,filterZA;
    Boolean filtersVisible;
    RatingBar ratingBar;

    public PlacesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlacesFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        placesApi= ApiClient.getInstanceRetrofit().create(PlacesApi.class);

        View placesView = inflater.inflate(R.layout.fragment_places, container, false);
        placesRecyclerView=placesView.findViewById(R.id.placesList);
        placesRecyclerView.setAdapter(placesAdapter);
        placesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchPlace= placesView.findViewById(R.id.places_search_bar);
        filterButton= placesView.findViewById(R.id.filterBtn);
        filterAZ= placesView.findViewById(R.id.AZFilter);
        filterZA= placesView.findViewById(R.id.ZAFilter);
        cityAZFilter= placesView.findViewById(R.id.cityAZFilter);
        cityZAFilter= placesView.findViewById(R.id.cityZAFilter);
        rateASCFilter= placesView.findViewById(R.id.rateASCFilter);
        rateDESCFilter= placesView.findViewById(R.id.rateDESCFilter);

        filterAZ.setVisibility(View.GONE);
        filterZA.setVisibility(View.GONE);
        cityAZFilter.setVisibility(View.GONE);
        cityZAFilter.setVisibility(View.GONE);
        rateASCFilter.setVisibility(View.GONE);
        rateDESCFilter.setVisibility(View.GONE);

        filtersVisible=false;


        Call<List<Place>> placeCall=placesApi.getTouristicPlaces();
        placeCall.enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                placesAdapter.reloadData(response.body());
            }
            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {
                Toast.makeText(getContext(), "Error al obtener los libros", Toast.LENGTH_SHORT).show();
            }
        });

        searchPlace.setOnQueryTextListener(this);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!filtersVisible) {
                    filterAZ.setVisibility(View.VISIBLE);
                    filterZA.setVisibility(View.VISIBLE);
                    cityAZFilter.setVisibility(View.VISIBLE);
                    cityZAFilter.setVisibility(View.VISIBLE);
                    rateASCFilter.setVisibility(View.VISIBLE);
                    rateDESCFilter.setVisibility(View.VISIBLE);

                    filtersVisible=true;
                } else {
                    filterAZ.setVisibility(View.GONE);
                    filterZA.setVisibility(View.GONE);
                    cityAZFilter.setVisibility(View.GONE);
                    cityZAFilter.setVisibility(View.GONE);
                    rateASCFilter.setVisibility(View.GONE);
                    rateDESCFilter.setVisibility(View.GONE);
                    filtersVisible = false;
                }
            }
        });

        cityZAFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placesAdapter.filterCityAZ();
            }
        });

        cityAZFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placesAdapter.filterCityZA();
            }
        });

        rateASCFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placesAdapter.filterRateASC();
            }
        });

        rateDESCFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placesAdapter.filterRateDESC();
            }
        });

        filterAZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placesAdapter.filterAZ();
            }
        });

        filterZA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placesAdapter.filterZA();
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
}