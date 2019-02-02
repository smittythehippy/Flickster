package com.example.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.flickster.Models.Movie;
import com.example.flickster.R;

import org.w3c.dom.Text;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<Movie> movies;

    public MoviesAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d("smile", "OnCreateViewHolder");
        int choice = 0;
        double baseline = 5;
        double movieRating = movies.get(i).getRating();

        // Test for landscape orientation here
        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_movie, viewGroup, false));
        else {
            // If statement here to decide which xml file to inflate
            if (movieRating >= baseline) {
                choice = 0;
            } else {
                choice = 1;
            }

            switch (choice) {
                case 0:
                    return new ViewHolderBackDropOnly(LayoutInflater.from(context).inflate(R.layout.backdrop_only, viewGroup, false));
                case 1:
                    return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_movie, viewGroup, false));
            }
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Log.d("smile", "OnBindViewHolder: " + position);
        Movie movie = movies.get(position);

        //Binding movie data into view holder
        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            ViewHolder vh1 = (ViewHolder)viewHolder;
            vh1.bind(movie);
        }
        else{
            if(movies.get(position).getRating() < 5.0) {
                ViewHolder vh1 = (ViewHolder)viewHolder;
                vh1.bind(movie);
            }
            else {
                ViewHolderBackDropOnly vh2 = (ViewHolderBackDropOnly)viewHolder;
                vh2.bind(movie);
            }
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public int getItemViewType(int position) {
        return position;
    }

    class ViewHolderBackDropOnly extends RecyclerView.ViewHolder{

        ImageView ivBackdrop;

        public ViewHolderBackDropOnly(@NonNull View itemView){
            super(itemView);
            ivBackdrop = itemView.findViewById(R.id.ivBackdrop);
        }

        public void bind(Movie movie) {
            Glide.with(context).load(movie.getBackdropPath()).into(ivBackdrop);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageUrl = movie.getPosterPath();
            // Reference the backdrop path if phone is in landscape
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                imageUrl = movie.getBackdropPath();
            }
            Glide.with(context).load(imageUrl).into(ivPoster);
        }
    }
}
