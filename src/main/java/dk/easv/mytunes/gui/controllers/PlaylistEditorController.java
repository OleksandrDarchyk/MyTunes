package dk.easv.mytunes.gui.controllers;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.gui.models.MyTunesModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PlaylistEditorController {
    @FXML
    private Button btnCancelPlaylist;
    @FXML
    private TextField txtName;

    private MyTunesModel myTunesModel;
    private MyTunesController myTunesController;
    private Playlist playlistToEdit;




    public void setParentController(MyTunesController myTunesController) {
        this.myTunesController = myTunesController;
    }

    public void onCancelPlaylistClick(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancelPlaylist.getScene().getWindow();
        stage.close();
    }

    public void onSavePlaylistClick(ActionEvent actionEvent) {
        try {
            if (txtName.getText().isEmpty()) {
                myTunesController.showWarningDialog("Validation Error", "Name cannot be empty!");
                return;
            }
            // Get the new name from the text field
            String newName = txtName.getText().trim();

            if (playlistToEdit != null) {
                // If an existing playlist is being edited
                playlistToEdit.setName(newName);
                myTunesModel.updatePlaylist(playlistToEdit);
            } else {
                // If a new playlist is being created
                Playlist newPlaylist = new Playlist(0, newName, 0, null);
                myTunesModel.createPlaylist(newPlaylist);
            }

            myTunesController.initializePlaylistTable();

            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            myTunesController.showWarningDialog("Error", "An error occurred while saving the playlist: " + e.getMessage());
        }
    }

    // Setter method to inject the MyTunesModel instance
    public void setMyTunesModel(MyTunesModel myTunesModel) {
        this.myTunesModel = myTunesModel;
    }

    public void setPlaylist(Playlist playlist) {
        // Store the passed playlist for editing
        this.playlistToEdit = playlist;
        txtName.setText(playlist.getName());
    }
}
