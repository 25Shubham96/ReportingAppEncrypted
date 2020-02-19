package com.example.shubhammundra.fromandto;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

@SuppressLint("Registered")
public class DiscountBarChart extends AppCompatActivity{

    BarChart barChart1, barChart2;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discount_type_barchart);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        barChart1 = findViewById(R.id.barchart1);
        barChart2 = findViewById(R.id.barchart2);

        imageView = findViewById(R.id.img1);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();

        ArrayList<String> CityName = (ArrayList<String>) intent.getSerializableExtra("City");
        ArrayList<Float> DiscAmt = (ArrayList<Float>) intent.getSerializableExtra("Disc Amt");
        ArrayList<Float> PeriodicDisc = (ArrayList<Float>) intent.getSerializableExtra("Periodic Disc");
        ArrayList<Float> LineDisc = (ArrayList<Float>) intent.getSerializableExtra("Line Disc");
        ArrayList<Float> CustDisc = (ArrayList<Float>) intent.getSerializableExtra("Customer Disc");
        ArrayList<Float> InfoCodeDisc = (ArrayList<Float>) intent.getSerializableExtra("Info Code Disc");
        ArrayList<Float> CustInvDisc = (ArrayList<Float>) intent.getSerializableExtra("Cust Invoice Disc");

        ArrayList<String> CityName2 = (ArrayList<String>) intent.getSerializableExtra("Diff City");
        ArrayList<Float> DiscPercen = (ArrayList<Float>) intent.getSerializableExtra("Discount per");

        ArrayList<BarEntry> y1Entrys = new ArrayList<>();

        for(int i = 0; i < DiscAmt.size(); i++){
            y1Entrys.add(new BarEntry(new float[]{DiscAmt.get(i),PeriodicDisc.get(i),LineDisc.get(i),CustDisc.get(i),InfoCodeDisc.get(i),CustInvDisc.get(i)},i));
        }

        BarDataSet barDataSet1 = new BarDataSet(y1Entrys, "");
        int[] color11 = {Color.parseColor("#e6194b"),
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

        int[] StackBarColor = {Color.parseColor("#e6194b"),
                Color.parseColor("#3cb44b"),
                Color.parseColor("#ffe119"),
                Color.parseColor("#0082c8"),
                Color.parseColor("#f58231"),
                Color.parseColor("#911eb4")};

        barDataSet1.setColors(StackBarColor);
        barDataSet1.setValueTextSize(14f);
        barDataSet1.setStackLabels(new String[]{"DiscAmt","Periodic","Line","Cust","InfoCode","CustInv"});
        barDataSet1.setValueFormatter(new myValueFormatter());

        BarData barData = new BarData(CityName, barDataSet1);
        barData.setValueFormatter(new myValueFormatter());

        barChart1.setData(barData);
        barChart1.animateY(2000, Easing.EasingOption.EaseInOutBack);

        barChart1.setDescription("");
        barChart1.setDescriptionTextSize(18f);

        ArrayList<BarEntry> y2Entrys = new ArrayList<>();

        for(int i = 0; i < DiscPercen.size(); i++){
            y2Entrys.add(new BarEntry(DiscPercen.get(i),i));
        }

        BarDataSet barDataSet2 = new BarDataSet(y2Entrys, "");
        barDataSet2.setColors(color11);
        barDataSet2.setValueTextSize(14f);
        barDataSet2.setValueFormatter(new myValueFormatter());

///////////
        ArrayList<Integer> myColor = new ArrayList<>();
        for (int w = 0 ; w < color11.length ; w++)
        {
            myColor.add(color11[w]);
        }

        int c2 = 0;

        while(myColor.size() < CityName2.size())
        {
            myColor.add(myColor.get(c2));
            c2++;
        }

        ArrayList<Integer> newColor = new ArrayList<>();

        for (int k = 0 ; k < CityName2.size() ; k++)
        {
            newColor.add(myColor.get(k));
        }
///////////

        Legend l = barChart2.getLegend();
        l.setCustom(newColor,CityName2);
        l.setWordWrapEnabled(true);

        BarData barData1 = new BarData(CityName2, barDataSet2);
        barData1.setValueFormatter(new myValueFormatter());

        barChart2.setData(barData1);
        barChart2.animateY(2000, Easing.EasingOption.EaseInOutBack);

        barChart2.setDescription("");
        barChart2.setDescriptionTextSize(18f);
    }
}
