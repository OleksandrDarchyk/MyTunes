package dk.easv.mytunes.gui.controllers;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.gui.models.MyTunesModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MyTunesController implements Initializable {
    public Label lblTitle;
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
    private ListView lstPlaylists;
    @FXML
    private TableView lstSongs;
    @FXML
    private Button btnEditSong;
    @FXML
    private Button btnAddSong;
    @FXML
    private Button btnEditPlaylist;
    @FXML
    private Button btnAddPlaylist;

    private final MyTunesModel myTunesModel = new MyTunesModel();
    private MediaPlayer mediaPlayer;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnClear.setDisable(true);
        lstSongs.getItems().clear();
        lstSongs.setItems(myTunesModel.getSongs());

        // For TableView columns, set the cell value factories
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

    }

    // Make play btn to play music
    public void onPlayButtonClick(ActionEvent actionEvent) {
        Song selectedSong = (Song) lstSongs.getSelectionModel().getSelectedItem();
        if (selectedSong != null) {
            String songPath = selectedSong.getFilePath(); // Ensure Song class has the getFilePath method
            if (songPath != null && !songPath.isEmpty()) {
                if (mediaPlayer != null) {
                    switch (mediaPlayer.getStatus()) {
                        case PLAYING -> mediaPlayer.pause();
                        case PAUSED -> mediaPlayer.play();
                        case STOPPED, READY, HALTED -> {
                            mediaPlayer.dispose();
                            playSong(songPath);
                        }
                    }
                } else {
                    playSong(songPath);
                }
            } else {
                showWarningDialog("Invalid Song Path", "The selected song's file path is invalid or empty.");
            }
        } else {
            showWarningDialog("No Song Selected", "Please select a song from the list before playing.");
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

    private void showWarningDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
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
                playSong(nextSong.getFilePath());
            }
        }

        // I wanna the playlist to restart from the beginning after the last song is played.
        if (currentIndex == lstSongs.getItems().size() - 1) {
            lstSongs.getSelectionModel().select(0); // Restart from the 1st song
            Song firstSong = (Song) lstSongs.getSelectionModel().getSelectedItem();
            if (firstSong != null) {
                playSong(firstSong.getFilePath());
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
                playSong(previousSong.getFilePath());
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
                playSong(nextSong.getFilePath());
            }
        } else {
            showWarningDialog("Playback error", "No next song. You are at the end of the playlist.");
        }
    }

    // Filter function starts from here
    /*public void onFilterBtnClick(ActionEvent actionEvent) {
        String query = txtQuery.getText().trim().toLowerCase(); // Get and trim the query text
        List<Song> filteredSongs = myTunesModel.getFilteredSongs(query);
        // If query is empty, show a warning
        if (query.isEmpty()) {
            showWarningDialog("Error", "Please input what you want to search!");
            return;
        }
        try {
            // Perform the filtering
            if (filteredSongs.isEmpty()) {
                btnClear.setDisable(false);
                btnClear.setText("Clear");
            } else {
                showWarningDialog("No Results", "No songs match the query: " + query);
                btnClear.setText("Filter");
                btnClear.setDisable(true);
            }

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
            lstSongs.getItems().clear();
            lstSongs.setItems(myTunesModel.getSongs());
        }
    }*/


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

    public void onEditPlaylistClick (ActionEvent actionEvent) throws IOException {
        openEditor("/dk/easv/mytunes/PlaylistEditor.fxml", "New/Edit Playlist", this);
    }

    public void onAddPlaylistClick (ActionEvent actionEvent) throws IOException {
        openEditor("/dk/easv/mytunes/PlaylistEditor.fxml", "New/Edit Playlist", this);
    }

    public void onEditSongClick (ActionEvent actionEvent) throws IOException {
        openEditor("/dk/easv/mytunes/SongEditor.fxml", "New/Edit Song", this);
    }

    public void onAddSongClick (ActionEvent actionEvent) throws IOException {
        openEditor("/dk/easv/mytunes/SongEditor.fxml", "New/Edit Song", this);
    }

    public void onClearBtnClick(ActionEvent actionEvent) {
    }

    public void onFilterBtnClick(ActionEvent actionEvent) {
    }
}


