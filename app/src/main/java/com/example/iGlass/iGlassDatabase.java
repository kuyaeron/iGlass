package com.example.iGlass;
import android.content.Context;

import androidx.room.*;
import androidx.room.Database;

import com.example.iGlass.data.ColorBlindnessResult;
import com.example.iGlass.data.ColorBlindnessResultDAO;
import com.example.iGlass.data.Converters;
import com.example.iGlass.data.VisualAcuityResult;
import com.example.iGlass.data.VisualAcuityResultDAO;

@Database(entities = {VisualAcuityResult.class, ColorBlindnessResult.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class iGlassDatabase extends RoomDatabase {
    public abstract VisualAcuityResultDAO vaDao();
    public abstract ColorBlindnessResultDAO colorBlindnessResultDAO();

    private static com.example.iGlass.iGlassDatabase instance;

    static com.example.iGlass.iGlassDatabase getDatabase(Context context){
        if(instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(), com.example.iGlass.iGlassDatabase.class, "iGlassDatabase.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}