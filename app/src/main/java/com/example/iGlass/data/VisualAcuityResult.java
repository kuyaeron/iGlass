package com.example.iGlass.data;

import androidx.room.*;

@Entity(tableName = "VisualAcuityResult")
public class VisualAcuityResult {
    @PrimaryKey(autoGenerate = true)
    public int VisualAcuityResultID;

    @ColumnInfo(name = "ScoreLeft")
    public double ScoreLeft;

    @ColumnInfo(name = "ScoreRight")
    public double ScoreRight;

    @ColumnInfo(name = "DateCompleted")
    public String DateCompleted;
}
