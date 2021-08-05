package com.example.iGlass.data;

import android.database.Cursor;

import androidx.room.*;

import java.util.List;

@Dao
public interface ColorBlindnessResultDAO {
    @Query("SELECT * FROM ColorBlindnessResult")
    List<ColorBlindnessResult> getAll();

    @Insert
    void insertAll(ColorBlindnessResult... CB_results);

    @Delete
    void delete(ColorBlindnessResult CB_result);

}
