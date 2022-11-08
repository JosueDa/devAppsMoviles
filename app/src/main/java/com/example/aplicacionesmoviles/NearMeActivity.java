package com.example.aplicacionesmoviles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.aplicacionesmoviles.adapter.ViewDistPageAdapter;
import com.example.aplicacionesmoviles.adapter.ViewFavPageAdapter;
import com.example.aplicacionesmoviles.databinding.ActivityFavPlacesBinding;
import com.example.aplicacionesmoviles.databinding.ActivityNearMeBinding;
import com.example.aplicacionesmoviles.utils.ErrorModal;
import com.example.aplicacionesmoviles.utils.SessionManagement;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class NearMeActivity extends AppCompatActivity {

    private final static  int REQUEST_CODE=100;
    FusedLocationProviderClient fusedLocationProviderClient;

    public double latitude;
    public double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_me);

        ActivityNearMeBinding binding = ActivityNearMeBinding.inflate(getLayoutInflater());
        View nearMeViewBinding = binding.getRoot();
        setContentView(nearMeViewBinding);

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();

        ViewDistPageAdapter viewPageAdapter = new ViewDistPageAdapter(getSupportFragmentManager(), getLifecycle());

        TabLayout tableLayout= binding.tabNearMeBar;
        ViewPager2 viewPager= binding.viewNearMePagerPlace;
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
    }

    public void getLastLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location!=null){
                        Geocoder geocoder= new Geocoder(NearMeActivity.this, Locale.getDefault());
                        List<Address> addresses= null;
                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                            latitude=addresses.get(0).getLatitude();
                            longitude=addresses.get(0).getLongitude();

                            SessionManagement sessionManagement = new SessionManagement(NearMeActivity.this);
                            sessionManagement.saveLocation(latitude,longitude);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }else{
            askPermission();
        }
    }

    public void askPermission(){
        ActivityCompat.requestPermissions(NearMeActivity.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==REQUEST_CODE){
            if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }else {
                ErrorModal.createErrorLocationDialog(this,"Es necesario tener el permiso de la aplicación",this);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}