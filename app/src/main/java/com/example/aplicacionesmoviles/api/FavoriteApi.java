package com.example.aplicacionesmoviles.api;

import com.example.aplicacionesmoviles.model.Favorite;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FavoriteApi {
    @GET("findFavoriteByUserId")
    Call<List<Favorite>> getFavoritesByUserId(@Query("id") int id);

    @POST("favorite")
    Call<Favorite> createFavorite(@Body Favorite favorite);

    @DELETE("favorite")
    Call<Void>deleteFav(@Query("id") int id);
}
