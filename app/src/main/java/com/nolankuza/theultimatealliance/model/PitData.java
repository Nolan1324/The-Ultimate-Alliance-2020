package com.nolankuza.theultimatealliance.model;

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

    public static class Data {
        public float length;
        public float width;
        public float height;
        public float weight;
        public float topSpeedFps;
        public String language;
        public String numWheels;
        public String wheelType;
        public String numSecondaryWheels;
        public String secondaryWheelType;
        public String driveTrainType;
        public String driveMotorType;
        public String numDriveMotors;
        public boolean startingPositionL;
        public boolean startingPositionC;
        public boolean startingPositionR;
        public boolean startingLevel1;
        public boolean startingLevel2;
        public boolean autoLeaveHab;
        public boolean autoShipL;
        public boolean autoShipC;
        public boolean autoShipR;
        public boolean autoRocketL;
        public boolean autoRocketR;
        public boolean retractFrame;
        public String defense;
        public boolean climbLevel1;
        public boolean climbLevel2;
        public boolean climbLevel3;
        public float climbTime;
        public String assistToLevel2;
        public String assistToLevel3;
        public boolean hatchFromStation;
        public boolean hatchFromFloor;
        public boolean hatchToShip;
        public boolean hatchToRocket1;
        public boolean hatchToRocket2;
        public boolean hatchToRocket3;
        public boolean hatchFromOpponentSide;
        public boolean cargoFromDepot;
        public boolean cargoFromStation;
        public boolean cargoFromFloor;
        public boolean cargoToShip;
        public boolean cargoToRocket1;
        public boolean cargoToRocket2;
        public boolean cargoToRocket3;
        public boolean cargoFromOpponentSide;
        public boolean hasCamera;
        public boolean hatchPreload;
        public boolean cargoPreload;
        public boolean willingToDefend;
        public float groundClearance;
        public String comments;
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
        data.length = in.readFloat();
        data.width = in.readFloat();
        data.height = in.readFloat();
        data.weight = in.readFloat();
        data.topSpeedFps = in.readFloat();
        data.language = in.readString();
        data.numWheels = in.readString();
        data.wheelType = in.readString();
        data.numSecondaryWheels = in.readString();
        data.secondaryWheelType = in.readString();
        data.driveTrainType = in.readString();
        data.driveMotorType = in.readString();
        data.numDriveMotors = in.readString();
        data.startingPositionL = toBoolean(in.readInt());
        data.startingPositionC = toBoolean(in.readInt());
        data.startingPositionR = toBoolean(in.readInt());
        data.startingLevel1 = toBoolean(in.readInt());
        data.startingLevel2 = toBoolean(in.readInt());
        data.autoLeaveHab = toBoolean(in.readInt());
        data.autoShipL = toBoolean(in.readInt());
        data.autoShipC = toBoolean(in.readInt());
        data.autoShipR = toBoolean(in.readInt());
        data.autoRocketL = toBoolean(in.readInt());
        data.autoRocketR = toBoolean(in.readInt());
        data.retractFrame = toBoolean(in.readInt());
        data.defense = in.readString();
        data.climbLevel1 = toBoolean(in.readInt());
        data.climbLevel2 = toBoolean(in.readInt());
        data.climbLevel3 = toBoolean(in.readInt());
        data.climbTime = in.readFloat();
        data.assistToLevel2 = in.readString();
        data.assistToLevel3 = in.readString();
        data.hatchFromStation = toBoolean(in.readInt());
        data.hatchFromFloor = toBoolean(in.readInt());
        data.hatchToShip = toBoolean(in.readInt());
        data.hatchToRocket1 = toBoolean(in.readInt());
        data.hatchToRocket2 = toBoolean(in.readInt());
        data.hatchToRocket3 = toBoolean(in.readInt());
        data.hatchFromOpponentSide = toBoolean(in.readInt());
        data.cargoFromDepot = toBoolean(in.readInt());
        data.cargoFromStation = toBoolean(in.readInt());
        data.cargoFromFloor = toBoolean(in.readInt());
        data.cargoToShip = toBoolean(in.readInt());
        data.cargoToRocket1 = toBoolean(in.readInt());
        data.cargoToRocket2 = toBoolean(in.readInt());
        data.cargoToRocket3 = toBoolean(in.readInt());
        data.cargoFromOpponentSide = toBoolean(in.readInt());
        data.hasCamera = toBoolean(in.readInt());
        data.hatchPreload = toBoolean(in.readInt());
        data.cargoPreload = toBoolean(in.readInt());
        data.willingToDefend = toBoolean(in.readInt());
        data.groundClearance = in.readFloat();
        data.comments = in.readString();
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
        parcel.writeFloat(data.length);
        parcel.writeFloat(data.width);
        parcel.writeFloat(data.height);
        parcel.writeFloat(data.weight);
        parcel.writeFloat(data.topSpeedFps);
        parcel.writeString(data.language);
        parcel.writeString(data.numWheels);
        parcel.writeString(data.wheelType);
        parcel.writeString(data.numSecondaryWheels);
        parcel.writeString(data.secondaryWheelType);
        parcel.writeString(data.driveTrainType);
        parcel.writeString(data.driveMotorType);
        parcel.writeString(data.numDriveMotors);
        parcel.writeInt(fromBoolean(data.startingPositionL));
        parcel.writeInt(fromBoolean(data.startingPositionC));
        parcel.writeInt(fromBoolean(data.startingPositionR));
        parcel.writeInt(fromBoolean(data.startingLevel1));
        parcel.writeInt(fromBoolean(data.startingLevel2));
        parcel.writeInt(fromBoolean(data.autoLeaveHab));
        parcel.writeInt(fromBoolean(data.autoShipL));
        parcel.writeInt(fromBoolean(data.autoShipC));
        parcel.writeInt(fromBoolean(data.autoShipR));
        parcel.writeInt(fromBoolean(data.autoRocketL));
        parcel.writeInt(fromBoolean(data.autoRocketR));
        parcel.writeInt(fromBoolean(data.retractFrame));
        parcel.writeString(data.defense);
        parcel.writeInt(fromBoolean(data.climbLevel1));
        parcel.writeInt(fromBoolean(data.climbLevel2));
        parcel.writeInt(fromBoolean(data.climbLevel3));
        parcel.writeFloat(data.climbTime);
        parcel.writeString(data.assistToLevel2);
        parcel.writeString(data.assistToLevel3);
        parcel.writeInt(fromBoolean(data.hatchFromStation));
        parcel.writeInt(fromBoolean(data.hatchFromFloor));
        parcel.writeInt(fromBoolean(data.hatchToShip));
        parcel.writeInt(fromBoolean(data.hatchToRocket1));
        parcel.writeInt(fromBoolean(data.hatchToRocket2));
        parcel.writeInt(fromBoolean(data.hatchToRocket3));
        parcel.writeInt(fromBoolean(data.hatchFromOpponentSide));
        parcel.writeInt(fromBoolean(data.cargoFromDepot));
        parcel.writeInt(fromBoolean(data.cargoFromStation));
        parcel.writeInt(fromBoolean(data.cargoFromFloor));
        parcel.writeInt(fromBoolean(data.cargoToShip));
        parcel.writeInt(fromBoolean(data.cargoToRocket1));
        parcel.writeInt(fromBoolean(data.cargoToRocket2));
        parcel.writeInt(fromBoolean(data.cargoToRocket3));
        parcel.writeInt(fromBoolean(data.cargoFromOpponentSide));
        parcel.writeInt(fromBoolean(data.hasCamera));
        parcel.writeInt(fromBoolean(data.hatchPreload));
        parcel.writeInt(fromBoolean(data.cargoPreload));
        parcel.writeInt(fromBoolean(data.willingToDefend));
        parcel.writeFloat(data.groundClearance);
        parcel.writeString(data.comments);
    }

    private boolean toBoolean(int value) {
        return value == 1;
    }

    private int fromBoolean(boolean value) {
        return value ? 1 : 0;
    }
    //endregion
}
