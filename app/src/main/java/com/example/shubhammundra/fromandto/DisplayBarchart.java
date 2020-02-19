package com.example.shubhammundra.fromandto;

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

public class DisplayBarchart extends AppCompatActivity{
    BarChart barChart;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_barchart);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        /*imageView = findViewById(R.id.img11);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/

        barChart = findViewById(R.id.barchart);

        Intent intent = getIntent();

        ArrayList<Float> y11Data = (ArrayList<Float>) intent.getSerializableExtra("Y1 Values");
        ArrayList<String> x11Data = (ArrayList<String>) intent.getSerializableExtra("X1 Values");

        ArrayList<BarEntry> y1Entrys = new ArrayList<>();

        for(int i = 0; i < y11Data.size(); i++){
            y1Entrys.add(new BarEntry(y11Data.get(i),i));
        }

        BarDataSet barDataSet = new BarDataSet(y1Entrys,"");

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

        ArrayList<Integer> myColor = new ArrayList<>();
        for (int w = 0 ; w < color11.length ; w++)
        {
            myColor.add(color11[w]);
        }

        int c = 0;

        while(myColor.size() < x11Data.size())
        {
            myColor.add(myColor.get(c));
            c++;
        }

        ArrayList<Integer> newColor = new ArrayList<>();

        for (int k = 0 ; k < x11Data.size() ; k++)
        {
            newColor.add(myColor.get(k));
        }

        barDataSet.setColors(color11);
        barDataSet.setValueTextSize(14f);
        barDataSet.setValueFormatter(new myValueFormatter());

        Legend l = barChart.getLegend();
        l.setCustom(newColor,x11Data);
        l.setWordWrapEnabled(true);

        BarData barData = new BarData(x11Data, barDataSet);
        barData.setValueFormatter(new myValueFormatter());

        barChart.setData(barData);
        barChart.animateY(2000, Easing.EasingOption.EaseInOutBack);

        barChart.setDescription("");
        barChart.setDescriptionTextSize(18f);
    }
}
