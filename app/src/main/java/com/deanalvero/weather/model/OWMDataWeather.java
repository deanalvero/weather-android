package com.deanalvero.weather.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dean on 08/17/16.
 */
public class OWMDataWeather implements Parcelable{

    private String mMain;
    private String mDescription;


    public OWMDataWeather(){

    }

    public void setMain(String main){ this.mMain = main; }
    public void setDescription(String description){ this.mDescription = description; }

    public String getMain(){ return this.mMain; }
    public String getDescription(){ return this.mDescription; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMain);
        dest.writeString(mDescription);
    }

    protected OWMDataWeather(Parcel in) {
        mMain = in.readString();
        mDescription = in.readString();
    }

    public static final Creator<OWMDataWeather> CREATOR = new Creator<OWMDataWeather>() {
        @Override
        public OWMDataWeather createFromParcel(Parcel in) {
            return new OWMDataWeather(in);
        }

        @Override
        public OWMDataWeather[] newArray(int size) {
            return new OWMDataWeather[size];
        }
    };
}
