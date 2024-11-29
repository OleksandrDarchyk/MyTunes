package dk.easv.mytunes.bll;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.dal.ISongDAO;
import dk.easv.mytunes.dal.SongDAO;
import dk.easv.mytunes.dal.db.SongDAODB;

import java.io.IOException;
import java.util.List;

public class MyTunesManager {
    private final SongDAO songDAO = new SongDAO();

    // Get all songs
    public List<Song> getSongs() throws IOException {

        System.out.println("Songs retrieved from DAO Manager");
        return songDAO.getAll();

    }

}
