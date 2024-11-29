package dk.easv.mytunes.be;

public class Song {

    private String title;
    private String artist;
    private String category;
    private String time;
    private String songPath;

    public Song(String title, String artist, String category, String time, String songPath) {

        this.title = title;
        this.artist = artist;
        this.category = category;
        this.time = time;
        this.songPath = songPath;
        System.out.println("Song created: " + title + " at path: " + songPath);
    }

    public String getFilePath() {
        return songPath;
    }

    public void setFilePath(String songPath) {
        this.songPath = songPath;
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

    @Override
    public String toString() {
        return title + "," + artist + "," + category + "," + time;
    }
}
