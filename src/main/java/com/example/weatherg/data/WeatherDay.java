package com.example.weatherg.data;

import com.example.weatherg.transport.data.WeatherDTO;
import javafx.scene.image.Image;

public class WeatherDay {
    // Дата прогноза
    public String date;
    // Температура
    public int temp;
    // Ощущаемая температура
    public int feelsLike;
    // Влажность
    public int humidity;
    // Давление
    public int pressure;
    // Скорость ветра
    public double windSpeed;
    // Направление ветра (в виде текстового описания)
    public String windDeg;
    // Облачность
    public int clouds;
    // Описание погодных условий
    public String weatherDescr;
    // Иконка погоды
    public Image weatherIcon;


    /*
     * Конструктор класса WeatherDay, используемый для создания объекта на основе данных о текущей погоде.
     * @param data Объект WeatherDTO, содержащий данные о текущей погоде.
     */
    public WeatherDay(WeatherDTO data) {
        // Присваиваем температуру, преобразуя ее из Double в int
        temp = data.main.temp.intValue();
        // Присваиваем ощущаемую температуру, преобразуя ее из Double в int
        feelsLike = data.main.feelsLike.intValue();
        // Присваиваем влажность
        humidity = data.main.humidity;
        // Присваиваем давление
        pressure = data.main.pressure;
        // Присваиваем скорость ветра
        windSpeed = data.wind.speed;
        // Массив с текстовыми направлениями ветра
        var directions = new String[]{"↑ N", "↗ NE", "→ E", "↘ SE", "↓ S", "↙ SW", "← W", "↖ NW"};
        // Определяем направление ветра на основе градусов
        windDeg = directions[Math.round((float) data.wind.deg / 45) % 8];
        // Присваиваем облачность
        clouds = data.clouds.all;
        // Присваиваем описание погодных условий
        weatherDescr = data.weather.get(0).description;
        // Присваиваем иконку погоды, загружая ее из сети
        weatherIcon = new Image(String.format("http://openweathermap.org/img/wn/%s@2x.png", data.weather.get(0).icon));
    }


    /*
     * Конструктор класса WeatherDay, используемый для создания объекта на основе уже обработанных данных.
     * @param date Дата прогноза.
     * @param temp Температура.
     * @param feelsLike Ощущаемая температура.
     * @param humidity Влажность.
     * @param pressure Давление.
     * @param windSpeed Скорость ветра.
     * @param windDeg Направление ветра (в градусах).
     * @param clouds Облачность.
     * @param weatherDescr Описание погоды.
     * @param weatherIcon Идентификатор иконки погоды.
     */
    public WeatherDay(String date, int temp, int feelsLike, int humidity, int pressure, double windSpeed, int windDeg, int clouds, String weatherDescr, String weatherIcon) {
        this.date = date;
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        // Массив с текстовыми направлениями ветра
        var directions = new String[]{"↑ N", "↗ NE", "→ E", "↘ SE", "↓ S", "↙ SW", "← W", "↖ NW"};
        // Определяем направление ветра на основе градусов
        this.windDeg = directions[Math.round((float) windDeg / 45) % 8];
        this.clouds = clouds;
        this.weatherDescr = weatherDescr;
        // Присваиваем иконку погоды, загружая ее из сети
        this.weatherIcon = new Image(String.format("http://openweathermap.org/img/wn/%s@2x.png", weatherIcon));
    }

}
