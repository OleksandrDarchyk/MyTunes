package dk.easv.mytunes.be;

public class Song {
    private int id;
    private String title;
    private String artist;
    private String category;
    private String time;
    private String songPath;

    public Song(int id, String title, String artist, String category, String time, String songPath) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.category = category;
        this.time = time;
        this.songPath = songPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    @Override
    public String toString() {
        return id + "," + title + "," + artist + "," + category + "," + time;
    }
}
