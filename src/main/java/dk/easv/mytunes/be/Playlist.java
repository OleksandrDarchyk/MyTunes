package dk.easv.mytunes.be;

import java.util.List;

public class Playlist {
    private  int id;
    private String name;
    private List<Song> songs;
    private int totalDuration;


    public Playlist(int id, String name, List<Song> songs, int totalDuration) {
        this.name = name;
        this.totalDuration = totalDuration;
        this.songs = songs;
        this.id = id;
    }
    public Playlist(int id ,String name) {
        this.name = name;
        this.id = id;

    }
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public List<Song> getSongs() {return songs;}
    public int getTotalDuration() {return totalDuration;}

    public void setTotalDuration(int totalDuration) {this.totalDuration = totalDuration;}
    public int totalDuration() {return totalDuration;}
}
