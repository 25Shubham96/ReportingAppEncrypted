package com.example.shubhammundra.fromandto;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class ViewBarChart extends KPIAnalysis{

    ImageView im2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kpi_chart);

        barChart = findViewById(R.id.barchart);
        barChart1 = findViewById(R.id.barchart2);
        barChart2 = findViewById(R.id.barchart3);
        barChart3 = findViewById(R.id.barchart4);

        im2 = findViewById(R.id.img1);

        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        Intent intent = getIntent();

        ArrayList<Float> netAmt = (ArrayList<Float>) intent.getSerializableExtra("Net Amount");
        ArrayList<String> year1 = (ArrayList<String>) intent.getSerializableExtra("Hour");

        ArrayList<Float> avgnetamt = (ArrayList<Float>) intent.getSerializableExtra("Avg Net Amount");
        ArrayList<String> year2 = (ArrayList<String>) intent.getSerializableExtra("Hour");

        ArrayList<Float> noOfreturnsales = (ArrayList<Float>) intent.getSerializableExtra("No Of Return Sales");
        ArrayList<String> year3 = (ArrayList<String>) intent.getSerializableExtra("Hour");

        ArrayList<Float> avgItemLine = (ArrayList<Float>) intent.getSerializableExtra("Avg Item Lines");
        ArrayList<String> year4 = (ArrayList<String>) intent.getSerializableExtra("Hour");


        ArrayList<BarEntry> yEntrys = new ArrayList<>();
        for(int i = 0; i < netAmt.size(); i++){
            yEntrys.add(new BarEntry(netAmt.get(i),i));
        }

        ArrayList<BarEntry> y1Entrys = new ArrayList<>();
        for(int i = 0; i < avgnetamt.size(); i++){
            y1Entrys.add(new BarEntry(avgnetamt.get(i),i));
        }

        ArrayList<BarEntry> y2Entrys = new ArrayList<>();
        for(int i = 0; i < noOfreturnsales.size(); i++){
            yEntrys.add(new BarEntry(noOfreturnsales.get(i),i));
        }

        ArrayList<BarEntry> y3Entrys = new ArrayList<>();
        for(int i = 0; i < avgItemLine.size(); i++){
            y1Entrys.add(new BarEntry(avgItemLine.get(i),i));
        }


        BarDataSet barDataSet = new BarDataSet(yEntrys,"");
        BarDataSet barDataSet1 = new BarDataSet(y1Entrys, "");
        BarDataSet barDataSet2 = new BarDataSet(y2Entrys,"");
        BarDataSet barDataSet3 = new BarDataSet(y3Entrys, "");

        int[] color12 = {Color.parseColor("#D32F2F"),
                Color.parseColor("#C2185B"),
                Color.parseColor("#7B1FA2"),
                Color.parseColor("#512DA8"),
                Color.parseColor("#303F9F"),
                Color.parseColor("#1976D2"),
                Color.parseColor("#0288D1"),
                Color.parseColor("#0097A7"),
                Color.parseColor("#00796B"),
                Color.parseColor("#388E3C"),
                Color.parseColor("#689F38"),
                Color.parseColor("#AFB42B"),
                Color.parseColor("#FBC02D"),
                Color.parseColor("#FFA000"),
                Color.parseColor("#F57C00"),
                Color.parseColor("#E64A19"),
                Color.parseColor("#5D4037"),
                Color.parseColor("#616161"),
                Color.parseColor("#455A64")};


        barDataSet.setColors(color12);
        barDataSet.setValueTextSize(14f);

        barDataSet1.setColors(color12);
        barDataSet1.setValueTextSize(14f);

        barDataSet2.setColors(color12);
        barDataSet2.setValueTextSize(14f);

        barDataSet3.setColors(color12);
        barDataSet3.setValueTextSize(14f);

        BarData barData = new BarData(year1, barDataSet);
        BarData barData1 = new BarData(year2, barDataSet1);
        BarData barData2 = new BarData(year3, barDataSet2);
        BarData barData3 = new BarData(year4, barDataSet3);


        barChart.setData(barData);
        barChart1.setData(barData1);
        barChart2.setData(barData2);
        barChart3.setData(barData3);

        barChart.animateY(2000, Easing.EasingOption.EaseInOutBack);
        barChart.setDescription("Net Amount");
        barChart.setDescriptionTextSize(18f);

        barChart1.animateY(2000, Easing.EasingOption.EaseInOutBack);
        barChart1.setDescription("Average Net Amount");
        barChart1.setDescriptionTextSize(18f);

        barChart2.animateY(2000, Easing.EasingOption.EaseInOutBack);
        barChart2.setDescription("Number Of Return Sales");
        barChart2.setDescriptionTextSize(18f);

        barChart3.animateY(2000, Easing.EasingOption.EaseInOutBack);
        barChart3.setDescription("Average Item Lines");
        barChart3.setDescriptionTextSize(18f);

    }
}
