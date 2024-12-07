package dk.easv.mytunes.gui.controllers;

import dk.easv.mytunes.gui.models.MyTunesModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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
    }

    public void onCancelSongClick(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancelSong.getScene().getWindow();
        stage.close();
    }

    public void onSaveSongClick(ActionEvent actionEvent) {
    }


    public void setParentController(MyTunesController myTunesController) {
        this.myTunesController = myTunesController;
    }

}
