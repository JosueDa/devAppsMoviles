package com.example.aplicacionesmoviles.mvp.model;

import com.example.aplicacionesmoviles.R;
import com.example.aplicacionesmoviles.api.ApiClient;
import com.example.aplicacionesmoviles.api.PlacesApi;
import com.example.aplicacionesmoviles.model.Place;
import com.example.aplicacionesmoviles.mvp.presenter.IPlacePresenter;
import com.example.aplicacionesmoviles.utils.ErrorModal;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceModel implements IPlaceModel{

    private PlacesApi placesApi;
    private IPlacePresenter presenter;
    private boolean typePlace;

    public PlaceModel(IPlacePresenter presenter, boolean typePlace){
        this.presenter=presenter;
        placesApi=ApiClient.getInstanceRetrofit().create(PlacesApi.class);
        this.typePlace=typePlace;
    }

    @Override
    public void getPlaces() {
        Call<List<Place>> placeCall;
        if (typePlace) placeCall=placesApi.getTouristicPlaces();
        else placeCall=placesApi.getRestaurantsPlaces();

        placeCall.enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                presenter.onPlacesSuccess(response.body());
            }
            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {
                presenter.onPlacesFailure("Lo sentimos, hubo un error");
            }
        });
    }
}
