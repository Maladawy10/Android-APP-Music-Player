package com.example.myaudioplayer;

public class MusicFiles {
    private String path;
    private String title;
    private String duration;
    private String artist;
    private String album;
    private String id;
    public MusicFiles(String path, String title, String duration, String artist, String album,String id ) {
        this.path = path;
        this.title = title;
        this.duration = duration;
        this.artist = artist;
        this.album = album;
        this.id = id;
    }

    public MusicFiles() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getID()
    {
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }
}
