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

public class SongDAO implements ISongDAO{
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
    public List<Song> filteredSongs(String query) throws IOException {
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
    }


    // Get the next available user ID
    /*private int getNextId() throws IOException {
        List<Song> songs = getAll();
        int maxId = 0;
        for (Song song : songs) {
            if (song.getId() > maxId) {
                maxId = song.getId();
            }
        }
        return maxId + 1;
    }*/

    /*public List<Song> filteredSongs(String query) {
        return songs.stream()
                .filter(song -> song.getTitle().toLowerCase().contains(query) ||
                        song.getArtist().toLowerCase().contains(query))
                .collect(Collectors.toList());
    }*/



    // Save (overwrite) the entire user list to the CSV file
    /*public void clearAndSave(List<Song> songs) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Song song : songs) {
            lines.add(song.getId() + splitChar + song.getTitle() + splitChar + song.getArtist() + splitChar +
                    song.getCategory() + splitChar + song.getTime());
        }
        try {
            Files.write(filePath, lines); // Overwrites the file
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/




}



