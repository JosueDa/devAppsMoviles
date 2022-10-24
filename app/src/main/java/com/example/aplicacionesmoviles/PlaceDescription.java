package com.example.aplicacionesmoviles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aplicacionesmoviles.adapter.ViewPageAdapter;
import com.example.aplicacionesmoviles.api.ApiClient;
import com.example.aplicacionesmoviles.api.ScoreApi;
import com.example.aplicacionesmoviles.api.VisitsApi;
import com.example.aplicacionesmoviles.databinding.ActivityPlaceDescriptionBinding;
import com.example.aplicacionesmoviles.model.Place;
import com.example.aplicacionesmoviles.model.Score;
import com.example.aplicacionesmoviles.model.Visit;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceDescription extends AppCompatActivity {

    private Place place;
    private Score score;
    private RatingBar ratingPlace;
    private ScoreApi scoreApi;
    private VisitsApi visitsApi;
    private int userId, day, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_description);
        ActivityPlaceDescriptionBinding binding = ActivityPlaceDescriptionBinding.inflate(getLayoutInflater());
        View placeDescriptionViewBinding = binding.getRoot();
        setContentView(placeDescriptionViewBinding);

        place =(Place) getIntent().getSerializableExtra("place");
        score =(Score) getIntent().getSerializableExtra("score");

        SharedPreferences sharedPreferences= getSharedPreferences("session", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("id",-1);

        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(), getLifecycle());

        TabLayout tableLayout= binding.tabBar;

        Button calendarBtn=binding.calendarBtn;

        TextView lastVisited=binding.lastVisited;

        if (!place.visit.equals("")){
            String date= "Última visita: "+place.visit;
            lastVisited.setText(date);
        }else{
            lastVisited.setText(R.string.WithoutVisit);
        }


        ViewPager2 viewPager= binding.viewPagerPlace;
        viewPager.setAdapter(viewPageAdapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tableLayout.selectTab(tableLayout.getTabAt(position));
            }
        });

        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        instanceViews(binding,place);
        Button coordinateBtn=binding.coordinateBtn;
        Button wazeBtn=binding.wazeBtn;
        coordinateBtn.setOnClickListener(v -> deepLinkGoogleMapsIntent(place.latitude,place.longitude));
        wazeBtn.setOnClickListener(v -> wazeDeepLinkIntent(place.latitude,place.longitude));


        ratingPlace=binding.ratingBarF;
        ratingPlace.setRating(score.score);

        ratingPlace.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> createScore(rating));

        calendarBtn.setOnClickListener(v -> onSetDateListener(lastVisited));
    }

    public void instanceViews(ActivityPlaceDescriptionBinding binding,Place place){
        TextView placeNameTextView= binding.placeName;
        placeNameTextView.setText(place.name);

        TextView placeCityTextView= binding.placeCity;
        placeCityTextView.setText(place.city);

        ImageView placeImage=binding.placeDetailImage;
        Glide.with(this).load(place.imageUrl).into(placeImage);

        RatingBar ratingPlace=binding.ratingBarF;
    }

    public Place getCurrentPlace() {
        return place;
    }

    public void wazeDeepLinkIntent(String latitude, String longitude){
            String url = "https://www.waze.com/ul?ll="+latitude+"%2C"+longitude +"&navigate=yes&zoom=17";
            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
            startActivity(intent);
    }

    public void deepLinkGoogleMapsIntent(String latitude, String longitude){
        Uri gmmIntentUri = Uri.parse("geo:"+latitude +","+longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void createScore(float rating){
        if (score.id==0){
            score.place=place.id;
            score.user=userId;
        }
        score.score=rating;
        scoreApi= ApiClient.getInstanceRetrofit().create(ScoreApi.class);
        Call<Score> scoreCall= scoreApi.createScore(score);
        scoreCall.enqueue(new Callback<Score>() {
            @Override
            public void onResponse(Call<Score> call, Response<Score> response) {
                Toast.makeText(PlaceDescription.this, "Valoramos tu opinión", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Score> call, Throwable t) {
                Toast.makeText(PlaceDescription.this, "Lo sentimos, hubo un error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createVisit(Visit visit, TextView lastVisited){
        visitsApi= ApiClient.getInstanceRetrofit().create(VisitsApi.class);
        Call<Visit> visitCall= visitsApi.createVisit(visit);
        visitCall.enqueue(new Callback<Visit>() {
            @Override
            public void onResponse(Call<Visit> call, Response<Visit> response) {
                Visit visitResponse= response.body();
                String date= "Última visita: "+visitResponse.visit;
                lastVisited.setText(date);
            }

            @Override
            public void onFailure(Call<Visit> call, Throwable t) {

            }
        });
    }

    public TextView datePickerTitle(){
        ViewGroup.LayoutParams lp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, // Width of TextView
                ViewGroup.LayoutParams.WRAP_CONTENT); // Height of TextView
        TextView tv = new TextView(PlaceDescription.this);
        tv.setLayoutParams(lp);
        tv.setPadding(10, 10, 10, 10);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
        tv.setText(R.string.CalendarVisitsTitle);
        return tv;
    }

    public Visit createVisitObject(String date){
        Visit newVisit=new Visit();
        newVisit.id=place.visitId;
        newVisit.visit= date;
        newVisit.user=userId;
        newVisit.place= place.id;
            return newVisit;
    }

    public void onSetDateListener(TextView lastVisited){
        final Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(PlaceDescription.this, (view, year, month, dayOfMonth) -> {
            createVisit(createVisitObject(date(year,month,dayOfMonth)), lastVisited);
        },year, month,day);

        datePickerDialog.setCustomTitle(datePickerTitle());
        datePickerDialog.show();
    }

    public String date( int year,int month, int dayOfMonth){
        String monthFinal = "";
        String dayFinal="";
        month+=1;
        if (month<=9){
            monthFinal="0"+month;

        }else monthFinal=String.valueOf(month);

        if (dayOfMonth<=9){
            dayFinal="0"+dayOfMonth;
        }
        else dayFinal=String.valueOf(dayOfMonth);

        return dayFinal+"/"+monthFinal+"/"+year;
    }

}
