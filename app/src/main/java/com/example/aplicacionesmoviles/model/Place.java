package com.example.aplicacionesmoviles.model;

import java.io.Serializable;

public class Place implements Serializable {

    public int id;
    public String name;
    public String city;
    public String imageUrl;
    public String score;
    public String type;
    public String description;
    public String latitude;
    public String longitude;
    public String visit;
    public int visitId;
    public double distance;

    public String getVisitDate(){
        if(visit.equals("")) return visit;
        else {
            String[] yearMonthDay = visit.split("/");
            return yearMonthDay[2]+"/"+yearMonthDay[1]+"/"+yearMonthDay[0];
        }
    }



}
