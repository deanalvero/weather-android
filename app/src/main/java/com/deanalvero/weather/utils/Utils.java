package com.deanalvero.weather.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by dean on 08/17/16.
 */
public class Utils {

    public static String getStringFromInputStream(InputStream inputStream){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return stringBuilder.toString();
    }

    public static double getDegreesCelciusFromKelvin(double kelvin){
        return kelvin - 273.15f;
    }


    public static double getDegreesFahrenheitFromKelvin(double kelvin){
        return (kelvin - 273.15f) * 9 / 5 + 32;
    }
}
