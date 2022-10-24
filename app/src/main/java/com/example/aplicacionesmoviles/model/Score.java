package com.example.aplicacionesmoviles.model;

import java.io.Serializable;

public class Score implements Serializable {

    public int id;
    public int user;
    public int place;
    public float score;

    public Score(){
        id=0;
        user=0;
        place=0;
        score=0;
    }
}
