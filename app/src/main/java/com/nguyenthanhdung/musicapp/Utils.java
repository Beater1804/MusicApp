package com.nguyenthanhdung.musicapp;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

final public class Utils {

    public static List<MusicModel> loadMusicList() {

//        Uri allOfSongUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
//
        List<MusicModel> data = new ArrayList<>();
//
//        Cursor cursor = getContentResolver().query(allOfSongUri, null, null, null, selection);
//
//        if (cursor.moveToFirst()) {
//            do {
//                String nameOfSong = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
////                int songId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
//
//                String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
//                String duration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
//
//                MusicModel musicModel = new MusicModel(nameOfSong, null, url, null, Long.valueOf(duration));
//                data.add(musicModel);
//            } while (cursor.moveToNext());
//
//        }
//        cursor.close();

        ArrayList<HashMap<String, String>> songList = getSongInFolder(Environment.getExternalStorageDirectory().getAbsolutePath());
        if (songList != null) {
            for (int i = 0; i < songList.size(); i++) {
                String fileName = songList.get(i).get("file_name");
                String filePath = songList.get(i).get("file_path");
                MusicModel musicModel = new MusicModel(fileName, null, filePath, null, 0);
                data.add(musicModel);
            }
        }
        return data;
    }

    private static ArrayList<HashMap<String, String>> getSongInFolder(String rootPath) {
        ArrayList<HashMap<String, String>> fileList = new ArrayList<>();


        try {
            File rootFolder = new File(rootPath);
            File[] files = rootFolder.listFiles(); //here you will get NPE if directory doesn't contains  any file,handle it like this.
            for (File file : files) {
                if (file.isDirectory()) {
                    if (getSongInFolder(file.getAbsolutePath()) != null) {
                        fileList.addAll(getSongInFolder(file.getAbsolutePath()));
                    } else {
                        break;
                    }
                } else if (file.getName().endsWith(".mp3")) {
                    HashMap<String, String> song = new HashMap<>();
                    song.put("file_path", file.getAbsolutePath());
                    song.put("file_name", file.getName());
                    fileList.add(song);
                }
            }
            return fileList;
        } catch (Exception e) {
            return null;
        }
    }
}
