package com.example.weatherg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main extends Application {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    @Override
    public void start(Stage stage) throws IOException {
        logger.info("Пользователь запустил приложение");
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("welcome-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1100, 650);
        stage.setResizable(false);
        stage.setTitle("Добро пожаловать!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}