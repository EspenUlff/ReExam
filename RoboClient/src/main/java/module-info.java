module com.dtu.roboclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jetbrains.annotations;
    requires common;
    requires java.net.http;

    //opens com.dtu to javafx.fxml;
    exports com.dtu.roboclient.controller;
    exports com.dtu.roboclient.view;
    exports com.dtu.roboclient;

}