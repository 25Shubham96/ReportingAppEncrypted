package com.example.shubhammundra.fromandto;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

@SuppressLint("Registered")
public class DiscSpecChart extends AppCompatActivity
{

    ImageView back;
    CombinedChart discSpecChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disc_spec_chart);

        back = findViewById(R.id.img11);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        discSpecChart = findViewById(R.id.combinedchart_discSpec);

        Intent getTimeChartIntent = getIntent();

        ArrayList<String> getTimePOS = (ArrayList<String>) getTimeChartIntent.getSerializableExtra("Discount Type");
        ArrayList<Float> getTimeNetAmt = (ArrayList<Float>) getTimeChartIntent.getSerializableExtra("Discount %");
        ArrayList<Float> getTimeAvgNetAmt = (ArrayList<Float>) getTimeChartIntent.getSerializableExtra("Net Amount");

        ArrayList<Entry> NetAmount = new ArrayList<>();
        ArrayList<BarEntry> AvgNetAmount = new ArrayList<>();

        for (int i = 0 ; i < getTimeNetAmt.size() ; i++)
        {
            NetAmount.add(new Entry(getTimeNetAmt.get(i),i));
        }

        for (int i = 0 ; i < getTimeAvgNetAmt.size() ; i++)
        {
            AvgNetAmount.add(new BarEntry(getTimeAvgNetAmt.get(i),i));
        }

        LineDataSet lineDataSet = new LineDataSet(NetAmount,"Net Amount");
        lineDataSet.setColor(Color.BLUE);
        lineDataSet.setValueFormatter(new myValueFormatter());

        LineData lineData = new LineData(getTimePOS, lineDataSet);
        lineData.setValueTextSize(16f);
        lineData.setValueFormatter(new myValueFormatter());

        BarDataSet barDataSet = new BarDataSet(AvgNetAmount,"Avg Net Amount");
        barDataSet.setColor(Color.parseColor("#388E3C"));
        barDataSet.setValueFormatter(new myValueFormatter());

        BarData barData = new BarData(getTimePOS,barDataSet);
        barData.setValueTextSize(16f);
        barData.setValueFormatter(new myValueFormatter());

        CombinedData data = new CombinedData(getTimePOS);
        data.setData(lineData);
        data.setData(barData);

        discSpecChart.setData(data);

        discSpecChart.setDescription("");
    }
}
