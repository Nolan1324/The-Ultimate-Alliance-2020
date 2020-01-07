package com.nolankuza.theultimatealliance.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.nolankuza.theultimatealliance.util.Prefs;

@Entity(tableName = "playoffdata", primaryKeys = {"index", "alliance", "driverStation"})
public class PlayoffData implements Parcelable {

    @NonNull
    public int index;
    @NonNull
    public Alliance alliance = Alliance.Red;
    public int driverStation;
    public int teamNumber;
    public String scouter = "";
    public int scouted;
    public boolean synced;

    @Embedded
    public Data data = new Data();

    public PlayoffData() {

    }

    public PlayoffData(int index) {
        this.index = index;
    }

    public String getKey() {
        return index + "_" + alliance + "_" + driverStation;
    }

    public String getRole() {
        return alliance.toString() + driverStation;
    }

    public static class Data {
        public byte preload;
        public byte startLevel;
        public byte endLevel;
        public int hatchS;
        public int hatchF;
        public int cargoS;
        public int cargoF;
    }

    public static PlayoffData fromMatch(Match match) {
        PlayoffData gameData = new PlayoffData();
        String team = "0000";
        switch(Prefs.getDriverStation("0")) {
            case "0":
                team = match.red1;
                gameData.alliance = Alliance.Red;
                gameData.driverStation = 1;
                break;
            case "1":
                team = match.red2;
                gameData.alliance = Alliance.Red;
                gameData.driverStation = 2;
                break;
            case "2":
                team = match.red3;
                gameData.alliance = Alliance.Red;
                gameData.driverStation = 3;
                break;
            case "3":
                team = match.blue1;
                gameData.alliance = Alliance.Blue;
                gameData.driverStation = 1;
                break;
            case "4":
                team = match.blue2;
                gameData.alliance = Alliance.Blue;
                gameData.driverStation = 2;
                break;
            case "5":
                team = match.blue3;
                gameData.alliance = Alliance.Blue;
                gameData.driverStation = 3;
                break;
        }
        gameData.teamNumber = Integer.parseInt(team);
        gameData.scouter = Prefs.getStudent("Anonymous");
        return gameData;
    }

    //region Parcelable
    public static final Creator CREATOR = new Creator() {
        public PlayoffData createFromParcel(Parcel in) {
            return new PlayoffData(in);
        }

        public PlayoffData[] newArray(int size) {
            return new PlayoffData[size];
        }
    };

    public PlayoffData(Parcel in) {
        index = in.readInt();
        alliance = in.readByte() == 0 ? Alliance.Red : Alliance.Blue;
        driverStation = in.readByte();
        teamNumber = in.readInt();
        scouter = in.readString();
        scouted = in.readInt();
        synced = in.readByte() == 1;

        data.preload = in.readByte();
        data.startLevel = in.readByte();
        data.endLevel = in.readByte();

        data.hatchS = in.readInt();
        data.hatchF = in.readInt();
        data.cargoS = in.readInt();
        data.cargoF = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(index);
        parcel.writeByte((byte) alliance.getCode());
        parcel.writeByte((byte) driverStation);
        parcel.writeInt(teamNumber);
        parcel.writeString(scouter);
        parcel.writeInt(scouted);
        parcel.writeByte(synced ? (byte) 1 : 0);

        parcel.writeByte(data.preload);
        parcel.writeByte(data.startLevel);
        parcel.writeByte(data.endLevel);

        parcel.writeInt(data.hatchS);
        parcel.writeInt(data.hatchF);
        parcel.writeInt(data.cargoS);
        parcel.writeInt(data.cargoF);
    }
    //endregion
}
