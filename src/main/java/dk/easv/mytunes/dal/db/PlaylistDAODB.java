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
    public List<Playlist> getAllPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        String sql = "SELECT p.PlaylistID AS playlist_id, p.name AS playlist_name, " +
                "ps.song_id, s.Artist, s.Category, s.FilePath, s.Time " +
                "FROM dbo.Playlists p " +
                "LEFT JOIN SongsOfPlaylist ps ON p.PlaylistID = ps.playlist_id " +
                "JOIN Songs s ON ps.song_id = s.SongID " +
                "ORDER BY p.PlaylistID;";
        try (Connection connection = con.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)){
            while (rs.next()){
                Playlist playlist = new Playlist(
                        rs.getInt("id"),
                        rs.getString("name")
                );
                playlists.add(playlist);
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
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
        String sql = "DELETE FROM playlists WHERE id = ?";
        try (Connection connection = con.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,playlistID);
            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
