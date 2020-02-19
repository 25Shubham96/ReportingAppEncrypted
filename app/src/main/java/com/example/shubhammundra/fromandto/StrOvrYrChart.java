package com.example.shubhammundra.fromandto;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;


public class StrOvrYrChart extends AppCompatActivity{

    CombinedChart chartAmtPro;
    BarChart Proper;
    ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_over_year_chart);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        chartAmtPro = findViewById(R.id.combiChart);
        Proper = findViewById(R.id.barchart);

        Intent newIntent = getIntent();

        back = findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ArrayList<String> Months123 = (ArrayList<String>) newIntent.getSerializableExtra("Months");
        ArrayList<Float> NetAmt123 = (ArrayList<Float>) newIntent.getSerializableExtra("Net Amount");
        ArrayList<Float> Profit123 = (ArrayList<Float>) newIntent.getSerializableExtra("Profit");
        ArrayList<Float> ProfitPer123 = (ArrayList<Float>) newIntent.getSerializableExtra("Profit %");

        ArrayList<BarEntry> NetAmount = new ArrayList<>();
        ArrayList<Entry> AvgNetAmount = new ArrayList<>();

        for (int i = 0 ; i < NetAmt123.size() ; i++)
        {
            NetAmount.add(new BarEntry(NetAmt123.get(i),i));
        }

        for (int i = 0 ; i < Profit123.size() ; i++)
        {
            AvgNetAmount.add(new Entry(Profit123.get(i),i));
        }

        LineDataSet lineDataSet = new LineDataSet(AvgNetAmount,"Profit");
        lineDataSet.setColor(Color.BLUE);
        lineDataSet.setValueFormatter(new myValueFormatter());

        LineData lineData = new LineData(Months123, lineDataSet);
        lineData.setValueTextSize(16f);
        lineData.setValueFormatter(new myValueFormatter());

        BarDataSet barDataSet = new BarDataSet(NetAmount,"Net Amount");
        barDataSet.setColor(Color.parseColor("#388E3C"));
        barDataSet.setValueFormatter(new myValueFormatter());

        BarData barData = new BarData(Months123,barDataSet);
        barData.setValueTextSize(16f);
        barData.setValueFormatter(new myValueFormatter());

        CombinedData data = new CombinedData(Months123);
        data.setData(lineData);
        data.setData(barData);

        chartAmtPro.setData(data);

        chartAmtPro.setDescription("");

        //Second Report

        ArrayList<BarEntry> yEntrys = new ArrayList<>();

        for(int i = 0; i < ProfitPer123.size(); i++){
            yEntrys.add(new BarEntry(ProfitPer123.get(i),i));
        }

        BarDataSet barDataSet1 = new BarDataSet(yEntrys,"");

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

        barDataSet1.setColors(color12);
        barDataSet1.setValueTextSize(14f);
        barDataSet1.setValueFormatter(new myValueFormatter());

///////////
        ArrayList<Integer> myColor = new ArrayList<>();
        for (int w = 0 ; w < color12.length ; w++)
        {
            myColor.add(color12[w]);
        }

        int c = 0;

        while(myColor.size() < Months123.size())
        {
            myColor.add(myColor.get(c));
            c++;
        }

        ArrayList<Integer> newColor = new ArrayList<>();

        for (int k = 0 ; k < Months123.size() ; k++)
        {
            newColor.add(myColor.get(k));
        }
///////////

        Legend l = Proper.getLegend();
        l.setCustom(newColor,Months123);
        l.setWordWrapEnabled(true);

        BarData barData1 = new BarData(Months123, barDataSet1);
        barData1.setValueFormatter(new myValueFormatter());

        Proper.setData(barData1);
        Proper.animateY(2000, Easing.EasingOption.EaseInOutBack);

        Proper.setDescription("");
        Proper.setDescriptionTextSize(18f);
    }
}
