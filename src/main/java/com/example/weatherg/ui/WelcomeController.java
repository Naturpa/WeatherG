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
    // Поле, связанное с кнопкой "Continue" в FXML
    @FXML
    private Button Continue;
    // Создаем логгер для записи информации о работе контроллера
    private static final Logger logger = LoggerFactory.getLogger(WelcomeController.class);

    /*
     * Обработчик нажатия на кнопку "Continue".
     * Выполняет переход к главному экрану.
     * @param actionEvent Событие нажатия кнопки
     */
    public void onContinueClicked(ActionEvent actionEvent) {
        // Логгируем информацию о переходе пользователя на главный экран
                logger.info("Пользователь переходит к главному экрану");
        // Получаем текущее окно (Stage) из кнопки "Continue"
                Stage stage = (Stage) Continue.getScene().getWindow();
        // Создаем FXMLLoader для загрузки FXML-файла главного экрана
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
                Scene scene;
                try {
                    // Загружаем FXML-файл главного экрана и создаем сцену
                    scene = new Scene(fxmlLoader.load(), 1100, 650);
                    // Выбрасываем RuntimeException если произошла ошибка загрузки
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        // Устанавливаем заголовок окна
                stage.setTitle("WeatherG");
        // Устанавливаем сцену в окно
                stage.setScene(scene);

        }
    }