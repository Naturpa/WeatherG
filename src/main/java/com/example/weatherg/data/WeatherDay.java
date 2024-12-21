package com.example.weatherg.data;

import com.example.weatherg.transport.data.WeatherDTO;
import javafx.scene.image.Image;

public class WeatherDay {
    public String date;
    public int temp;
    public int feelsLike;
    public int humidity;
    public int pressure;
    public double windSpeed;
    public String windDeg;
    public int clouds;
    public String weatherDescr;
    public Image weatherIcon;


    public WeatherDay(WeatherDTO data) {
        temp = data.main.temp.intValue();
        feelsLike = data.main.feelsLike.intValue();
        humidity = data.main.humidity;
        pressure = data.main.pressure;
        windSpeed = data.wind.speed;
        var directions = new String[]{"↑ N", "↗ NE", "→ E", "↘ SE", "↓ S", "↙ SW", "← W", "↖ NW"};
        windDeg = directions[Math.round((float) data.wind.deg / 45) % 8];
        clouds = data.clouds.all;
        weatherDescr = data.weather.get(0).description;
        weatherIcon = new Image(String.format("http://openweathermap.org/img/wn/%s@2x.png", data.weather.get(0).icon));
    }

    public WeatherDay(String date, int temp, int feelsLike, int humidity, int pressure, double windSpeed, int windDeg, int clouds, String weatherDescr, String weatherIcon) {
        this.date = date;
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        var directions = new String[]{"↑ N", "↗ NE", "→ E", "↘ SE", "↓ S", "↙ SW", "← W", "↖ NW"};
        this.windDeg = directions[Math.round((float) windDeg / 45) % 8];
        this.clouds = clouds;
        this.weatherDescr = weatherDescr;
        this.weatherIcon = new Image(String.format("http://openweathermap.org/img/wn/%s@2x.png", weatherIcon));
    }

}
