package com.deanalvero.weather.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by dean on 08/17/16.
 */
public class OWMData implements Parcelable{
    //    http://openweathermap.org/current#current_JSON

    public static final String KEY_COD = "cod";
    public static final String KEY_WEATHER = "weather";
    public static final String KEY_WEATHER_MAIN = "main";
    public static final String KEY_WEATHER_DESCRIPTION = "description";
    public static final String KEY_MAIN = "main";
    public static final String KEY_MAIN_TEMP = "temp";
    public static final String KEY_MAIN_TEMP_MIN = "temp_min";
    public static final String KEY_MAIN_TEMP_MAX = "temp_max";
    public static final String KEY_MAIN_HUMIDITY = "humidity";
    public static final String KEY_MAIN_PRESSURE = "pressure";
    public static final String KEY_MAIN_SEA_LEVEL = "sea_level";
    public static final String KEY_MAIN_GRND_LEVEL = "grnd_level";
    public static final String KEY_NAME = "name";

    private String mName;
    private List<OWMDataWeather> mWeatherList;
    private OWMDataMain mMain;

    public OWMData(){

    }

    public void setName(String name){ this.mName = name; }
    public void setWeatherList(List<OWMDataWeather> weatherList){ this.mWeatherList = weatherList; }
    public void setMain(OWMDataMain main){ this.mMain = main; }

    public String getName(){ return this.mName; }
    public List<OWMDataWeather> getWeatherList(){ return this.mWeatherList; }
    public OWMDataMain getMain(){ return this.mMain; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeParcelable(mMain, flags);
        dest.writeList(mWeatherList);
    }

    protected OWMData(Parcel in) {
        mName = in.readString();
        mMain = in.readParcelable(OWMDataMain.class.getClassLoader());
        mWeatherList = in.createTypedArrayList(OWMDataWeather.CREATOR);
    }

    public static final Creator<OWMData> CREATOR = new Creator<OWMData>() {
        @Override
        public OWMData createFromParcel(Parcel in) {
            return new OWMData(in);
        }

        @Override
        public OWMData[] newArray(int size) {
            return new OWMData[size];
        }
    };
}
