package com.nolankuza.theultimatealliance.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "assignments")
public class Assignment implements Parcelable {
    @PrimaryKey
    @NonNull
    public String name;
    public byte role;
    public boolean playoffs;
    @Ignore
    public int teamPortionAssignment;
    @Ignore
    public int totalPitScouters;
    public String student;

    public Assignment() {

    }

    //region Parcelable
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Assignment createFromParcel(Parcel in) {
            return new Assignment(in);
        }

        public Assignment[] newArray(int size) {
            return new Assignment[size];
        }
    };

    public Assignment(Parcel in) {
        name = in.readString();
        role = in.readByte();
        playoffs = in.readByte() == 1;
        teamPortionAssignment = in.readInt();
        totalPitScouters = in.readInt();
        student = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeByte(role);
        parcel.writeByte(playoffs ? (byte) 1 : 0);
        parcel.writeInt(teamPortionAssignment);
        parcel.writeInt(totalPitScouters);
        parcel.writeString(student);
    }
    //endregion
}
