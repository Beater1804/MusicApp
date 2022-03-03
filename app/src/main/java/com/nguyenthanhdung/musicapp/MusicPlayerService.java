package com.nguyenthanhdung.musicapp;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
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

    public static final String SONG_POSITION_KEY = "POSITION_KEY";


    public static List<MusicModel> musicModels = new ArrayList<>();

    private MediaPlayer mediaPlayer = new MediaPlayer();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        switch (action) {
            case ACTION_START_LOAD_MUSIC: {
                loadMusicLists();
                break;
            }
            case ACTION_PLAY_MUSIC: {
                int position = intent.getIntExtra(SONG_POSITION_KEY,0);
                playMusic(position);
                break;
            }
            default: {
                break;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void loadMusicLists() {
        List<MusicModel> musicModels = Utils.loadMusicList();
       MusicPlayerService.musicModels.addAll(musicModels);

       Intent intent = new Intent(ACTION_LOAD_MUSIC_DONE);
        sendBroadcast(intent);
    }

    private void playMusic(int position){
        MusicModel musicModel = MusicPlayerService.musicModels.get(position);

        try{
            mediaPlayer.reset();
            mediaPlayer.setDataSource(musicModel.getUrl());
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
