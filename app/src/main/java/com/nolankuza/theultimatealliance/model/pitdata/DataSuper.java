package com.nolankuza.theultimatealliance.model.pitdata;

import android.os.Parcel;

abstract class DataSuper {
    public abstract void readFromParcel(Parcel in);

    public abstract void writeToParcel(Parcel parcel, int flags);

    static boolean toBoolean(int value) {
        return value == 1;
    }

    static int fromBoolean(boolean value) {
        return value ? 1 : 0;
    }
}