package com.nguyenthanhdung.musicapp;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MusicPlayerService extends Service {
    public static final String MUSIC_URL_KEY = "MUSIC_URL_KEY";
    public static final String ACTION_LOAD_MUSIC_DONE = "com.nguyenthanhdung.musicapp.load_music_done";
    public static final String ACTION_START_LOAD_MUSIC = "com.nguyenthanhdung.musicapp.start_load_music";
    public static final String ACTION_PLAY_MUSIC = "com.nguyenthanhdung.musicapp.play_music";
    public static final String ACTION_PLAY_PREVIOUS_MUSIC = "com.nguyenthanhdung.musicapp.play_previous_music";
    public static final String ACTION_PLAY_NEXT_MUSIC = "com.nguyenthanhdung.musicapp.play_next_music";
    public static final String ACTION_PLAY_OR_PAUSE_MUSIC = "com.nguyenthanhdung.musicapp.play_or_pause_music";

    public static final String ACTION_PLAY = "com.nguyenthanhdung.musicapp.play";
    public static final String ACTION_PAUSE = "com.nguyenthanhdung.musicapp.pause";

    public static final String ACTION_UPDATE_STATUS = "com.nguyenthanhdung.musicapp.update_status";

    public static final String SONG_POSITION_KEY = "POSITION_KEY";
    public static final String IS_PLAYING_MUSIC_KEY = "PLAYING_MUSIC_KEY";
    public static final String PLAYING_SONG_KEY = "PLAYING_SONG_KEY";


    public static List<MusicModel> musicModels = new ArrayList<>();

    public static MediaPlayer mediaPlayer = new MediaPlayer();
    private int interval = 100;
    private Handler handler;

    private int currentPositionSong = -1;


    Runnable statusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                updateStatus();
            } finally {
                handler.postDelayed(statusChecker, interval);
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();

        startRepeatingTask();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    void startRepeatingTask() {
        statusChecker.run();
    }

    void stopRepeatingTask() {
        handler.removeCallbacks(statusChecker);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        switch (action) {
            case ACTION_START_LOAD_MUSIC: {
                if(mediaPlayer != null){
                    loadMusicLists();
                }
                break;
            }
            case ACTION_PLAY_MUSIC: {
                int position = intent.getIntExtra(SONG_POSITION_KEY, 0);
                playMusic(position);
                break;
            }
            case ACTION_PLAY_PREVIOUS_MUSIC: {
                playPreviousMusic();
                break;
            }
            case ACTION_PLAY_OR_PAUSE_MUSIC: {
                playOrPauseMusic();
                break;
            }
            case ACTION_PLAY_NEXT_MUSIC: {
                playNextMusic();
                break;
            }
            default: {
                break;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void loadMusicLists() {
        List<MusicModel> musicModels = Utils.loadMusicList(this);
        MusicPlayerService.musicModels.addAll(musicModels);

        Intent intent = new Intent(ACTION_LOAD_MUSIC_DONE);
        sendBroadcast(intent);
    }

    private void playMusic(int position) {
        MusicModel musicModel = MusicPlayerService.musicModels.get(position);

        currentPositionSong = position%musicModels.size();
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(musicModel.getUrl());
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playPreviousMusic() {
        if(currentPositionSong == 0){
            currentPositionSong = musicModels.size() - 1;
            playMusic(currentPositionSong );
        }
        else{
            playMusic(currentPositionSong - 1);
        }

    }

    private void playOrPauseMusic() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                setActionPlayOrPauseMusic(ACTION_PAUSE);
            } else {
                mediaPlayer.start();
                setActionPlayOrPauseMusic(ACTION_PLAY);
            }
        }
    }

    private void playNextMusic() {
        if(currentPositionSong == musicModels.size() -1){
            currentPositionSong = 0;
            playMusic(currentPositionSong);
        }
        else{
            playMusic(currentPositionSong + 1);

        }
    }

    private void setActionPlayOrPauseMusic(String action){
        Intent intent = new Intent(action);
        sendBroadcast(intent);
    }
    private void updateStatus() {
        if(currentPositionSong == -1){
            return;
        }

        Intent intent = new Intent(ACTION_UPDATE_STATUS);
        Bundle bundle = new Bundle();

        bundle.putBoolean(IS_PLAYING_MUSIC_KEY, mediaPlayer.isPlaying());
        bundle.putSerializable(PLAYING_SONG_KEY, MusicPlayerService.musicModels.get(currentPositionSong));
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }


}
