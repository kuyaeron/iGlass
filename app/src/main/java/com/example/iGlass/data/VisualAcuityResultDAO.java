package com.example.iGlass.data;

import android.database.Cursor;

import androidx.room.*;

import java.util.List;

@Dao
public interface VisualAcuityResultDAO {
    @Query("SELECT * FROM VisualAcuityResult")
    List<VisualAcuityResult> getAll();

    @Query("SELECT * FROM VisualAcuityResult WHERE VisualAcuityResultID IN (:userIds)")
    List<VisualAcuityResult> loadAllByIds(int[] userIds);

    /*@Query("SELECT DateCompleted FROM VisualAcuityResult")
    Cursor cursorDate();*/

    @Query("SELECT ScoreLeft FROM VisualAcuityResult")
    Cursor cursorScoreLeft();

    @Query("SELECT ScoreRight FROM VisualAcuityResult")
    Cursor cursorScoreRight();

    /*@Query("SELECT * FROM VisualAcuityResult WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    VisualAcuityResult findByName(String first, String last);*/

    @Insert
    void insertAll(VisualAcuityResult... VA_results);

    @Delete
    void delete(VisualAcuityResult VA_result);
}
