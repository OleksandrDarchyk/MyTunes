package dk.easv.mytunes.dal.db;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.dal.IPlaylistDAO;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAODB implements IPlaylistDAO {
    private DBConnection con = new DBConnection();

    @Override
    public List<Playlist> getAllPlaylists() throws IOException{
        List<Playlist> playlists = new ArrayList<>();
        try {
            Connection c = con.getConnection();
            //System.out.println("Connected to the database.");
            //String sql = "SELECT * FROM playlist";
            String sql = "SELECT \n" +
                    "    p.id,\n" +
                    "    p.name,\n" +
                    "    COUNT(sp.song_id) AS Songs,\n" +
                    "    FORMAT(\n" +
                    "        DATEADD(SECOND, SUM(DATEDIFF(SECOND, '00:00:00', s.time)), '00:00:00'), \n" +
                    "        'HH:mm:ss'\n" +
                    "    ) AS totalDuration\n" +
                    "FROM Playlist p\n" +
                    "LEFT JOIN SongsOnPlaylist sp ON p.id = sp.playlist_id\n" +
                    "LEFT JOIN Song s ON sp.song_id = s.id\n" +
                    "GROUP BY p.id, p.name;";

            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            //System.out.println("Query executed successfully.");
            while (rs.next()){ // while there are rows
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int songs = rs.getInt("Songs");
                Time totalDuration = rs.getTime("totalDuration");
                //System.out.printf("Playlist: id=%d, name=%s, songs=%d, duration=%s%n", id, name, songs, totalDuration);
                Playlist playlist = new Playlist(id, name, songs,totalDuration);
                playlists.add(playlist);
            }
        } catch (SQLException e) {
            //System.err.println("SQL Error: " + e.getMessage());
            throw new IOException("Couldn't get all playlists from SQL database",e);
        }
        return playlists;
    }

    @Override
    public void addPlaylist(Playlist playlist) {
        String sql = "INSERT INTO playlist (name) VALUES (?)";
        try (Connection connection = con.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setString(1,playlist.getName());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    playlist.setId(rs.getInt(1));
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletePlaylist(int playlistID) {
        String deleteSongsFromPlaylist = "DELETE FROM SongsOnPlaylist WHERE playlist_id = ?";
        String deletePlaylist = "DELETE FROM playlist WHERE id = ?";

        try (Connection connection = con.getConnection()) {
            try (PreparedStatement ps1 = connection.prepareStatement(deleteSongsFromPlaylist)) {
                ps1.setInt(1, playlistID);
                ps1.executeUpdate();
            }
            try (PreparedStatement ps2 = connection.prepareStatement(deletePlaylist)) {
                ps2.setInt(1, playlistID);
                ps2.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting playlist and associated songs: " + e.getMessage(), e);
        }
    }

    @Override
    public void updatePlaylist(Playlist playlist) {
        String sql = "UPDATE playlist SET name = ? WHERE id = ?";
        try (Connection connection = con.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, playlist.getName());
            ps.setInt(2,playlist.getId());
            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
