package mymediaplayer;

import javafx.animation.PauseTransition;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class FXMLController {

    private MediaPlayer mediaPlayer;

    @FXML
    private MediaView mediaView;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Slider timeSlider;

    @FXML
    private BorderPane window;

    @FXML
    private VBox menu;

    @FXML
    private Text loadingText;

    private String filePath;

    @FXML
    private void openVideoFile(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select a File (*.mp4)", "*.mp4");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);
        filePath = file.toURI().toString();

        if(filePath != null) {
            Media media = new Media(filePath);
            mediaPlayer = new MediaPlayer(media);
            loadingText.setText("Video is loading...");
            mediaView.setMediaPlayer(mediaPlayer);
            DoubleProperty width = mediaView.fitWidthProperty();
            DoubleProperty height = mediaView.fitHeightProperty();
            width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
            height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));

            mediaPlayer.statusProperty().addListener(new ChangeListener<MediaPlayer.Status>() {
                @Override
                public void changed(ObservableValue<? extends MediaPlayer.Status> observableValue, MediaPlayer.Status oldStatus, MediaPlayer.Status newStatus) {
                    if (newStatus == MediaPlayer.Status.READY) loadingText.setText("");
                }
            });

            volumeSlider.setValue(mediaPlayer.getVolume() * 100);
            volumeSlider.valueProperty().addListener(observable -> mediaPlayer.setVolume(volumeSlider.getValue() / 100));

            mediaPlayer.totalDurationProperty().addListener((observableValue, oldValue, newValue) -> timeSlider.setMax(newValue.toSeconds()));
/*            timeSlider.valueChangingProperty().addListener(((observableValue, wasChanging, isChanging) -> { //continue play if slider position dont changing
                if(!isChanging) {
                    mediaPlayer.seek(Duration.seconds(timeSlider.getValue()));
                }
            }));*/
            timeSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> { //change time by click
                final double MIN_CHANGE = 0.5 ;
                if (! timeSlider.isValueChanging()) {
                    double currentTime = mediaPlayer.getCurrentTime().toSeconds();
                    if (Math.abs(currentTime - newValue.doubleValue()) > MIN_CHANGE) {
                        mediaPlayer.seek(Duration.seconds(newValue.doubleValue()));
                    }
                }
            });
            mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> { //change slider position during play, if user dont seek
                if (! timeSlider.isValueChanging()) {
                    timeSlider.setValue(newTime.toSeconds());
                }
            });

            BooleanProperty mouseMoving = new SimpleBooleanProperty();
            mouseMoving.addListener((obs, wasMoving, isNowMoving) -> {
                menu.setVisible(isNowMoving);
            });
            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(actionEvent -> mouseMoving.set(false));
            window.setOnMouseMoved(mouseEvent -> {
                mouseMoving.set(true);
                pause.playFromStart();
            });

            mediaPlayer.play();
        }
    }


    @FXML
    private void playVideo(ActionEvent event) {
        mediaPlayer.play();
    }

    @FXML
    private void pauseVideo(ActionEvent event) {
        mediaPlayer.pause();
    }

    @FXML
    private void stopVideo(ActionEvent event) {
        mediaPlayer.setRate(1);
        mediaPlayer.stop();
    }

    @FXML
    private void speed50percentVideo(ActionEvent event) {
        mediaPlayer.setRate(0.5);
    }

    @FXML
    private void speed75percentVideo(ActionEvent event) {
        mediaPlayer.setRate(0.75);
    }

    @FXML
    private void speed100percentVideo(ActionEvent event) {
        mediaPlayer.setRate(1);
    }

    @FXML
    private void speed150percentVideo(ActionEvent event) {
        mediaPlayer.setRate(1.5);
    }

    @FXML
    private void speed200percentVideo(ActionEvent event) {
        mediaPlayer.setRate(2);
    }

    @FXML
    private void onOffVolume(ActionEvent event) {
        if(mediaPlayer.getVolume() > 0) {
            mediaPlayer.setVolume(0);
        } else {
            mediaPlayer.setVolume(1);
        }
        volumeSlider.setValue(mediaPlayer.getVolume() * 100);
    }

    @FXML
    private void exitVideo(ActionEvent event) {
        System.exit(0);
    }
}