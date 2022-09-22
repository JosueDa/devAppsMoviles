package com.example.aplicacionesmoviles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.aplicacionesmoviles.api.ApiClient;
import com.example.aplicacionesmoviles.api.UsersApi;
import com.example.aplicacionesmoviles.databinding.ActivityMainBinding;
import com.example.aplicacionesmoviles.model.User;
import com.example.aplicacionesmoviles.utils.SessionManagement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private UsersApi mUserApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUserApi= ApiClient.getInstanceRetrofit().create(UsersApi.class);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View mainViewBinding = binding.getRoot();
        setContentView(mainViewBinding);

        binding.logInBtn.setOnClickListener(listener ->
                login(binding.email.getText().toString(),binding.password.getText().toString()));

        binding.registerBtn.setOnClickListener(listener -> {
            Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(registerIntent);
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
        String userName=sessionManagement.getSession();

        if (!userName.equals("")) moveToHomeActivity();
    }

    public void login(String email, String password){
        Call<User> userCall=mUserApi.getUserByEmail(email);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user= response.body();
                if (user.password.equals(password)){
                    SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
                    sessionManagement.saveSession(user);
                    moveToHomeActivity();
                }else {
                    Toast.makeText(MainActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void moveToHomeActivity(){
        Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(homeIntent);
    }
}