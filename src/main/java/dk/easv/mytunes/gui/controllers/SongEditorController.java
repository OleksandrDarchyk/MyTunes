package dk.easv.mytunes.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SongEditorController {
    @FXML
    private Button btnChoose;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnSave;

    private MyTunesController myTunesController;

    public void setParentController(MyTunesController myTunesController) {
        this.myTunesController = myTunesController;
        System.out.println("Parent controller set successfully: " + myTunesController);
    }




    public void onLoadMoreClick(ActionEvent actionEvent) {
    }

    public void onChooseClick(ActionEvent actionEvent) {
    }

    public void onSaveClick(ActionEvent actionEvent) {
    }

    public void onCancelClick(ActionEvent actionEvent) {
    }
}
