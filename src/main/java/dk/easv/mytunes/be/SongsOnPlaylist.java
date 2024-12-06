package dk.easv.mytunes.be;

public class SongsOnPlaylist {
    private int id;
    private int playlistId;
    private int songId;
    private String title;
    private int sequentialId;

    public SongsOnPlaylist(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    @Override
    public String toString() {
        return id + ". " + title;
    }
}
