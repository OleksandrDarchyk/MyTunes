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

public class SongDAO {
    private final String splitChar = ";";
    private final Path filePath;
    public SongDAO()
    {filePath = Paths.get("songs.csv");}

    // Load songs from csv file
    public List<Song> getAll() throws IOException {
        List<Song> songs = new ArrayList<>();
        if (Files.exists(filePath)) {
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                String[] parts = line.split(splitChar);
                if (parts.length == 5) {
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        String title = parts[1].trim();
                        String artist = parts[2].trim();
                        String category = parts[3].trim();
                        String time = parts[4].trim();
                        songs.add(new Song(id, title, artist, category, time));
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

    // Save (overwrite) the entire user list to the CSV file
    public void clearAndSave(List<Song> songs) throws IOException {
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
    }

    // Get the next available user ID
    private int getNextId() throws IOException {
        List<Song> songs = getAll();
        int maxId = 0;
        for (Song song : songs) {
            if (song.getId() > maxId) {
                maxId = song.getId();
            }
        }
        return maxId + 1;
    }


}



