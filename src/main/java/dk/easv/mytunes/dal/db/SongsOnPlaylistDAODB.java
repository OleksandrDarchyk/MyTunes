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
            System.out.println("Executing SQL query for playlist ID: " + playlistId);
/*            String sql = "SELECT s.id, s.title FROM Song s " +
                    "JOIN SongOfPlaylist sp ON s.id = sp.song_id " +
                    "WHERE sp.playlist_id = ?";*/
            String sql = "SELECT sp.id AS songsOnPlaylistId, s.title AS songTitle FROM Song s JOIN SongsOnPlaylist sp ON s.id = sp.song_id WHERE sp.playlist_id = ?;";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, playlistId);
            ResultSet rs = ps.executeQuery();
            System.out.println("SQL query executed successfully. Processing results...");
            while (rs.next()) {
                int id = rs.getInt("songsOnPlaylistId");
                String title = rs.getString("songTitle"); // Fetch the song title
                songsOnPlaylist.add(new SongsOnPlaylist(id,title));
                System.out.println("Fetched Song: ");
            }

        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
            throw new IOException("Error fetching songs for playlist", e);
        }
        return songsOnPlaylist;
    }


}
