package com.example.shubhammundra.fromandto;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

@SuppressLint("Registered")
public class FreqBar extends AppCompatActivity{
    BarChart barChart,barChart2,barChart3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.freq_barchart);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        barChart = findViewById(R.id.barchart);
        barChart2 = findViewById(R.id.barchart2);
        barChart3 = findViewById(R.id.barchart3);

        Intent intent = getIntent();

        ArrayList<Float> y1Data = (ArrayList<Float>) intent.getSerializableExtra("Y Values");
        ArrayList<String> x1Data = (ArrayList<String>) intent.getSerializableExtra("X Values");

        ArrayList<Float> y2Data = (ArrayList<Float>) intent.getSerializableExtra("Bar2");
        ArrayList<String> x2Data = (ArrayList<String>) intent.getSerializableExtra("Bar2String");

        ArrayList<Float> y3Data = (ArrayList<Float>) intent.getSerializableExtra("Bar3");
        ArrayList<String> x3Data = (ArrayList<String>) intent.getSerializableExtra("Bar3String");

        ArrayList<BarEntry> yEntrys = new ArrayList<>();
        ArrayList<BarEntry> yEntrys2 = new ArrayList<>();
        ArrayList<BarEntry> yEntrys3 = new ArrayList<>();

        for(int i = 0; i < y1Data.size(); i++){
            yEntrys.add(new BarEntry(y1Data.get(i),i));
        }

        for(int j = 0; j < y2Data.size(); j++){
            yEntrys2.add(new BarEntry(y2Data.get(j),j));
        }

        for(int k = 0; k < y3Data.size(); k++){
            yEntrys3.add(new BarEntry(y3Data.get(k),k));
        }

        BarDataSet barDataSet = new BarDataSet(yEntrys,"");
        BarDataSet barDataSet2 = new BarDataSet(yEntrys2,"");
        BarDataSet barDataSet3 = new BarDataSet(yEntrys3,"");

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

        barDataSet.setColors(color12);
        barDataSet.setValueTextSize(14f);
        barDataSet2.setColors(color12);
        barDataSet2.setValueTextSize(14f);
        barDataSet3.setColors(color12);
        barDataSet3.setValueTextSize(14f);
        barDataSet.setValueFormatter(new myValueFormatter());
        barDataSet2.setValueFormatter(new myValueFormatter());
        barDataSet3.setValueFormatter(new myValueFormatter());

///////////
        ArrayList<Integer> myColor = new ArrayList<>();
        for (int w = 0 ; w < color12.length ; w++)
        {
            myColor.add(color12[w]);
        }

        int c = 0;

        while(myColor.size() < x1Data.size())
        {
            myColor.add(myColor.get(c));
            c++;
        }

        ArrayList<Integer> newColor = new ArrayList<>();

        for (int k = 0 ; k < x1Data.size() ; k++)
        {
            newColor.add(myColor.get(k));
        }
///////////

///////////
        ArrayList<Integer> myColor2 = new ArrayList<>();
        for (int w = 0 ; w < color12.length ; w++)
        {
            myColor2.add(color12[w]);
        }

        int c2 = 0;

        while(myColor2.size() < x2Data.size())
        {
            myColor2.add(myColor2.get(c2));
            c2++;
        }

        ArrayList<Integer> newColor2 = new ArrayList<>();

        for (int k = 0 ; k < x2Data.size() ; k++)
        {
            newColor2.add(myColor2.get(k));
        }
///////////

///////////
        ArrayList<Integer> myColor3 = new ArrayList<>();
        for (int w = 0 ; w < color12.length ; w++)
        {
            myColor3.add(color12[w]);
        }

        int c3 = 0;

        while(myColor3.size() < x3Data.size())
        {
            myColor3.add(myColor3.get(c3));
            c3++;
        }

        ArrayList<Integer> newColor3 = new ArrayList<>();

        for (int k = 0 ; k < x3Data.size() ; k++)
        {
            newColor3.add(myColor3.get(k));
        }
///////////

        Legend l = barChart.getLegend();
        l.setCustom(newColor,x1Data);
        l.setWordWrapEnabled(true);

        Legend l2 = barChart2.getLegend();
        l2.setCustom(newColor2,x2Data);
        l2.setWordWrapEnabled(true);

        Legend l3 = barChart3.getLegend();
        l3.setCustom(newColor3,x3Data);
        l3.setWordWrapEnabled(true);

        BarData barData = new BarData(x1Data, barDataSet);
        barData.setValueFormatter(new myValueFormatter());

        BarData barData2 = new BarData(x2Data, barDataSet2);
        barData2.setValueFormatter(new myValueFormatter());

        BarData barData3 = new BarData(x3Data, barDataSet3);
        barData3.setValueFormatter(new myValueFormatter());

        barChart.setData(barData);
        barChart.animateY(2000, Easing.EasingOption.EaseInOutBack);

        barChart.setDescription("");
        barChart.setDescriptionTextSize(18f);

        barChart2.setData(barData2);
        barChart2.animateY(2000, Easing.EasingOption.EaseInOutBack);

        barChart2.setDescription("");
        barChart2.setDescriptionTextSize(18f);

        barChart3.setData(barData3);
        barChart3.animateY(2000, Easing.EasingOption.EaseInOutBack);

        barChart3.setDescription("");
        barChart3.setDescriptionTextSize(18f);
    }
}
