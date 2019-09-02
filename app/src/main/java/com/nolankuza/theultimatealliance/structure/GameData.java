package com.nolankuza.theultimatealliance.structure;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import static com.nolankuza.theultimatealliance.ApplicationState.prefs;

@Entity(tableName = "gamedata", primaryKeys = {"matchKey", "alliance", "driverStation"})
public class GameData implements Parcelable {

    @NonNull
    public String matchKey;
    public int matchNumber;
    @NonNull
    public Alliance alliance;
    public int driverStation;
    public int teamNumber;
    public String scouter = "";
    public int scouted;
    public boolean synced;

    @Embedded
    public Data data = new Data();

    public GameData() {

    }

    public String getKey() {
        return matchKey + "_" + alliance + "_" + driverStation;
    }

    public String getRole() {
        return alliance.toString() + driverStation;
    }

    public static class Data extends com.nolankuza.theultimatealliance.structure.gamedata.Data {
    }

    public static GameData fromMatch(Match match) {
        GameData gameData = new GameData();
        gameData.matchKey = match.key;
        gameData.matchNumber = match.matchNumber;
        String team = "0000";
        switch(prefs.getString("driver_pref", "0")) {
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
        gameData.scouter = prefs.getString("student_pref", "Anonymous");
        return gameData;
    }

    //region Parcelable
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public GameData createFromParcel(Parcel in) {
            return new GameData(in);
        }

        public GameData[] newArray(int size) {
            return new GameData[size];
        }
    };

    public GameData(Parcel in) {
        matchKey = in.readString();
        matchNumber = in.readInt();
        alliance = in.readByte() == 0 ? Alliance.Red : Alliance.Blue;
        driverStation = in.readByte();
        teamNumber = in.readInt();
        scouter = in.readString();
        scouted = in.readInt();
        synced = in.readByte() == 1;
        data.readFromParcel(in);

        /*
        data.testInt = in.readInt();
        data.testBoolean = in.readByte() == 1;
        data.testString = in.readString();
        data.testFloat = in.readFloat();
        */

        /*
        for(Field f : Data.class.getFields()) {
            if(!fieldIsMeta(f.getName())) {
                try {
                    Class type = f.getType();
                    if (type.equals(int.class)) {
                        f.setInt(data, in.readByte());
                    } else if (type.equals(boolean.class)) {
                        f.setBoolean(data, in.readByte() == 1);
                    } else if (type.equals(String.class)) {
                        f.set(data, in.readString());
                    } else if (type.equals(float.class)) {
                        f.setFloat(data, in.readFloat());
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        */
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(matchKey);
        parcel.writeInt(matchNumber);
        parcel.writeByte((byte) alliance.getCode());
        parcel.writeByte((byte) driverStation);
        parcel.writeInt(teamNumber);
        parcel.writeString(scouter);
        parcel.writeInt(scouted);
        parcel.writeByte(synced ? (byte) 1 : 0);
        data.writeToParcel(parcel, flags);

        /*
        parcel.writeInt(data.testInt);
        parcel.writeByte(data.testBoolean ? (byte) 1 : 0);
        parcel.writeString(data.testString);
        parcel.writeFloat(data.testFloat);
        */

        /*
        for(Field f : Data.class.getFields()) {
            if(!fieldIsMeta(f.getName())) {
                try {
                    Class type = f.getType();
                    if (type.equals(int.class)) {
                        parcel.writeByte((byte) f.getInt(data));
                    } else if (type.equals(boolean.class)) {
                        parcel.writeByte(f.getBoolean(data) ? (byte) 1 : (byte) 0);
                    } else if (type.equals(String.class)) {
                        parcel.writeString((String) f.get(data));
                    } else if (type.equals(float.class)) {
                        parcel.writeFloat(f.getFloat(data));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        */
    }
    //endregion
}
