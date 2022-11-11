package com.example.aplicacionesmoviles.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.aplicacionesmoviles.PlaceDescription;
import com.example.aplicacionesmoviles.R;
import com.example.aplicacionesmoviles.RatePlaceActivity;
import com.example.aplicacionesmoviles.UpdateProfileActivity;
import com.example.aplicacionesmoviles.adapter.CommentAdapter;
import com.example.aplicacionesmoviles.api.ApiClient;
import com.example.aplicacionesmoviles.api.PlacesApi;
import com.example.aplicacionesmoviles.api.ScoreApi;
import com.example.aplicacionesmoviles.model.Comment;
import com.example.aplicacionesmoviles.model.Place;
import com.example.aplicacionesmoviles.model.Score;
import com.example.aplicacionesmoviles.utils.ErrorModal;
import com.example.aplicacionesmoviles.utils.LoadingBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    PlacesApi placesApi;
    Place currentPlace=new Place();
    private LoadingBar loadingDialog;
    CommentAdapter commentAdapter= new CommentAdapter(new ArrayList<>());
    Score score;


    public CommentsFragment() {
    }

    public CommentsFragment(Score score) {
        this.score=score;
    }

    public static CommentsFragment newInstance(String param1, String param2) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_comments, container, false);

        PlaceDescription placeDescriptionActivity= (PlaceDescription) getActivity();
        currentPlace= Objects.requireNonNull(placeDescriptionActivity).getCurrentPlace();

        EditText addCommentEditText= view.findViewById(R.id.addCommentEditText);
        Button addCommentBtn= view.findViewById(R.id.addCommentButton);


        placesApi= ApiClient.getInstanceRetrofit().create(PlacesApi.class);

        RecyclerView commentRecyclerView=(RecyclerView) view.findViewById(R.id.commentsList);
        commentRecyclerView.setAdapter(commentAdapter);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Call<List<Comment>> commentsCall=placesApi.getCommentsPlacesById(currentPlace.id);
        commentsCall.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                commentAdapter.reloadData(response.body());
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                ErrorModal.createErrorDialog(getContext(),getString(R.string.genericErrorText));
            }
        });

        addCommentBtn.setOnClickListener(v -> {
            loadingDialog= new LoadingBar(getContext());
            loadingDialog.startDialog();
            addComment(createCommentObject(v, addCommentEditText));
            if (score.id==0){
                ratingDialog(getContext());
            }
        });
        return view;
    }

    public String getDate(){
        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        Date dateObj = calendar.getTime();
        return dtf.format(dateObj);
    }

    public void addComment(Comment comment){
        Call<Comment> commentCall=placesApi.createComment(comment);
        commentCall.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                Comment commentResponse= response.body();
                commentAdapter.addComment(commentResponse);
                loadingDialog.hideDialog();
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                loadingDialog.hideDialog();
                ErrorModal.createErrorDialog(getContext(),getString(R.string.genericErrorText));
            }
        });
    }

    public Comment createCommentObject(View v, EditText addCommentEditText){
        Comment comment= new Comment();

        SharedPreferences sharedPreferences=v.getContext().getSharedPreferences("session", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("id",-1);
        comment.userName= sharedPreferences.getString("name","");
        comment.text=addCommentEditText.getText().toString();
        comment.createdDate=getDate();
        comment.place_id=currentPlace.id;
        comment.user_id=userId;

        addCommentEditText.getText().clear();
        return comment;
    }

    public void ratingDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Deseas dejar una calificación?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Ir a calificar",
                (dialog, id) -> {
                    Intent intent = new Intent(context, RatePlaceActivity.class);
                    intent.putExtra("score", score);
                    intent.putExtra("place", currentPlace);
                    context.startActivity(intent);
                });

        builder.setNegativeButton(
                "En otro momento",
                (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder.create();
        alert11.show();
    }


}