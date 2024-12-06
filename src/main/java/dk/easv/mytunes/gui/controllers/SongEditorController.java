package dk.easv.mytunes.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SongEditorController {
    @FXML
    private Button btnCancelSong;
    @FXML
    private Button btnChoose;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnSave;

    private MyTunesController myTunesController;

    public void setParentController(MyTunesController myTunesController) {
        this.myTunesController = myTunesController;
    }

    public void onLoadMoreClick(ActionEvent actionEvent) {
    }

    public void onChooseClick(ActionEvent actionEvent) {
    }

    public void onCancelSongClick(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancelSong.getScene().getWindow();
        stage.close();
    }

    public void onSaveSongClick(ActionEvent actionEvent) {
    }
}
