package dk.easv.mytunes.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class MyTunesController {

    @FXML
    private Button btnEditSong;
    @FXML
    private Button btnAddSong;
    @FXML
    private Button btnEditPlaylist;
    @FXML
    private Button btnAddPlaylist;

    public void onEditSongClick(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SongEditor.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        SongEditorController songEditorController = fxmlLoader.getController();
        songEditorController.setParentController(this);

        Stage stage = new Stage();
        stage.setTitle("New/Edit Song");
        stage.setScene(scene);
        stage.show();
    }

    public void onAddSongClick(javafx.event.ActionEvent actionEvent) {
    }

    public void onEditPlaylistClick(javafx.event.ActionEvent actionEvent) {
    }

    public void onAddPlaylistClick(javafx.event.ActionEvent actionEvent) {
    }
}
