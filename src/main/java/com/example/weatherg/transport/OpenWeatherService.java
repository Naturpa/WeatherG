package com.example.weatherg.transport;

import com.example.weatherg.Env;
import com.example.weatherg.transport.data.WeatherDTO;
import com.example.weatherg.transport.data.ForecastDTO;
import com.example.weatherg.transport.data.GeocodingDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class OpenWeatherService {
    // Создаем логгер для записи информации о работе сервиса
    private static final Logger logger = LoggerFactory.getLogger(OpenWeatherService.class);
    // Базовый URI для API OpenWeatherMap
    private static final String BASE_URI = "https://api.openweathermap.org";
    // Эндпоинт для получения текущей погоды
    // %f используются для подстановки значений широты и долготы
    private static final String CurrentEndpoint = "/data/2.5/weather?lat=%f&lon=%f&units=metric&lang=ru&appid=" + Env.API_KEY;
    // Эндпоинт для получения прогноза погоды
    // %f используются для подстановки значений широты и долготы
    private static final String ForecastEndpoint = "/data/2.5/forecast?lat=%f&lon=%f&units=metric&lang=ru&appid=" + Env.API_KEY;
    // Эндпоинт для геокодирования (получения координат по названию города)
    // %s используется для подстановки названия города
    private static final String GeocodingEndpoint = "/geo/1.0/direct?q=%s,,RU&limit=5&appid=" + Env.API_KEY;
    /*
    * Получает список GeocodingDTO (объектов с информацией о координатах) по названию города
     * @param name Название города
     * @return ArrayList<GeocodingDTO> Список объектов с информацией о координатах
     * @throws NetException Если произошла ошибка при взаимодействии с API
     */
    public static ArrayList<GeocodingDTO> getGeocoding(String name) throws NetException {
        try {
            // Создаем HTTP клиент
            HttpClient client = HttpClient.newHttpClient();
            // Создаем HTTP запрос
            HttpRequest request = HttpRequest.newBuilder()
                    // Формируем URI для запроса, подставляя название города
                    .uri(URI.create(BASE_URI + String.format(GeocodingEndpoint, name)))
                    .GET()
                    .build();
            // Отправляем запрос и получаем ответ в виде строки
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // Преобразуем JSON из тела ответа в список объектов GeocodingDTO
            ArrayList<GeocodingDTO> result = new Gson().fromJson(response.body(), new TypeToken<ArrayList<GeocodingDTO>>(){}.getType());
            // Логгируем информацию об отправленном запросе
            logger.info("Отправлен запрос: " + request.uri().toString());
            // Возвращаем результат
            return result;
        } catch (Exception e) {
            // Логгируем сообщение об ошибке
            logger.error(e.getMessage());
            // Выбрасываем исключение NetException, которое сигнализирует о проблеме
            throw new NetException("Ошибка взаимодействия с api: проверьте logs/errors.log");
        }
    }

    /*
     * Получает объект WeatherDTO (объект с информацией о текущей погоде) по координатам
     * @param lat  Широта
     * @param lon  Долгота
     * @return WeatherDTO Объект с информацией о текущей погоде, null если произошла ошибка
     */
    public static WeatherDTO getCurrent(Double lat, Double lon) {
        try {
            // Создаем HTTP клиент
            HttpClient client = HttpClient.newHttpClient();
            // Создаем HTTP запрос
            HttpRequest request = HttpRequest.newBuilder()
                    // Формируем URI для запроса, подставляя широту и долготу
                    .uri(URI.create(BASE_URI + String.format(CurrentEndpoint, lat, lon)))
                    .GET() // Указываем тип запроса - GET
                    .build();
            // Отправляем запрос и получаем ответ в виде строки
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // Преобразуем JSON из тела ответа в объект WeatherDTO
            WeatherDTO result = new Gson().fromJson(response.body(), WeatherDTO.class);
            // Логгируем информацию об отправленном запросе
            logger.info("Отправлен запрос: " + request.uri().toString());
            return result;
        } catch (Exception e) {
            // Выводим сообщение об ошибке в консоль
            e.printStackTrace();
            // Возвращаем null, если произошла ошибка
            return null;
        }
    }
    /*
     * Получает объект ForecastDTO (объект с информацией о прогнозе погоды) по координатам
     * @param lat  Широта
     * @param lon  Долгота
     * @return ForecastDTO Объект с информацией о прогнозе погоды, null если произошла ошибка
     */
    public static ForecastDTO getForecast(Double lat, Double lon) {
        try {
            // Создаем HTTP клиент
            HttpClient client = HttpClient.newHttpClient();
            // Создаем HTTP запрос
            HttpRequest request = HttpRequest.newBuilder()
                    // Формируем URI для запроса, подставляя широту и долготу
                    .uri(URI.create(BASE_URI + String.format(ForecastEndpoint, lat, lon)))
                    .GET() // Указываем тип запроса - GET
                    .build();
            // Отправляем запрос и получаем ответ в виде строки
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // Преобразуем JSON из тела ответа в объект ForecastDTO
            ForecastDTO result = new Gson().fromJson(response.body(), ForecastDTO.class);
            logger.info("Отправлен запрос: " + request.uri().toString());
            return result;
        } catch (Exception e) {
            // Выводим сообщение об ошибке в консоль
            e.printStackTrace();
            // Возвращаем null, если произошла ошибка
            return null;
        }
    }

}

//Библиотека Gson используется для преобразования JSON в Java-объекты и обратно
