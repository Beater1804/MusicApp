package com.nguyenthanhdung.musicapp;

import static com.nguyenthanhdung.musicapp.MusicPlayerService.ACTION_LOAD_MUSIC_DONE;
import static com.nguyenthanhdung.musicapp.MusicPlayerService.ACTION_PAUSE;
import static com.nguyenthanhdung.musicapp.MusicPlayerService.ACTION_PLAY;
import static com.nguyenthanhdung.musicapp.MusicPlayerService.ACTION_PLAY_NEXT_MUSIC;
import static com.nguyenthanhdung.musicapp.MusicPlayerService.ACTION_PLAY_OR_PAUSE_MUSIC;
import static com.nguyenthanhdung.musicapp.MusicPlayerService.ACTION_PLAY_PREVIOUS_MUSIC;
import static com.nguyenthanhdung.musicapp.MusicPlayerService.ACTION_UPDATE_STATUS;
import static com.nguyenthanhdung.musicapp.MusicPlayerService.IS_PLAYING_MUSIC_KEY;
import static com.nguyenthanhdung.musicapp.MusicPlayerService.PLAYING_SONG_KEY;
import static com.nguyenthanhdung.musicapp.MusicPlayerService.mediaPlayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MusicPlayFragment extends Fragment {

    private boolean isSmallMusicPlay;

    private static final String MUSIC_PLAY_TYPE_KEY = "MUSIC_PLAY_TYPE";

    private MusicReceiver musicReceiver = new MusicReceiver();

    private TextView tvNameOfSongSelected, tvNameOfArtist, tvProgressDuration, tvDuration;
    public MusicPlayFragment() {
        // Required empty public constructor
    }

    public static MusicPlayFragment newInstance(boolean isSmallMusicPlay) {
        MusicPlayFragment fragment = new MusicPlayFragment();
        Bundle args = new Bundle();
        args.putBoolean(MUSIC_PLAY_TYPE_KEY, isSmallMusicPlay);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isSmallMusicPlay = getArguments().getBoolean(MUSIC_PLAY_TYPE_KEY);
        }
        registerReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        int layout = isSmallMusicPlay ? R.layout.fragment_music_play_small : R.layout.fragment_music_play;
        return inflater.inflate(layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews(view);
    }

    private void setupViews(View view){
        tvNameOfSongSelected = view.findViewById(R.id.tvNameOfSongSelected);
        tvNameOfArtist =view.findViewById(R.id.tvNameOfArtist);

        view.findViewById(R.id.btnPrevious).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickPreviousSong();
            }
        });

        view.findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNextSong();
            }
        });

        view.findViewById(R.id.btnPlaySong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    onClickPlayOrPauseSong();
                    view.findViewById(R.id.btnPlaySong).setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                }
                else{
                    onClickPlayOrPauseSong();
                    view.findViewById(R.id.btnPlaySong).setBackgroundResource(R.drawable.ic_baseline_pause_24);
                }
            }
        });


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                onClickNextSong();
            }
        });



    }

    public void onClickPreviousSong() {
        Intent intent = new Intent(getActivity(), MusicPlayerService.class);
        intent.setAction(ACTION_PLAY_PREVIOUS_MUSIC);
        getActivity().startService(intent);
    }

    public void onClickNextSong() {
        Intent intent = new Intent(getActivity(), MusicPlayerService.class);
        intent.setAction(ACTION_PLAY_NEXT_MUSIC);
        getActivity().startService(intent);
    }

    public void onClickPlayOrPauseSong(){
        Intent intent = new Intent(getActivity(), MusicPlayerService.class);
        intent.setAction(ACTION_PLAY_OR_PAUSE_MUSIC);
        getActivity().startService(intent);
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter(ACTION_LOAD_MUSIC_DONE);
        intentFilter.addAction(ACTION_UPDATE_STATUS);
        getActivity().registerReceiver(musicReceiver, intentFilter);
    }

    private void unRegisterReceiver() {
        getActivity().unregisterReceiver(musicReceiver);
    }


    class MusicReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getExtras();
            boolean isPlaying = bundle.getBoolean(IS_PLAYING_MUSIC_KEY, false);

            if (isPlaying) {
                MusicModel musicModel = (MusicModel) bundle.get(PLAYING_SONG_KEY);
                tvNameOfSongSelected.setText(musicModel.getNameOfSong());
                tvNameOfArtist.setText(musicModel.getArtist());
            }

        }
    }

}