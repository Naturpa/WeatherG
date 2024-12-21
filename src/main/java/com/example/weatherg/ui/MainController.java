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
    // Поле для поиска города
    @FXML
    private TextField searchBar;
    // Контейнер для отображения текущей погоды
    @FXML
    private HBox currentRow;
    // Контейнер для отображения прогноза погоды
    @FXML
    private HBox forecastRow;

    // Список предложений для автозаполнения поиска
    private ArrayList<GeocodingDTO> suggestions;
    // Список предложений для автозаполнения поиска
    private WeatherDay current;
    // Объект для хранения прогноза погоды
    private WeatherBundle forecast;

    // Логгер для записи информации
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
     /*
     * Метод инициализации контроллера.
     * Выполняется после загрузки FXML-файла.
     */
    public void initialize() {
        // Логгируем начало инициализации
        logger.info("Инициализация главного экрана");
        // Настраиваем автозаполнение для поля поиска
        TextFields.bindAutoCompletion(searchBar, iSuggestionRequest -> {
            // Проверяем, что строка поиска не пуста
            if (!Objects.equals(iSuggestionRequest.getUserText(), "")) {
                try {
                    // Получаем список предложений для автозаполнения
                    suggestions = OpenWeatherService.getGeocoding(iSuggestionRequest.getUserText());
                    // Если список не пуст, преобразуем его в список строк и возвращаем
                    if (suggestions != null)
                        return suggestions.stream().map(GeocodingDTO::getFullName).collect(Collectors.toList());
                    // Если список пуст, возвращаем null
                    else return null;
                } catch (NetException e) {
                    // Логгируем ошибку, если не удалось получить геокодирование
                    e.printStackTrace();
                    return null;
                }
            } else return null;
        });
        // Создаем новый поток для загрузки сохраненного города и погоды
        new Thread(() -> {
            // Получаем название сохраненного города
            String savedCity = StorageService.getCity();
            // Обновляем поле поиска в UI-потоке
            Platform.runLater(() -> searchBar.setText(savedCity));
            // Если название города не null
            if (savedCity != null) {
                GeocodingDTO city = null;
                try {
                    // Получаем объект GeocodingDTO для сохраненного города
                    city = Objects.requireNonNull(OpenWeatherService.getGeocoding(savedCity.split(",")[0])).get(0);
                    // Получаем объекты WeatherDay и WeatherBundle для текущей погоды и прогноза
                    current = new WeatherDay(Objects.requireNonNull(OpenWeatherService.getCurrent(city.lat, city.lon)));
                    forecast = new WeatherBundle(Objects.requireNonNull(OpenWeatherService.getForecast(city.lat, city.lon)));
                    // Обновляем UI с полученными данными
                    update();
                } catch (NetException e) {
                    // Логгируем ошибку, если не удалось получить данные о погоде
                    e.printStackTrace();
                }
            }
            // Логгируем завершение инициализации
            logger.info("Инициализация главного экрана завершена");
        }).start();
    }

    /*
     * Метод для обновления UI с текущей погодой.
     * Выполняет обновление в UI потоке с помощью Platform.runLater.
     */
    private void updateCurrent() {
        // Используем Platform.runLater, чтобы обновить UI в основном потоке
        Platform.runLater(() -> {
            // Очищаем контейнер с предыдущими данными
            currentRow.getChildren().clear();

            // Создаем заголовок "Текущая:"
            Text currentLabel = new Text("Текущая:");
            currentLabel.setFont(new Font(20));

            // Формируем строку с текущей температурой
            String currentNow = "Температура: " + current.temp + "°C\n";
            // Формируем строку с ощущаемой температурой
            String currentFeelsLike = "Ощущается как: " + current.feelsLike + "°C\n";
            // Формируем строку с описанием осадков
            String currentFallout = "Осадки: " + current.weatherDescr + "\n";

            // Создаем текстовое поле с информацией о температуре
            Text tempText = new Text(currentNow + currentFeelsLike + currentFallout);
            tempText.setFont(new Font(16)); // Устанавливаем размер шрифта

            // Формируем строку с влажностью
            String currentHumidity = "Влажность: " + current.humidity + "%" + "\n";
            // Формируем строку с давлением
            String currentPressure = "Давление: " + current.pressure + " мм рт. ст." + "\n";

            // Создаем текстовое поле с информацией о влажности и давлении
            Text currentParams = new Text(currentHumidity + currentPressure);
            currentParams.setFont(new Font(16)); // Устанавливаем размер шрифта

            // Формируем строку со скоростью ветра
            String currentWindSpeed = "Скорость ветра: " + current.windSpeed + " м/c" + "\n";
            // Формируем строку с направлением ветра
            String currentWindDeg = "Направление ветра: " + current.windDeg + "\n";

            // Создаем текстовое поле с информацией о ветре
            Text windText = new Text(currentWindSpeed + currentWindDeg);
            windText.setFont(new Font(16)); // Устанавливаем размер шрифта


            // Создаем ImageView для отображения иконки погоды
            ImageView icon = new ImageView(current.weatherIcon);

            // Создаем HBox для размещения всех элементов текущей погоды
            HBox currentTile = new HBox(tempText, icon, currentParams, windText);
            currentTile.setSpacing(40); // Устанавливаем интервал между элементами
            currentTile.setPrefWidth(1050); // Устанавливаем ширину контейнера
            currentTile.setStyle("-fx-background-color:pink;-fx-background-radius:30");
            currentTile.setPadding(new Insets(20, 30, 20, 30)); // Устанавливаем отступы
            currentTile.setAlignment(Pos.CENTER); // Выравниваем элементы по центру

            // Добавляем созданный контейнер на экран
            currentRow.getChildren().add(currentTile);
        });
    }

     /*
     * Метод для обновления UI с прогнозом погоды.
     * Выполняет обновление в UI потоке с помощью Platform.runLater.
     */
    private void updateForecast() {
        // Используем Platform.runLater, чтобы обновить UI в основном потоке
        Platform.runLater(() -> {
            // Очищаем контейнер с предыдущими данными
            forecastRow.getChildren().clear();

            // Цикл для отрисовки прогноза на ближайшие 4 дня
            for (int i = 0; i < 4; i++) {
                // Создаем текстовое поле с датой прогноза
                Text forecastLabel = new Text(forecast.days.get(i).date);
                forecastLabel.setFont(new Font(18)); // Устанавливаем размер шрифта

                // Формируем строку с температурой прогноза
                String forecastNow = "Температура: " + forecast.days.get(i).temp + "°C\n";
                // Формируем строку с ощущаемой температурой прогноза
                String forecastFeelsLike = "По ощущениям: " + forecast.days.get(i).feelsLike + "°C\n";
                // Формируем строку с описанием осадков прогноза
                String forecastFallout = "Осадки: " + forecast.days.get(i).weatherDescr + "\n";

                // Создаем текстовое поле с информацией о погоде
                Text forecastWeather = new Text(forecastNow + forecastFeelsLike + forecastFallout);
                forecastWeather.setFont(new Font(14));

                // Создаем ImageView для отображения иконки погоды
                ImageView icon = new ImageView(forecast.days.get(i).weatherIcon);

                // Создаем VBox для размещения элементов прогноза на один день
                VBox forecastColumn = new VBox(forecastLabel, forecastWeather, icon);
                forecastColumn.setStyle("-fx-background-color:pink;-fx-background-radius:30"); // Устанавливаем стиль
                forecastColumn.setPadding(new Insets(20, 20, 0, 20)); // Устанавливаем отступы
                forecastColumn.setAlignment(Pos.CENTER); //Выравниваем элементы по центру
                forecastColumn.setMaxWidth(255); //Устанавливаем максимальную ширину
                forecastColumn.setPrefWidth(255); // Устанавливаем предпочтительную ширину

                // Добавляем созданный контейнер на экран
                forecastRow.getChildren().add(forecastColumn);
            }
        });
    }

     /*
     * Обновляет отображение текущей погоды и прогноза.
     */
    private void update() {
        // Обновляем UI с данными о текущей погоде
        updateCurrent();
        // Обновляем UI с данными о прогнозе погоды
        updateForecast();
        // Обновляем UI с данными о прогнозе погоды
        logger.info("Перерисовка главного экрана");
    }

     /*
     * Обработчик нажатия на кнопку поиска.
     * Выполняет поиск города и обновление UI.
     * @param actionEvent Событие нажатия кнопки
     */
    public void onSearchClicked(ActionEvent actionEvent) {
        // Логгируем нажатие на кнопку поиска
        logger.info("Пользователь нажал на поиск");
        // Создаем новый поток для поиска и обновления погоды
        new Thread(() -> {
            try {
                // Ищем выбранный город в списке предложений
                GeocodingDTO selected = suggestions.stream().filter(it -> Objects.equals(it.getFullName(), searchBar.getText())).toList().get(0);
                // Получаем данные о текущей погоде
                current = new WeatherDay(Objects.requireNonNull(OpenWeatherService.getCurrent(selected.lat, selected.lon)));
                // Получаем данные о прогнозе погоды
                forecast = new WeatherBundle(Objects.requireNonNull(OpenWeatherService.getForecast(selected.lat, selected.lon)));
                // Сохраняем название города
                StorageService.saveCity(searchBar.getText());
                // Обновляем UI с полученными данными
                update();
            } catch (Exception e) {
                // Логгируем сообщение об ошибке, если не удалось найти город
                logger.info("Пользователь забыл или не смог ввести город");
                // Выводим сообщение об ошибке в UI потоке
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
