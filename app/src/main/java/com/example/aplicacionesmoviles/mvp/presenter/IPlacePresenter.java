package com.example.aplicacionesmoviles.mvp.presenter;

import com.example.aplicacionesmoviles.model.Place;

import java.util.List;

public interface IPlacePresenter {

    void getPlaces();

    void onPlacesSuccess(List<Place> places);

    void onPlacesFailure(String errorMessage);
}
