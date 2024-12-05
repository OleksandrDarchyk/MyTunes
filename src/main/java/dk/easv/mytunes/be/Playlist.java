package dk.easv.mytunes.be;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private int id;
    private String name;
    private int songs;
    private Time totalDuration;


    public Playlist(int id, String name, int songs, Time totalDuration) {
        this.name = name;
        this.id = id;
        this.songs = songs;
        this.totalDuration = totalDuration;
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

    public Time getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Time totalDuration) {
        this.totalDuration = totalDuration;
    }

    @Override
    public String toString() {
        return name;
    }
}
