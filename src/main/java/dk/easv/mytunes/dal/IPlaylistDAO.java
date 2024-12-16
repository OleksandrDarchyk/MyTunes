package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;

import java.io.IOException;
import java.util.List;

public interface IPlaylistDAO {
    List<Playlist> getAllPlaylists() throws IOException;
    void addPlaylist(Playlist playlist);
    void deletePlaylist(int playlistID);
    void updatePlaylist(Playlist playlist);
}
