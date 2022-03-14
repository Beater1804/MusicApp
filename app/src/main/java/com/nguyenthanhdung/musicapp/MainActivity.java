package com.nguyenthanhdung.musicapp;

import static com.nguyenthanhdung.musicapp.MusicPlayerService.ACTION_LOAD_MUSIC_DONE;
import static com.nguyenthanhdung.musicapp.MusicPlayerService.ACTION_PLAY_MUSIC;
import static com.nguyenthanhdung.musicapp.MusicPlayerService.ACTION_START_LOAD_MUSIC;
import static com.nguyenthanhdung.musicapp.MusicPlayerService.ACTION_UPDATE_STATUS;
import static com.nguyenthanhdung.musicapp.MusicPlayerService.IS_PLAYING_MUSIC_KEY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_WRITE_STORAGE = 1;
    public static final String SMALL_FRAGMENT_TAG = "SMALL_FRAGMENT_TAG";

    private MusicAdapter musicAdapter;
    private ProgressBar progressBar;

    private MusicReceiver musicReceiver = new MusicReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupView();
//        loadMusicList();
        checkPermission();
        registerReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterReceiver();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter(ACTION_LOAD_MUSIC_DONE);
        intentFilter.addAction(ACTION_UPDATE_STATUS);
        registerReceiver(musicReceiver, intentFilter);
    }

    private void unRegisterReceiver() {
        unregisterReceiver(musicReceiver);
    }

    private void setupView() {
        progressBar = findViewById(R.id.progressBar);

        RecyclerView recyclerView = findViewById(R.id.rvListOfSong);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        musicAdapter = new MusicAdapter(new MusicAdapter.Callback() {
            @Override
            public void onClickItem(int position) {
                onClickSong(position);
            }
        });
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
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction(ACTION_START_LOAD_MUSIC);
        startService(intent);
    }


    public void onClickSong(int position) {
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction(ACTION_PLAY_MUSIC);
        intent.putExtra(MusicPlayerService.SONG_POSITION_KEY, position);
        startService(intent);
    }

    class MusicReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_LOAD_MUSIC_DONE.equals(intent.getAction())) {
                progressBar.setVisibility(View.GONE);
                musicAdapter.setData(MusicPlayerService.musicModels);
            } else {
                Bundle bundle = intent.getExtras();
                boolean isPlaying = bundle.getBoolean(IS_PLAYING_MUSIC_KEY, false);

                if (isPlaying) {
                    addFragment();
                } else {
                    removeFragment();
                }
            }

        }
    }

    private void addFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(SMALL_FRAGMENT_TAG) == null) {
            MusicPlayFragment musicPlayFragment = MusicPlayFragment.newInstance(true);
            fragmentManager.beginTransaction().add(R.id.smallMusicPlay, musicPlayFragment, SMALL_FRAGMENT_TAG).commit();
        }
    }

    private void removeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(SMALL_FRAGMENT_TAG);
        if (fragment != null) {
            fragmentManager.beginTransaction().remove(fragment);
        }
    }
}