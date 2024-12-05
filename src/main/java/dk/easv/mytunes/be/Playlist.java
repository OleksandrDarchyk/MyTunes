package dk.easv.mytunes.be;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private int id;
    private String name;
    private int songs;
    private String totalDuration;


    public Playlist(int id, String name, int songs, String totalDuration) {
        this.name = name;
        this.id = id;
        this.songs = songs;
        this.totalDuration = this.totalDuration;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public int getSongs() {
        return songs;
    }

    public void setSongs(int songs) {
        this.songs = songs;
    }

    public String getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(String totalDuration) {
        this.totalDuration = totalDuration;
    }

    @Override
    public String toString() {
        return name;
    }
}
