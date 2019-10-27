package com.nolankuza.theultimatealliance.room;

import android.arch.persistence.room.TypeConverter;

import com.nolankuza.theultimatealliance.model.Alliance;

public class Converters {
    @TypeConverter
    public Alliance fromCode(int value) {
        return value == 0 ? Alliance.Red : Alliance.Blue;
    }

    @TypeConverter
    public int toCode(Alliance alliance) {
        return alliance.getCode();
    }
}