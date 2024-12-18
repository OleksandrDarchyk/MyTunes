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

    @Override
    public void createSong(Song song) throws IOException {
        String sql = "INSERT INTO Song (title, artist, category, time, songPath) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = con.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
             ps.setString(1, song.getTitle());
             ps.setString(2, song.getArtist());
             ps.setString(3, song.getCategory());
             ps.setTime(4, song.getTime());
             ps.setString(5, song.getSongPath());
             ps.executeUpdate();
        } catch (SQLException e) {
            throw new IOException("Error adding song to the database: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteSong(int songId) throws IOException {
        String deleteFromSongsOnPlaylist = "DELETE FROM SongsOnPlaylist WHERE song_id = ?";
        String deleteFromSong = "DELETE FROM Song WHERE id = ?";
        try (Connection connection = con.getConnection();
            PreparedStatement ps1 = connection.prepareStatement(deleteFromSongsOnPlaylist);
            PreparedStatement ps2 = connection.prepareStatement(deleteFromSong)) {
            ps1.setInt(1, songId);
            ps1.executeUpdate();
            ps2.setInt(1, songId);
            ps2.executeUpdate();
        } catch (SQLException e) {
            throw new IOException("Error deleting song and its dependencies: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateSong(Song song) throws IOException {
        String sql = "UPDATE Song SET title = ?, artist = ?, category = ?, time = ?, songPath = ? WHERE id = ?";
        try (Connection connection = con.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, song.getTitle());
            ps.setString(2, song.getArtist());
            ps.setString(3, song.getCategory());
            if (song.getTime() != null) {
                ps.setTime(4, song.getTime());
            } else {
                ps.setNull(4, Types.TIME);
            }
            ps.setString(5, song.getSongPath());
            ps.setInt(6, song.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IOException("Error updating song in the database: " + e.getMessage(), e);
        }
    }
}


