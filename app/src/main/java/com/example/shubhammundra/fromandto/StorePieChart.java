package com.example.shubhammundra.fromandto;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import java.util.ArrayList;
import me.relex.circleindicator.CircleIndicator;

public class StorePieChart extends AppCompatActivity{
    PieChart storePieChart1,storePieChart2;

    private static ViewPager mPager;
    private ArrayList<PieChart> ChartArray = new ArrayList<>();
    private ArrayList<String> Title = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_report);

        storePieChart1 = new PieChart(StorePieChart.this);
        storePieChart2 = new PieChart(StorePieChart.this);

        Intent getStorePieIntent = getIntent();

        ArrayList<Float> getStoreAmount = (ArrayList<Float>) getStorePieIntent.getSerializableExtra("Store Amount");
        ArrayList<Float> NoOfTransaction = (ArrayList<Float>) getStorePieIntent.getSerializableExtra("No of TX");

        ArrayList<String> getStoreNo = (ArrayList<String>) getStorePieIntent.getSerializableExtra("Store No");

        ArrayList<Entry> PieStoreAmount = new ArrayList<>();

        for (int i = 0 ; i < getStoreAmount.size() ; i++)
        {
            PieStoreAmount.add(new Entry(getStoreAmount.get(i),i));
        }

        ArrayList<Entry> NofTXEntry = new ArrayList<>();

        for (int i = 0 ; i < NoOfTransaction.size() ; i++)
        {
            NofTXEntry.add(new Entry(NoOfTransaction.get(i),i));
        }

        PieDataSet storePieDataSet = new PieDataSet(PieStoreAmount,"");

        PieDataSet storePieDataSet2 = new PieDataSet(NofTXEntry,"");

        int[] storePieColor = {Color.parseColor("#e6194b"),
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

        storePieDataSet.setColors(storePieColor);
        storePieDataSet.setValueTextSize(16f);
        storePieDataSet.setValueTextColor(Color.WHITE);
        storePieDataSet.setSliceSpace(0.5f);
        storePieDataSet.setValueFormatter(new myValueFormatter());

        storePieDataSet2.setColors(storePieColor);
        storePieDataSet2.setValueTextSize(16f);
        storePieDataSet2.setValueTextColor(Color.WHITE);
        storePieDataSet2.setSliceSpace(0.5f);
        storePieDataSet2.setValueFormatter(new myValueFormatter());

        Legend l = storePieChart1.getLegend();
        l.setWordWrapEnabled(true);

        Legend l2 = storePieChart2.getLegend();
        l2.setWordWrapEnabled(true);

        PieData storePieData = new PieData(getStoreNo,storePieDataSet);
        storePieData.setValueFormatter(new myValueFormatter());

        PieData storePieData2 = new PieData(getStoreNo,storePieDataSet2);
        storePieData2.setValueFormatter(new myValueFormatter());

        storePieChart1.setData(storePieData);
        storePieChart1.setHoleColor(Color.TRANSPARENT);
        storePieChart1.setCenterText("Store");
        storePieChart1.setCenterTextColor(Color.WHITE);
        storePieChart1.setDragDecelerationFrictionCoef(0.90f);
        storePieChart1.animateY(2000, Easing.EasingOption.EaseInCirc);
        storePieChart1.setDescription("Store Wise Entry");
        storePieChart1.setDescriptionTextSize(18f);
        storePieChart1.setData(storePieData);

        storePieChart2.setHoleColor(Color.TRANSPARENT);
        storePieChart2.setCenterText("Store");
        storePieChart2.setCenterTextColor(Color.WHITE);
        storePieChart2.setDragDecelerationFrictionCoef(0.90f);
        storePieChart2.animateY(2000, Easing.EasingOption.EaseInCirc);
        storePieChart2.setDescription("Store Wise Entry");
        storePieChart2.setDescriptionTextSize(18f);
        storePieChart2.setData(storePieData2);

        ChartArray.add(storePieChart1);
        ChartArray.add(storePieChart2);

        Title.add("Net Sales");
        Title.add("No of Transactions");

        mPager = findViewById(R.id.pager);
        mPager.setAdapter(new MyPieChartAdapter(StorePieChart.this,ChartArray,Title));

        CircleIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
    }
}
