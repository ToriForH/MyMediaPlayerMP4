package mymediaplayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class MyMediaPlayer extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLFile.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Media Player");
        scene.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if(mouseEvent.getClickCount() == 2) {
                    stage.setFullScreen(true);
                }
            });
        scene.getStylesheets()
                .setAll(getClass()
                        .getResource("style.css")
                        .toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}