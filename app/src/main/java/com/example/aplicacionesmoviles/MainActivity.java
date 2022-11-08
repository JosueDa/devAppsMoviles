package com.example.aplicacionesmoviles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.aplicacionesmoviles.api.ApiClient;
import com.example.aplicacionesmoviles.api.UsersApi;
import com.example.aplicacionesmoviles.databinding.ActivityMainBinding;
import com.example.aplicacionesmoviles.model.User;
import com.example.aplicacionesmoviles.utils.ErrorModal;
import com.example.aplicacionesmoviles.utils.LoadingBar;
import com.example.aplicacionesmoviles.utils.SessionManagement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private UsersApi mUserApi;
    LoadingBar loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUserApi= ApiClient.getInstanceRetrofit().create(UsersApi.class);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View mainViewBinding = binding.getRoot();
        setContentView(mainViewBinding);

        binding.logInBtn.setOnClickListener(listener ->{
                    loadingDialog= new LoadingBar(this);
                    loadingDialog.startDialog();
                    login(binding.email.getText().toString(),binding.password.getText().toString());
                });

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
                    loadingDialog.hideDialog();
                    moveToHomeActivity();
                }else {
                    loadingDialog.hideDialog();
                    ErrorModal.createErrorDialog(MainActivity.this,getString(R.string.emailErrorText));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                loadingDialog.hideDialog();
                ErrorModal.createErrorDialog(MainActivity.this,getString(R.string.emailErrorText));
            }
        });
    }

    public void moveToHomeActivity(){
        Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(homeIntent);
    }
}