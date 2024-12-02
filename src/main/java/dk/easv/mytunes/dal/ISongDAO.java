package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.Song;

import java.io.IOException;
import java.util.List;

public interface ISongDAO {
    List<Song> getAll() throws IOException;
   //void filteredSongs(Song song) throws IOException;

}
