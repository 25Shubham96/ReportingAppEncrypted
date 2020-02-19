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

public class InventryOverviewChart extends AppCompatActivity {

    LineChart lineChart;

    ArrayList<String> GetQuaterYear = new ArrayList<>();
    ArrayList<Float> GetCostAmount = new ArrayList<>();
    ArrayList<Float> GetRotationDays = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventry_overview_chart);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        lineChart = findViewById(R.id.linechart);

        Intent i = getIntent();

        GetQuaterYear = (ArrayList<String>) i.getSerializableExtra("Year Quat");
        GetCostAmount = (ArrayList<Float>) i.getSerializableExtra("Cost Amount");
        GetRotationDays = (ArrayList<Float>) i.getSerializableExtra("Rotation Days");

        ArrayList<Entry> StockValue = new ArrayList<>();
        ArrayList<Entry> RotationDy = new ArrayList<>();

        for (int j = 0 ; j < GetCostAmount.size() ; j++)
        {
            StockValue.add(new Entry(GetCostAmount.get(j),j));
        }

        for (int k = 0 ; k < GetRotationDays.size() ; k ++)
        {
            RotationDy.add(new Entry(GetRotationDays.get(k),k));
        }

        LineDataSet lineDataSet1 = new LineDataSet(StockValue,"Stock Value");
        lineDataSet1.setColor(Color.BLUE);
        lineDataSet1.setValueTextSize(18f);
        lineDataSet1.setLineWidth(2f);
        lineDataSet1.setDrawFilled(true);
        lineDataSet1.setFillColor(Color.BLUE);
        lineDataSet1.setFillAlpha(255);
        lineDataSet1.setValueTextColor(Color.BLACK);

        LineDataSet lineDataSet2 = new LineDataSet(RotationDy,"Rotation Days");
        lineDataSet2.setColor(Color.GREEN);
        lineDataSet2.setValueTextSize(18f);

        ArrayList<LineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(lineDataSet1);
        lineDataSets.add(lineDataSet2);

        LineData lineData = new LineData(GetQuaterYear,lineDataSets);

        lineChart.setData(lineData);
        lineChart.setDescription("");
        lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
    }
}
