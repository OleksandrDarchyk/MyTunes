package dk.easv.mytunes.dal.db;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.be.SongOfPlaylist;
import dk.easv.mytunes.dal.ISongOfPlaylistDAO;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongOfPlaylistDAODB implements ISongOfPlaylistDAO {
    private DBConnection con = new DBConnection();

    @Override
    public List<Song> getSongsOnPlaylist(int playlistId) throws IOException{
        List<Song> songsOnPlaylist = new ArrayList<>();
        try {
            Connection c = con.getConnection();
            System.out.println("Executing SQL query for playlist ID: " + playlistId);
            String sql = "SELECT s.id, s.title FROM Song s " +
                    "JOIN SongOfPlaylist sp ON s.id = sp.song_id " +
                    "WHERE sp.playlist_id = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, playlistId);
            ResultSet rs = ps.executeQuery();
            System.out.println("SQL query executed successfully. Processing results...");
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");

                songsOnPlaylist.add(new Song(id,title, "", "", null, ""));
                System.out.println("Fetched Song: " + title);
            }

        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
            throw new IOException("Error fetching songs for playlist", e);
        }
        return songsOnPlaylist;
    }

    @Override
    public List<SongOfPlaylist> getSongOfPlaylist() throws IOException {
        return List.of();
    }
}
