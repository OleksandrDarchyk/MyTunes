package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.Song;

import java.io.IOException;
import java.util.List;

public interface ISongDAO {
    List<Song> getAll() throws IOException;

    void createSong(Song song) throws IOException;



    void deleteSong(int song) throws IOException;

    void updateSong(Song song) throws IOException;
}
