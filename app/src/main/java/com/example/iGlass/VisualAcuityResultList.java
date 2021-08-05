package com.example.iGlass;

import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.example.iGlass.data.VisualAcuityResult;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

public class VisualAcuityResultList extends AppCompatActivity {

    private VisualAcuityResultListAdapter vaListAdapter;
    Boolean emptyDB = true;
    LineChart va_chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_acuity_result_list);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.va_result_history_toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        va_chart = findViewById(R.id.va_chart);
        va_chart.setTouchEnabled(true);
        va_chart.setPinchZoom(false);
        va_chart.setBackgroundColor(Color.WHITE);
        va_chart.setNoDataText("No data");
        va_chart.setDrawGridBackground(false);
        va_chart.getAxisRight().setEnabled(false);

        Description desc = new Description();
        desc.setText("VA Results");
        va_chart.setDescription(desc);

        /*ArrayList<String> xVals = new ArrayList<>();
        try{
            for(int i = 0; i < queryXData().size(); i++) {
                xVals.add(queryXData().get(i));
            }
        }
        catch (Exception e){
            //
        }*/


        XAxis xAxis = va_chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
        xAxis.setGranularity(1f);
        xAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(false);
        YAxis yAxis = va_chart.getAxisLeft();
        yAxis.setGranularity(1f);
        yAxis.setDrawLabels(false);
        yAxis.setDrawGridLines(false);
        setData();

        va_chart.setVisibleXRangeMaximum(4);

        listCheck();
        initRecyclerView();
        loadVisualAcuityList();
    }

    /*public ArrayList<String> queryXData() throws ParseException {
        iGlassDatabase db = iGlassDatabase.getDatabase(this.getApplicationContext());
        ArrayList<String> xNewData = new ArrayList<>();
        Cursor cursor = db.vaDao().cursorDate();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String d = cursor.getString(cursor.getColumnIndex("DateCompleted"));
            Date date = new SimpleDateFormat("MMM dd, yyyy hh:mm a").parse(d);
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy\nhh:mm a");
            xNewData.add(df.format(date));

        }
        cursor.close();
        return xNewData;
    }*/

    public ArrayList<Float> queryYData1(){
        iGlassDatabase db = iGlassDatabase.getDatabase(this.getApplicationContext());
        ArrayList<Float> yNewData = new ArrayList<>();
        Cursor cursorScoreLeft = db.vaDao().cursorScoreLeft();
        for (cursorScoreLeft.moveToFirst(); !cursorScoreLeft.isAfterLast(); cursorScoreLeft.moveToNext()) {
            yNewData.add(cursorScoreLeft.getFloat(cursorScoreLeft.getColumnIndex("ScoreLeft")));
        }
        cursorScoreLeft.close();
        return yNewData;
    }

    public ArrayList<Float> queryYData2(){
        iGlassDatabase db = iGlassDatabase.getDatabase(this.getApplicationContext());
        ArrayList<Float> yNewData = new ArrayList<>();
        Cursor cursorScoreRight = db.vaDao().cursorScoreRight();
        for (cursorScoreRight.moveToFirst(); !cursorScoreRight.isAfterLast(); cursorScoreRight.moveToNext()) {
            yNewData.add(cursorScoreRight.getFloat(cursorScoreRight.getColumnIndex("ScoreRight")));
        }
        cursorScoreRight.close();
        return yNewData;
    }

    public void setData() {

        ArrayList<Entry> yVals1 = new ArrayList<>();
        for (int i = 0; i < queryYData1().size(); i++)
            yVals1.add(new Entry(i, queryYData1().get(i)));

        ArrayList<Entry> yVals2 = new ArrayList<>();
        for (int i = 0; i < queryYData2().size(); i++)
            yVals2.add(new Entry(i, queryYData2().get(i)));

        LineDataSet set1= new LineDataSet(yVals1, "Left eye scores");
        set1.setColor(getResources().getColor(R.color.primary));
        set1.setLineWidth(1.5f);
        set1.setFillAlpha(65);
        set1.setDrawFilled(true);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.set1_fade);
        set1.setFillDrawable(drawable);
        set1.setCircleHoleColor(getResources().getColor(R.color.primary));
        set1.setCircleColor(getResources().getColor(R.color.background));
        set1.setHighLightColor(Color.rgb(244, 117, 117));

        LineDataSet set2 = new LineDataSet(yVals2, "Right eye scores");
        set2.setColor(getResources().getColor(R.color.line_color));
        set2.setLineWidth(1.5f);
        set2.setDrawFilled(true);
        Drawable drawable1 = ContextCompat.getDrawable(this, R.drawable.set2_fade);
        set2.setFillDrawable(drawable1);
        set2.setCircleHoleColor(getResources().getColor(R.color.line_color));
        set2.setCircleColor(getResources().getColor(R.color.background));

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        LineData data = new LineData(dataSets);
        va_chart.setData(data);
        va_chart.invalidate();

/*
        if (va_chart.getData() != null && va_chart.getData().getDataSetCount() > 0){
            set1 = (LineDataSet) va_chart.getData().getDataSetByIndex(0);
            set.setValues();
            va_chart.getData().notifyDataChanged();
            va_chart.notifyDataSetChanged();

        }*/
    }

    public void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        //recyclerView.addItemDecoration(dividerItemDecoration);
        TextView header = findViewById(R.id.va_heading);
        if (emptyDB) header.setText("Result history is empty. Please take the assessment for visual acuity.");
        else header.setText("Visual acuity result history");
        vaListAdapter = new VisualAcuityResultListAdapter(this);
        recyclerView.setAdapter(vaListAdapter);
    }

    public void loadVisualAcuityList(){
        iGlassDatabase db = iGlassDatabase.getDatabase(this.getApplicationContext());
        List<VisualAcuityResult> va_list = db.vaDao().getAll();
        vaListAdapter.setVa_List(va_list);
    }

    public void listCheck(){
        iGlassDatabase db = iGlassDatabase.getDatabase(this.getApplicationContext());
        List<VisualAcuityResult> va_list = db.vaDao().getAll();

        if (va_list.size()!=0){
            emptyDB = false;
        }
    }
}