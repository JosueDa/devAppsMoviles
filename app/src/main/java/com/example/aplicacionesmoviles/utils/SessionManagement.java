package com.example.aplicacionesmoviles.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.aplicacionesmoviles.model.User;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String shared_pref_name="session";
    String session_key="session_user";

    public SessionManagement(Context context){
        sharedPreferences=context.getSharedPreferences(shared_pref_name,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    public void saveSession(User user){
        String email=user.email;
        editor.putString(session_key,email);

        String name=user.name;
        editor.putString("name",name);

        String lastname=user.lastName;
        editor.putString("lastname",lastname);

        String phone=user.phone;
        editor.putString("phone",phone);

        String country=user.country;
        editor.putString("country",country);

        String pass=user.password;
        editor.putString("pass",pass);

        int id=user.id;
        editor.putInt("id",id);

        editor.commit();
    }

    public void saveLocation(Double latitude, Double longitude){
        editor.putFloat("latitude",latitude.floatValue());
        editor.putFloat("longitude",longitude.floatValue());
        editor.commit();
    }

    public String getSession(){
        return sharedPreferences.getString(session_key,"");
    }

    public void removeSession(){
        editor.putString(session_key,"").commit();
    }
}
