package com.example.iGlass;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iGlass.data.ColorBlindnessResult;

import java.util.List;
import java.util.Objects;

import com.example.iGlass.data.ColorBlindnessResult;

public class ColorBlindnessResultList extends AppCompatActivity {

    private ColorBlindnessResultListAdapter cbListAdapter;
    Boolean emptyDB = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_blindness_result_list);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.cb_result_history_toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        listCheck();
        initRecyclerView();
        loadColorBlindnessList();
    }

    public void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        //recyclerView.addItemDecoration(dividerItemDecoration);
        TextView header = findViewById(R.id.cb_heading);
        if (emptyDB) header.setText("Result history is empty. Please take the assessment to find out if you are colorblind.");
        else header.setText("Color blindness result history");

        cbListAdapter = new ColorBlindnessResultListAdapter(this);
        recyclerView.setAdapter(cbListAdapter);
    }

    public void loadColorBlindnessList(){
        iGlassDatabase db = iGlassDatabase.getDatabase(this.getApplicationContext());
        List<ColorBlindnessResult> cb_list = db.colorBlindnessResultDAO().getAll();
        cbListAdapter.setCB_List(cb_list);
    }
    public void listCheck(){
        iGlassDatabase db = iGlassDatabase.getDatabase(this.getApplicationContext());
        List<ColorBlindnessResult> cb_list = db.colorBlindnessResultDAO().getAll();

        if (cb_list.size()!=0){
            emptyDB = false;
        }
    }
}