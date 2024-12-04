package dk.easv.mytunes.bll;

import dk.easv.mytunes.be.SongOfPlaylist;
import dk.easv.mytunes.dal.ISongDAO;
import dk.easv.mytunes.dal.ISongOfPlaylistDAO;
import dk.easv.mytunes.dal.db.SongOfPlaylistDAODB;

import java.io.IOException;
import java.util.List;

public class SongOfPlaylistManager {
    private final ISongOfPlaylistDAO songOfPlaylistDAO = new SongOfPlaylistDAODB();

    public List<SongOfPlaylist> getSongOfPlaylist() throws IOException{
        return songOfPlaylistDAO.getSongOfPlaylist();
    }
}
