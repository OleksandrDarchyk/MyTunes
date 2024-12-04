package dk.easv.mytunes.bll;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.bll.util.SongFilter;
import dk.easv.mytunes.dal.IPlaylistDAO;
import dk.easv.mytunes.dal.ISongDAO;
import dk.easv.mytunes.dal.db.PlaylistDAODB;
import dk.easv.mytunes.dal.db.SongDAODB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyTunesManager {
    private final ISongDAO songDAO = new SongDAODB();
    private SongFilter songFilter = new SongFilter();
    private final IPlaylistDAO playlistDAO = new PlaylistDAODB();

    // Get all songs
    public List<Song> getAllSongs() throws IOException {
        return songDAO.getAll();
    }

    // Get filtered songs based on a query
    public List<Song> filterSongs(String query) throws IOException {
        List<Song> allSongs = getAllSongs();
        System.out.println("Total songs before filtering: " + allSongs.size());

        List<Song> filterResult = songFilter.filter(allSongs,query);
        return filterResult;
    }

    public List<Playlist> getAllPlaylists() throws IOException {
        return playlistDAO.getAllPlaylists();
}

}
