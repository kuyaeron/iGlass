package com.example.iGlass;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class Activity2 extends AppCompatActivity {
    private Button button;
    MediaPlayer ins;
    String indicator = "va";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.act2_toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ins = MediaPlayer.create(this, R.raw.instructions);
        ins.start();

        button = (Button) findViewById(R.id.visualAcuity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScreenFace();
                ins.stop();
            }
        });

        button = (Button) findViewById(R.id.back_visual);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backVisual();
                ins.stop();
            }
        });

    }

    public void openScreenFace(){
        Intent intent = new Intent (this, ScreenFaceDistance.class);
        Bundle b = new Bundle();
        b.putString("test", indicator);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void backVisual(){
        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent (this, MainActivity.class);
        ins.stop();
        startActivity(intent);
        //super.onBackPressed();  // optional depending on your needs
    }
}