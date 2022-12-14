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
        int id=user.id;
        editor.putInt("id",id);
        editor.commit();
    }

    public String getSession(){
        return sharedPreferences.getString(session_key,"");
    }

    public void removeSession(){
        editor.putString(session_key,"").commit();
    }
}
