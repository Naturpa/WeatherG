
package com.example.weatherg.transport.data;

import java.util.List;

import com.example.weatherg.transport.data.util.*;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javafx.scene.image.Image;

public class WeatherDTO {
    @SerializedName("weather")
    @Expose
    public List<Weather> weather;
    @SerializedName("main")
    @Expose
    public Main main;
    @SerializedName("wind")
    @Expose
    public Wind wind;
    @SerializedName("rain")
    @Expose
    public Rain rain;
    @SerializedName("clouds")
    @Expose
    public Clouds clouds;
    @SerializedName("dt")
    @Expose
    public Integer dt;

    @SerializedName("name")
    @Expose
    public String name;

}
