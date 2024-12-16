package dk.easv.mytunes.bll;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.bll.util.SongFilter;
import dk.easv.mytunes.dal.ISongDAO;
import dk.easv.mytunes.dal.db.SongDAODB;

import java.io.IOException;
import java.util.List;

public class SongManager {

    private final ISongDAO songDAO = new SongDAODB();
    private final SongFilter songFilter = new SongFilter();

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

