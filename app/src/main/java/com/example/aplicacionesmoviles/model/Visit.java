package com.example.aplicacionesmoviles.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Visit implements Serializable {

    public int id;
    public int user;
    public int place;
    @SerializedName("date")
    public String visit="";

}
