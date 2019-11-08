package com.nolankuza.theultimatealliance.model.pitdata;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "pitdata", primaryKeys = {"eventKey", "teamNumber"})
public class PitData implements Parcelable {

    @NonNull
    public String eventKey;
    public int teamNumber;
    public String teamName;
    public String scouter;
    public boolean scouted = false;
    public boolean synced;

    @Embedded
    public Data data = new Data();

    public PitData() {

    }

    public String getKey() {
        return eventKey + "_" + teamNumber + "_" + scouter;
    }

    public static class Data extends com.nolankuza.theultimatealliance.model.pitdata.Data {

    }

    //region Parcelable
    public static final Creator CREATOR = new Creator() {
        public PitData createFromParcel(Parcel in) {
            return new PitData(in);
        }

        public PitData[] newArray(int size) {
            return new PitData[size];
        }
    };

    public PitData(Parcel in) {
        eventKey = in.readString();
        scouter = in.readString();
        teamNumber = in.readInt();
        teamName = in.readString();
        scouted = toBoolean(in.readInt());
        synced = toBoolean(in.readInt());
        data.readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(eventKey);
        parcel.writeString(scouter);
        parcel.writeInt(teamNumber);
        parcel.writeString(teamName);
        parcel.writeInt(fromBoolean(scouted));
        parcel.writeInt(fromBoolean(synced));
        data.writeToParcel(parcel, flags);
    }

    private boolean toBoolean(int value) {
        return value == 1;
    }

    private int fromBoolean(boolean value) {
        return value ? 1 : 0;
    }
    //endregion
}
