package com.nolankuza.theultimatealliance.model.gamedata;

import android.os.Parcel;

public class Data extends DataSuper {

    //region Auto
    public byte startPosition;
    public byte startLevel;
    public byte preload;
    public byte droveOff = 1;
    //endregion

    //region Auto Scoring
    public int autoHatchLoading;
    public int autoHatchFloor;
    public int autoCargoLoading;
    public int autoCargoFloor;
    public int autoHatchRocket3S;
    public int autoHatchRocket2S;
    public int autoHatchRocket1S;
    public int autoHatchRocket3F;
    public int autoHatchRocket2F;
    public int autoHatchRocket1F;
    public int autoCargoRocket3S;
    public int autoCargoRocket2S;
    public int autoCargoRocket1S;
    public int autoCargoRocket3F;
    public int autoCargoRocket2F;
    public int autoCargoRocket1F;
    public int autoHatchFumble;
    public int autoCargoFumble;
    public int autoHatchShipS;
    public int autoHatchShipF;
    public int autoCargoShipS;
    public int autoCargoShipF;

    //endregion

    //region Teleop
    public int hatchLoading;
    public int hatchFloor;
    public int cargoLoading;
    public int cargoFloor;
    public int hatchRocket3S;
    public int hatchRocket2S;
    public int hatchRocket1S;
    public int hatchRocket3F;
    public int hatchRocket2F;
    public int hatchRocket1F;
    public int cargoRocket3S;
    public int cargoRocket2S;
    public int cargoRocket1S;
    public int cargoRocket3F;
    public int cargoRocket2F;
    public int cargoRocket1F;
    public int hatchFumble;
    public int cargoFumble;
    public int hatchShipS;
    public int hatchShipF;
    public int cargoShipS;
    public int cargoShipF;
    //endregion

    //region End Game
    public boolean endLevel1S;
    public boolean endLevel1F;
    public boolean endLevel2S;
    public boolean endLevel2F;
    public int endLevel2Assisted;
    public boolean endLevel2WasAssisted;
    public boolean endLevel3S;
    public boolean endLevel3F;
    public int endLevel3Assisted;
    public boolean endLevel3WasAssisted;
    public boolean endLevel3FromLevel2;
    public boolean endLevel3SharedPlatform;
    public boolean endNoHabAttempt;

    public float climbTime;
    public boolean totallyWorking;
    public boolean partiallyWorking;
    public boolean noShow;
    public boolean beatToDeath;
    public boolean selfDied;
    public boolean fellOver;
    public boolean pushedOver;
    public boolean playedDefense;
    public String comments;
    //endregion
    
