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
import java.util.List;
import java.util.ResourceBundle;

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

        handleSongSelection();
        try {
            handlePlaylistSelection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        // Add listener for playlist selection changes
        lstPlaylist.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //handlePlaylistSelection();
            Playlist selectedPlaylist = (Playlist) lstPlaylist.getSelectionModel().getSelectedItem();
            if (selectedPlaylist == null) {
                lstSongOnPlaylist.getItems().clear(); // Clear the ListView if no playlist is selected
                return;
            }
            // Fetch songs for the selected playlist from the database
            int playlistId = selectedPlaylist.getId();
            List<SongsOnPlaylist> songsOnPlaylist = myTunesModel.getSongsOnPlaylist(playlistId);
            for (SongsOnPlaylist song : songsOnPlaylist) {
                lstSongOnPlaylist.getItems().setAll(songsOnPlaylist);
            }
        });
    }

    public void handleSongSelection() {
        // Add listener for song selection changes
        lstSongs.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Song selectedSong = (Song) lstSongs.getSelectionModel().getSelectedItem();
            if (selectedSong == null) {
                // Clear the song display or take appropriate action if no song is selected
                labelCurrentSong.setText("");
                return;
            }

          /*  // Call your existing play functionality to play the selected song
            String songPath = selectedSong.getSongPath();
            if (songPath != null && !songPath.isEmpty()) {
                playSong(songPath);
                displayCurrentlyPlayingSong(selectedSong);
            } else {
                showWarningDialog("Invalid Song Path", "The selected song's file path is invalid or empty.");
            }*/
            displayCurrentlyPlayingSong(selectedSong);
        });
    }

    // Make play btn to play music
    public void onPlayButtonClick(ActionEvent actionEvent) {
        Song selectedSong = lstSongs.getSelectionModel().getSelectedItem();
        Playlist selectedPlaylist = lstPlaylist.getSelectionModel().getSelectedItem();
        if (selectedPlaylist != null) {
            // If a playlist is selected, play all songs in the playlist
            playPlaylist(selectedPlaylist.getId());
        }
        else if (selectedSong != null) {
            String songPath = selectedSong.getSongPath();
            if (songPath != null && !songPath.isEmpty()) {
                // Check if a song is already playing
                if (mediaPlayer != null) {
                    // If the same song is selected again, pause or resume.
                    if (mediaPlayer.getMedia().getSource().equals(new File(songPath).toURI().toString())) {
                        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                            mediaPlayer.pause();
                        } else if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED || mediaPlayer.getStatus() == MediaPlayer.Status.READY) {
                            mediaPlayer.play();
                        }

                        return; // Exit method since no new song needs to be started
                    }
                    // Stop and dispose of the current media player if a new song is selected
                    mediaPlayer.stop();
                    mediaPlayer.dispose();
                }
                // Play the new song
                playSong(songPath);
                displayCurrentlyPlayingSong(selectedSong);
            } else {
                showWarningDialog("Invalid Song Path", "The selected song's file path is invalid or empty.");
            }
        } else {
            showWarningDialog("No Song Selected", "Please select a song from the list before playing.");
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

        // Play songs in the playlist sequentially
        new Thread(() -> {
            for (SongsOnPlaylist songOnPlaylist : songsInPlaylist) {
                int songId = songOnPlaylist.getSongId();
                Song song = myTunesModel.getAllSongs().stream()
                        .filter(s -> s.getId() == songId)
                        .findFirst()
                        .orElse(null);

                if (song != null) {
                    String songPath = song.getSongPath();

                    if (songPath != null && !songPath.isEmpty()) {
                        try {
                            // Play the song
                            playSong(songPath);

                            // Update the UI on the JavaFX Application Thread
                            Platform.runLater(() -> displayCurrentlyPlayingSong(song));

                            // Wait until the song finishes
                            waitForSongToEnd();

                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            System.out.println("Playback interrupted.");
                            return;
                        }
                    } else {
                        System.out.println("Invalid path for song: " + song.getTitle());
                    }
                } else {
                    System.out.println("Song with ID " + songId + " not found.");
                }
            }
        }).start();
    }

    private void waitForSongToEnd() throws InterruptedException {
        if (mediaPlayer != null) {
            Object lock = new Object();
            mediaPlayer.setOnEndOfMedia(() -> {
                synchronized (lock) {
                    lock.notify();
                }
            });

            synchronized (lock) {
                lock.wait(); // Wait until the song finishes
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
        int currentIndex = lstSongs.getSelectionModel().getSelectedIndex();
        if (currentIndex > 0) // Ensure the current song is not the 1st one
        {
            lstSongs.getSelectionModel().select(currentIndex - 1); // Select the previous song in list
            Song previousSong = (Song) lstSongs.getSelectionModel().getSelectedItem(); // Get the previous song
            if (previousSong != null) {
                playSong(previousSong.getSongPath());
                displayCurrentlyPlayingSong(previousSong);
            }
        } else {
            showWarningDialog("Playback error", "No previous song. You are at the start of the playlist.");
        }
    }

    public void onFFClick(ActionEvent actionEvent) {
        int currentIndex = lstSongs.getSelectionModel().getSelectedIndex();
        if (currentIndex < lstSongs.getItems().size() - 1) // Ensure the current song is not the last one
        {
            lstSongs.getSelectionModel().select(currentIndex + 1);
            Song nextSong = (Song) lstSongs.getSelectionModel().getSelectedItem();
            if (nextSong != null) {
                playSong(nextSong.getSongPath());
                displayCurrentlyPlayingSong(nextSong);
            }
        } else {
            showWarningDialog("Playback error", "No next song. You are at the end of the playlist.");
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
                    setText(index + ". " + song.getTitle()); // Assuming SongsOnPlaylist has a getSong() method
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


