package com.example.shubhammundra.fromandto;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class ItemLineChart extends AppCompatActivity{
    LineChart viewItemLineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_linechart);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        viewItemLineChart = findViewById(R.id.linechart);

        Intent getItemLineIntent = getIntent();

        ArrayList<Float> getItemLineamount = (ArrayList<Float>) getItemLineIntent.getSerializableExtra("Item Line Amount");
        ArrayList<String> itemLineCode = (ArrayList<String>) getItemLineIntent.getSerializableExtra("Item Line Code");

        ArrayList<Entry> ItemLineAmount = new ArrayList<>();

        for (int i = 0 ; i < getItemLineamount.size() ; i++){
            ItemLineAmount.add(new Entry(getItemLineamount.get(i),i));
        }

        LineDataSet ItemlineDataSet = new LineDataSet(ItemLineAmount,"");

        int[] itemLineColor = {Color.parseColor("#e6194b"),
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

        ArrayList<Integer> myColor = new ArrayList<>();
        for (int w = 0 ; w < itemLineColor.length ; w++)
        {
            myColor.add(itemLineColor[w]);
        }

        int c = 0;

        while(myColor.size() < itemLineCode.size())
        {
            myColor.add(myColor.get(c));
            c++;
        }

        ArrayList<Integer> newColor = new ArrayList<>();

        for (int k = 0 ; k < itemLineCode.size() ; k++)
        {
            newColor.add(myColor.get(k));
        }

        ItemlineDataSet.setColors(itemLineColor);
        ItemlineDataSet.setValueTextSize(16f);
        ItemlineDataSet.setValueTextColor(Color.BLACK);

        Legend l = viewItemLineChart.getLegend();
        l.setCustom(newColor,itemLineCode);
        l.setWordWrapEnabled(true);

        LineData ItemlineData = new LineData(itemLineCode,ItemlineDataSet);

        viewItemLineChart.setData(ItemlineData);

        viewItemLineChart.setDescription("");

        viewItemLineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
    }
}
