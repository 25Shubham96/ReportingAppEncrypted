package com.example.shubhammundra.fromandto;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.util.ArrayList;
import me.relex.circleindicator.CircleIndicator;

public class DisplayPiechart extends AppCompatActivity{
    BarChart barChart1,barChart2;

    private static ViewPager mPager;
    private ArrayList<BarChart> ChartArray = new ArrayList<>();
    private ArrayList<String> Title = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_report);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        barChart1 = new BarChart(DisplayPiechart.this);
        barChart2 = new BarChart(DisplayPiechart.this);

        Intent intent = getIntent();

        ArrayList<Float> y1Data = (ArrayList<Float>) intent.getSerializableExtra("Y Values");
        ArrayList<Float> NoOfTransaction = (ArrayList<Float>) intent.getSerializableExtra("No of TX");

        ArrayList<String> x1Data = (ArrayList<String>) intent.getSerializableExtra("X Values");

        ArrayList<BarEntry> yEntrys = new ArrayList<>();

        for(int i = 0; i < y1Data.size(); i++){
            yEntrys.add(new BarEntry(y1Data.get(i),i));
        }

        ArrayList<BarEntry> NofTXEntry = new ArrayList<>();

        for (int i = 0 ; i < NoOfTransaction.size() ; i++)
        {
            NofTXEntry.add(new BarEntry(NoOfTransaction.get(i),i));
        }

        BarDataSet barDataSet = new BarDataSet(yEntrys,"");

        BarDataSet barDataSet2 = new BarDataSet(NofTXEntry,"");

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

        barDataSet.setColors(color12);
        barDataSet.setValueTextSize(14f);
        barDataSet.setValueFormatter(new myValueFormatter());

        barDataSet2.setColors(color12);
        barDataSet2.setValueTextSize(14f);
        barDataSet2.setValueFormatter(new myValueFormatter());

        Legend l = barChart1.getLegend();
        l.setCustom(newColor,x1Data);
        l.setWordWrapEnabled(true);

        Legend l2 = barChart2.getLegend();
        l2.setCustom(newColor,x1Data);
        l2.setWordWrapEnabled(true);

        BarData barData = new BarData(x1Data, barDataSet);
        barData.setValueFormatter(new myValueFormatter());

        BarData barData2 = new BarData(x1Data, barDataSet2);
        barData2.setValueFormatter(new myValueFormatter());

        barChart1.setData(barData);
        barChart1.animateY(2000, Easing.EasingOption.EaseInOutBack);
        barChart1.setDescription("");

        barChart2.setData(barData2);
        barChart2.animateY(2000, Easing.EasingOption.EaseInOutBack);
        barChart2.setDescription("");

        ChartArray.add(barChart1);
        ChartArray.add(barChart2);

        Title.add("Net Sales");
        Title.add("No of Transactions");

        mPager = findViewById(R.id.pager);
        mPager.setAdapter(new MyAdapter(DisplayPiechart.this,ChartArray,Title));

        CircleIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
    }
}
