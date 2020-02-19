package com.example.shubhammundra.fromandto;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class DualBarChart extends AppCompatActivity {

    ArrayList<String> TTDesc = new ArrayList<>();
    ArrayList<Float> Amt1 = new ArrayList<>();
    ArrayList<Float> Amt2 = new ArrayList<>();

    String CY = new String();
    String PY = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dual_bar_chart);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Intent intent = getIntent();
        TTDesc = (ArrayList<String>) intent.getSerializableExtra("Tender Type Des");
        Amt1 = (ArrayList<Float>) intent.getSerializableExtra("Current Amount");
        Amt2 = (ArrayList<Float>) intent.getSerializableExtra("Previous Amount");

        CY = intent.getStringExtra("Current Year");
        PY = intent.getStringExtra("Previous Year");

        BarChart barChart = (BarChart) findViewById(R.id.barchart);

        ArrayList<BarEntry> CurrentYearAmt = new ArrayList<>();
        for (int i = 0 ; i < Amt1.size() ; i++)
        {
            CurrentYearAmt.add(new BarEntry(Amt1.get(i),i));
        }

        ArrayList<BarEntry> PreviousYearAmt = new ArrayList<>();
        for (int i = 0 ; i < Amt2.size() ; i++)
        {
            PreviousYearAmt.add(new BarEntry(Amt2.get(i),i));
        }

        BarDataSet barDataSet1 = new BarDataSet(CurrentYearAmt, "" + CY);
        barDataSet1.setColor(Color.BLUE);
        BarDataSet barDataSet2 = new BarDataSet(PreviousYearAmt, "" + PY);
        barDataSet2.setColor(Color.GREEN);

        ArrayList<BarDataSet> dualBar = new ArrayList<>();
        dualBar.add(barDataSet1);
        dualBar.add(barDataSet2);

        BarData barData = new BarData(TTDesc, dualBar);
        barChart.setData(barData);
        barChart.setDescription("");
        barChart.animateY(2000, Easing.EasingOption.EaseInOutBack);
    }
}