    @Override
    public void readFromParcel(Parcel in) {
        startPosition = in.readByte();
        startLevel = in.readByte();
        preload = in.readByte();
        droveOff = in.readByte();

        autoHatchLoading = in.readInt();
        autoHatchFloor = in.readInt();
        autoCargoLoading = in.readInt();
        autoCargoFloor = in.readInt();
        autoHatchRocket3S = in.readInt();
        autoHatchRocket2S = in.readInt();
        autoHatchRocket1S = in.readInt();
        autoHatchRocket3F = in.readInt();
        autoHatchRocket2F = in.readInt();
        autoHatchRocket1F = in.readInt();
        autoCargoRocket3S = in.readInt();
        autoCargoRocket2S = in.readInt();
        autoCargoRocket1S = in.readInt();
        autoCargoRocket3F = in.readInt();
        autoCargoRocket2F = in.readInt();
        autoCargoRocket1F = in.readInt();
        autoHatchFumble = in.readInt();
        autoCargoFumble = in.readInt();
        autoHatchShipS = in.readInt();
        autoHatchShipF = in.readInt();
        autoCargoShipS = in.readInt();
        autoCargoShipF = in.readInt();

        hatchLoading = in.readInt();
        hatchFloor = in.readInt();
        cargoLoading = in.readInt();
        cargoFloor = in.readInt();
        hatchRocket3S = in.readInt();
        hatchRocket2S = in.readInt();
        hatchRocket1S = in.readInt();
        hatchRocket3F = in.readInt();
        hatchRocket2F = in.readInt();
        hatchRocket1F = in.readInt();
        cargoRocket3S = in.readInt();
        cargoRocket2S = in.readInt();
        cargoRocket1S = in.readInt();
        cargoRocket3F = in.readInt();
        cargoRocket2F = in.readInt();
        cargoRocket1F = in.readInt();
        hatchFumble = in.readInt();
        cargoFumble = in.readInt();
        hatchShipS = in.readInt();
        hatchShipF = in.readInt();
        cargoShipS = in.readInt();
        cargoShipF = in.readInt();

        endLevel1S = toBoolean(in.readInt());
        endLevel1F = toBoolean(in.readInt());
        endLevel2S = toBoolean(in.readInt());
        endLevel2F = toBoolean(in.readInt());
        endLevel2Assisted = in.readInt();
        endLevel2WasAssisted = toBoolean(in.readInt());
        endLevel3S = toBoolean(in.readInt());
        endLevel3F = toBoolean(in.readInt());
        endLevel3Assisted = in.readInt();
        endLevel3WasAssisted = toBoolean(in.readInt());
        endLevel3FromLevel2 = toBoolean(in.readInt());
        endLevel3SharedPlatform = toBoolean(in.readInt());
        endNoHabAttempt = toBoolean(in.readInt());

        climbTime = in.readFloat();
        totallyWorking = toBoolean(in.readInt());
        partiallyWorking = toBoolean(in.readInt());
        noShow = toBoolean(in.readInt());
        beatToDeath = toBoolean(in.readInt());
        selfDied = toBoolean(in.readInt());
        fellOver = toBoolean(in.readInt());
        pushedOver = toBoolean(in.readInt());
        playedDefense = toBoolean(in.readInt());
        comments = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeByte(startPosition);
        parcel.writeByte(startLevel);
        parcel.writeByte(preload);
        parcel.writeByte(droveOff);

        parcel.writeInt(autoHatchLoading);
        parcel.writeInt(autoHatchFloor);
        parcel.writeInt(autoCargoLoading);
        parcel.writeInt(autoCargoFloor);
        parcel.writeInt(autoHatchRocket3S);
        parcel.writeInt(autoHatchRocket2S);
        parcel.writeInt(autoHatchRocket1S);
        parcel.writeInt(autoHatchRocket3F);
        parcel.writeInt(autoHatchRocket2F);
        parcel.writeInt(autoHatchRocket1F);
        parcel.writeInt(autoCargoRocket3S);
        parcel.writeInt(autoCargoRocket2S);
        parcel.writeInt(autoCargoRocket1S);
        parcel.writeInt(autoCargoRocket3F);
        parcel.writeInt(autoCargoRocket2F);
        parcel.writeInt(autoCargoRocket1F);
        parcel.writeInt(autoHatchFumble);
        parcel.writeInt(autoCargoFumble);
        parcel.writeInt(autoHatchShipS);
        parcel.writeInt(autoHatchShipF);
        parcel.writeInt(autoCargoShipS);
        parcel.writeInt(autoCargoShipF);

        parcel.writeInt(hatchLoading);
        parcel.writeInt(hatchFloor);
        parcel.writeInt(cargoLoading);
        parcel.writeInt(cargoFloor);
        parcel.writeInt(hatchRocket3S);
        parcel.writeInt(hatchRocket2S);
        parcel.writeInt(hatchRocket1S);
        parcel.writeInt(hatchRocket3F);
        parcel.writeInt(hatchRocket2F);
        parcel.writeInt(hatchRocket1F);
        parcel.writeInt(cargoRocket3S);
        parcel.writeInt(cargoRocket2S);
        parcel.writeInt(cargoRocket1S);
        parcel.writeInt(cargoRocket3F);
        parcel.writeInt(cargoRocket2F);
        parcel.writeInt(cargoRocket1F);
        parcel.writeInt(hatchFumble);
        parcel.writeInt(cargoFumble);
        parcel.writeInt(hatchShipS);
        parcel.writeInt(hatchShipF);
        parcel.writeInt(cargoShipS);
        parcel.writeInt(cargoShipF);

        parcel.writeInt(fromBoolean(endLevel1S));
        parcel.writeInt(fromBoolean(endLevel1F));
        parcel.writeInt(fromBoolean(endLevel2S));
        parcel.writeInt(fromBoolean(endLevel2F));
        parcel.writeInt(endLevel2Assisted);
        parcel.writeInt(fromBoolean(endLevel2WasAssisted));
        parcel.writeInt(fromBoolean(endLevel3S));
        parcel.writeInt(fromBoolean(endLevel3F));
        parcel.writeInt(endLevel3Assisted);
        parcel.writeInt(fromBoolean(endLevel3WasAssisted));
        parcel.writeInt(fromBoolean(endLevel3FromLevel2));
        parcel.writeInt(fromBoolean(endLevel3SharedPlatform));
        parcel.writeInt(fromBoolean(endNoHabAttempt));

        parcel.writeFloat(climbTime);
        parcel.writeInt(fromBoolean(totallyWorking));
        parcel.writeInt(fromBoolean(partiallyWorking));
        parcel.writeInt(fromBoolean(noShow));
        parcel.writeInt(fromBoolean(beatToDeath));
        parcel.writeInt(fromBoolean(selfDied));
        parcel.writeInt(fromBoolean(fellOver));
        parcel.writeInt(fromBoolean(pushedOver));
        parcel.writeInt(fromBoolean(playedDefense));
        parcel.writeString(comments);
    }
}
