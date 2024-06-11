module com.example.splitza {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;


    opens com.example.splitza to javafx.fxml;
    opens com.example.splitza.model to javafx.base;
    opens com.example.splitza.controller to javafx.fxml;
    exports com.example.splitza;
}