package com.nolankuza.theultimatealliance.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.nolankuza.theultimatealliance.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class Event implements Parcelable {
    public String key;
    public String name;
    @SerializedName("start_date")
    public Date startDate;
    public String city;
    @SerializedName("state_prov")
    public String stateProvince;
    public String country;

    public Event() {

    }

    //Display get methods
    public String getStartDateUS() {
        if(startDate != null) {
            SimpleDateFormat fmt = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            return fmt.format(startDate);
        } else {
            return "";
        }
    }

    public String getAddress() {
        /*
        Address address = new Address(Locale.US);
        address.setCountryCode(country);
        address.setAdminArea(stateProvince);
        address.setLocality(city);
        return address.toString();
        */
        return city + ", " + stateProvince + ", " + country;
    }

    //Internal get methods
    private String getStartDate() {
        if(startDate != null) {
            return Constants.EVENT_DATE_FMT.format(startDate);
        } else {
            return "";
        }
    }

    private void setStartDate(String startDate) {
        try {
            this.startDate = Constants.EVENT_DATE_FMT.parse(startDate);
        } catch (ParseException e) {
            //Do nothing
        }
    }

    //Parcelable logic (also has to do with internal get methods)
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public Event(Parcel in) {
        this.key = in.readString();
        this.name = in.readString();
        this.setStartDate(in.readString());
        this.city = in.readString();
        this.stateProvince = in.readString();
        this.country = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.getStartDate());
        dest.writeString(this.city);
        dest.writeString(this.stateProvince);
        dest.writeString(this.country);
    }

    //Utility method for sorting
    public static class Compare implements Comparator<Event> {
        @Override
        public int compare(Event o1, Event o2) {
            return o1.startDate.compareTo(o2.startDate);
        }
    }
}
