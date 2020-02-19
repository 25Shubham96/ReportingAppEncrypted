package com.example.shubhammundra.fromandto;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class ExpenseTypChart extends AppCompatActivity
{
    BarChart bc1,bc2;

    ImageView im2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_report_chart);

        bc1 = findViewById(R.id.barchart1);
        bc2 = findViewById(R.id.barchart2);

        im2 = findViewById(R.id.img11);

        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Intent intent = getIntent();

        ArrayList<Float> y1Data = new ArrayList<>();
        y1Data.clear();
        y1Data = (ArrayList<Float>) intent.getSerializableExtra("Exp Net Amount");

        ArrayList<String> x1Data = new ArrayList<>();
        x1Data.clear();
        x1Data = (ArrayList<String>) intent.getSerializableExtra("Expense Type");

        ArrayList<Float> y2Data = new ArrayList<>();
        y2Data.clear();
        y2Data = (ArrayList<Float>) intent.getSerializableExtra("City Net Amount");

        ArrayList<String> x2Data = new ArrayList<>();
        x2Data.clear();
        x2Data = (ArrayList<String>) intent.getSerializableExtra("Expense City");

        ArrayList<BarEntry> yEntrys = new ArrayList<>();

        for(int i = 0; i < y1Data.size(); i++){
            yEntrys.add(new BarEntry(y1Data.get(i),i));
        }

        ArrayList<BarEntry> y1Entrys = new ArrayList<>();

        for(int i = 0; i < y2Data.size(); i++){
            y1Entrys.add(new BarEntry(y2Data.get(i),i));
        }


        BarDataSet barDataSet = new BarDataSet(yEntrys,"");
        barDataSet.setValueFormatter(new myValueFormatter());
        BarDataSet barDataSet1 = new BarDataSet(y1Entrys, "");
        barDataSet1.setValueFormatter(new myValueFormatter());

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

        barDataSet1.setColors(color12);
        barDataSet1.setValueTextSize(14f);

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

        Legend l = bc1.getLegend();
        l.setCustom(newColor,x1Data);
        l.setWordWrapEnabled(true);

///////////
        ArrayList<Integer> myColor2 = new ArrayList<>();
        for (int w = 0 ; w < color12.length ; w++)
        {
            myColor2.add(color12[w]);
        }

        int c2 = 0;

        while(myColor2.size() < x1Data.size())
        {
            myColor2.add(myColor2.get(c2));
            c2++;
        }

        ArrayList<Integer> newColor2 = new ArrayList<>();

        for (int k = 0 ; k < x1Data.size() ; k++)
        {
            newColor2.add(myColor2.get(k));
        }
///////////

        Legend l2 = bc2.getLegend();
        l2.setCustom(newColor2,x2Data);
        l2.setWordWrapEnabled(true);

        BarData barData = new BarData(x1Data, barDataSet);
        barData.setValueFormatter(new myValueFormatter());
        BarData barData1 = new BarData(x2Data, barDataSet1);
        barData1.setValueFormatter(new myValueFormatter());

        bc1.setData(barData);
        bc2.setData(barData1);

        bc1.animateY(2000, Easing.EasingOption.EaseInOutBack);
        bc2.animateY(2000, Easing.EasingOption.EaseInOutBack);

        bc1.setDescription("");
        bc1.setDescriptionTextSize(18f);

        bc2.setDescription("");
        bc2.setDescriptionTextSize(18f);

    }
}
