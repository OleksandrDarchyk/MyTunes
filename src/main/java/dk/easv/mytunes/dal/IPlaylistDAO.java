package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.Playlist;

import java.util.List;

public interface IPlaylistDAO {
    List<Playlist> getAllPlaylists();
    void addPlaylist(Playlist playlist);
    void deletePlaylist(int playlistID);
}
