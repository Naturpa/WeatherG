package com.example.weatherg.data;

import com.example.weatherg.transport.data.ForecastDTO;
import com.example.weatherg.transport.data.WeatherDTO;
import com.example.weatherg.transport.data.util.Stamp;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Objects;

public class WeatherBundle {
    // Список объектов WeatherDay, представляющих прогноз погоды на несколько дней
    public ArrayList<WeatherDay> days;

    /*
     * Конструктор класса WeatherBundle, обрабатывает данные прогноза погоды.
     * @param data Объект ForecastDTO, содержащий данные о прогнозе погоды.
     */
    public WeatherBundle(ForecastDTO data) {

        // Инициализируем список для хранения объектов WeatherDay
        days = new ArrayList<>();
        // Создаем список для хранения отфильтрованных данных о погоде
        var actualStamps = new ArrayList<Stamp>();

        // Получаем дату первого дня прогноза
        var firstDay = data.list.get(0).dtTxt.split(" ")[0];
        // Получаем дату последнего дня прогноза
        var lastDay = data.list.get(data.list.size() - 1).dtTxt.split(" ")[0];

        // Фильтруем данные прогноза, исключая первый и последний дни
        for (var stamp : data.list) {
            // Если дата не равна первому и последнему дню, добавляем в список
            if (!Objects.equals(stamp.dtTxt.split(" ")[0], firstDay) && !Objects.equals(stamp.dtTxt.split(" ")[0], lastDay)) {
                actualStamps.add(stamp);
            }
        }

        // Обрабатываем данные для создания прогноза на 4 дня
        for (int i = 0; i < 4; i++) {
            // Получаем дату для текущего дня из списка
            String date = actualStamps.get(8 * i).dtTxt.split(" ")[0];
            // Объявляем переменные для хранения усредненных значений
            int temp = 0; // Температура
            int feelsLike = 0; // Ощущаемая температура
            int humidity = 0; // Влажность
            int pressure = 0; // Давление
            double windSpeed = 0; // Скорость ветра
            int windDeg = 0; // Направление ветра
            int clouds = 0; // Облачность
            // Получаем описание погоды
            String weatherDescr = actualStamps.get(8 * i + 3).weather.get(0).description;
            // Получаем иконку погоды
            String weatherIcon = actualStamps.get(8 * i + 3).weather.get(0).icon;

            // Усредняем значения погоды за 8 периодов
            for (int j = 8 * i; j < 8 * i + 7; j++) {
                temp += actualStamps.get(j).main.temp;
                feelsLike += actualStamps.get(j).main.feelsLike;
                humidity += actualStamps.get(j).main.humidity;
                pressure += actualStamps.get(j).main.pressure;
                windSpeed += actualStamps.get(j).wind.speed;
                windDeg += actualStamps.get(j).wind.deg;
                clouds += actualStamps.get(j).clouds.all;
            }
            // Усредняем полученные значения
            temp /= 8;
            feelsLike /= 8;
            humidity /= 8;
            pressure /= 8;
            windSpeed /= 8;
            windDeg /= 8;
            clouds /= 8;
            // Создаем объект WeatherDay и добавляем его в список
            days.add(new WeatherDay(date, temp, feelsLike, humidity, pressure, windSpeed, windDeg, clouds, weatherDescr, weatherIcon));
        }
    }
}
