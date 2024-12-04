package dk.easv.mytunes.dal.db;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.dal.ISongDAO;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongDAODB implements ISongDAO {
    private DBConnection con = new DBConnection();

    @Override
    public List<Song> getAll() throws IOException{
        List<Song> songs = new ArrayList<>();
        try {
            Connection c = con.getConnection();
            String sql = "SELECT * FROM song";
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String artist = rs.getString("artist");
                String category = rs.getString("category");
                Time time = rs.getTime("time");
                String songPath = rs.getString("songPath");
                Song song = new Song(id,title,artist,category,time,songPath);
                songs.add(song);
            }
        } catch (SQLException e) {
           e.printStackTrace();
            throw new IOException("Error fetching songs from the database: " + e.getMessage(), e);
        }
        return songs;
    }

}
