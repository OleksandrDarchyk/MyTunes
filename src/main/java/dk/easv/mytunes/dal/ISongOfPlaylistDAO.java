package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.SongOfPlaylist;

import java.io.IOException;
import java.util.List;

public interface ISongOfPlaylistDAO {
    List<SongOfPlaylist> getSongOfPlaylist() throws IOException;

}
