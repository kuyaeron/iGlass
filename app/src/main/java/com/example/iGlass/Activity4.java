package com.example.iGlass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import com.example.iGlass.data.ColorBlindnessResult;

public class Activity4 extends AppCompatActivity {

    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.act4_toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        button = (Button) findViewById(R.id.cbResultListButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCBResultList();
            }
        });

        button = (Button) findViewById(R.id.vaResultListButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openVAResultList();
            }
        });
        //add();

    }

    public void openCBResultList(){
        Intent intent = new Intent (this, ColorBlindnessResultList.class);
        startActivity(intent);
    }

    public void openVAResultList(){
        Intent intent = new Intent (this, VisualAcuityResultList.class);
        startActivity(intent);
    }

    public void add(){
        iGlassDatabase db = iGlassDatabase.getDatabase(this.getApplicationContext());

        ColorBlindnessResult cbResult = new ColorBlindnessResult();
        cbResult.Finding = "PASSED";
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        cbResult.DateCompleted = df.format(date);

        db.colorBlindnessResultDAO().insertAll(cbResult);
    }
}
