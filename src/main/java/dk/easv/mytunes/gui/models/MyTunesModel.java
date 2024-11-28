package dk.easv.mytunes.gui.models;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.bll.MyTunesManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MyTunesModel {
    private final MyTunesManager myTunesManager = new MyTunesManager();
    private final ObservableList<Song> songs = FXCollections.observableArrayList();


    // Get observable list of songs
    public ObservableList<Song> getSongs() {
        return songs;
    }




}
