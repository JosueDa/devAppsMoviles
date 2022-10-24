package com.example.aplicacionesmoviles.api;

import com.example.aplicacionesmoviles.model.Comment;
import com.example.aplicacionesmoviles.model.Place;
import com.example.aplicacionesmoviles.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PlacesApi {

    @GET("touristic")
    Call<List<Place>> getTouristicPlaces();

    @GET("restaurants")
    Call<List<Place>> getRestaurantsPlaces();

    @GET("comments")
    Call<List<Comment>> getCommentsPlaces();

    @GET("commentsByPlace")
    Call<List<Comment>> getCommentsPlacesById(@Query("id") int id);

    @GET("findPlaceFavByUserId")
    Call<List<Place>> getFavPlacesById(@Query("id") int id);

    @GET("findRestFavByUserId")
    Call<List<Place>> getFavRestaurantsById(@Query("id") int id);

    @DELETE("comment")
    Call<Void> deleteComments(@Query("id") int id);

    @POST("comment")
    Call<Comment> createComment(@Body Comment comment);


}
