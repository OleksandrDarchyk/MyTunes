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
            // Retrieve and trim the playlist name from the text field
            String name = txtName.getText().trim();
            // Create a new Playlist object with default values
            Playlist newPlaylist = new Playlist(0, name, 0, null);

            myTunesModel.createPlaylist(newPlaylist);
            // Refresh the playlist table in the main controller
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

}
