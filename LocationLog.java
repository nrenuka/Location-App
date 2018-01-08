package com.example.android.locationreached;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by renuka on 4/29/17.
 */

public class LocationLog implements Parcelable{
    public Double latitude;
    public Double longitude;
    public String requesteeKey;
    public String requesterKey;
    public String requesteeUN;
    public String requesterUN;
    public String status;
    public String key;

    public LocationLog() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public LocationLog(Double latitude, Double longitude, String requesteeKey, String requesterKey, String requesteeUN, String requesterUN, String key) {
        this.key = key;
        this.latitude = latitude;
        this.longitude = longitude;
        this.requesteeKey = requesteeKey;
        this.requesterKey = requesterKey;
        this.requesteeUN = requesteeUN;
        this.requesterUN = requesterUN;
        this.status = "pending";
    }


    /**+
     * didn't work out - null reference
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.requesteeKey);
        dest.writeString(this.requesterKey);
        dest.writeString(this.requesteeUN);
        dest.writeString(this.requesterKey);
        dest.writeString(this.status);
    }

    protected LocationLog(Parcel in) {
        this.key = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.requesteeKey = in.readString();
        this.requesterKey = in.readString();
        this.requesteeUN = in.readString();
        this.requesterUN = in.readString();
        this.requesteeUN = in.readString();
    }

    public static final Parcelable.Creator<LocationLog> CREATOR = new Parcelable.Creator<LocationLog>() {
        @Override
        public LocationLog createFromParcel(Parcel source) {
            return new LocationLog(source);
        }

        @Override
        public LocationLog[] newArray(int size) {
            return new LocationLog[size];
        }
    };

}
