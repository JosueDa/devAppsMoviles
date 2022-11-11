package com.example.aplicacionesmoviles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.aplicacionesmoviles.databinding.ActivityHomeBinding;
import com.example.aplicacionesmoviles.databinding.ActivityProfileBinding;
import com.example.aplicacionesmoviles.model.User;
import com.example.aplicacionesmoviles.utils.SessionManagement;

public class ProfileActivity extends AppCompatActivity {

    private User user;
    private TextView nameTV,lastNameTV,emailTV,phoneTV;
    private ImageButton logOut,updateBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActivityProfileBinding binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View profileViewBinding = binding.getRoot();
        setContentView(profileViewBinding);

        user=new User();
        getData();
        instanceButtons(binding);

        nameTV.setText(user.name);
        lastNameTV.setText(user.lastName);
        emailTV.setText(user.email);
        phoneTV.setText(user.phone);

        logOut.setOnClickListener(v -> logOut());
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UpdateProfileActivity.class);
                intent.putExtra("user", user);
                v.getContext().startActivity(intent);
            }
        });
    }

    public void getData(){
        SharedPreferences sharedPreferences= getSharedPreferences("session", Context.MODE_PRIVATE);
        user.id=sharedPreferences.getInt("id",-1);
        user.name = sharedPreferences.getString("name","");
        user.lastName = sharedPreferences.getString("lastname","");
        user.email = sharedPreferences.getString("session_user","");
        user.phone = sharedPreferences.getString("phone","");
        user.country = sharedPreferences.getString("country","");
        user.password = sharedPreferences.getString("pass","");
    }

    public void instanceButtons(ActivityProfileBinding binding){
        nameTV=binding.name;
        lastNameTV=binding.lastName;
        emailTV=binding.email;
        phoneTV=binding.phone;
        logOut=binding.logOutBtn;
        updateBtn=binding.editButton;
    }

    public void logOut(){
        SessionManagement sessionManagement= new SessionManagement(ProfileActivity.this);
        sessionManagement.removeSession();
        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivity);
    }

    @Override
    public void onBackPressed() {
        Intent profileIntent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(profileIntent);
        super.onBackPressed();
    }
}