package com.example.flickster;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.flickster.Models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sefford.circularprogressdrawable.CircularProgressDrawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class DetailActivity extends YouTubeBaseActivity {

    private static final String YOUTUBE_API_KEY = "AIzaSyAgpdXCRz4VNU0_QxzALSVinrSoS1W66jk";
    private static final String TRAILERS_API = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    Movie movie;

    TextView tvTitle;
    TextView tvOverview;
    TextView tvRelease;
    RatingBar ratingBar;
    ImageView ivBackdrop;
    YouTubePlayerView youTubePlayerView;
    Button ratingInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        tvRelease = findViewById(R.id.tvRelease);
        ratingBar = findViewById(R.id.ratingBar);
        ratingInfo = findViewById(R.id.ratingInfo);
        youTubePlayerView = findViewById(R.id.player);
        ivBackdrop = findViewById(R.id.ivBackdrop);
        movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        tvTitle.setText(movie.getTitle());
        tvOverview.setText("     " + movie.getOverview());
        ratingBar.setRating((float)movie.getRating());

        tvRelease.setText("Release Date: " + movie.getReleaseDate());


        GlideApp.with(this)
                .load(movie.getBackdropPath())
                .into(ivBackdrop);

        ivBackdrop.setVisibility(View.INVISIBLE);

        ratingInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), String.format("%s average from %d total votes.", movie.getRating(), movie.getVoteCount()), Toast.LENGTH_LONG).show();
            }
        });
        //Get all trailers for movie
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(TRAILERS_API, movie.getMovieId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray results = response.getJSONArray("results");
                    if(results.length() == 0){

                        youTubePlayerView.setVisibility(View.INVISIBLE);
                        ivBackdrop.setVisibility(View.VISIBLE);
                    }
                    else {
                        JSONObject movieTrailer = results.getJSONObject(0);
                        String youtubeKey = movieTrailer.getString("key");
                        initializeYoutube(youtubeKey);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void initializeYoutube(final String youtubeKey) {
        youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d("smile", "sucesss");
                if(movie.getRating() >= 7){
                    youTubePlayer.loadVideo(youtubeKey);
                }
                else {
                    youTubePlayer.cueVideo(youtubeKey);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("smile", "fail");
            }
        });
    }
}
