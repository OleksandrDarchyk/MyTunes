package dk.easv.mytunes.gui.controllers;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.be.SongsOnPlaylist;
import dk.easv.mytunes.gui.models.MyTunesModel;
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
    private TableView lstPlaylist;
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

        volumeSlider.setValue(mediaPlayer.getVolume() * 100); // volume slider
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mediaPlayer.setVolume(volumeSlider.getValue() / 100 );
            }
        });

        btnClear.setDisable(true);
        initializeSongTable();
        initializePlaylistTable();
        setSongsOnPlaylistTableId();
        try {
            handlePlaylistSelection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        System.out.println("Initializing Playlist Table...");
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

    // Make play btn to play music
    public void onPlayButtonClick(ActionEvent actionEvent) {
        Song selectedSong = lstSongs.getSelectionModel().getSelectedItem();
        if (selectedSong != null) {
            String songPath = selectedSong.getSongPath();
            if (songPath != null && !songPath.isEmpty()) {
                // Check if a song is already playing
                if (mediaPlayer != null) {
                    // Pause or resume if the same song is selected
                    if (mediaPlayer.getMedia().getSource().equals(new File(songPath).toURI().toString())) {
                        switch (mediaPlayer.getStatus()) {
                            case PLAYING -> mediaPlayer.pause();
                            case PAUSED -> mediaPlayer.play();
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

    private void playSong(String songPath) {
        // Stop the current song if one is playing
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose(); // Release resources of the old player
        }
        try {
            Media media = new Media(new File(songPath).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
            mediaPlayer.setOnEndOfMedia(this::playNextSong); // Automatically play the next song when the current one ends
        } catch (Exception e) {
            showWarningDialog("Playback Error", "Unable to play the selected song.");
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
        openEditor("/dk/easv/mytunes/PlaylistEditor.fxml", "New/Edit Playlist", this);
    }

    public void onAddPlaylistClick (ActionEvent actionEvent) throws IOException {
        openEditor("/dk/easv/mytunes/PlaylistEditor.fxml", "New/Edit Playlist", this);
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

        int selectedSongId = lstSongs.getSelectionModel().getSelectedItem().getId();
        try {
            myTunesModel.deleteSong(selectedSongId);
            lstSongs.getItems().removeIf(song -> song.getId() == selectedSongId);
            lstSongs.refresh();
            showInfoDialog("Success", "The song was successfully deleted.");
        }
        catch (Exception e) {
            showWarningDialog(e.getMessage(), "The selected song's file path is invalid or empty.");
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


