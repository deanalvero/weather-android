package com.deanalvero.weather.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dean on 08/17/16.
 */
public class OWMDataMain implements Parcelable{

    private double mTemp;
    private double mPressure;
    private double mHumidity;
    private double mTempMin;
    private double mTempMax;
    private double mSeaLevel;
    private double mGrndLevel;

    public OWMDataMain(){

    }

    public void setTemp(double temp){ this.mTemp = temp; }
    public void setPressure(double pressure){ this.mPressure = pressure; }
    public void setHumidity(double humidity){ this.mHumidity = humidity; }
    public void setTempMin(double tempMin){ this.mTempMin = tempMin; }
    public void setTempMax(double tempMax){ this.mTempMax = tempMax; }
    public void setSeaLevel(double seaLevel){ this.mSeaLevel = seaLevel; }
    public void setGrndLevel(double grndLevel){ this.mGrndLevel = grndLevel; }

    public double getTemp(){ return this.mTemp; }
    public double getPressure(){ return this.mPressure; }
    public double getHumidity(){ return this.mHumidity; }
    public double getTempMin(){ return this.mTempMin; }
    public double getTempMax(){ return this.mTempMax; }
    public double getSeaLevel(){ return this.mSeaLevel; }
    public double getGrndLevel(){ return this.mGrndLevel; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mTemp);
        dest.writeDouble(mPressure);
        dest.writeDouble(mHumidity);
        dest.writeDouble(mTempMin);
        dest.writeDouble(mTempMax);
        dest.writeDouble(mSeaLevel);
        dest.writeDouble(mGrndLevel);
    }

    protected OWMDataMain(Parcel in) {
        mTemp = in.readDouble();
        mPressure = in.readDouble();
        mHumidity = in.readDouble();
        mTempMin = in.readDouble();
        mTempMax = in.readDouble();
        mSeaLevel = in.readDouble();
        mGrndLevel = in.readDouble();
    }

    public static final Creator<OWMDataMain> CREATOR = new Creator<OWMDataMain>() {
        @Override
        public OWMDataMain createFromParcel(Parcel in) {
            return new OWMDataMain(in);
        }

        @Override
        public OWMDataMain[] newArray(int size) {
            return new OWMDataMain[size];
        }
    };
}
