package com.example.aplicacionesmoviles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.aplicacionesmoviles.api.ApiClient;
import com.example.aplicacionesmoviles.api.UsersApi;
import com.example.aplicacionesmoviles.databinding.ActivityUpdatePasswordBinding;
import com.example.aplicacionesmoviles.model.User;
import com.example.aplicacionesmoviles.utils.ErrorModal;
import com.example.aplicacionesmoviles.utils.LoadingBar;
import com.example.aplicacionesmoviles.utils.SessionManagement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePasswordActivity extends AppCompatActivity {

    private EditText pass, confirmPass;
    private User user;
    private Button update;
    private LoadingBar loadingDialog;
    private UsersApi mUserApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        ActivityUpdatePasswordBinding binding=ActivityUpdatePasswordBinding.inflate(getLayoutInflater());
        View updateViewBinding = binding.getRoot();
        setContentView(updateViewBinding);

        mUserApi= ApiClient.getInstanceRetrofit().create(UsersApi.class);

        user =(User) getIntent().getSerializableExtra("user");

        pass=binding.pass;
        confirmPass=binding.confirmPass;
        update=binding.updateBtn;

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation(binding);
            }
        });

    }

    public void validation(ActivityUpdatePasswordBinding binding){
        if (pass.getText().toString().equals("")){
            binding.errorMsg.setText("La contraseña no puede estar vacía");
        }else {
            if (pass.getText().toString().equals(confirmPass.getText().toString())){
                loadingDialog= new LoadingBar(this);
                loadingDialog.startDialog();
                user.password=pass.getText().toString();
                updateUser(user);
            }else {
                binding.errorMsg.setText("La contraseña no coincide");
            }
        }
    }

    public void updateUser(User user){
        Call<User> userCall=mUserApi.updateUser(user);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                SessionManagement sessionManagement = new SessionManagement(UpdatePasswordActivity.this);
                sessionManagement.saveSession(response.body());
                Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(profileIntent);
                loadingDialog.hideDialog();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                ErrorModal.createErrorDialog(UpdatePasswordActivity.this,getString(R.string.genericErrorText));
                loadingDialog.hideDialog();
            }
        });
    }
}