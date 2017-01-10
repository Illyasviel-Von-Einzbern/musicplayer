package com.example.mypc.musicplay;

import android.database.Cursor;
import android.os.StrictMode;
import android.provider.MediaStore;

import java.io.Serializable;

/**
 * Created by My PC on 2017-01-05.
 */
public class DataSearch implements Serializable{
    private int albumid;
    private String albumPath;
    private String title;
    private String artist;
    private String path;
    private String duration;

    public void DataSearch(int albumid, String title, String artist, String path, String duration){
        this.albumid = albumid;
        this.title = title;
        this.artist = artist;
        this.path = path;
        this.duration = duration;
    }

    public void setAlbumId(int albumid){
        this.albumid = albumid;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setArtist(String artist){
        this.artist = artist;
    }

    public void setPath(String path){
        this.path = path;
    }

    public void setDuration(String duration){
        this.duration = duration;
    }

    public int getAlbumid(){
        return albumid;
    }

    public String getTitle(){
        return title;
    }

    public String getArtist(){
        return artist;
    }

    public String getPath(){
        return path;
    }

    public String getDuration(){
        return duration;
    }

    public String getAlbumPath() {
        return albumPath;
    }

    public void setAlbumPath(String albumPath) {
        this.albumPath = albumPath;
    }
}
