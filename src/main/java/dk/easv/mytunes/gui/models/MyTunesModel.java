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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    // Get observableList of filtered songs.
    public ObservableList<Song> getFilteredSongs(String query) {
        try {
            List<Song> filterResult = songManager.filterSongs(query); // Filter songs based on query
            filteredSongs.setAll(filterResult); // Update observable list with filtered songs
        } catch (IOException e) {
            System.out.println("Error filtering songs: " + e.getMessage());
        }
        return filteredSongs;
    }

    // Get ObservableList of playlist
    public ObservableList<Playlist> getAllPlaylists() {
        try {
            List<Playlist> allPlayists = playlistManager.getAllPlaylists();
            playlists.setAll(allPlayists);  // Update the ObservableList with the loaded songs
        } catch (IOException e) {
            System.out.println("Error loading playlists: " + e.getMessage());
        }
        return playlists;
    }

    // Get ObservableList of songs on playlist
    public List<SongsOnPlaylist> getSongsOnPlaylist(int playlistId) {
        try {
            List<SongsOnPlaylist> songList = songsOnPlaylistManager.getSongsOnPlaylist(playlistId);
            songsOnPlaylist.setAll(songList);
        } catch (IOException e) {
            System.out.println("Error loading songs on playlist: " + e.getMessage());
        }
        return songsOnPlaylist;
    }

    // Add a new song
    public void createSong(Song newSong) throws IOException {
        songManager.createSong(newSong);
    }

    public ObservableList<String> getCategory() throws IOException {
        Set<String> categorySet = new HashSet<>(); // Use a Set to automatically remove duplicates
        List<Song> songs = songManager.getAllSongs();
        for (Song song : songs) {
            String category = song.getCategory();
            if (category != null && !category.isEmpty()) { // Ensure the category is not null or empty
                categorySet.add(category);
            }
        }
        // Convert the Set to an ObservableList
        return FXCollections.observableArrayList(categorySet);
    }

    public void deleteSong(int songId) throws IOException {
        songManager.deleteSong(songId);
    }

    public void updateSong(Song songToEdit) throws IOException {
        songManager.updateSong(songToEdit);
    }

    public void addSongToPlaylist(int playlistId, int songId) throws IOException {
        songsOnPlaylistManager.addSongToPlaylist(playlistId, songId);
    }

    public void removeSongFromPlaylist(int playlistId, int songId) throws IOException {
        songsOnPlaylistManager.removeSongFromPlaylist(playlistId, songId);
    }

    public void deletePlaylist(int playlistId) throws IOException {
        playlistManager.deletePlaylist(playlistId);
    }
    public void createPlaylist(Playlist newPlaylist) throws IOException {
        playlistManager.addPlaylist(newPlaylist);
    }

    public void updatePlaylist(Playlist playlist) throws IOException {
        playlistManager.updatePlaylist(playlist);
    }
}





