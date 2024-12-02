package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.Song;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class SongDAO implements ISongDAO{
    private final String splitChar = ";";
    private final Path filePath;
    private List<Song> songs;
    public SongDAO() {
        filePath = Paths.get("songs.csv");

    }

    // Load songs from csv file
    public List<Song> getAll() throws IOException {
        List<Song> songs = new ArrayList<>();
        if (Files.exists(filePath)) {
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                String[] parts = line.split(splitChar);
                if (parts.length == 6) {
                    try {
                        int id = Integer.parseInt(parts[0]);
                        String title = parts[1].trim();
                        String artist = parts[2].trim();
                        String category = parts[3].trim();
                        String time = parts[4].trim();
                        String songPath = parts[5].trim();
                        songs.add(new Song(id, title, artist, category, time, songPath));
                    } catch (NumberFormatException e){
                        // Log the error instead of printing it
                        Logger.getLogger(SongDAO.class.getName()).log(Level.WARNING,"Invalid title: " + parts[0],e);
                    }
                } else {
                    Logger.getLogger(SongDAO.class.getName()).log(Level.WARNING,"Invalid line format: " + line);
                }
            }
        }
        return songs;
    }

    // Get filtered songs based on the query
    /*public List<Song> filteredSongs(String query) throws IOException {
        List<Song> matchingSongs = new ArrayList<>();  // To store songs that match the query
        List<Song> all = getAll();  // Get all songs
        for (Song song : all) {
            // Check if the title or artist contains the query (case-insensitive)
            if (song.getTitle().trim().toLowerCase().contains(query.toLowerCase()) ||
                    song.getArtist().trim().toLowerCase().contains(query.toLowerCase())) {
                matchingSongs.add(song);  // Add matching song to the list
            }
        }
        return matchingSongs;  // Return the list of matching songs
    }*/





}



