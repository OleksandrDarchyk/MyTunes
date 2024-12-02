package dk.easv.mytunes.dal.db;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.dal.ISongDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SongDAODB implements ISongDAO {
    private DBConnection con = new DBConnection();

    @Override
    public List<Song> getAll() throws IOException{
        List<Song> songs = new ArrayList<>();
        System.out.println("Attempting to fetch all songs from the database...");
        try {
            Connection c = con.getConnection();
            String sql = "SELECT * FROM songs";
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println("Query executed successfully. Processing results...");
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String artist = rs.getString("artist");
                String category = rs.getString("category");
                String time = rs.getString("time");
                String filePath = rs.getString("file_path");
                Song song = new Song(id,title,artist,category,time,filePath);
                songs.add(song);
                System.out.println("All songs fetched successfully. Total songs: " + songs.size());
            }
        } catch (SQLException e) {
            System.err.println("Error fetching songs from the database: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Error fetching songs from the database: " + e.getMessage(), e);
        }
        return songs;
    }



    /*@Override
    public Song getFilteredSong(int query) throws IOException {
        try {
            Connection c = con.getConnection();
            String sql = "SELECT * FROM songs WHERE query = ?";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt

            song.getTitle().trim().toLowerCase().contains(query.toLowerCase()) ||
                    song.getArtist().trim().toLowerCase().contains(query.toLowerCase()))
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){ // while there are rows
                int id = rs.getInt("id");
                String username = rs.getString("username");
                User user = new User(id, username);
                return user;
            }

        } catch (SQLException e) {
            throw new WorkoutException(e);
        }
        throw new WorkoutException("User not found: " + userId);
    }*/

}
