package com.deanalvero.weather.tasks;

import android.os.AsyncTask;

import com.deanalvero.weather.utils.Utils;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dean on 08/17/16.
 */
public class APICallTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        if (strings == null || strings.length == 0) return null;

        try {
            URL url = new URL(strings[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            String response;
            try {
                response = Utils.getStringFromInputStream(httpURLConnection.getInputStream());
            } finally {
                httpURLConnection.disconnect();
            }

            return response;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}