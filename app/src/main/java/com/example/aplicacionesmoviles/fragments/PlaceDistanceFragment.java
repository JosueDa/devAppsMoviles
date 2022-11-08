package com.example.aplicacionesmoviles.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.aplicacionesmoviles.PlaceDescription;
import com.example.aplicacionesmoviles.R;
import com.example.aplicacionesmoviles.adapter.PlaceAdapter;
import com.example.aplicacionesmoviles.api.ApiClient;
import com.example.aplicacionesmoviles.api.ScoreApi;
import com.example.aplicacionesmoviles.model.Place;
import com.example.aplicacionesmoviles.model.Score;
import com.example.aplicacionesmoviles.mvp.presenter.IPlacePresenter;
import com.example.aplicacionesmoviles.mvp.presenter.PlacePresenter;
import com.example.aplicacionesmoviles.mvp.view.IPlaceView;
import com.example.aplicacionesmoviles.utils.ErrorModal;
import com.example.aplicacionesmoviles.utils.LoadingBar;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaceDistanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaceDistanceFragment extends Fragment implements IPlaceView {

    RecyclerView placesRecyclerView;
    PlaceAdapter placesAdapter;
    IPlacePresenter presenter;
    FloatingActionButton filter20km,filter10km,filter15km,filter5km;
    int userId;
    double latitude, longitude;
    private ScoreApi scoreApi;
    private Score score=new Score();
    LoadingBar loadingDialog;
    Boolean typePlace;

    public PlaceDistanceFragment() {
        // Required empty public constructor
    }

    public PlaceDistanceFragment(boolean typePlace) {
        this.typePlace=typePlace;
    }

  public static PlaceDistanceFragment newInstance(String param1, String param2) {
        PlaceDistanceFragment fragment = new PlaceDistanceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_place_distance, container, false);
        loadingDialog= new LoadingBar(getContext());
        loadingDialog.startDialog();

        instanceButtons(view);

        SharedPreferences sharedPreferences= getContext().getSharedPreferences("session", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("id",-1);
        latitude=sharedPreferences.getFloat("latitude",-1);
        longitude=sharedPreferences.getFloat("longitude",-1);


        presenter= new PlacePresenter(this,typePlace);
        placesAdapter= new PlaceAdapter( new ArrayList<>(),userId);
        presenter.getPlaces();

        placesRecyclerView=(RecyclerView) view.findViewById(R.id.placesList);
        placesRecyclerView.setAdapter(placesAdapter);
        placesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        placesAdapter.setOnClickListener(v ->navToIndividualView(userId,v));

        filterButtons();

        return view;
    }

    @Override
    public void onPlacesSuccess(List<Place> places) {
        placesAdapter.reloadData(places);
        loadingDialog.hideDialog();
    }

    @Override
    public void onPlacesFailure(String errorMessage) {
        loadingDialog.hideDialog();
        ErrorModal.createErrorDialog(getContext(),errorMessage);
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

    public void intentToIndividualPlace(Place place, View v){
        Intent intent = new Intent(v.getContext(), PlaceDescription.class);
        intent.putExtra("place", place);
        intent.putExtra("score", score);
        v.getContext().startActivity(intent);
    }

    public void instanceButtons(View view){
        filter20km= view.findViewById(R.id.filter20km);
        filter15km= view.findViewById(R.id.filter15km);
        filter10km= view.findViewById(R.id.filter10km);
        filter5km= view.findViewById(R.id.filter5km);
    }

    public void filterButtons(){
        filter5km.setOnClickListener(v -> placesAdapter.placeDistance(latitude,longitude,5));
        filter10km.setOnClickListener(v -> placesAdapter.placeDistance(latitude,longitude,10));
        filter15km.setOnClickListener(v -> placesAdapter.placeDistance(latitude,longitude,15));
        filter20km.setOnClickListener(v -> placesAdapter.placeDistance(latitude,longitude,20));
    }
}