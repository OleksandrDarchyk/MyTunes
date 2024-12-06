package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.be.SongOfPlaylist;

import java.io.IOException;
import java.util.List;

public interface ISongOfPlaylistDAO {
    List<Song> getSongsOnPlaylist(int playlistId) throws IOException;

    List<SongOfPlaylist> getSongOfPlaylist() throws IOException;

}
