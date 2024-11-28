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

public class SongDAO implements ISongDAO {
    private final String splitChar = ";";
    private final Path filePath;
    public SongDAO() {filePath = Paths.get("songs.csv");}

    // Load songs from csv file
    @Override
    public List<Song> getAll() throws IOException {
        List<Song> songs = new ArrayList<>();
        if (Files.exists(filePath)) {
            List<String> lines = null;
            try {
                lines = Files.readAllLines(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            for (String line : lines)
            {
                String[] parts = line.split(splitChar);
                if (parts.length == 4){
                    try{
                        String title = parts[0].trim();
                        String artist = parts[1].trim();
                        String category = parts[2].trim();
                        String time = parts[3].trim();
                        songs.add(new Song(title, artist, category, time));
                        System.out.println("Getting all songs");
                    } catch (NumberFormatException e){
                        // Log the error instead of printing it
                        Logger.getLogger(SongDAO.class.getName()).log(Level.WARNING,"Invalid user ID format: " + parts[0],e);
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
            lines.add(song.getTitle() + splitChar + song.getArtist() + splitChar + song.getCategory() + splitChar + song.getTime());
        }
        Files.write(filePath, lines); // Overwrites the file
    }


}



