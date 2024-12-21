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
    private static final Logger logger = LoggerFactory.getLogger(OpenWeatherService.class);
    private static final String BASE_URI = "https://api.openweathermap.org";
    private static final String CurrentEndpoint = "/data/2.5/weather?lat=%f&lon=%f&units=metric&lang=ru&appid=" + Env.API_KEY;
    private static final String ForecastEndpoint = "/data/2.5/forecast?lat=%f&lon=%f&units=metric&lang=ru&appid=" + Env.API_KEY;
    private static final String GeocodingEndpoint = "/geo/1.0/direct?q=%s,,RU&limit=5&appid=" + Env.API_KEY;

    public static ArrayList<GeocodingDTO> getGeocoding(String name) throws NetException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URI + String.format(GeocodingEndpoint, name)))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ArrayList<GeocodingDTO> result = new Gson().fromJson(response.body(), new TypeToken<ArrayList<GeocodingDTO>>(){}.getType());
            logger.info("Отправлен запрос: " + request.uri().toString());
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new NetException("Ошибка взаимодействия с api: проверьте logs/errors.log");
        }
    }

    public static WeatherDTO getCurrent(Double lat, Double lon) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URI + String.format(CurrentEndpoint, lat, lon)))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            WeatherDTO result = new Gson().fromJson(response.body(), WeatherDTO.class);
            logger.info("Отправлен запрос: " + request.uri().toString());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ForecastDTO getForecast(Double lat, Double lon) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URI + String.format(ForecastEndpoint, lat, lon)))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ForecastDTO result = new Gson().fromJson(response.body(), ForecastDTO.class);
            logger.info("Отправлен запрос: " + request.uri().toString());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

