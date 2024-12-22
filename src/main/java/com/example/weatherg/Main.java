package com.example.weatherg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main extends Application {
    // Создаем логгер для записи информации о работе приложения
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

     /*
     * Метод start, который вызывается при запуске приложения.
     * @param stage Главное окно приложения.
     * @throws IOException Если произошла ошибка при загрузке FXML-файла.
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Логгируем информацию о запуске приложения
        logger.info("Пользователь запустил приложение");
        // Создаем FXMLLoader для загрузки FXML-файла
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("welcome-view.fxml"));
        // Загружаем FXML-файл и создаем сцену с заданными размерами
        Scene scene = new Scene(fxmlLoader.load(), 1100, 650);
        // Запрещаем изменение размера окна
        stage.setResizable(false);
        // Устанавливаем заголовок окна
        stage.setTitle("Добро пожаловать!");
        // Устанавливаем сцену в окно
        stage.setScene(scene);
        // Устанавливаем сцену в окно
        stage.show();
    }

     /*
     * Метод main, который является точкой входа в приложение.
     * @param args Аргументы командной строки.
     */
    public static void main(String[] args) {
        launch();
    }  // Запускаем приложение JavaFX
}