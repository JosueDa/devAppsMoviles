package com.example.aplicacionesmoviles.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplicacionesmoviles.PlaceDescription;
import com.example.aplicacionesmoviles.R;
import com.example.aplicacionesmoviles.api.ApiClient;
import com.example.aplicacionesmoviles.api.ScoreApi;
import com.example.aplicacionesmoviles.model.Place;
import com.example.aplicacionesmoviles.model.Score;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaceDescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaceDescriptionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlaceDescriptionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment placeDescriptionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaceDescriptionFragment newInstance(String param1, String param2) {
        PlaceDescriptionFragment fragment = new PlaceDescriptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_place_description, container, false);

        PlaceDescription placeDescriptionActivity= (PlaceDescription) getActivity();
        Place currentPlace=placeDescriptionActivity.getCurrentPlace();

        TextView placeDescriptionTextView= view.findViewById(R.id.placeDescription);
        placeDescriptionTextView.setText(currentPlace.description);

        // Inflate the layout for this fragment
        return view;
    }

}