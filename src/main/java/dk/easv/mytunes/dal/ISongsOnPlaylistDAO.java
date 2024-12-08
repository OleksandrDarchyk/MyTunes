package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.be.SongsOnPlaylist;

import java.io.IOException;
import java.util.List;

public interface ISongsOnPlaylistDAO {
    List<SongsOnPlaylist> getSongsOnPlaylist(int playlistId) throws IOException;
    void addSongToPlaylist(int playlistId, int songId) throws IOException;
}
