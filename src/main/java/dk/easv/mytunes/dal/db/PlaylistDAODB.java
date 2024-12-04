package dk.easv.mytunes.dal.db;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
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
            String sql = "SELECT * FROM playlist";
            /*String sql = "SELECT p.id, p.name, COUNT(sp.song_id) AS Songs, " +
                    "SEC_TO_TIME(SUM(TIME_TO_SEC(s.duration))) AS totalDuration " +
                    "FROM Playlist p " +
                    "LEFT JOIN SongOfPlaylist sp ON p.id = sp.playlist_id " +
                    "LEFT JOIN Song s ON sp.song_id = s.id " +
                    "GROUP BY p.id";*/
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){ // while there are rows
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int songs = rs.getInt("Songs");
                String totalDuration = rs.getString("totalDuration");

                Playlist playlist = new Playlist(id, name, songs,totalDuration);
                playlists.add(playlist);
            }
        } catch (SQLException e) {
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

    @Override
    public List<Song> getSongInPlaylist(int playlistID) {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT * FROM playlist";

        /*String sql = "SELECT s.* FROM Songs s " +
                "JOIN SongsOfPlaylist sop ON s.SongID = sop.song_id " +
                "WHERE sop.playlist_id = ?";*/
        try (Connection connection = con.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, playlistID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                songs.add(new Song(
                        rs.getInt("SongID"),
                        rs.getString("Title"),
                        rs.getString("Artist"),
                        rs.getString("Category"),
                        rs.getTime("Time"),
                        rs.getString("FilePath")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching songs from playlist: " + e.getMessage(), e);
        }
        return songs;
    }

    @Override
    public void addSongToPlaylist(int playlistID, int songID) {
        String sql = "INSERT INTO SongsOfPlaylist (playlist_id, song_id) VALUES (?, ?)";
        try (Connection connection = con.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, playlistID);
            ps.setInt(2, songID);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding song to playlist: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteSongFromPlaylist(int playlistID, int songID) {
        String sql = "DELETE FROM SongsOfPlaylist WHERE playlist_id = ? AND song_id = ?";
        try (Connection connection = con.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, playlistID);
            ps.setInt(2, songID);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error removing song from playlist: " + e.getMessage(), e);
        }
    }
}
