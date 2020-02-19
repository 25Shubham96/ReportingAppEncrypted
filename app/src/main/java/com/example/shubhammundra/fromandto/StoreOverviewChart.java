package com.example.shubhammundra.fromandto;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class StoreOverviewChart extends AppCompatActivity {

    ArrayList<ArrayList<Float>> GetNetAmount = new ArrayList<>();
    ArrayList<String> GetYear = new ArrayList<>();
    ArrayList<String> GetStoreName = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_overview_chart);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Intent intent = getIntent();

        GetNetAmount = (ArrayList<ArrayList<Float>>) intent.getSerializableExtra("Net Amount");
        GetStoreName = (ArrayList<String>) intent.getSerializableExtra("Store Name");

        GetYear = (ArrayList<String>) intent.getSerializableExtra("Selected Years");

        LineChart lineChart = findViewById(R.id.lineChart);

        ArrayList<ArrayList<Entry>> diffStores = new ArrayList<>();

        ArrayList<LineDataSet> multiLine = new ArrayList<>();

        int[] BarColor = {Color.parseColor("#D32F2F"),
                Color.parseColor("#7B1FA2"),
                Color.parseColor("#303F9F"),
                Color.parseColor("#1976D2"),
                Color.parseColor("#00796B"),
                Color.parseColor("#689F38"),
                Color.parseColor("#FBC02D"),
                Color.parseColor("#FFA000"),
                Color.parseColor("#F57C00"),
                Color.parseColor("#E64A19"),
                Color.parseColor("#5D4037"),
                Color.parseColor("#616161"),
                Color.parseColor("#455A64"),
                Color.parseColor("#e6194b"),
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

        for (int i = 0 ; i < GetNetAmount.size() ; i++)
        {
            diffStores.add(new ArrayList<Entry>());

            for (int j = 0 ; j < GetNetAmount.get(i).size() ; j++)
            {
                diffStores.get(i).add(new Entry(GetNetAmount.get(i).get(j),j));
            }

            LineDataSet lineDataSet = new LineDataSet(diffStores.get(i),"" + GetStoreName.get(i));
            lineDataSet.setValueTextSize(12f);
            lineDataSet.setLineWidth(3f);
            lineDataSet.setColor(BarColor[i]);
            lineDataSet.setValueFormatter(new myValueFormatter());
            multiLine.add(lineDataSet);
        }

        LineData lineData = new LineData(GetYear, multiLine);
        lineData.setValueFormatter(new myValueFormatter());
        lineChart.setData(lineData);
        lineChart.setDescription("");
        lineChart.getXAxis().setTextSize(14f);
        lineChart.animateY(2000, Easing.EasingOption.EaseInOutBack);
    }
}
