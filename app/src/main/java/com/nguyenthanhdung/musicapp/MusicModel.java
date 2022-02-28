package com.nguyenthanhdung.musicapp;

public class MusicModel {
    private String nameOfSong;
    private String album;
    private String url;
    private String thumbnailUrl;
    private long duration;

    public MusicModel(String nameOfSong, String album, String url, String thumbnailUrl, long duration) {
        this.nameOfSong = nameOfSong;
        this.album = album;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
        this.duration = duration;
    }

    public String getNameOfSong() {
        return nameOfSong;
    }

    public String getAlbum() {
        return album;
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
}
