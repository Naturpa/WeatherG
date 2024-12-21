module com.example.weatherg {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.google.gson;
    requires java.net.http;
    requires org.slf4j;

    opens com.example.weatherg to javafx.fxml;
    exports com.example.weatherg;
    exports com.example.weatherg.ui;
    opens com.example.weatherg.ui to javafx.fxml;
    opens com.example.weatherg.transport.data to com.google.gson;
    opens com.example.weatherg.transport.data.util to com.google.gson;
}