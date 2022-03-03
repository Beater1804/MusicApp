package com.nguyenthanhdung.musicapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MusicPlayFragment extends Fragment {

    private boolean isSmallMusicPlay;

    private static final String MUSIC_PLAY_TYPE_KEY = "MUSIC_PLAY_TYPE";

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        int layout = isSmallMusicPlay ? R.layout.fragment_music_play_small : R.layout.fragment_music_play;
        return inflater.inflate(layout, container, false);
    }
}