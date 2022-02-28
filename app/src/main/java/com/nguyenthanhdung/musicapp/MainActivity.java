package com.nguyenthanhdung.musicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private MusicAdapter musicAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupView();
        loadMusicList();
    }

    private void setupView() {
        progressBar = findViewById(R.id.progressBar);

        RecyclerView recyclerView = findViewById(R.id.rvListOfSong);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        musicAdapter = new MusicAdapter();
        recyclerView.setAdapter(musicAdapter);
    }

    private void loadMusicList() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

}