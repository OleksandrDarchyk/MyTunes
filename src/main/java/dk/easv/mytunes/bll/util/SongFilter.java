package dk.easv.mytunes.bll.util;

import dk.easv.mytunes.be.Song;

import java.util.ArrayList;
import java.util.List;

public class SongFilter {

    public List<Song> filter(List<Song> searchBase, String query){
        List<Song> filteredResult = new ArrayList<Song>();
        for(Song song : searchBase){
            if(compareToSongTitle(query, song) || compareToSongArtist(query,song))
            {
                filteredResult.add(song);
            } else {
                System.out.println("No match for song: " + song); // Print non-matching songs
            }
        }
        return filteredResult;
    }

    private boolean compareToSongTitle(String query, Song song){
        return song.getTitle().toLowerCase().contains(query.toLowerCase());
    }
    private boolean compareToSongArtist(String query, Song song){
        return song.getArtist().toLowerCase().contains(query.toLowerCase());
    }
}
