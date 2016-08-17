package com.deanalvero.weather.utils;

import android.location.Location;
import android.util.Log;

import com.deanalvero.weather.model.OWMData;
import com.deanalvero.weather.model.OWMDataMain;
import com.deanalvero.weather.model.OWMDataWeather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dean on 08/17/16.
 */
public class OpenWeatherMapAPIHelper {
    private static final String TAG = OpenWeatherMapAPIHelper.class.getSimpleName();

    public static String getWeatherByLatLong(Location location){
        //  api.openweathermap.org/data/2.5/weather?lat=35&lon=139

        if (location == null) return null;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Constant.BASE_URL_OPEN_WEATHER_MAP);
        stringBuilder.append("/data/2.5/weather?lat=");
        stringBuilder.append(location.getLatitude());
        stringBuilder.append("&lon=");
        stringBuilder.append(location.getLongitude());
        stringBuilder.append("&APPID=");
        stringBuilder.append(Constant.API_KEY_OPEN_WEATHER_MAP);

        return stringBuilder.toString();
    }

    public static OWMData getDataFromJSONString(String jSONString){
        if (jSONString == null) return null;


        OWMData owmData = new OWMData();

        try {
            JSONObject jsonObject = new JSONObject(jSONString);

            if (jsonObject == null) return null;
            if (jsonObject.getInt(OWMData.KEY_COD) != 200) return null;

            Log.e(TAG, jsonObject.toString());

            if (jsonObject.has(OWMData.KEY_WEATHER)){
                JSONArray jsonArrayWeather = jsonObject.getJSONArray(OWMData.KEY_WEATHER);

                if (jsonArrayWeather != null){

                    List<OWMDataWeather> weatherList = new ArrayList<>();

                    int weatherArrayLength = jsonArrayWeather.length();
                    for (int i = 0; i < weatherArrayLength; i++){
                        JSONObject jsonWeather = jsonArrayWeather.getJSONObject(i);

                        OWMDataWeather owmDataWeather = new OWMDataWeather();

                        if (jsonWeather.has(OWMData.KEY_WEATHER_MAIN)){
                            owmDataWeather.setMain(jsonWeather.getString(OWMData.KEY_WEATHER_MAIN));
                        }

                        if (jsonWeather.has(OWMData.KEY_WEATHER_DESCRIPTION)){
                            owmDataWeather.setDescription(jsonWeather.getString(OWMData.KEY_WEATHER_DESCRIPTION));
                        }

                        weatherList.add(owmDataWeather);

                    }

                    owmData.setWeatherList(weatherList);

                }
            }


            if (jsonObject.has(OWMData.KEY_MAIN)){
                JSONObject jsonMain = jsonObject.getJSONObject(OWMData.KEY_MAIN);

                OWMDataMain owmDataMain = new OWMDataMain();

                if (jsonMain != null){

                    if (jsonMain.has(OWMData.KEY_MAIN_TEMP)){
                        owmDataMain.setTemp(jsonMain.getDouble(OWMData.KEY_MAIN_TEMP));
                    }

                    if (jsonMain.has(OWMData.KEY_MAIN_PRESSURE)){
                        owmDataMain.setPressure(jsonMain.getDouble(OWMData.KEY_MAIN_PRESSURE));
                    }

                    if (jsonMain.has(OWMData.KEY_MAIN_HUMIDITY)){
                        owmDataMain.setHumidity(jsonMain.getDouble(OWMData.KEY_MAIN_HUMIDITY));
                    }

                    if (jsonMain.has(OWMData.KEY_MAIN_TEMP_MIN)){
                        owmDataMain.setTempMin(jsonMain.getDouble(OWMData.KEY_MAIN_TEMP_MIN));
                    }

                    if (jsonMain.has(OWMData.KEY_MAIN_TEMP_MAX)){
                        owmDataMain.setTempMax(jsonMain.getDouble(OWMData.KEY_MAIN_TEMP_MAX));
                    }


                    if (jsonMain.has(OWMData.KEY_MAIN_SEA_LEVEL)){
                        owmDataMain.setSeaLevel(jsonMain.getDouble(OWMData.KEY_MAIN_SEA_LEVEL));
                    }

                    if (jsonMain.has(OWMData.KEY_MAIN_GRND_LEVEL)){
                        owmDataMain.setGrndLevel(jsonMain.getDouble(OWMData.KEY_MAIN_GRND_LEVEL));
                    }
                }

                owmData.setMain(owmDataMain);
            }


            if (jsonObject.has(OWMData.KEY_NAME)){
                owmData.setName(jsonObject.getString(OWMData.KEY_NAME));
            }


            return owmData;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
