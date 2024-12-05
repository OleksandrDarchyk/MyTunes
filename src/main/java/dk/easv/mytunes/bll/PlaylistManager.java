package dk.easv.mytunes.bll;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.bll.util.SongFilter;
import dk.easv.mytunes.dal.IPlaylistDAO;
import dk.easv.mytunes.dal.ISongDAO;
import dk.easv.mytunes.dal.db.PlaylistDAODB;
import dk.easv.mytunes.dal.db.SongDAODB;

import java.io.IOException;
import java.util.List;

public class PlaylistManager {
    private final IPlaylistDAO playlistDAO = new PlaylistDAODB();

    public List<Playlist> getAllPlaylists() throws IOException {
        return playlistDAO.getAllPlaylists();
    }
}
