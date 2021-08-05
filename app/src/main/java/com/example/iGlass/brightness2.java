package com.example.iGlass;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

public class brightness2 extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brightness2);
        askPermission(this);

        SeekBar seekBar = findViewById(R.id.seekbar);

        seekBar.setMax(255);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        button = (Button) findViewById(R.id.next2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAct3();
            }
        });
    }

    public void askPermission(Context c){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(Settings.System.canWrite(c)){
                //you have permission to write settings
            }
            else{
                Intent i = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                c.startActivity(i);
            }
        }
    }

    public void openAct3(){
        Intent intent = new Intent (this, Activity3.class);
        startActivity(intent);
    }
}
