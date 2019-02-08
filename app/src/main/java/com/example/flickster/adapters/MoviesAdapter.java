package com.example.flickster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.flickster.DetailActivity;
import com.example.flickster.Models.Movie;
import com.example.flickster.R;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<Movie> movies;
    double baseline = 7.0;

    public MoviesAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d("smile", "OnCreateViewHolder");

        double movieRating = movies.get(i).getRating();

        // Test for landscape orientation here
        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_movie, viewGroup, false)); }
        else {
            // If statement here to decide which xml file to inflate
            if (movieRating >= baseline) {
             return new ViewHolderBackDropOnly(LayoutInflater.from(context).inflate(R.layout.backdrop_only, viewGroup, false));
            }
             return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_movie, viewGroup, false));
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
            if(movies.get(position).getRating() < baseline) {
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
        ConstraintLayout container;

        public ViewHolderBackDropOnly(@NonNull View itemView){
            super(itemView);
            ivBackdrop = itemView.findViewById(R.id.ivBackdrop);
            container = itemView.findViewById(R.id.container);
        }

        public void bind(final Movie movie) {
            Glide.with(context).load(movie.getBackdropPath()).into(ivBackdrop);

            //Add click listener on the whole row
            //Navigate to detail activity
            container.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("movie", Parcels.wrap(movie));
                    context.startActivity(intent);
                }
            });
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        ConstraintLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            container = itemView.findViewById(R.id.container);
        }

        public void bind(final Movie movie) {

            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageUrl = movie.getPosterPath();
            // Reference the backdrop path if phone is in landscape
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                imageUrl = movie.getBackdropPath();
            }
            Glide.with(context).load(imageUrl).into(ivPoster);

            //Add click listener on the whole row
            //Navigate to detail activity
            container.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("movie", Parcels.wrap(movie));
                    context.startActivity(intent);
                }
            });
        }
    }
}
