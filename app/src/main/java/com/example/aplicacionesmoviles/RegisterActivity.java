package com.example.aplicacionesmoviles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.aplicacionesmoviles.api.ApiClient;
import com.example.aplicacionesmoviles.api.UsersApi;
import com.example.aplicacionesmoviles.databinding.ActivityMainBinding;
import com.example.aplicacionesmoviles.databinding.ActivityRegisterBinding;
import com.example.aplicacionesmoviles.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private UsersApi mUserApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUserApi= ApiClient.getInstanceRetrofit().create(UsersApi.class);

        ActivityRegisterBinding binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View registerViewBinding = binding.getRoot();
        setContentView(registerViewBinding);

        binding.registerBtn.setOnClickListener(listener -> {
            User user = new User(binding.name.getText().toString(), binding.lastName.getText().toString(),
                    binding.email.getText().toString(), binding.country.getSelectedCountryName(),
                    binding.phone.getText().toString(),binding.password.getText().toString());

            createUser(user);
        });
    }

    public void createUser(User user){
        Call<User> userCall=mUserApi.createUser(user);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.body().email.isEmpty()){
                    Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(homeIntent);
                }else {
                    Toast.makeText(RegisterActivity.this, "Error al crear el usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}