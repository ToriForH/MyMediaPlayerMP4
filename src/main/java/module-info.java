module com.victoria.herchanivska.mymediaplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens mymediaplayer to javafx.fxml;
    exports mymediaplayer;
}