package com.example.weatherg.ui;

import com.example.weatherg.data.WeatherBundle;
import com.example.weatherg.data.WeatherDay;
import com.example.weatherg.transport.NetException;
import com.example.weatherg.transport.OpenWeatherService;
import com.example.weatherg.transport.StorageService;
import com.example.weatherg.transport.data.GeocodingDTO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.controlsfx.control.textfield.TextFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;


public class MainController {

    @FXML
    private TextField searchBar;
    @FXML
    private HBox currentRow;
    @FXML
    private HBox forecastRow;

    private ArrayList<GeocodingDTO> suggestions;

    private WeatherDay current;
    private WeatherBundle forecast;

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    public void initialize() {
        logger.info("Инициализация главного экрана");
        TextFields.bindAutoCompletion(searchBar, iSuggestionRequest -> {
            if (!Objects.equals(iSuggestionRequest.getUserText(), "")) {
                try {
                    suggestions = OpenWeatherService.getGeocoding(iSuggestionRequest.getUserText());
                    if (suggestions != null)
                        return suggestions.stream().map(GeocodingDTO::getFullName).collect(Collectors.toList());
                    else return null;
                } catch (NetException e) {
                    e.printStackTrace();
                    return null;
                }
            } else return null;
        });
        new Thread(() -> {
            String savedCity = StorageService.getCity();
            Platform.runLater(() -> searchBar.setText(savedCity));
            if (savedCity != null) {
                GeocodingDTO city = null;
                try {
                    city = Objects.requireNonNull(OpenWeatherService.getGeocoding(savedCity.split(",")[0])).get(0);
                    current = new WeatherDay(Objects.requireNonNull(OpenWeatherService.getCurrent(city.lat, city.lon)));
                    forecast = new WeatherBundle(Objects.requireNonNull(OpenWeatherService.getForecast(city.lat, city.lon)));
                    update();
                } catch (NetException e) {
                    e.printStackTrace();
                }
            }
            logger.info("Инициализация главного экрана завершена");
        }).start();
    }

    private void updateCurrent() {
        Platform.runLater(() -> {
            currentRow.getChildren().clear();

            Text currentLabel = new Text("Текущая:");
            currentLabel.setFont(new Font(20));

            String currentNow = "Температура: " + current.temp + "°C\n";
            String currentFeelsLike = "Ощущается как: " + current.feelsLike + "°C\n";
            String currentFallout = "Осадки: " + current.weatherDescr + "\n";

            Text tempText = new Text(currentNow + currentFeelsLike + currentFallout);
            tempText.setFont(new Font(16));

            String currentHumidity = "Влажность: " + current.humidity + "%" + "\n";
            String currentPressure = "Давление: " + current.pressure + " мм рт. ст." + "\n";

            Text currentParams = new Text(currentHumidity + currentPressure);
            currentParams.setFont(new Font(16));

            String currentWindSpeed = "Скорость ветра: " + current.windSpeed + " м/c" + "\n";
            String currentWindDeg = "Направление ветра: " + current.windDeg + "\n";

            Text windText = new Text(currentWindSpeed + currentWindDeg);
            windText.setFont(new Font(16));


            ImageView icon = new ImageView(current.weatherIcon);

            HBox currentTile = new HBox(tempText, icon, currentParams, windText);
            currentTile.setSpacing(40);
            currentTile.setPrefWidth(1050);
            currentTile.setStyle("-fx-background-color:pink;-fx-background-radius:30");
            currentTile.setPadding(new Insets(20, 30, 20, 30));
            currentTile.setAlignment(Pos.CENTER);

            currentRow.getChildren().add(currentTile);
        });
    }


    private void updateForecast() {
        Platform.runLater(() -> {
            forecastRow.getChildren().clear();

            for (int i = 0; i < 4; i++) {
                Text forecastLabel = new Text(forecast.days.get(i).date);
                forecastLabel.setFont(new Font(18));

                String forecastNow = "Температура: " + forecast.days.get(i).temp + "°C\n";
                String forecastFeelsLike = "По ощущениям: " + forecast.days.get(i).feelsLike + "°C\n";
                String forecastFallout = "Осадки: " + forecast.days.get(i).weatherDescr + "\n";

                Text forecastWeather = new Text(forecastNow + forecastFeelsLike + forecastFallout);
                forecastWeather.setFont(new Font(14));

                ImageView icon = new ImageView(forecast.days.get(i).weatherIcon);

                VBox forecastColumn = new VBox(forecastLabel, forecastWeather, icon);
                forecastColumn.setStyle("-fx-background-color:pink;-fx-background-radius:30");
                forecastColumn.setPadding(new Insets(20, 20, 0, 20));
                forecastColumn.setAlignment(Pos.CENTER);
                forecastColumn.setMaxWidth(255);
                forecastColumn.setPrefWidth(255);

                forecastRow.getChildren().add(forecastColumn);
            }
        });
    }

    private void update() {
        updateCurrent();
        updateForecast();
        logger.info("Перерисовка главного экрана");
    }

    public void onSearchClicked(ActionEvent actionEvent) {
        logger.info("Пользователь нажал на поиск");
        new Thread(() -> {
            try {
                GeocodingDTO selected = suggestions.stream().filter(it -> Objects.equals(it.getFullName(), searchBar.getText())).toList().get(0);
                current = new WeatherDay(Objects.requireNonNull(OpenWeatherService.getCurrent(selected.lat, selected.lon)));
                forecast = new WeatherBundle(Objects.requireNonNull(OpenWeatherService.getForecast(selected.lat, selected.lon)));
                StorageService.saveCity(searchBar.getText());
                update();
            } catch (Exception e) {
                logger.info("Пользователь забыл или не смог ввести город");
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText(null);
                    alert.setContentText("Город не выбран или не найден");
                    alert.showAndWait();
                });
            }
        }).start();
    }
}
