package dk.easv.mytunes.bll;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.dal.IPlaylistDAO;
import dk.easv.mytunes.dal.db.PlaylistDAODB;

import java.io.IOException;
import java.util.List;

public class PlaylistManager {

    private final IPlaylistDAO playlistDAO = new PlaylistDAODB();

    public List<Playlist> getAllPlaylists() throws IOException {
        return playlistDAO.getAllPlaylists();
    }

    public void deletePlaylist(int playlistId) {
        playlistDAO.deletePlaylist(playlistId);
    }

    public void addPlaylist(Playlist playlist) throws IOException {
        playlistDAO.addPlaylist(playlist);
    }

    public void updatePlaylist(Playlist playlist) {
        playlistDAO.updatePlaylist(playlist);
    }
}
