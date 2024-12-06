package dk.easv.mytunes.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PlaylistEditorController {
    @FXML
    private Button btnCancelPlaylist;
    private MyTunesController myTunesController;

    public void setParentController(MyTunesController myTunesController) {
        this.myTunesController = myTunesController;
    }



    public void onCancelPlaylistClick(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancelPlaylist.getScene().getWindow();
        stage.close();


    }

    public void onSavePlaylistClick(ActionEvent actionEvent) {
    }
}
