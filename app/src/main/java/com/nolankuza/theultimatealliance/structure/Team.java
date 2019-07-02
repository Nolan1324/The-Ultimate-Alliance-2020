package com.nolankuza.theultimatealliance.structure;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

@Entity(tableName = "teams")
public class Team implements Parcelable {
    @PrimaryKey
    @NonNull
    public String key = "";
    public String nickname;
    @SerializedName("team_number")
    public int teamNumber;
    public String city;

    public Team() {

    }

    //Utility method for sorting
    public static class Compare implements Comparator<Team> {
        @Override
        public int compare(Team o1, Team o2) {
            return Integer.compare(o1.teamNumber, o2.teamNumber);
        }
    }

    //region Parcelable
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        public Team[] newArray(int size) {
            return new Team[size];
        }
    };

    public Team(Parcel in) {
        key = in.readString();
        nickname = in.readString();
        teamNumber = in.readInt();
        city = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(key);
        parcel.writeString(nickname);
        parcel.writeInt(teamNumber);
        parcel.writeString(city);
    }
    //endregion
}
