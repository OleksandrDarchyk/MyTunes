package dk.easv.mytunes.gui.controllers;

import javafx.fxml.FXMLLoader;import javafx.scene.Scene;import javafx.scene.control.Button;import javafx.stage.Stage;import java.awt.event.ActionEvent;import java.io.IOException;

public class SongEditorController {
    private MyTunesController myTunesController;

    public static void setParentController(MyTunesController myTunesController) {
        //this.myTunesController = myTunesController;
    }
public static class MyTunesController {


    public Button btnEditSong;
    public Button btnAddSong;
    public Button btnEditPlaylist;
    public Button btnAddPlaylist;

    public void onEditSongClick(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SongEditor.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        //SongEditorController songEditorController = fxmlLoader.getController();

        setParentController(this);

        Stage stage = new Stage();
        stage.setTitle("New/Edit Song");
        stage.setScene(scene);
        stage.show();

    }

    public void onEditSongClick(javafx.event.ActionEvent actionEvent) {
    }

    public void onAddSongClick(javafx.event.ActionEvent actionEvent) {
    }

    public void onEditPlaylistClick(javafx.event.ActionEvent actionEvent) {
    }

    public void onAddPlaylistClick(javafx.event.ActionEvent actionEvent) {
    }
}}
