package com.app.activeparks.data.storage.bd;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.app.activeparks.data.model.dictionaries.ExerciseCategory;

import java.util.List;

@Dao
public interface BdDao {

    @Query("SELECT * FROM exercisecategory")
    List<ExerciseCategory> getAll();


    @Insert
    void insert(ExerciseCategory employee);

    @Update
    void update(ExerciseCategory employee);

    @Delete
    void delete(ExerciseCategory employee);
}
