package dk.easv.mytunes.bll;

import dk.easv.mytunes.be.SongsOnPlaylist;
import dk.easv.mytunes.dal.ISongsOnPlaylistDAO;
import dk.easv.mytunes.dal.db.SongsOnPlaylistDAODB;

import java.io.IOException;
import java.util.List;

public class SongsOnPlaylistManager {
    private final ISongsOnPlaylistDAO songsOnPlaylistDAO = new SongsOnPlaylistDAODB();

    public List<SongsOnPlaylist> getSongsOnPlaylist(int playlistId) throws IOException{
        return songsOnPlaylistDAO.getSongsOnPlaylist(playlistId);
    }

    // Add a single song to a playlist
    public void addSongToPlaylist(int playlistId, int songId) throws IOException {
        songsOnPlaylistDAO.addSongToPlaylist(playlistId, songId);
    }

    // Delete a single song from a playlist
    public void removeSongFromPlaylist(int playlistId, int songId) throws IOException {
        songsOnPlaylistDAO.removeSongFromPlaylist(playlistId,songId);
    }
}
