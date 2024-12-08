package dk.easv.mytunes.dal.db;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.be.SongsOnPlaylist;
import dk.easv.mytunes.dal.ISongsOnPlaylistDAO;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongsOnPlaylistDAODB implements ISongsOnPlaylistDAO {
    private DBConnection con = new DBConnection();

    @Override
    public List<SongsOnPlaylist> getSongsOnPlaylist(int playlistId) throws IOException{
        List<SongsOnPlaylist> songsOnPlaylist = new ArrayList<>();
        try {
            Connection c = con.getConnection();
            String sql = "SELECT sp.id AS songsOnPlaylistId, s.title AS songTitle FROM Song s JOIN SongsOnPlaylist sp ON s.id = sp.song_id WHERE sp.playlist_id = ?;";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, playlistId);
            ResultSet rs = ps.executeQuery();
            System.out.println("SQL query executed successfully. Processing results...");
            while (rs.next()) {
                int id = rs.getInt("songsOnPlaylistId");
                String title = rs.getString("songTitle"); // Fetch the song title
                songsOnPlaylist.add(new SongsOnPlaylist(id, title));
            }
        } catch (SQLException e) {
            throw new IOException("Error fetching songs for playlist", e);
        }
        return songsOnPlaylist;
    }

    @Override
    public void addSongToPlaylist(int playlistId, int songId) throws IOException{
        try {
            Connection c = con.getConnection();
            String sql = "INSERT INTO SongsOnPlaylist (playlist_id, song_id) VALUES (?, ?)";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, playlistId);
            ps.setInt(2, songId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IOException("Error fetching songs for playlist", e);
        }
    }
}
