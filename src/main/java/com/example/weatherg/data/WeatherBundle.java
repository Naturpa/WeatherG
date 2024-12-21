package com.example.weatherg.data;

import com.example.weatherg.transport.data.ForecastDTO;
import com.example.weatherg.transport.data.WeatherDTO;
import com.example.weatherg.transport.data.util.Stamp;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Objects;

public class WeatherBundle {
    public ArrayList<WeatherDay> days;

    public WeatherBundle(ForecastDTO data) {

        days = new ArrayList<>();
        var actualStamps = new ArrayList<Stamp>();

        var firstDay = data.list.get(0).dtTxt.split(" ")[0];
        var lastDay = data.list.get(data.list.size() - 1).dtTxt.split(" ")[0];

        for (var stamp : data.list) {
            if (!Objects.equals(stamp.dtTxt.split(" ")[0], firstDay) && !Objects.equals(stamp.dtTxt.split(" ")[0], lastDay)) {
                actualStamps.add(stamp);
            }
        }

        for (int i = 0; i < 4; i++) {
            String date = actualStamps.get(8 * i).dtTxt.split(" ")[0];
            int temp = 0;
            int feelsLike = 0;
            int humidity = 0;
            int pressure = 0;
            double windSpeed = 0;
            int windDeg = 0;
            int clouds = 0;
            String weatherDescr = actualStamps.get(8 * i + 3).weather.get(0).description;
            String weatherIcon = actualStamps.get(8 * i + 3).weather.get(0).icon;
            for (int j = 8 * i; j < 8 * i + 7; j++) {
                temp += actualStamps.get(j).main.temp;
                feelsLike += actualStamps.get(j).main.feelsLike;
                humidity += actualStamps.get(j).main.humidity;
                pressure += actualStamps.get(j).main.pressure;
                windSpeed += actualStamps.get(j).wind.speed;
                windDeg += actualStamps.get(j).wind.deg;
                clouds += actualStamps.get(j).clouds.all;
            }
            temp /= 8;
            feelsLike /= 8;
            humidity /= 8;
            pressure /= 8;
            windSpeed /= 8;
            windDeg /= 8;
            clouds /= 8;
            days.add(new WeatherDay(date, temp, feelsLike, humidity, pressure, windSpeed, windDeg, clouds, weatherDescr, weatherIcon));
        }
    }
}
