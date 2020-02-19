package com.example.shubhammundra.fromandto;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

@SuppressLint("Registered")
public class ReceiptBarChart extends AppCompatActivity
{

    BarChart b1,b2,b3,b4;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private ArrayList<BarChart> ChartArray = new ArrayList<BarChart>();
    private ArrayList<String> Title = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_report);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        b1 = new BarChart(this);
        b2 = new BarChart(this);
        b3 = new BarChart(this);
        b4 = new BarChart(this);

        ArrayList<Float> CitySales;
        ArrayList<String> noOfCities;
        ArrayList<Float> MonthSales;
        ArrayList<String> noOfMonths;
        ArrayList<Float> CashierSales;
        ArrayList<String> noOfCashier;
        ArrayList<Float> HourSales;
        ArrayList<String> noOfHours;

        Intent getReceiptintent = getIntent();

        CitySales = (ArrayList<Float>) getReceiptintent.getSerializableExtra("City Sales");
        noOfCities = (ArrayList<String>) getReceiptintent.getSerializableExtra("City");

        MonthSales = (ArrayList<Float>) getReceiptintent.getSerializableExtra("Month Sales");
        noOfMonths = (ArrayList<String>) getReceiptintent.getSerializableExtra("Month");

        CashierSales = (ArrayList<Float>) getReceiptintent.getSerializableExtra("Cashier Sales");
        noOfCashier = (ArrayList<String>) getReceiptintent.getSerializableExtra("Cashier");

        HourSales = (ArrayList<Float>) getReceiptintent.getSerializableExtra("Hour Sales");
        noOfHours = (ArrayList<String>) getReceiptintent.getSerializableExtra("Hour");

        int[] ReceiptColor = {Color.parseColor("#e6194b"),
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
//1
        ArrayList<BarEntry> CWS = new ArrayList<>();
        for(int i = 0; i < CitySales.size(); i++){
            CWS.add(new BarEntry(CitySales.get(i),i));
        }

        BarDataSet bds1 = new BarDataSet(CWS,"");
        bds1.setColors(ReceiptColor);
        bds1.setValueTextSize(14f);
        bds1.setValueFormatter(new myValueFormatter());

///////////
        ArrayList<Integer> myColor = new ArrayList<>();
        for (int w = 0 ; w < ReceiptColor.length ; w++)
        {
            myColor.add(ReceiptColor[w]);
        }

        int c = 0;

        while(myColor.size() < noOfCities.size())
        {
            myColor.add(myColor.get(c));
            c++;
        }

        ArrayList<Integer> newColor = new ArrayList<>();

        for (int k = 0 ; k < noOfCities.size() ; k++)
        {
            newColor.add(myColor.get(k));
        }
///////////

        Legend l = b1.getLegend();
        l.setCustom(newColor,noOfCities);
        l.setWordWrapEnabled(true);

        BarData bd1 = new BarData(noOfCities,bds1);
        bd1.setValueFormatter(new myValueFormatter());

        b1.setData(bd1);
        b1.animateY(2000, Easing.EasingOption.EaseInOutBack);

        b1.setDescription(" ");
        b1.setDescriptionTextSize(18f);
//2
        ArrayList<BarEntry> MWS = new ArrayList<>();
        for(int i = 0; i < MonthSales.size(); i++){
            MWS.add(new BarEntry(MonthSales.get(i),i));
        }

        BarDataSet bds2 = new BarDataSet(MWS,"");
        bds2.setColors(ReceiptColor);
        bds2.setValueTextSize(14f);
        bds2.setValueFormatter(new myValueFormatter());


///////////
        ArrayList<Integer> myColor2 = new ArrayList<>();
        for (int w = 0 ; w < ReceiptColor.length ; w++)
        {
            myColor2.add(ReceiptColor[w]);
        }

        int c2 = 0;

        while(myColor2.size() < noOfMonths.size())
        {
            myColor2.add(myColor2.get(c2));
            c2++;
        }

        ArrayList<Integer> newColor2 = new ArrayList<>();

        for (int k = 0 ; k < noOfMonths.size() ; k++)
        {
            newColor2.add(myColor2.get(k));
        }
///////////

        Legend l2 = b2.getLegend();
        l2.setCustom(newColor2,noOfMonths);
        l2.setWordWrapEnabled(true);

        BarData bd2 = new BarData(noOfMonths,bds2);
        bd2.setValueFormatter(new myValueFormatter());

        b2.setData(bd2);
        b2.animateY(2000, Easing.EasingOption.EaseInOutBack);

        b2.setDescription(" ");
        b2.setDescriptionTextSize(18f);
//3
        ArrayList<BarEntry> CaWS = new ArrayList<>();
        for(int i = 0; i < CashierSales.size(); i++){
            CaWS.add(new BarEntry(CashierSales.get(i),i));
        }

        BarDataSet bds3 = new BarDataSet(CaWS,"");
        bds3.setColors(ReceiptColor);
        bds3.setValueTextSize(14f);
        bds3.setValueFormatter(new myValueFormatter());


        ArrayList<Integer> newColor3 = new ArrayList<>();

        for (int k = 0 ; k < noOfCashier.size() ; k++)
        {
            newColor3.add(ReceiptColor[k]);
        }

        Legend l3 = b3.getLegend();
        l3.setCustom(newColor3,noOfCashier);
        l3.setWordWrapEnabled(true);

        BarData bd3 = new BarData(noOfCashier,bds3);
        bd3.setValueFormatter(new myValueFormatter());

        b3.setData(bd3);
        b3.animateY(2000, Easing.EasingOption.EaseInOutBack);

        b3.setDescription(" ");
        b3.setDescriptionTextSize(18f);
//4
        ArrayList<BarEntry> HWS = new ArrayList<>();
        for(int i = 0; i < HourSales.size(); i++){
            HWS.add(new BarEntry(HourSales.get(i),i));
        }

        BarDataSet bds4 = new BarDataSet(HWS,"");
        bds4.setColors(ReceiptColor);
        bds4.setValueTextSize(14f);
        bds4.setValueFormatter(new myValueFormatter());

        ArrayList<Integer> newColor4 = new ArrayList<>();

        for (int k = 0 ; k < noOfHours.size() ; k++)
        {
            newColor4.add(ReceiptColor[k]);
        }

        Legend l4 = b4.getLegend();
        l4.setCustom(newColor4,noOfHours);
        l4.setWordWrapEnabled(true);

        BarData bd4 = new BarData(noOfHours,bds4);
        bd4.setValueFormatter(new myValueFormatter());

        b4.setData(bd4);
        b4.animateY(2000, Easing.EasingOption.EaseInOutBack);

        b4.setDescription(" ");
        b4.setDescriptionTextSize(18f);

        ChartArray.add(b1);
        ChartArray.add(b2);
        ChartArray.add(b3);
        ChartArray.add(b4);

        Title.add("Net Payment Chart");
        Title.add("AvgNet Payment Chart");
        Title.add("NetPayment By Type Chart");
        Title.add("AvgNet Payment By Type Chart");

        mPager = findViewById(R.id.pager);
        mPager.setAdapter(new MyAdapter(ReceiptBarChart.this,ChartArray,Title));

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
    }
}
