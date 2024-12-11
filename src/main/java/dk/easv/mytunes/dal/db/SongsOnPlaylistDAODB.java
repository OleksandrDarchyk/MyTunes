package dk.easv.mytunes.dal.db;

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
            String sql = "SELECT sp.id AS songsOnPlaylistId, s.title AS songTitle, sp.song_id AS songId, sp.playlist_id AS playlistId " +
                    "FROM Song s JOIN SongsOnPlaylist sp ON s.id = sp.song_id WHERE sp.playlist_id = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, playlistId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("songsOnPlaylistId");
                String title = rs.getString("songTitle");
                int songId = rs.getInt("songId");
                int playlistIdFromDb = rs.getInt("playlistId");
                System.out.println("song id is" + songId);
                songsOnPlaylist.add(new SongsOnPlaylist(id, title, songId, playlistIdFromDb));
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

    @Override
    public void removeSongFromPlaylist(int playlistId, int songId) throws IOException {
        try {
            Connection c = con.getConnection();
            String sql = "DELETE FROM songsOnPlaylist WHERE playlist_id = ? AND song_id = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, playlistId);
            ps.setInt(2, songId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IOException("Error deleting songs for playlist", e);
        }
    }
}
