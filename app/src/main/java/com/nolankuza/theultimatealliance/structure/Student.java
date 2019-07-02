package com.nolankuza.theultimatealliance.structure;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Comparator;

@Entity(tableName = "students")
public class Student implements Parcelable {
    @NonNull
    @PrimaryKey
    public String primaryKey;
    public String lastName;
    public String firstName;
    public int grade;

    public Student(@NonNull String firstName, @NonNull String lastName, int grade) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.grade = grade;

        primaryKey = lastName + "_" + firstName;
    }

    public static class Compare implements Comparator<Student> {
        @Override
        public int compare(Student o1, Student o2) {
            return o1.firstName.compareTo(o2.firstName);
        }
    }

    //region Parcelable
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    public Student(Parcel in) {
        primaryKey = in.readString();
        lastName = in.readString();
        firstName = in.readString();
        grade = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(primaryKey);
        parcel.writeString(lastName);
        parcel.writeString(firstName);
        parcel.writeInt(grade);
    }
    //endregion
}
