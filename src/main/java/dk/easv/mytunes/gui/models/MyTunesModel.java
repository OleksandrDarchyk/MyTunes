package dk.easv.mytunes.gui.models;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.bll.MyTunesManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyTunesModel {
    private final MyTunesManager myTunesManager = new MyTunesManager();
    private final ObservableList<Song> songs = FXCollections.observableArrayList();
    private final ObservableList<Song> filteredSongs = FXCollections.observableArrayList();
    private final ObservableList<Playlist> playlists = FXCollections.observableArrayList();

    // Get observable list of songs
    public ObservableList<Song> getAllSongs() {
        try {
            List<Song> songList = myTunesManager.getAllSongs();
            songs.setAll(songList);  // Update the ObservableList with the loaded songs
        } catch (IOException e) {
            System.out.println("Error loading songs: " + e.getMessage());
        }
        return songs;
    }

    public ObservableList<Song> getFilteredSongs(String query) {
        try {
            List<Song> filterResult = myTunesManager.filterSongs(query); // Filter songs based on query
            System.out.println("Songs after filtering in model: " + filterResult); // Log the filtered results
            filteredSongs.setAll(filterResult); // Update observable list with filtered songs
        } catch (IOException e) {
            System.out.println("Error filtering songs: " + e.getMessage());
        }
        return filteredSongs;
    }

    public ObservableList<Playlist> getAllPlaylists() {
        try {
            List<Playlist> allPlayists = myTunesManager.getAllPlaylists();
            playlists.setAll(allPlayists);  // Update the ObservableList with the loaded songs
        } catch (IOException e) {
            System.out.println("Error loading playlists: " + e.getMessage());
        }
        return playlists;
    }
}





