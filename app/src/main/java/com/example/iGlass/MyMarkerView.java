package com.example.iGlass;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;

public class MyMarkerView extends MarkerView {
    private TextView tvContent;
    ArrayList<String> mXLabels;

    public MyMarkerView(Context context, int layoutResource, ArrayList<String> xLabels) {
        super(context, layoutResource);
        mXLabels = xLabels;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        //get x value
        String xVal = mXLabels.get((int) e.getX());

        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }

    /*@Override
    public int getXOffset(float xpos) {
        // this will center the marker-view horizontally
        return -(getWidth() / 2);

    }

    @Override
    public int getYOffset(float ypos) {
        // this will cause the marker-view to be above the selected value
        return -getHeight();
    }*/
}
