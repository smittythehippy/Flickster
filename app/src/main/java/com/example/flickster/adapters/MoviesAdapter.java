package com.example.flickster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sefford.circularprogressdrawable.CircularProgressDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import com.example.flickster.DetailActivity;
import com.example.flickster.GlideApp;
import com.example.flickster.Models.Movie;
import com.example.flickster.R;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MoviesAdapter<val> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<Movie> movies;
    final static double baseline = 5.0;
    final int radius = 25;
    final int margin = 5;


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
        ProgressBar progressBar;
        ConstraintLayout container;

        public ViewHolderBackDropOnly(@NonNull View itemView){
            super(itemView);
            ivBackdrop = itemView.findViewById(R.id.ivBackdrop);
            container = itemView.findViewById(R.id.container);
            progressBar = itemView.findViewById(R.id.progressBar);
        }

        public void bind(final Movie movie) {

            GlideApp.with(context)
                    .load(movie.getBackdropPath())
                    .fitCenter()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .into(ivBackdrop);

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
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            progressBar = itemView.findViewById(R.id.progressBar);
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
            GlideApp.with(context)
                    .load(imageUrl)
                    .fitCenter()

                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .into(ivPoster);

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
