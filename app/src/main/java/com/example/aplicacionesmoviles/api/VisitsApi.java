package com.example.aplicacionesmoviles.api;

import com.example.aplicacionesmoviles.model.Score;
import com.example.aplicacionesmoviles.model.Visit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface VisitsApi {
    @GET("visits")
    Call<List<Visit>> getVisits();

    @POST("visit")
    Call<Visit> createVisit(@Body Visit visit);

    @GET("findVisitsByUserId")
    Call<List<Visit>>findVisitsByUserId(@Query("id") int id);

    @GET("findVisitByUserAndPlace")
    Call<Visit>findVisitByUserAndPlace(@Query("userId") int userId,@Query("placeId") int placeId);
}
