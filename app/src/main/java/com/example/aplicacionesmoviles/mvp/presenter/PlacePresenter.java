package com.example.aplicacionesmoviles.mvp.presenter;

import com.example.aplicacionesmoviles.model.Place;
import com.example.aplicacionesmoviles.mvp.model.IPlaceModel;
import com.example.aplicacionesmoviles.mvp.model.PlaceModel;
import com.example.aplicacionesmoviles.mvp.view.IPlaceView;

import java.util.List;

public class PlacePresenter implements IPlacePresenter {

    private IPlaceView view;
    private IPlaceModel model;

    public PlacePresenter(IPlaceView view, boolean typePlace){
        this.view=view;
        model= new PlaceModel(this,typePlace);
    }

    @Override
    public void getPlaces() {
        model.getPlaces();
    }

    @Override
    public void onPlacesSuccess(List<Place> places) {
        if (view==null) return;
        view.onPlacesSuccess(places);
    }

    @Override
    public void onPlacesFailure(String errorMessage) {
        if (view==null) return;
        view.onPlacesFailure(errorMessage);
    }
}
