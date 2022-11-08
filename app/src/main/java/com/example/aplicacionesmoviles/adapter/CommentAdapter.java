package com.example.aplicacionesmoviles.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicacionesmoviles.MainActivity;
import com.example.aplicacionesmoviles.R;
import com.example.aplicacionesmoviles.RegisterActivity;
import com.example.aplicacionesmoviles.api.ApiClient;
import com.example.aplicacionesmoviles.api.FavoriteApi;
import com.example.aplicacionesmoviles.api.PlacesApi;
import com.example.aplicacionesmoviles.model.Comment;
import com.example.aplicacionesmoviles.model.Favorite;
import com.example.aplicacionesmoviles.model.User;
import com.example.aplicacionesmoviles.utils.ErrorModal;
import com.example.aplicacionesmoviles.utils.SessionManagement;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<Comment> mComments;
    SharedPreferences sharedPreferences;
    Context context;
    PlacesApi placesApi= ApiClient.getInstanceRetrofit().create(PlacesApi.class);

    public CommentAdapter(List<Comment> mComments) {
        this.mComments = mComments;
    }

    public void reloadData(List<Comment> mComments){
        this.mComments=mComments;
        notifyDataSetChanged();
    }

    public Comment getCommentByIndex(int index) {
        return mComments.get(index);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());

        View contactView=inflater.inflate(R.layout.item_comment,parent,false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment =getCommentByIndex(position);
        sharedPreferences=context.getSharedPreferences("session",Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("id",-1);

        TextView mUserNameTextView= holder.mUserName;
        mUserNameTextView.setText(comment.userName);

        TextView mCommentTextTextView= holder.mCommentText;
        mCommentTextTextView.setText(comment.text);

        TextView mCreateDateTextView= holder.mCreateDate;
        mCreateDateTextView.setText(comment.createdDate);

        Button deleteButton= holder.deleteButton;

        if (userId!=comment.user_id){
            deleteButton.setVisibility(View.GONE);
        }

        deleteButton.setOnClickListener(listener->deleteComment(comment.id,position));
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView mUserName;
        private final TextView mCommentText;
        private final TextView mCreateDate;
        private final Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mUserName=(TextView)itemView.findViewById(R.id.user_name_comment);
            mCommentText=(TextView)itemView.findViewById(R.id.textComment);
            mCreateDate=(TextView) itemView.findViewById(R.id.createdDate);
            deleteButton=(Button) itemView.findViewById(R.id.deleteCommentButton);
        }
    }

    public void deleteComment(int id, int position){
        Call<Void> commentCall=placesApi.deleteComments(id);
        commentCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mComments.remove(position);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                ErrorModal.createErrorDialog(context,context.getString(R.string.genericErrorText));
            }
        });
    }

    public void addComment(Comment comment){
        mComments.add(comment);
        notifyDataSetChanged();
    }
}
