package dk.easv.mytunes.bll;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.bll.util.SongFilter;
import dk.easv.mytunes.dal.IPlaylistDAO;
import dk.easv.mytunes.dal.ISongDAO;
import dk.easv.mytunes.dal.db.PlaylistDAODB;
import dk.easv.mytunes.dal.db.SongDAODB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SongManager {
    private final ISongDAO songDAO = new SongDAODB();
    private final SongFilter songFilter = new SongFilter();
    private final Set<String> categorySet = new HashSet<>(); // Local storage for categories

    // Get all songs
    public List<Song> getAllSongs() throws IOException {
        return songDAO.getAll();
    }

    // Get filtered songs based on a query
    public List<Song> filterSongs(String query) throws IOException {
        List<Song> allSongs = getAllSongs();
        List<Song> filterResult = songFilter.filter(allSongs,query);
        return filterResult;
    }

    // Create a new song
    public void createSong(Song song) throws IOException {
        songDAO.createSong(song);
    }
    public void deleteSong(int songId) throws IOException {
        songDAO.deleteSong(songId);
    }
    public void updateSong(Song song) throws IOException {
        songDAO.updateSong(song); // Передаємо оновлення в DAO
    }
}

