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

import java.util.ArrayList;

public class InventryValueByVendorChart extends AppCompatActivity {

    ArrayList<ArrayList<Float>> GetCostAmount = new ArrayList<>();
    ArrayList<String> GetVendorName = new ArrayList<>();
    ArrayList<String> GetYears = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventry_value_by_vendor_chart);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Intent intent = getIntent();

        GetCostAmount = (ArrayList<ArrayList<Float>>) intent.getSerializableExtra("Cost Amount");
        GetVendorName = (ArrayList<String>) intent.getSerializableExtra("Vendor Name");
        GetYears = (ArrayList<String>) intent.getSerializableExtra("Selected Years");

        BarChart barChart = findViewById(R.id.barchart);

        ArrayList<ArrayList<BarEntry>> diffVendors = new ArrayList<>();

        ArrayList<BarDataSet> multiBar = new ArrayList<>();

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
            diffVendors.add(new ArrayList<BarEntry>());

            for (int j = 0 ; j < GetCostAmount.get(i).size() ; j++)
            {
                diffVendors.get(i).add(new BarEntry(GetCostAmount.get(i).get(j),j));
            }

            BarDataSet barDataSet = new BarDataSet(diffVendors.get(i),"" + GetYears.get(i));
            barDataSet.setValueTextSize(12f);
            barDataSet.setColor(BarColor[i]);
            multiBar.add(barDataSet);
        }

        BarData barData = new BarData(GetVendorName, multiBar);
        barChart.setData(barData);
        barChart.setDescription("");
        barChart.getXAxis().setTextSize(14f);
        barChart.animateY(2000, Easing.EasingOption.EaseInOutBack);
    }
}
