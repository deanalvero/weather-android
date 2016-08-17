package com.deanalvero.weather;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.deanalvero.weather.adapter.WeatherRecyclerViewAdapter;
import com.deanalvero.weather.model.OWMData;
import com.deanalvero.weather.model.OWMDataMain;
import com.deanalvero.weather.tasks.APICallTask;
import com.deanalvero.weather.utils.OpenWeatherMapAPIHelper;
import com.deanalvero.weather.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private LocationManager mLocationManager;
    private static final long LOCATION_INTERVAL = 5000;
    private static final float LOCATION_DISTANCE = Float.MIN_VALUE;

    private Location mLocation;
    private OWMData mData;

    private Toolbar mToolbar;
    private CoordinatorLayout mCoordinatorLayout;

    private TextView mTextViewName,
            mTextViewSensorLatitude,
            mTextViewSensorLongitude,
            mTextViewSensorProvider,
            mTextViewMainTemp,
            mTextViewMainPressure,
            mTextViewMainHumidity,
            mTextViewMainTempMin,
            mTextViewMainTempMax,
            mTextViewMainSeaLevel,
            mTextViewMainGrndLevel;

    private RecyclerView mRecyclerView;
    private WeatherRecyclerViewAdapter mWeatherRecyclerViewAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private APICallTask mAPICallTask;

    private static final String KEY_LOCATION = "LOCATION";
    private static final String KEY_DATA = "DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupOnCreate();

        if (savedInstanceState != null){
            mLocation = savedInstanceState.getParcelable(MainActivity.KEY_LOCATION);
            mData = savedInstanceState.getParcelable(MainActivity.KEY_DATA);
        }

        if (mLocation == null || mData == null) {
            requestLocation();
        } else {
            refreshUI();
        }
    }

    private void setupOnCreate() {
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mTextViewName = (TextView) findViewById(R.id.textView_name);

        mTextViewSensorLatitude = (TextView) findViewById(R.id.textView_latitude_sensor);
        mTextViewSensorLongitude = (TextView) findViewById(R.id.textView_longitude_sensor);
        mTextViewSensorProvider = (TextView) findViewById(R.id.textView_provider_sensor);

        mTextViewMainTemp = (TextView) findViewById(R.id.textView_main_temp);
        mTextViewMainPressure = (TextView) findViewById(R.id.textView_main_pressure);
        mTextViewMainHumidity = (TextView) findViewById(R.id.textView_main_humidity);
        mTextViewMainTempMin = (TextView) findViewById(R.id.textView_main_temp_min);
        mTextViewMainTempMax = (TextView) findViewById(R.id.textView_main_temp_max);
        mTextViewMainSeaLevel = (TextView) findViewById(R.id.textView_main_sea_level);
        mTextViewMainGrndLevel = (TextView) findViewById(R.id.textView_main_grnd_level);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(getWeatherRecyclerViewAdapter());

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestLocation();
            }
        });

        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLocation();
            }
        });


        setSupportActionBar(mToolbar);

    }

    private WLocationListener[] mLocationListeners = new WLocationListener[]{
            new WLocationListener(LocationManager.GPS_PROVIDER),
            new WLocationListener(LocationManager.NETWORK_PROVIDER)
    };

    private void stopRefreshing(){
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);
    }

    private void startRefreshing(){
        if (mSwipeRefreshLayout != null && !mSwipeRefreshLayout.isRefreshing()){
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            });
        }
    }

    private void requestLocation() {
        startRefreshing();

        try {
            getLocationManager().requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListeners[0]);
        } catch (Exception e){
            Log.e(TAG, "e requestLocationUpdates 0");
            stopRefreshing();
        }

        try {
            getLocationManager().requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListeners[1]);
        } catch (Exception e){
            Log.e(TAG, "e requestLocationUpdates 1");
            stopRefreshing();
        }
    }

    private class WLocationListener implements LocationListener {

        private String mSource;

        public WLocationListener(String source){
            this.mSource = source;
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, mSource + " " + location.toString());

            removeLocationListeners();
            refreshLocationUI(location);
            executeGetWeatherByLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }

    private void executeGetWeatherByLocation(final Location location) {
        this.mAPICallTask = new APICallTask(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                startRefreshing();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                stopRefreshing();

                if (s == null){
                    showSnackbarMessage(R.string.api_error);
                }

                mData = OpenWeatherMapAPIHelper.getDataFromJSONString(s);
                MainActivity.this.mLocation = location;
                refreshUI();
            }
        };
        this.mAPICallTask.execute(OpenWeatherMapAPIHelper.getWeatherByLatLong(location));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(MainActivity.KEY_LOCATION, mLocation);
        outState.putParcelable(MainActivity.KEY_DATA, mData);
    }

    @Override
    protected void onDestroy() {
        removeLocationListeners();

        if (this.mAPICallTask != null){
            this.mAPICallTask.cancel(true);
        }
        super.onDestroy();
    }


    private void showSnackbarMessage(int stringId){
        if (this.mCoordinatorLayout != null) {
            Snackbar.make(this.mCoordinatorLayout, stringId, Snackbar.LENGTH_LONG).show();
        }
    }

    private void refreshLocationUI(Location location){
        setText(mTextViewSensorLatitude, getString(R.string.latitude_format, location.getLatitude()));
        setText(mTextViewSensorLongitude, getString(R.string.longitude_format, location.getLongitude()));
        setText(mTextViewSensorProvider, getString(R.string.provider_format, location.getProvider()));
    }

    private void refreshUI() {
        if (mLocation != null){
            refreshLocationUI(mLocation);
        }

        if (mData != null){
            setText(mTextViewName, mData.getName());

            OWMDataMain owmDataMain = mData.getMain();
            if (owmDataMain != null){
                double temp = owmDataMain.getTemp();
                double tempMin = owmDataMain.getTempMin();
                double tempMax = owmDataMain.getTempMax();

                setText(mTextViewMainTemp, getString(R.string.temp_format, temp, Utils.getDegreesCelciusFromKelvin(temp), Utils.getDegreesFahrenheitFromKelvin(temp)));
                setText(mTextViewMainPressure, getString(R.string.pressure_format, owmDataMain.getPressure()));
                setText(mTextViewMainHumidity, getString(R.string.humidity_format, owmDataMain.getHumidity()));
                setText(mTextViewMainTempMin, getString(R.string.temp_min_format, tempMin, Utils.getDegreesCelciusFromKelvin(tempMin), Utils.getDegreesFahrenheitFromKelvin(tempMin)));
                setText(mTextViewMainTempMax, getString(R.string.temp_max_format, tempMax, Utils.getDegreesCelciusFromKelvin(tempMax), Utils.getDegreesFahrenheitFromKelvin(tempMax)));
                setText(mTextViewMainSeaLevel, getString(R.string.sea_level_format, owmDataMain.getSeaLevel()));
                setText(mTextViewMainGrndLevel, getString(R.string.grnd_level_format, owmDataMain.getGrndLevel()));
            }

            getWeatherRecyclerViewAdapter().setItemList(mData.getWeatherList());
        }
    }



    private void setText(TextView textView, String text){
        if (textView != null){
            textView.setText(text);
        }
    }

    private WeatherRecyclerViewAdapter getWeatherRecyclerViewAdapter(){
        if (mWeatherRecyclerViewAdapter == null){
            mWeatherRecyclerViewAdapter = new WeatherRecyclerViewAdapter();
        }
        return mWeatherRecyclerViewAdapter;
    }

    private LocationManager getLocationManager(){
        if (mLocationManager == null){
            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        }
        return mLocationManager;
    }

    private void removeLocationListeners(){
        Log.e(TAG, "removeLocationListeners");
        if (getLocationManager() != null && mLocationListeners != null){
            int lengthLocationListeners = mLocationListeners.length;

            for (int i = 0; i < lengthLocationListeners; i++){
                try {
                    getLocationManager().removeUpdates(mLocationListeners[i]);
                } catch (Exception e){
                    Log.e(TAG, "e removeUpdates " + i);
                }
            }
        }
    }
}
