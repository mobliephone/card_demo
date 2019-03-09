package com.st.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017-11-24.
 */

public class GpsData implements Parcelable {

    private String position;
    private String placeName;


    public GpsData() {
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    protected GpsData(Parcel in) {
        position = in.readString();
        placeName = in.readString();
    }

    public static final Creator<GpsData> CREATOR = new Creator<GpsData>() {
        @Override
        public GpsData createFromParcel(Parcel in) {
            return new GpsData(in);
        }

        @Override
        public GpsData[] newArray(int size) {
            return new GpsData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(position);
        parcel.writeString(placeName);
    }
}
