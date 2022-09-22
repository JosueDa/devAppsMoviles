package com.example.aplicacionesmoviles.api;

import com.example.aplicacionesmoviles.model.Place;
import com.example.aplicacionesmoviles.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PlacesApi {

    @GET("touristic")
    Call<List<Place>> getTouristicPlaces();

    @GET("restaurants")
    Call<List<Place>> getRestaurantsPlaces();
}
