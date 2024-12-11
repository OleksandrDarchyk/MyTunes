package dk.easv.mytunes.gui.controllers;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.gui.models.MyTunesModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.util.List;
import java.util.ResourceBundle;

public class SongEditorController implements Initializable {
    @FXML
    private TextField txtTitle;
    @FXML
    private TextField txtArtist;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private TextField txtTime;
    @FXML
    private TextField txtFilePath;
    @FXML
    private Button btnCancelSong;
    @FXML
    private Button btnChoose;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnSave;

    private MyTunesController myTunesController;
    private final MyTunesModel myTunesModel = new MyTunesModel();
    private Song songToEdit;

    public void initialize(URL location, ResourceBundle resources) {
        displayCategory();
    }

    public void displayCategory(){
        try {
            // Fetch categories from the model
            ObservableList<String> categories = myTunesModel.getCategory();
            comboBox.setItems(categories);
            if (!categories.isEmpty()) {
                comboBox.setValue(categories.get(0)); // Set first category as default
            }
        } catch (IOException e) {
            System.out.println("Error loading categories: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void onLoadMoreClick(ActionEvent actionEvent) {
    }

    public void onChooseClick(ActionEvent actionEvent) {
        // Open a file chooser dialog to select a song file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a Song File"); // Set the title of the file chooser window
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav", "*.m4a") // Restrict file types to common audio formats
        );

        // Show the file chooser and store the selected file
        File selectedFile = fileChooser.showOpenDialog(btnChoose.getScene().getWindow());
        if (selectedFile != null) {
            // If a file is selected, set its path in the text field
            txtFilePath.setText(selectedFile.getAbsolutePath());

            try {
                // Create a Media object from the selected file
                Media media = new Media(selectedFile.toURI().toString());
                // Create a MediaPlayer object to interact with the media
                MediaPlayer mediaPlayer = new MediaPlayer(media);

                // When the media is ready, calculate its duration
                mediaPlayer.setOnReady(() -> {
                    double durationInSeconds = media.getDuration().toSeconds();
                    int minutes = (int) (durationInSeconds / 60);
                    int seconds = (int) (durationInSeconds % 60);
                    String timeString = String.format("%d:%02d", minutes, seconds);
                    txtTime.setText(timeString);
                });
            } catch (Exception e) {
                myTunesController.showWarningDialog("File Error", "Unable to read the selected audio file.");
                txtTime.setText("00:00");// Set a default value for the time in case of an error
            }
        }

    }

    public void onCancelSongClick(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancelSong.getScene().getWindow();
        stage.close();
    }

    public void onSaveSongClick(ActionEvent actionEvent) {
       try {
           if(txtTitle.getText().isEmpty() || txtArtist.getText().isEmpty() ||
                   txtTime.getText().isEmpty() || txtFilePath.getText().isEmpty()) {
               myTunesController.showWarningDialog("Validation Error", "All fields must be filled!");
               return;
           }

           Time time;
           try {
               time = Time.valueOf("00:" + txtTime.getText());
           } catch (IllegalArgumentException e) {
               myTunesController.showWarningDialog("Invalid Time Format", "Time must be in the format mm:ss.");
               return;
           }

           String title = txtTitle.getText();
           String artist = txtArtist.getText();
           String category = comboBox.getValue();
           String path = txtFilePath.getText();

           if(songToEdit != null) {
               songToEdit.setTitle(title);
               songToEdit.setArtist(artist);
               songToEdit.setCategory(category);
               songToEdit.setSongPath(path);
               songToEdit.setTime(time);

               myTunesModel.updateSong(songToEdit);
           }else {
               Song newSong = new Song(title,artist,category,time,path);
               myTunesModel.createSong(newSong);

           }   myTunesController.initializeSongTable();
           Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
           stage.close();

       }catch (Exception e) {
           myTunesController.showWarningDialog("Error", "An error occurred while saving the song: " + e.getMessage());
       }

        /* try {
            String title = txtTitle.getText();
            String artist = txtArtist.getText();
            String category = comboBox.getValue();
            String filePath = txtFilePath.getText();

            Time time = Time.valueOf("00:"+txtTime.getText());

            Song newSong = new Song(title, artist, category, time, filePath);

            myTunesModel.createSong(newSong);

            myTunesController.initializeSongTable();

            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Error saving song: " + e.getMessage());
        } */
    }


    public void setParentController(MyTunesController myTunesController) {
        this.myTunesController = myTunesController;
    }

    public void setSong(Song selectedSong) {
        this.songToEdit = selectedSong;

        txtTitle.setText(selectedSong.getTitle());
        txtArtist.setText(selectedSong.getArtist());
        comboBox.setValue(selectedSong.getCategory());
        txtTime.setText(selectedSong.getTime().toString());
        txtFilePath.setText(selectedSong.getSongPath());

        java.sql.Time time = selectedSong.getTime();
        String[] timeParts = time.toString().split(":");
        String formattedTime = timeParts[1] + ":" + timeParts[2];
        txtTime.setText(formattedTime);
        txtTime.setEditable(false);

        txtFilePath.setText(selectedSong.getSongPath());

    }
}
