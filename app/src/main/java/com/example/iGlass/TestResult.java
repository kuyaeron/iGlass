package com.example.iGlass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TestResult extends AppCompatActivity {
    private Button button;
    TextView result_va, indicator, result_va1;
    String[] scoreDisplay = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);

        result_va = findViewById(R.id.result_va);
        result_va1 = findViewById(R.id.result_va1);
        indicator = findViewById(R.id.indicator);

        if(getIntent().hasExtra("scoreDisplay")){
            scoreDisplay = getIntent().getStringArrayExtra("scoreDisplay");
            result_va.setText(scoreDisplay[1]);
            result_va1.setText(scoreDisplay[0]);
            if (!scoreDisplay[0].equals("0.0") || !scoreDisplay[1].equals("0.0")) indicator.setText("Please see an optometrist to get your prescription glasses.");
            else indicator.setText("You have a healthy eyesight");
        }

        button = (Button) findViewById(R.id.back_visual);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backVisual();
            }
        });

    }

    public void backVisual(){
        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}