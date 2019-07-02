package com.nolankuza.theultimatealliance.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.nolankuza.theultimatealliance.structure.Student;

import java.util.List;

@Dao
public interface StudentDao {
    @Query("SELECT * FROM students ORDER BY firstName")
    List<Student> getAll();

    @Query("SELECT firstName || ' ' || lastName FROM students ORDER BY firstName")
    List<String> getNames();

    @Query("SELECT * FROM students WHERE lastName=:lastName")
    Student getByLastName(String lastName);

    @Query("SELECT COUNT(primaryKey) FROM students")
    int getStudentCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Student student);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Student> students);

    @Delete
    void delete(Student student);

    @Query("DELETE FROM students")
    void deleteAll();
}
