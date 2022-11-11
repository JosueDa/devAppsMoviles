package com.example.aplicacionesmoviles.api;

import com.example.aplicacionesmoviles.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UsersApi {
    @GET("user")
    Call<User> getUserByEmail(@Query("email") String email);

    @POST("user")
    Call<User> createUser(@Body User user);

    @POST("updateUser")
    Call<User> updateUser(@Body User user);


}
