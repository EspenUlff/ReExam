module com.dtu.roboclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jetbrains.annotations;


    opens com.dtu.roboclient to javafx.fxml;
    exports com.dtu.roboclient;
    exports com.dtu;
    opens com.dtu to javafx.fxml;
}