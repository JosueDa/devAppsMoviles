package com.example.aplicacionesmoviles.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Visit implements Serializable {

    public int id;
    public int user;
    public int place;
    @SerializedName("date")
    public String visit="";

    public String getVisitDate(){
        if(visit.equals("")) return visit;
        else {
            String[] yearMonthDay = visit.split("/");
            return yearMonthDay[2]+"/"+yearMonthDay[1]+"/"+yearMonthDay[0];
        }
    }

}
