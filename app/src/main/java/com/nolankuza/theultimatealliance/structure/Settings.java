package com.nolankuza.theultimatealliance.structure;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "settings")
public class Settings implements Parcelable {
    @PrimaryKey
    public int key = 1;
    public String eventKey = "";
    public boolean allowStudentsToChangeName;
    public boolean showAll;
    public boolean fieldReverse;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public byte[] passwordHash;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public byte[] passwordSalt;

    public Settings() {

    }

    //region Parcelable
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Settings createFromParcel(Parcel in) {
            return new Settings(in);
        }

        public Settings[] newArray(int size) {
            return new Settings[size];
        }
    };

    public Settings(Parcel in) {
        key = in.readInt();
        eventKey = in.readString();
        allowStudentsToChangeName = in.readByte() == 1;
        showAll = in.readByte() == 1;
        fieldReverse = in.readByte() == 1;
        passwordHash = readByteArray(in);
        passwordSalt = readByteArray(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(key);
        parcel.writeString(eventKey);
        parcel.writeByte(allowStudentsToChangeName ? (byte) 1 : 0);
        parcel.writeByte(showAll ? (byte) 1 : 0);
        parcel.writeByte(fieldReverse ? (byte) 1 : 0);
        writeByteArray(parcel, passwordHash);
        writeByteArray(parcel, passwordSalt);
    }

    private static void writeByteArray(Parcel parcel, byte[] byteArray) {
        parcel.writeInt(byteArray.length);
        parcel.writeByteArray(byteArray);
    }

    private static byte[] readByteArray(Parcel parcel) {
        byte[] byteArray = new byte[parcel.readInt()];
        parcel.readByteArray(byteArray);
        return byteArray;
    }
    //endregion
}
