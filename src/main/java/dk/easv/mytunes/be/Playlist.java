package dk.easv.mytunes.be;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private int id;
    private String name;
    private List<Song> songs;
    private Time totalDuration;


    public Playlist(int id, String name, int songs, String totalDuration) {
        this.name = name;
        this.id = id;
        this.songs = new ArrayList<Song>();
        this.totalDuration = this.totalDuration;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    // Count the number of songs in the playlist
    public int getSongCount() {
        return songs != null ? songs.size() : 0;
    }

    // Calculate the total duration of the playlist
    /*public int getTotalDuration() {
        return songs != null ? songs.stream().mapToInt(Song::getTime).sum() : 0;
    }*/


    @Override
    public String toString() {
        return name;
    }
}
