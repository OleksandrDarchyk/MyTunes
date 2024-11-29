package dk.easv.mytunes.gui.models;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.bll.MyTunesManager;
import dk.easv.mytunes.gui.controllers.MyTunesController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;

public class MyTunesModel {
    private final MyTunesManager myTunesManager = new MyTunesManager();
    private final ObservableList<Song> songs = FXCollections.observableArrayList();

    // Get observable list of songs
    public ObservableList<Song> getSongs() {
        try {
            List<Song> songList = myTunesManager.getSongs();
            System.out.println("Loaded songs: " + songList.size());
            songs.setAll(songList);  // Update the ObservableList with the loaded songs
        } catch (IOException e) {
            System.out.println("Error loading songs: " + e.getMessage());
        }
        return songs;
    }




}
