package com.example.iGlass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TestResult2 extends AppCompatActivity {
    private Button button1;
    TextView result_cb, indicator;
    String finding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result2);

        result_cb = findViewById(R.id.result_cb);
        indicator = findViewById(R.id.indicator);

        if(getIntent().hasExtra("finding")){
            finding = getIntent().getStringExtra("finding");
            result_cb.setText(finding);
            indicator.setText("");
        }

        button1 = findViewById(R.id.back_visual);
        button1.setOnClickListener(new View.OnClickListener() {
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