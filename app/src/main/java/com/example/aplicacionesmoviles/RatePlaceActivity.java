package com.example.aplicacionesmoviles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.aplicacionesmoviles.api.ApiClient;
import com.example.aplicacionesmoviles.api.ScoreApi;
import com.example.aplicacionesmoviles.model.Place;
import com.example.aplicacionesmoviles.model.Score;
import com.example.aplicacionesmoviles.model.User;
import com.example.aplicacionesmoviles.utils.ErrorModal;
import com.example.aplicacionesmoviles.utils.LoadingBar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatePlaceActivity extends AppCompatActivity {

    int userId;
    private ScoreApi scoreApi;
    private Score score;
    private Place currentPlace;
    private LoadingBar loadingDialog;
    private RatingBar placeRatingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_place);
        currentPlace =(Place) getIntent().getSerializableExtra("place");
        score =(Score) getIntent().getSerializableExtra("score");
        SharedPreferences sharedPreferences= getSharedPreferences("session", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("id",-1);

        placeRatingBar=(RatingBar) findViewById(R.id.finalRatingBar);
        placeRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                loadingDialog= new LoadingBar(RatePlaceActivity.this);
                loadingDialog.startDialog();
                createScore(rating,RatePlaceActivity.this);
            }
        });
    }

    public void createScore(float rating, Context context){
        if (score.id==0){
            score.place=currentPlace.id;
            score.user=userId;
        }
        score.score=rating;
        scoreApi= ApiClient.getInstanceRetrofit().create(ScoreApi.class);
        Call<Score> scoreCall= scoreApi.createScore(score);
        scoreCall.enqueue(new Callback<Score>() {
            @Override
            public void onResponse(Call<Score> call, Response<Score> response) {
                loadingDialog.hideDialog();
                Toast.makeText(context, "Gracias por tu calificación!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, HomeActivity.class);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(Call<Score> call, Throwable t) {
                loadingDialog.hideDialog();
                ErrorModal.createErrorDialog(RatePlaceActivity.this,getString(R.string.genericErrorText));
            }
        });
    }
}