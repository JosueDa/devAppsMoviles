package com.example.aplicacionesmoviles.api;

import com.example.aplicacionesmoviles.model.Score;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ScoreApi {
    @GET("scores")
    Call<List<Score>> getScores();

    @POST("score")
    Call<Score> createScore(@Body Score score);

    @GET("findScoreByUserId")
    Call<List<Score>>findScoreByUserId(@Query("id") int id);

    @GET("findByUserAndPlace")
    Call<Score>findByUserAndPlace(@Query("userId") int userId,@Query("placeId") int placeId);
}
