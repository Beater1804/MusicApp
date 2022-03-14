package com.nguyenthanhdung.musicapp;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class MusicModel implements Serializable {
    private String nameOfSong;
    private String artist;
    private String url;
    private String thumbnailUrl;
    private long duration;

    public MusicModel(String nameOfSong, String artist, String url, String thumbnailUrl, long duration) {
        this.nameOfSong = nameOfSong;
        this.artist = artist;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
        this.duration = duration;
    }

    public String getNameOfSong() {
        return nameOfSong;
    }

    public String getArtist() {
        return artist;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public long getDuration() {
        return duration;
    }

    public String getDurationFormat(int duration) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        String timeText = simpleDateFormat.format(duration);
        return timeText;
    }
}
