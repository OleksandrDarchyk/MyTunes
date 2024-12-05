package dk.easv.mytunes.dal.db;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.mytunes.be.SongOfPlaylist;
import dk.easv.mytunes.dal.ISongOfPlaylistDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SongOfPlaylistDAODB implements ISongOfPlaylistDAO {
    private DBConnection con = new DBConnection();

    @Override
    public List<SongOfPlaylist> getSongOfPlaylist() throws IOException {
        List<SongOfPlaylist> songsOnPlaylist = new ArrayList<SongOfPlaylist>();
        try {
            Connection conn = con.getConnection();
            //String sql = "SELECT * FROM songOfPlaylist";
            String sql = "SELECT s.id, s.title, s.artist, s.category, s.time " +
                    "FROM SongOfPlaylist sp " +
                    "JOIN Song s ON sp.song_id = s.id " +
                    "WHERE sp.playlist_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String artist = rs.getString("artist");
                String category = rs.getString("category");
                String time = rs.getString("time"); // Assuming time is stored as a string
                //songsOnPlaylist.add(new SongOfPlaylist(id, title, artist, category, time));
            }
        } catch (SQLServerException e) {
            throw new IOException("SQL Server Exception occurred", e);
        } catch (SQLException e) {
            throw new IOException("SQL Exception occurred", e);
        }
        return songsOnPlaylist;
    }

}
