package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.be.SongsOnPlaylist;

import java.io.IOException;
import java.util.List;

public interface ISongsOnPlaylistDAO {
    //List<Song> getSongsOnPlaylist(int playlistId) throws IOException;

    List<SongsOnPlaylist> getSongsOnPlaylist(int playlistId) throws IOException;

}
