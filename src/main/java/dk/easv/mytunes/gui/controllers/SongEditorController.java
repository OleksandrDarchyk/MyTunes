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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a Song File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav", "*.m4a")
        );

        // Відкриваємо діалогове вікно для вибору файлу
        File selectedFile = fileChooser.showOpenDialog(btnChoose.getScene().getWindow());
        if (selectedFile != null) {
            txtFilePath.setText(selectedFile.getAbsolutePath()); // Встановлюємо шлях до файлу у текстове поле

            // Обчислюємо тривалість файлу
            try {
                Media media = new Media(selectedFile.toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(media);

                // Використовуємо MediaPlayer, щоб отримати тривалість файлу, коли він готовий
                mediaPlayer.setOnReady(() -> {
                    double durationInSeconds = media.getDuration().toSeconds();
                    int minutes = (int) (durationInSeconds / 60);
                    int seconds = (int) (durationInSeconds % 60);
                    String timeString = String.format("%d:%02d", minutes, seconds); // Формат 4:23
                    txtTime.setText(timeString); // Встановлюємо тривалість у поле
                });
            } catch (Exception e) {
                myTunesController.showWarningDialog("File Error", "Unable to read the selected audio file.");
                txtTime.setText("00:00"); // У разі помилки встановлюємо час за замовчуванням
            }
        }

    }

    public void onCancelSongClick(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancelSong.getScene().getWindow();
        stage.close();
    }

    public void onSaveSongClick(ActionEvent actionEvent) {
        try {
            // Зчитуємо дані з текстових полів
            String title = txtTitle.getText();
            String artist = txtArtist.getText();
            String category = comboBox.getValue(); // Обраний стиль пісні
            String filePath = txtFilePath.getText();

            // Автоматично обчислюємо час (приклад для заглушки)
            Time time = Time.valueOf("00:04:00"); // Пізніше можна додати обчислення тривалості файлу

            // Створюємо нову пісню
            Song newSong = new Song(title, artist, category, time, filePath);

            // Викликаємо метод для додавання пісні
            myTunesModel.createSong(newSong);

            // Оновлюємо таблицю пісень в головному вікні
            myTunesController.initializeSongTable();

            // Закриваємо вікно
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.out.println("Error saving song: " + e.getMessage());
        }
    }


    public void setParentController(MyTunesController myTunesController) {
        this.myTunesController = myTunesController;
    }

}
