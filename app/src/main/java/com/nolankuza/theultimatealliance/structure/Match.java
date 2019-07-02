package com.nolankuza.theultimatealliance.structure;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Entity(tableName = "matches")
public class Match implements Parcelable {
    @PrimaryKey
    @NonNull
    public String key = "key_missing";
    @SerializedName("match_number")
    public int matchNumber;
    public int time;

    public String red1;
    public String red2;
    public String red3;
    public String blue1;
    public String blue2;
    public String blue3;

    public boolean scouted = false;

    public Match() {

    }

    //region Alliance deserialization and flattening
    @Ignore
    private Alliances alliances;

    public static class Alliances {
        Blue blue;
        Red red;

        static class Blue {
            @SerializedName("team_keys")
            String[] teamKeys;
        }

        static class Red {
            @SerializedName("team_keys")
            String[] teamKeys;
        }
    }

    public void build() {
        String[] redTeams = alliances.red.teamKeys;
        String[] blueTeams = alliances.blue.teamKeys;

        red1 = trimTeamKey(redTeams[0]);
        red2 = trimTeamKey(redTeams[1]);
        red3 = trimTeamKey(redTeams[2]);

        blue1 = trimTeamKey(blueTeams[0]);
        blue2 = trimTeamKey(blueTeams[1]);
        blue3 = trimTeamKey(blueTeams[2]);
    }
    //endregion

    public String getTimeUS() {
        Date time = new Date(((long) this.time) * 1000);
        SimpleDateFormat fmt = new SimpleDateFormat("hh:mm:ss aa", Locale.US);
        fmt.setTimeZone(TimeZone.getTimeZone("EST"));
        return fmt.format(time);
    }

    //Utility method for sorting
    public static class Compare implements Comparator<Match> {
        @Override
        public int compare(Match o1, Match o2) {
            return Integer.compare(o1.matchNumber, o2.matchNumber);
        }
    }

    //Team key trimming helper method
    private static String trimTeamKey(String key) {
        return key.replace("frc", "");
    }

    //region Parcelable
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Match createFromParcel(Parcel in) {
            return new Match(in);
        }

        public Match[] newArray(int size) {
            return new Match[size];
        }
    };

    public Match(Parcel in) {
        key = in.readString();
        matchNumber = in.readInt();
        time = in.readInt();
        red1 = in.readString();
        red2 = in.readString();
        red3 = in.readString();
        blue1 = in.readString();
        blue2 = in.readString();
        blue3 = in.readString();
        scouted = in.readByte() == 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(key);
        parcel.writeInt(matchNumber);
        parcel.writeInt(time);
        parcel.writeString(red1);
        parcel.writeString(red2);
        parcel.writeString(red3);
        parcel.writeString(blue1);
        parcel.writeString(blue2);
        parcel.writeString(blue3);
        parcel.writeByte((byte) (scouted ? 1 : 0));
    }
    //endregion
}
