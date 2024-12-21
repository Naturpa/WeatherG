
package com.example.weatherg.transport.data.util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class City {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("coord")
    @Expose
    public Coord coord;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("population")
    @Expose
    public Integer population;
    @SerializedName("timezone")
    @Expose
    public Integer timezone;
    @SerializedName("sunrise")
    @Expose
    public Integer sunrise;
    @SerializedName("sunset")
    @Expose
    public Integer sunset;

}
