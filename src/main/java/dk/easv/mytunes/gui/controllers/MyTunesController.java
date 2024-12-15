package dk.easv.mytunes.gui.controllers;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.be.SongsOnPlaylist;
import dk.easv.mytunes.gui.models.MyTunesModel;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MyTunesController implements Initializable {
    @FXML
    private Button btnClose;
    @FXML
    private Button btnMute;
    @FXML
    private ListView lstSongOnPlaylist;
    @FXML
    private TableView <Playlist> lstPlaylist;
    @FXML
    private TableColumn nameColumn;
    @FXML
    private TableColumn songsColumn;
    @FXML
    private TableColumn durationColumn;
    @FXML
    private Label labelCurrentSong;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnFilter;
    @FXML
    private TextField txtQuery;
    @FXML
    private Label lblFilter;
    @FXML
    private TableColumn artistColumn;
    @FXML
    private TableColumn categoryColumn;
    @FXML
    private TableColumn timeColumn;
    @FXML
    private TableColumn titleColumn;
    @FXML
    private TableView <Song> lstSongs;
    @FXML
    private Button btnEditSong;
    @FXML
    private Button btnAddSong;
    @FXML
    private Button btnEditPlaylist;
    @FXML
    private Button btnAddPlaylist;
    @FXML
    private Slider volumeSlider;
    private boolean playingFromPlaylist = false;
    private boolean playingFromSongs = false;
    private List<Song> currentSongList;
    private int currentSongIndex = -1;


    private final MyTunesModel myTunesModel = new MyTunesModel();
    private MediaPlayer mediaPlayer;

    public void displayCurrentlyPlayingSong(Song song) {
        labelCurrentSong.setText(song.getTitle() + " - " + song.getArtist());
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        Media media = new Media(new File("music/1.mp3").toURI().toString()); // file for volume slider
        mediaPlayer = new MediaPlayer(media);
        setVolumeSlider();
        btnClear.setDisable(true);
        initializeSongTable();
        initializePlaylistTable();
        setSongsOnPlaylistTableId();

        try {
            handleSongSelection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            handlePlaylistSelection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        lstSongs.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                lstSongOnPlaylist.getSelectionModel().clearSelection();
            }
        });

        lstSongOnPlaylist.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                lstSongs.getSelectionModel().clearSelection();
            }
        });
    }

    public void setVolumeSlider(){
    volumeSlider.setValue(mediaPlayer.getVolume() * 100); // volume slider
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
        @Override
        public void invalidated(Observable observable) {
            mediaPlayer.setVolume(volumeSlider.getValue() / 100 );
        }
    });
    }

    public void initializeSongTable() {
        lstSongs.getItems().clear();
        lstSongs.setItems(myTunesModel.getAllSongs());
        // set the tableview columns for songs.
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
    }

    public void initializePlaylistTable() {
        lstPlaylist.getItems().clear();
        lstPlaylist.setItems(myTunesModel.getAllPlaylists());
        // set the tableview columns for playlists.
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        songsColumn.setCellValueFactory(new PropertyValueFactory<>("songs"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("totalDuration"));
    }

    public void handlePlaylistSelection() throws IOException {
        lstPlaylist.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Playlist selectedPlaylist = lstPlaylist.getSelectionModel().getSelectedItem();
            if (selectedPlaylist == null) {
                lstSongOnPlaylist.getItems().clear();
                return;
            }
            int playlistId = selectedPlaylist.getId();
            List<SongsOnPlaylist> songsOnPlaylist = myTunesModel.getSongsOnPlaylist(playlistId);

            // Map SongsOnPlaylist to Song objects
            List<Song> songs = songsOnPlaylist.stream()
                    .map(sop -> myTunesModel.getAllSongs().stream()
                            .filter(song -> song.getId() == sop.getSongId())
                            .findFirst()
                            .orElse(null))
                    .filter(Objects::nonNull)
                    .toList();
            lstSongOnPlaylist.getItems().setAll(songsOnPlaylist);
            setSongsOnPlaylistTableId();
        });
    }

    public void handleSongSelection() throws IOException{
        // Add listener for song selection changes
        lstSongs.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Song selectedSong = (Song) lstSongs.getSelectionModel().getSelectedItem();
            if (selectedSong == null) {
                // Clear the song display or take appropriate action if no song is selected
                labelCurrentSong.setText("");
                return;
            }
            displayCurrentlyPlayingSong(selectedSong);
        });
    }

    /*public void onPlayButtonClick(ActionEvent actionEvent) {
        SongsOnPlaylist selectedPlaylistSong = (SongsOnPlaylist) lstSongOnPlaylist.getSelectionModel().getSelectedItem();
        Playlist selectedPlaylist = lstPlaylist.getSelectionModel().getSelectedItem();
        Song selectedSong = lstSongs.getSelectionModel().getSelectedItem();
        // Check if mediaPlayer exists and is paused
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
        // If no music is playing or paused, proceed with the regular play logic
        if (selectedPlaylistSong != null && selectedPlaylist == null && selectedSong == null) {
            // Play the selected song from the playlist
            String songPath = selectedSong.getSongPath();
            if (songPath != null && !songPath.isEmpty()) {
                playSong(songPath);
                displayCurrentlyPlayingSong(selectedSong);
            } else {
                showWarningDialog("Invalid Song Path", "The selected song's file path is invalid or empty.");
            }
            return;
        }

        if (selectedPlaylist != null) {
            playPlaylist(selectedPlaylist.getId());
            return;
        }

        if (selectedSong != null) {
            // Play a single song from the songs table
            String songPath = selectedSong.getSongPath();
            if (songPath != null && !songPath.isEmpty()) {
                playSong(songPath);
                displayCurrentlyPlayingSong(selectedSong);
            } else {
                showWarningDialog("Invalid Song Path", "The selected song's file path is invalid or empty.");
            }
        } else {
            showWarningDialog("No Selection", "Please select a playlist or song to play.");
        }
        // If no new selection, resume the currently paused song
        mediaPlayer.play();
        System.out.println("Music resumed.");
        return;
        }
    }*/

    public void onPlayButtonClick(ActionEvent actionEvent) {
        SongsOnPlaylist selectedPlaylistSong = (SongsOnPlaylist) lstSongOnPlaylist.getSelectionModel().getSelectedItem();
        Playlist selectedPlaylist = lstPlaylist.getSelectionModel().getSelectedItem();
        Song selectedSong = lstSongs.getSelectionModel().getSelectedItem();

        if (mediaPlayer != null) {
            MediaPlayer.Status status = mediaPlayer.getStatus();

            if (status == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
                System.out.println("Music paused.");
                return;
            } else if (status == MediaPlayer.Status.PAUSED) {
                if (selectedPlaylistSong == null && selectedPlaylist == null && selectedSong == null) {
                    mediaPlayer.play();
                    System.out.println("Music resumed.");
                    return;
                }
            }
        }

        if (selectedPlaylistSong != null && selectedPlaylist != null) {
            playingFromPlaylist = true;
            playingFromSongs = false;

            List<SongsOnPlaylist> sop = myTunesModel.getSongsOnPlaylist(selectedPlaylist.getId());
            currentSongList = sop.stream()
                    .map(item -> myTunesModel.getAllSongs().stream()
                            .filter(s -> s.getId() == item.getSongId())
                            .findFirst()
                            .orElse(null))
                    .filter(Objects::nonNull)
                    .toList();

            int selectedIndex = lstSongOnPlaylist.getSelectionModel().getSelectedIndex();
            currentSongIndex = (selectedIndex >= 0) ? selectedIndex : 0;

            if (currentSongList.isEmpty()) {
                showWarningDialog("No Songs in Playlist", "This playlist has no valid songs to play.");
                return;
            }

            Song songToPlay = currentSongList.get(currentSongIndex);
            playSong(songToPlay.getSongPath());
            displayCurrentlyPlayingSong(songToPlay);
            return;
        }

        if (selectedSong != null) {
            playingFromSongs = true;
            playingFromPlaylist = false;

            currentSongList = new ArrayList<>(lstSongs.getItems());
            currentSongIndex = lstSongs.getSelectionModel().getSelectedIndex();
            Song songToPlay = currentSongList.get(currentSongIndex);
            if (songToPlay.getSongPath() != null && !songToPlay.getSongPath().isEmpty()) {
                playSong(songToPlay.getSongPath());
                displayCurrentlyPlayingSong(songToPlay);
            } else {
                showWarningDialog("Invalid Song Path", "The selected song's file path is invalid or empty.");
            }
        } else {
            showWarningDialog("No Selection", "Please select a playlist song or a regular song to play.");
        }
    }

    // Play a new song from the playlist
    private void playNewPlaylistSong(SongsOnPlaylist selectedPlaylistSong) {
        Song song = (Song) myTunesModel.getSongsOnPlaylist(selectedPlaylistSong.getId());
        if (song != null) {
            // Only initialize a new MediaPlayer if a new song is selected
            if (mediaPlayer == null || !mediaPlayer.getMedia().getSource().contains(song.getSongPath())) {
                playSong(song.getSongPath());
                displayCurrentlyPlayingSong(song);
            } else {
                mediaPlayer.play(); // Resume if the song is already loaded
            }
        } else {
            showWarningDialog("Invalid Song", "The selected song from the playlist is not valid.");
        }
    }

    // Play a new single song from the songs table
    private void playNewSingleSong(Song selectedSong) {
        String songPath = selectedSong.getSongPath();
        if (songPath != null && !songPath.isEmpty()) {
            // Only initialize a new MediaPlayer if a new song is selected
            if (mediaPlayer == null || !mediaPlayer.getMedia().getSource().contains(songPath)) {
                playSong(songPath);
                displayCurrentlyPlayingSong(selectedSong);
            } else {
                mediaPlayer.play(); // Resume if the song is already loaded
            }
        } else {
            showWarningDialog("Invalid Song Path", "The selected song's file path is invalid or empty.");
        }
    }

    private void playSong(String songPath) {
        try {
            if (songPath == null || songPath.isEmpty()) {
                System.out.println("Error: Song path is null or empty.");
                showWarningDialog("Invalid Song Path", "The song path is invalid or empty.");
                return;
            }

            // If mediaPlayer is already initialized, stop and dispose of the current player
            if (mediaPlayer != null) {
                System.out.println("Stopping current song: " + mediaPlayer.getMedia().getSource());
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }

            // Log the song path being used
            System.out.println("Playing song with path: " + songPath);

            // Create a new MediaPlayer instance for the new song
            Media media = new Media(new File(songPath).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            // Log media info (optional)
            System.out.println("Media player created for song: " + media.getSource());

            mediaPlayer.setOnEndOfMedia(this::playNextSong); // Automatically play the next song when the current one ends
            mediaPlayer.play();

        } catch (Exception e) {
            System.out.println("Error during playback: " + e.getMessage());
            showWarningDialog("Playback Error", "Unable to play the selected song.");
        }
    }

    public void playPlaylist(int playlistId) {
        // Get the songs on the playlist
        List<SongsOnPlaylist> songsInPlaylist = myTunesModel.getSongsOnPlaylist(playlistId);

        if (songsInPlaylist.isEmpty()) {
            showWarningDialog("No Songs in Playlist", "The selected playlist has no songs to play.");
            return;
        }

        // Convert playlist songs to a queue
        Queue<Song> playlistQueue = new LinkedList<>();
        for (SongsOnPlaylist songOnPlaylist : songsInPlaylist) {
            int songId = songOnPlaylist.getSongId();
            Song song = myTunesModel.getAllSongs().stream()
                    .filter(s -> s.getId() == songId)
                    .findFirst()
                    .orElse(null);

            if (song != null) {
                playlistQueue.add(song);
            }
        }

        if (playlistQueue.isEmpty()) {
            showWarningDialog("No Valid Songs in Playlist", "None of the songs in this playlist have a valid file path.");
            return;
        }

        // Start playing the first song in the playlist
        playPlaylistQueue(playlistQueue);
    }

    private void playPlaylistQueue(Queue<Song> playlistQueue) {
        if (playlistQueue.isEmpty()) {
            System.out.println("Playlist finished.");
            return; // End playback when the queue is empty
        }
        Song currentSong = playlistQueue.poll();
        if (currentSong != null) {
            String songPath = currentSong.getSongPath();
            if (songPath != null && !songPath.isEmpty()) {
                try {
                    playSong(songPath);
                    Platform.runLater(() -> displayCurrentlyPlayingSong(currentSong));

                    // Wait for the current song to finish, then play the next song
                    mediaPlayer.setOnEndOfMedia(() -> playPlaylistQueue(playlistQueue));
                } catch (Exception e) {
                    System.out.println("Error playing song: " + currentSong.getTitle() + " - " + e.getMessage());
                    playPlaylistQueue(playlistQueue); // Skip to the next song if there's an error
                }
            } else {
                System.out.println("Invalid song path for: " + currentSong.getTitle());
                playPlaylistQueue(playlistQueue); // Skip to the next song if path is invalid
            }
        }
    }

     @FXML
     private void onMute(ActionEvent actionEvent) {
        if (mediaPlayer != null) {
            if(mediaPlayer.isMute()){
                mediaPlayer.setMute(false);
                btnMute.setText("\uD83D\uDD0A");
            }else{
                mediaPlayer.setMute(true);
                btnMute.setText("\uD83D\uDD07");
            }
        }
     }

    public void showWarningDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showInfoDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Інформаційне повідомлення
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void playNextSong() {
        //  Get the current index of the selected song
        int currentIndex = lstSongs.getSelectionModel().getSelectedIndex();
        // Ensure there is a song selected and have at least two songs in the list
        if (currentIndex >= 0 && currentIndex < lstSongs.getItems().size() - 1) {
            lstSongs.getSelectionModel().select(currentIndex + 1); //Select the next song in the list
            Song nextSong = (Song) lstSongs.getSelectionModel().getSelectedItem(); // Get the next song
            if (nextSong != null) {
                playSong(nextSong.getSongPath());
                displayCurrentlyPlayingSong(nextSong);
            }
        }

        // I wanna the playlist to restart from the beginning after the last song is played.
        if (currentIndex == lstSongs.getItems().size() - 1) {
            lstSongs.getSelectionModel().select(0); // Restart from the 1st song
            Song firstSong = (Song) lstSongs.getSelectionModel().getSelectedItem();
            if (firstSong != null) {
                playSong(firstSong.getSongPath());
                displayCurrentlyPlayingSong(firstSong);
            }
        }
    }

    public void onREWClick(ActionEvent actionEvent) {
        if (playingFromPlaylist && currentSongList != null && !currentSongList.isEmpty()) {
            if (currentSongIndex > 0) {
                currentSongIndex--;
            }
            Song prevSong = currentSongList.get(currentSongIndex);
            playSong(prevSong.getSongPath());
            displayCurrentlyPlayingSong(prevSong);
        } else if (playingFromSongs && currentSongList != null && !currentSongList.isEmpty()) {
            if (currentSongIndex > 0) {
                currentSongIndex--;
                Song prevSong = currentSongList.get(currentSongIndex);
                playSong(prevSong.getSongPath());
                displayCurrentlyPlayingSong(prevSong);
            } else {
                showWarningDialog("Playback Error", "No previous song. You are at the start of the song list.");
            }
        } else {
            showWarningDialog("No Selection", "Please start playback first.");
        }
    }

    public void REWPlayForSongTable() {
        int currentIndex = lstSongs.getSelectionModel().getSelectedIndex();
        if (currentIndex >= 0) { // Ensure a valid song is selected
            if (currentIndex > 0) { // Ensure the current song is not the first one
                lstSongs.getSelectionModel().select(currentIndex - 1); // Select the previous song in the list
                Song previousSong = (Song) lstSongs.getSelectionModel().getSelectedItem(); // Get the previous song
                if (previousSong != null) {
                    playSong(previousSong.getSongPath());
                    displayCurrentlyPlayingSong(previousSong);
                }
            } else {
                showWarningDialog("Playback Error", "No previous song. You are at the start of the song list.");
            }
        } else {
            showWarningDialog("Playback Error", "No song selected. Please select a song to play.");
        }
    }

    public void REWPlayForPlaylistTable() {
        try {
            // Step 1: Get the current index of the selected song in the playlist
            int currentIndex = lstSongOnPlaylist.getSelectionModel().getSelectedIndex();
            System.out.println("Current index: " + currentIndex); // Debug log

            // Step 2: Check if there is a previous song
            if (currentIndex > 0) { // Ensure we're not at the start of the playlist
                // Move selection to previous song
                lstSongOnPlaylist.getSelectionModel().select(currentIndex - 1);

                // Step 3: Get the previous SongsOnPlaylist item
                SongsOnPlaylist previousSongOnPlaylist = (SongsOnPlaylist) lstSongOnPlaylist.getSelectionModel().getSelectedItem();
                System.out.println("Previous SongsOnPlaylist: " + previousSongOnPlaylist); // Debug log

                if (previousSongOnPlaylist != null) {
                    // Step 4: Get the song's path directly from the SongsOnPlaylist item
                    String songPath = previousSongOnPlaylist.getSongPath(); // Assuming this is directly in SongsOnPlaylist
                    System.out.println("Song Path: " + songPath); // Debug log

                    if (songPath != null && !songPath.isEmpty()) {
                        // Play the previous song
                        playSong(songPath);
                        //displayCurrentlyPlayingSong(previousSongOnPlaylist); // Update UI to show the current song
                    } else {
                        showWarningDialog("Playback Error", "The selected song's file path is invalid or empty.");
                    }
                } else {
                    showWarningDialog("Playback Error", "No previous song could be found in the playlist.");
                }
            } else {
                showWarningDialog("Playback Error", "You are already at the start of the playlist.");
            }
        } catch (Exception e) {
            // Catch any unexpected errors and log them
            System.err.println("Error while playing previous song: " + e.getMessage());
            e.printStackTrace();
            showWarningDialog("Playback Error", "An unexpected error occurred while trying to play the previous song.");
        }
    }


    public void onFFClick(ActionEvent actionEvent) {
        if (playingFromPlaylist && currentSongList != null && !currentSongList.isEmpty()) {
            if (currentSongIndex < currentSongList.size() - 1) {
                currentSongIndex++;
            } else {
                // За бажанням можна зациклити:
                // currentSongIndex = 0;
            }
            Song nextSong = currentSongList.get(currentSongIndex);
            playSong(nextSong.getSongPath());
            displayCurrentlyPlayingSong(nextSong);
        } else if (playingFromSongs && currentSongList != null && !currentSongList.isEmpty()) {
            if (currentSongIndex < currentSongList.size() - 1) {
                currentSongIndex++;
                Song nextSong = currentSongList.get(currentSongIndex);
                playSong(nextSong.getSongPath());
                displayCurrentlyPlayingSong(nextSong);
            } else {
                showWarningDialog("Playback error", "No next song. You are at the end of the list.");
            }
        } else {
            showWarningDialog("No Selection", "Please start playback first.");
        }
    }

    // Filter function starts from here
    public void onFilterBtnClick(ActionEvent actionEvent) throws IOException {
        String query = txtQuery.getText().trim().toLowerCase(); // Get and trim the query text
        // If query is empty, show a warning
        if (query.isEmpty()) {
            showWarningDialog("Error", "Please input what you want to search!");
            return;
        }
        try {
            // Perform the filtering
            myTunesModel.getFilteredSongs(query); // Apply filter
            System.out.println("Filtered songs in TableView: " + lstSongs.getItems()); // Log the items in the TableView
            lstSongs.setItems(myTunesModel.getFilteredSongs(query));  // Update TableView with filtered songs
            lstSongs.refresh();
            btnClear.setText("Clear");
            btnClear.setDisable(false);
        } catch (Exception e) {
            showWarningDialog("Error", "An error occurred while filtering songs.");
        }
    }

    // Method for the Clear button click action
    public void onClearBtnClick(ActionEvent actionEvent) {
        // Only allow clearing if the button is enabled (i.e., when the filter is active)
        if (!btnClear.isDisable()) {
            btnClear.setText("Filter");
            btnClear.setDisable(true);  // Disable the Clear button
            txtQuery.clear();
            lstSongs.refresh();
            lstSongs.setItems(myTunesModel.getAllSongs());
        }
    }

    public void onEditPlaylistClick (ActionEvent actionEvent) throws IOException {
        //openEditor("/dk/easv/mytunes/PlaylistEditor.fxml", "New/Edit Playlist", this);
        // Get the selected playlist
        Playlist selectedPlaylist = lstPlaylist.getSelectionModel().getSelectedItem();
        if (selectedPlaylist == null) {
            showWarningDialog("No Playlist Selected", "Please select a playlist to edit.");
            return;
        }

        // Load the FXML for the editor
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/mytunes/PlaylistEditor.fxml"));
        Parent root = loader.load();

        // Pass the selected playlist to the editor controller
        PlaylistEditorController controller = loader.getController();
        controller.setParentController(this);
        controller.setMyTunesModel(myTunesModel);
        controller.setPlaylist(selectedPlaylist); // Передаємо плейлист у редактор

        // Display the editor
        Stage stage = new Stage();
        stage.setTitle("Edit Playlist");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        stage.showAndWait();

        // Refresh the playlist table after editing
        initializePlaylistTable();
    }

    public void onAddPlaylistClick (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/mytunes/PlaylistEditor.fxml"));
        Parent root = loader.load();

        // Get the controller associated with the PlaylistEditor
        PlaylistEditorController controller = loader.getController();
        // Set the parent controller to establish a connection
        controller.setParentController(this);
        controller.setMyTunesModel(myTunesModel);
        Stage stage = new Stage();
        stage.setTitle("New/Edit Playlist");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        stage.showAndWait();
    }

    public void onEditSongClick (ActionEvent actionEvent) throws IOException {
       // openEditor("/dk/easv/mytunes/SongEditor.fxml", "New/Edit Song", this);

        // Check if a song is selected for editing
        Song selectedSong = lstSongs.getSelectionModel().getSelectedItem();
        if (selectedSong == null) {
            showWarningDialog("No Song Selected", "Please select a song to edit.");
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/mytunes/SongEditor.fxml"));
        Parent root = loader.load();

        // Get the controller for the SongEditor view and pass the selected song
        SongEditorController songEditorController = loader.getController();
        songEditorController.setParentController(this);
        songEditorController.setSong(selectedSong);

        Stage stage = new Stage();
        stage.setTitle("New/Edit Song");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        stage.showAndWait();

        // Refresh the song list to reflect changes made during editing
        lstSongs.refresh();
    }

    public void onAddSongClick (ActionEvent actionEvent) throws IOException {
        openEditor("/dk/easv/mytunes/SongEditor.fxml", "New/Edit Song", this);
    }

    // Show New/Edit dialog by clicking btn new and btn edit.
    private void openEditor (String fxmlPath, String title, Object parentController) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        Scene scene = new Scene(fxmlLoader.load());
        // Set the parent controller
        Object controller = fxmlLoader.getController();
        if (controller instanceof SongEditorController) {
            ((SongEditorController) controller).setParentController((MyTunesController) parentController);
        } else if (controller instanceof PlaylistEditorController) {
            ((PlaylistEditorController) controller).setParentController((MyTunesController) parentController);
        }
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public void onDeleteSongClick(ActionEvent actionEvent) {
        Song selectedSong = lstSongs.getSelectionModel().getSelectedItem();

        if (selectedSong != null) {
            // Create a confirmation dialog
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Deletion");
            confirmationAlert.setHeaderText("Are you sure you want to delete this song?");
            confirmationAlert.setContentText("Song: " + selectedSong.getTitle() + " by " + selectedSong.getArtist());

            // Wait for the user's response
            var result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    // Delete the song from the database via the model
                    myTunesModel.deleteSong(selectedSong.getId());

                    lstSongs.getItems().remove(selectedSong);

                    lstSongs.refresh();

                    // Inform the user about the successful deletion
                    showInfoDialog("Success", "The song was successfully deleted.");
                } catch (Exception e) {
                    showWarningDialog("Error", "An error occurred while deleting the song: " + e.getMessage());
                }
            }
        } else {
            // Show a warning if no song is selected
            showWarningDialog("No Song Selected", "Please select a song to delete.");
        }
    }

    public void onCloseBtnClick(ActionEvent actionEvent) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    public void onMoveDownClick(ActionEvent actionEvent) {
        int selectedIndex = lstSongOnPlaylist.getSelectionModel().getSelectedIndex();

        // Ensure it is not the last element
        if (selectedIndex >= 0 && selectedIndex < lstSongOnPlaylist.getItems().size() - 1) {
            var items = lstSongOnPlaylist.getItems();
            // Swap the current song with the next one
            var selectedSong = items.get(selectedIndex);
            items.set(selectedIndex, items.get(selectedIndex + 1));
            items.set(selectedIndex + 1, selectedSong);

            // Update selection
            lstSongOnPlaylist.getSelectionModel().select(selectedIndex + 1);
        }
    }

    public void onMoveUpClick(ActionEvent actionEvent) {
        int selectedIndex = lstSongOnPlaylist.getSelectionModel().getSelectedIndex();

        if (selectedIndex > 0) { // Ensure it is not the first element
            var items = lstSongOnPlaylist.getItems();
            // Swap the current song with the previous one
            var selectedSong = items.get(selectedIndex);
            items.set(selectedIndex, items.get(selectedIndex - 1));
            items.set(selectedIndex - 1, selectedSong);

            // Update selection
            lstSongOnPlaylist.getSelectionModel().select(selectedIndex - 1);
        }
    }

    public void onDeletePlaylistClick(ActionEvent actionEvent) {
        Playlist selectedPlaylist = lstPlaylist.getSelectionModel().getSelectedItem();
        if (selectedPlaylist != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Deletion");
            confirmationAlert.setHeaderText("Are you sure you want to delete this Playlist?");
            confirmationAlert.setContentText("Playlist" + selectedPlaylist.getName());

            var result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    myTunesModel.deletePlaylist(selectedPlaylist.getId());
                    lstPlaylist.getItems().remove(selectedPlaylist);
                    lstPlaylist.refresh();
                    showInfoDialog("Success", "The playlist was successfully deleted.");
                }catch (Exception e){
                    showWarningDialog("Error", "An error occurred while deleting the playlist: " + e.getMessage());
                }
            }
        }else {
            showWarningDialog("No Playlist Selected", "Please select a song to delete.");
        }
    }

    public void onAddSongsToPlaylistClick(ActionEvent actionEvent) {
        Playlist selectedPlaylist = (Playlist) lstPlaylist.getSelectionModel().getSelectedItem();
        Song selectedSong = (Song) lstSongs.getSelectionModel().getSelectedItem();
        if (selectedPlaylist != null && selectedSong != null) {
            try {
                //MyTunesModel model= new MyTunesModel();
                myTunesModel.addSongToPlaylist(selectedPlaylist.getId(),selectedSong.getId());
                refreshPlaylistTable();
                refreshSongsOnPlaylistView(selectedPlaylist);
                // Re-select the previously selected playlist
                lstPlaylist.getSelectionModel().select(selectedPlaylist);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            showWarningDialog("Error", "Please select both a playlist and a song.");
        }
    }

    private void setSongsOnPlaylistTableId(){
        // Set up the cell factory for the ListView
        lstSongOnPlaylist.setCellFactory(lv -> new ListCell<SongsOnPlaylist>() {
            @Override
            protected void updateItem(SongsOnPlaylist song, boolean empty) {
                super.updateItem(song, empty);
                if (empty || song == null) {
                    setText(null);
                } else {
                    int index = getIndex() + 1; // Use the index for numbering
                    setText(index + ". " + song.getTitle());
                }
            }
        });
    }

    private void refreshSongsOnPlaylistView(Playlist playlist) {
        List<SongsOnPlaylist> sop = myTunesModel.getSongsOnPlaylist(playlist.getId());
        lstSongOnPlaylist.setItems(FXCollections.observableArrayList(sop));
    }

    private void refreshPlaylistTable() throws IOException {
        List<Playlist> playlists = myTunesModel.getAllPlaylists(); // Fetch updated playlists
        lstPlaylist.setItems(FXCollections.observableArrayList(playlists)); // Update the table view
    }

    public void onDeleteSongsOnPlaylistClick(ActionEvent actionEvent) {
        SongsOnPlaylist selectedSongsOnPlaylist = (SongsOnPlaylist) lstSongOnPlaylist.getSelectionModel().getSelectedItem();
        Playlist selectedPlaylist = (Playlist) lstPlaylist.getSelectionModel().getSelectedItem();
        if (selectedSongsOnPlaylist != null && selectedPlaylist != null) {
            System.out.println("Deleting song from playlist...");
            System.out.println("Selected Playlist ID: " + selectedPlaylist.getId());
            System.out.println("Selected Song ID: " + selectedSongsOnPlaylist.getId());
            try {
                // Remove song from playlist
                myTunesModel.removeSongFromPlaylist(selectedSongsOnPlaylist.getPlaylistId(), selectedSongsOnPlaylist.getSongId());
                // Refresh both tables
                refreshPlaylistTable();
                refreshSongsOnPlaylistView(selectedPlaylist);
                System.out.println("Updated songsOnPlaylist table for playlist ID: " + selectedPlaylist.getId());
            } catch (IOException e) {
                showWarningDialog("Error", "Failed to remove song from playlist.");
                e.printStackTrace();
            }
        } else {
            System.out.println("No song or playlist selected.");
            showWarningDialog("Error", "No song selected.");
        }
    }
}


