package com.example.shubhammundra.fromandto;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class DisplayChart extends AppCompatActivity {
    CombinedChart viewTimeChart;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_multilinechart);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        viewTimeChart = findViewById(R.id.combinedchart);

        Intent getTimeChartIntent = getIntent();

        ArrayList<String> getTimePOS = (ArrayList<String>) getTimeChartIntent.getSerializableExtra("Hour");
        ArrayList<Float> getTimeNetAmt = (ArrayList<Float>) getTimeChartIntent.getSerializableExtra("Net Amount");
        ArrayList<Float> getTimeAvgNetAmt = (ArrayList<Float>) getTimeChartIntent.getSerializableExtra("Avg Net Amount");
        String getStoreNo = getTimeChartIntent.getStringExtra("Store No.");

        TextView textView = findViewById(R.id.tv_storeNo);
        textView.setText(getStoreNo);

        ArrayList<BarEntry> NetAmount = new ArrayList<>();
        ArrayList<Entry> AvgNetAmount = new ArrayList<>();

        for (int i = 0 ; i < getTimeNetAmt.size() ; i++)
        {
            NetAmount.add(new BarEntry(getTimeNetAmt.get(i),i));
        }

        for (int i = 0 ; i < getTimeAvgNetAmt.size() ; i++)
        {
            AvgNetAmount.add(new Entry(getTimeAvgNetAmt.get(i),i));
        }

        LineDataSet lineDataSet = new LineDataSet(AvgNetAmount,"Avg Net Amount");
        lineDataSet.setColor(Color.BLUE);
        lineDataSet.setValueFormatter(new myValueFormatter());

        LineData lineData = new LineData(getTimePOS, lineDataSet);
        lineData.setValueTextSize(16f);
        lineData.setValueFormatter(new myValueFormatter());

        BarDataSet barDataSet = new BarDataSet(NetAmount,"Net Amount");
        barDataSet.setColor(Color.parseColor("#388E3C"));
        barDataSet.setValueFormatter(new myValueFormatter());

        BarData barData = new BarData(getTimePOS,barDataSet);
        barData.setValueTextSize(16f);
        barData.setValueFormatter(new myValueFormatter());

        CombinedData data = new CombinedData(getTimePOS);
        data.setData(lineData);
        data.setData(barData);

        viewTimeChart.setData(data);

        viewTimeChart.setDescription("");
    }
}
