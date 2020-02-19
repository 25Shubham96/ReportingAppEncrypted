package com.example.shubhammundra.fromandto;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.charts.LineChart;
import java.util.ArrayList;


public class InventryValueByLocationChart extends AppCompatActivity {

    ArrayList<ArrayList<Float>> GetCostAmount = new ArrayList<>();
    ArrayList<String> GetQuaterYear = new ArrayList<>();
    ArrayList<String> GetLocations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventry_value_by_location_chart);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Intent intent = getIntent();

        GetCostAmount = (ArrayList<ArrayList<Float>>) intent.getSerializableExtra("Cost Amount");
        GetQuaterYear = (ArrayList<String>) intent.getSerializableExtra("Quater Year");
        GetLocations = (ArrayList<String>) intent.getSerializableExtra("Selected Location");

        LineChart lineChart = findViewById(R.id.linechart);

        ArrayList<ArrayList<Entry>> diffLocations = new ArrayList<>();

        ArrayList<LineDataSet> multiLine = new ArrayList<>();

        int[] BarColor = {Color.parseColor("#e6194b"),
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
                Color.parseColor("#808080"),
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

        for (int i = 0 ; i < GetCostAmount.size() ; i++)
        {
            diffLocations.add(new ArrayList<Entry>());

            for (int j = 0 ; j < GetCostAmount.get(i).size() ; j++)
            {
                diffLocations.get(i).add(new Entry(GetCostAmount.get(i).get(j),j));
            }

            LineDataSet lineDataSet = new LineDataSet(diffLocations.get(i),"" + GetLocations.get(i));
            lineDataSet.setValueTextSize(12f);
            lineDataSet.setLineWidth(3f);
            lineDataSet.setColor(BarColor[i]);
            multiLine.add(lineDataSet);
        }

        LineData lineData = new LineData(GetQuaterYear, multiLine);
        lineChart.setData(lineData);
        lineChart.setDescription("");
        lineChart.getXAxis().setTextSize(14f);
        lineChart.animateY(2000, Easing.EasingOption.EaseInOutBack);
    }
}
