module com.dtu.roboclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires annotations;


    opens com.dtu.roboclient to javafx.fxml;
    exports com.dtu.roboclient;
}