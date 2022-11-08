package com.example.aplicacionesmoviles.mvp.view;

import com.example.aplicacionesmoviles.model.Place;

import java.util.List;

public interface IPlaceView {

    void onPlacesSuccess(List<Place> places);

    void onPlacesFailure(String errorMessage);
}
