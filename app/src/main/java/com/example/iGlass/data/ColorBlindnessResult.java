package com.example.iGlass.data;

import androidx.room.*;

@Entity(tableName = "ColorBlindnessResult")
public class ColorBlindnessResult {
    @PrimaryKey(autoGenerate = true)
    public int ColorBlindnessResultID;

    @ColumnInfo(name = "Finding")
    public String Finding;

    @ColumnInfo(name = "DateCompleted")
    public String DateCompleted;
}
