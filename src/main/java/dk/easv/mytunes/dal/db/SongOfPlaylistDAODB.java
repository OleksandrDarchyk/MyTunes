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
                String sql = "SELECT * FROM songOfPlaylist";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int playlistId = rs.getInt("playlist_id");
                int songId = rs.getInt("song_id");
                SongOfPlaylist songOfPlaylist = new SongOfPlaylist(playlistId, songId);
                songsOnPlaylist.add(songOfPlaylist);
            }
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return songsOnPlaylist;
    }

}
