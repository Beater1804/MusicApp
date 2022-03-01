package com.nguyenthanhdung.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_WRITE_STORAGE = 1;

    private MusicAdapter musicAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupView();
//        loadMusicList();
        checkPermission();
    }

    private void setupView() {
        progressBar = findViewById(R.id.progressBar);

        RecyclerView recyclerView = findViewById(R.id.rvListOfSong);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        musicAdapter = new MusicAdapter();
        recyclerView.setAdapter(musicAdapter);
    }

    private boolean hasStoragePermission() {
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    private void checkPermission() {
        boolean hasPermission = hasStoragePermission();
        if (hasPermission) {
            loadMusicList();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_WRITE_STORAGE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadMusicList();

        }
    }


    public void loadMusicList() {
        progressBar.setVisibility(View.VISIBLE);
        Uri allOfSongUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        List<MusicModel> data = new ArrayList<>();

        Cursor cursor = getContentResolver().query(allOfSongUri, null, null, null, selection);

        if (cursor.moveToFirst()) {
            do {
                String nameOfSong = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
//                int songId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));

                String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                String duration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

                MusicModel musicModel = new MusicModel(nameOfSong, null, url, null, Long.valueOf(duration));
                data.add(musicModel);
            } while (cursor.moveToNext());

        }
        cursor.close();
        progressBar.setVisibility(View.GONE);
        musicAdapter.setData(data);
    }

}