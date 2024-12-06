package dk.easv.mytunes.gui.models;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.be.SongsOnPlaylist;
import dk.easv.mytunes.bll.PlaylistManager;
import dk.easv.mytunes.bll.SongManager;
import dk.easv.mytunes.bll.SongsOnPlaylistManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;

public class MyTunesModel {

    private final SongManager songManager = new SongManager();
    private final PlaylistManager playlistManager = new PlaylistManager();
    private final SongsOnPlaylistManager songsOnPlaylistManager = new SongsOnPlaylistManager();
    private final ObservableList<Song> songs = FXCollections.observableArrayList();
    private final ObservableList<Song> filteredSongs = FXCollections.observableArrayList();
    private final ObservableList<Playlist> playlists = FXCollections.observableArrayList();
    private final ObservableList<SongsOnPlaylist> songsOnPlaylist = FXCollections.observableArrayList();

    // Get observable list of songs
    public ObservableList<Song> getAllSongs() {
        try {
            List<Song> songList = songManager.getAllSongs();
            songs.setAll(songList);  // Update the ObservableList with the loaded songs
        } catch (IOException e) {
            System.out.println("Error loading songs: " + e.getMessage());
        }
        return songs;
    }

    public ObservableList<Song> getFilteredSongs(String query) {
        try {
            List<Song> filterResult = songManager.filterSongs(query); // Filter songs based on query
            filteredSongs.setAll(filterResult); // Update observable list with filtered songs
        } catch (IOException e) {
            System.out.println("Error filtering songs: " + e.getMessage());
        }
        return filteredSongs;
    }

    public ObservableList<Playlist> getAllPlaylists() {
        try {
            List<Playlist> allPlayists = playlistManager.getAllPlaylists();
            playlists.setAll(allPlayists);  // Update the ObservableList with the loaded songs
        } catch (IOException e) {
            System.out.println("Error loading playlists: " + e.getMessage());
        }
        return playlists;
    }

    public ObservableList<SongsOnPlaylist> getSongsOnPlaylist(int playlistId) {
        try {
            List<SongsOnPlaylist> songList = songsOnPlaylistManager.getSongsOnPlaylist(playlistId);
            System.out.println("Songs fetched from SongOfPlaylistManager:");
            songsOnPlaylist.setAll(songList);
        } catch (IOException e) {
            System.out.println("Error loading songs on playlist: " + e.getMessage());
        }
        return songsOnPlaylist;
    }
}





