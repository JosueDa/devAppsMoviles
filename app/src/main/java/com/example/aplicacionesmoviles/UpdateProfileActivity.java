package com.example.aplicacionesmoviles;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.aplicacionesmoviles.api.ApiClient;
import com.example.aplicacionesmoviles.api.UsersApi;
import com.example.aplicacionesmoviles.databinding.ActivityUpdateProfileBinding;
import com.example.aplicacionesmoviles.model.Place;
import com.example.aplicacionesmoviles.model.User;
import com.example.aplicacionesmoviles.utils.ErrorModal;
import com.example.aplicacionesmoviles.utils.LoadingBar;
import com.example.aplicacionesmoviles.utils.SessionManagement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity {

    private LoadingBar loadingDialog;
    private UsersApi mUserApi;
    private Button updateBtn;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        mUserApi= ApiClient.getInstanceRetrofit().create(UsersApi.class);

        user =(User) getIntent().getSerializableExtra("user");

        ActivityUpdateProfileBinding binding=ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        View updateViewBinding = binding.getRoot();
        setContentView(updateViewBinding);

        binding.country.setCountryForNameCode(user.country);
        binding.phone.setText(user.phone);
        binding.name.setText(user.name);
        binding.lastName.setText(user.lastName);

        binding.updateBtn.setOnClickListener(v -> {
            loadingDialog= new LoadingBar(this);
            loadingDialog.startDialog();
            user.country=binding.country.getSelectedCountryNameCode();
            user.lastName=binding.lastName.getText().toString();
            user.phone=binding.phone.getText().toString();
            user.name=binding.name.getText().toString();
            updateUser(user);
        });

        binding.updatePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(getApplicationContext(), UpdatePasswordActivity.class);
                profileIntent.putExtra("user", user);
                startActivity(profileIntent);
            }
        });
    }

    public void updateUser(User user){
        Call<User> userCall=mUserApi.updateUser(user);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                SessionManagement sessionManagement = new SessionManagement(UpdateProfileActivity.this);
                sessionManagement.saveSession(response.body());
                Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(profileIntent);
                loadingDialog.hideDialog();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                ErrorModal.createErrorDialog(UpdateProfileActivity.this,getString(R.string.genericErrorText));
                loadingDialog.hideDialog();
            }
        });
    }

}