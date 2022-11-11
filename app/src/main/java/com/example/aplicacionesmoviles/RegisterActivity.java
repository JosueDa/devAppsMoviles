package com.example.aplicacionesmoviles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.aplicacionesmoviles.api.ApiClient;
import com.example.aplicacionesmoviles.api.UsersApi;
import com.example.aplicacionesmoviles.databinding.ActivityRegisterBinding;
import com.example.aplicacionesmoviles.model.User;
import com.example.aplicacionesmoviles.utils.ErrorModal;
import com.example.aplicacionesmoviles.utils.LoadingBar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {


    private LoadingBar loadingDialog;
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
            validation(binding);

        });
    }

    public void validation(ActivityRegisterBinding binding){
        if (!emptyValidation(binding)){
            binding.errorMsg.setText("Debes llenar todos los campos");
        }else {
            if (binding.password.getText().toString().equals(binding.confirmPass.getText().toString())){
                loadingDialog= new LoadingBar(this);
                loadingDialog.startDialog();
                User user = new User(binding.name.getText().toString(), binding.lastName.getText().toString(),
                        binding.email.getText().toString(), binding.country.getSelectedCountryNameCode(),
                        binding.phone.getText().toString(),binding.password.getText().toString());

                createUser(user);
            }else {
                binding.errorMsg.setText("La contraseña no coincide");
            }
        }

    }

    public boolean emptyValidation(ActivityRegisterBinding binding){
        if (binding.name.getText().toString().equals("")||
                binding.lastName.getText().toString().equals("")||
                binding.email.getText().toString().equals("")||
                binding.country.getSelectedCountryNameCode().equals("")||
                binding.phone.getText().toString().equals("")||
                binding.password.getText().toString().equals("")){
            return false;
        }else return true;
    }
    public void createUser(User user){
        Call<User> userCall=mUserApi.createUser(user);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                loadingDialog.hideDialog();
                if(!response.body().email.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Usuario creado exitosamente!", Toast.LENGTH_SHORT).show();
                    Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(homeIntent);
                }else {
                    ErrorModal.createErrorDialog(RegisterActivity.this,getString(R.string.genericErrorText));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                loadingDialog.hideDialog();
                ErrorModal.createErrorDialog(RegisterActivity.this,getString(R.string.genericErrorText));
            }
        });
    }
}