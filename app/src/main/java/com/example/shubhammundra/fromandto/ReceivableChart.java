package com.example.shubhammundra.fromandto;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class ReceivableChart extends AppCompatActivity {

    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receivable_chart);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        lineChart = findViewById(R.id.linechart);

        Intent getStoreLineIntent = getIntent();

        ArrayList<Float> getStoreLineAmount = (ArrayList<Float>) getStoreLineIntent.getSerializableExtra("Amount");
        ArrayList<String> storeLineCode = (ArrayList<String>) getStoreLineIntent.getSerializableExtra("Year");

        ArrayList<Entry> storeLineAmount = new ArrayList<>();

        for (int i = 0 ; i < getStoreLineAmount.size() ; i++){
            storeLineAmount.add(new Entry(getStoreLineAmount.get(i),i));
        }

        int[] color12 = {Color.parseColor("#e6194b"),
                Color.parseColor("#3cb44b"),
                Color.parseColor("#ffe119"),
                Color.parseColor("#0082c8"),
                Color.parseColor("#f58231"),
                Color.parseColor("#911eb4"),
                Color.parseColor("#008080"),
                Color.parseColor("#aa6e28"),
                Color.parseColor("#800000"),
                Color.parseColor("#808000"),
                Color.parseColor("#000080"),
                Color.parseColor("#f032e6"),
                Color.parseColor("#46f0f0"),
                Color.parseColor("#d2f53c"),
                Color.parseColor("#fabebe"),
                Color.parseColor("#aaffc3"),
                Color.parseColor("#fffac8"),
                Color.parseColor("#ffd8b1"),
                Color.parseColor("#808080")};

        LineDataSet storelineDataSet = new LineDataSet(storeLineAmount,"");

        storelineDataSet.setColors(color12);
        storelineDataSet.setValueTextSize(16f);
        storelineDataSet.setValueTextColor(Color.BLACK);

        ArrayList<Integer> newColor = new ArrayList<>();

        for (int k = 0 ; k < storeLineCode.size() ; k++)
        {
            newColor.add(color12[k]);
        }

        Legend l = lineChart.getLegend();
        l.setCustom(newColor,storeLineCode);
        l.setWordWrapEnabled(true);

        LineData ItemlineData = new LineData(storeLineCode,storelineDataSet);

        lineChart.setData(ItemlineData);

        lineChart.setDescription("");

        lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);    }
}
