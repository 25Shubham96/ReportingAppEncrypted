package com.example.shubhammundra.fromandto;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.util.ArrayList;
import me.relex.circleindicator.CircleIndicator;

public class StoreLineChart extends AppCompatActivity{
    LineChart viewStoreLineChart1,viewStoreLineChart2;

    private static ViewPager mPager;
    private ArrayList<LineChart> ChartArray = new ArrayList<>();
    private ArrayList<String> Title = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_report);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        viewStoreLineChart1 = new LineChart(StoreLineChart.this);
        viewStoreLineChart2 = new LineChart(StoreLineChart.this);

        Intent getStoreLineIntent = getIntent();

        ArrayList<Float> getStoreLineAmount = (ArrayList<Float>) getStoreLineIntent.getSerializableExtra("Store Line Amount");
        ArrayList<Float> NoOfTransaction = (ArrayList<Float>) getStoreLineIntent.getSerializableExtra("No of TX");

        ArrayList<String> storeLineCode = (ArrayList<String>) getStoreLineIntent.getSerializableExtra("Store Line No");

        ArrayList<Entry> storeLineAmount = new ArrayList<>();

        for (int i = 0 ; i < getStoreLineAmount.size() ; i++){
            storeLineAmount.add(new Entry(getStoreLineAmount.get(i),i));
        }

        ArrayList<Entry> NofTXEntry = new ArrayList<>();

        for (int i = 0 ; i < NoOfTransaction.size() ; i++)
        {
            NofTXEntry.add(new Entry(NoOfTransaction.get(i),i));
        }

        LineDataSet storelineDataSet = new LineDataSet(storeLineAmount,"");

        LineDataSet storelineDataSet2 = new LineDataSet(NofTXEntry,"");

        int[] storeLineColor = {Color.parseColor("#e6194b"),
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
        for (int w = 0 ; w < storeLineColor.length ; w++)
        {
            myColor.add(storeLineColor[w]);
        }

        int c = 0;

        while(myColor.size() < storeLineCode.size())
        {
            myColor.add(myColor.get(c));
            c++;
        }

        ArrayList<Integer> newColor = new ArrayList<>();

        for (int k = 0 ; k < storeLineCode.size() ; k++)
        {
            newColor.add(myColor.get(k));
        }

        storelineDataSet.setColors(storeLineColor);
        storelineDataSet.setValueTextSize(14f);
        storelineDataSet.setValueFormatter(new myValueFormatter());

        storelineDataSet2.setColors(storeLineColor);
        storelineDataSet2.setValueTextSize(14f);
        storelineDataSet2.setValueFormatter(new myValueFormatter());

        Legend l = viewStoreLineChart1.getLegend();
        l.setCustom(newColor,storeLineCode);
        l.setWordWrapEnabled(true);

        Legend l2 = viewStoreLineChart2.getLegend();
        l2.setCustom(newColor,storeLineCode);
        l2.setWordWrapEnabled(true);

        LineData ItemlineData = new LineData(storeLineCode,storelineDataSet);
        ItemlineData.setValueFormatter(new myValueFormatter());

        LineData ItemlineData2 = new LineData(storeLineCode,storelineDataSet2);
        ItemlineData2.setValueFormatter(new myValueFormatter());

        viewStoreLineChart1.setData(ItemlineData);
        viewStoreLineChart1.setDescription("");
        viewStoreLineChart1.animateY(2000, Easing.EasingOption.EaseInCubic);

        viewStoreLineChart2.setData(ItemlineData2);
        viewStoreLineChart2.setDescription("");
        viewStoreLineChart2.animateY(2000, Easing.EasingOption.EaseInCubic);

        ChartArray.add(viewStoreLineChart1);
        ChartArray.add(viewStoreLineChart2);

        Title.add("Net Sales");
        Title.add("No of Transactions");

        mPager = findViewById(R.id.pager);
        mPager.setAdapter(new MyLineChartAdapter(StoreLineChart.this,ChartArray,Title));

        CircleIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
    }
}
