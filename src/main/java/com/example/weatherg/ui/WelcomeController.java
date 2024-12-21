package com.example.weatherg.ui;

import com.example.weatherg.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class WelcomeController {
    @FXML
    private Button Continue;

    private static final Logger logger = LoggerFactory.getLogger(WelcomeController.class);

    public void onContinueClicked(ActionEvent actionEvent) {
                logger.info("Пользователь переходит к главному экрану");
                Stage stage = (Stage) Continue.getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
                Scene scene;
                try {
                    scene = new Scene(fxmlLoader.load(), 1100, 650);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.setTitle("WeatherG");
                stage.setScene(scene);

        }
    }