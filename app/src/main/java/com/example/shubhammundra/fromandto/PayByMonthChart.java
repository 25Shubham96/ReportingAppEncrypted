package com.example.shubhammundra.fromandto;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.security.KeyStore;
import java.util.ArrayList;

public class PayByMonthChart extends AppCompatActivity {

    LineChart myLineChart;

    ArrayList<String> YearMonthData = new ArrayList<>();
    ArrayList<Float> NetAmtData = new ArrayList<>();
    ArrayList<Float> AvgNetAmtData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_by_month_chart);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        myLineChart = findViewById(R.id.linechart);

        Intent i = getIntent();

        YearMonthData = (ArrayList<String>) i.getSerializableExtra("YearMonth");
        NetAmtData = (ArrayList<Float>) i.getSerializableExtra("NetAmt");
        AvgNetAmtData = (ArrayList<Float>) i.getSerializableExtra("AvgNetAmt");

        ArrayList<Entry> PayMonAmt = new ArrayList<>();
        ArrayList<Entry> PayMonAvgAmt = new ArrayList<>();

        for (int j = 0 ; j < NetAmtData.size() ; j ++)
        {
            PayMonAmt.add(new Entry(NetAmtData.get(j),j));
        }

        for (int k = 0 ; k < AvgNetAmtData.size() ; k ++)
        {
            PayMonAvgAmt.add(new Entry(AvgNetAmtData.get(k),k));
        }

        LineDataSet lineDataSet1 = new LineDataSet(PayMonAmt,"Net Amt");
        lineDataSet1.setColor(Color.BLUE);
        lineDataSet1.setValueTextSize(18f);
        LineDataSet lineDataSet2 = new LineDataSet(PayMonAvgAmt,"Avg Net Amt");
        lineDataSet2.setColor(Color.GREEN);
        lineDataSet2.setValueTextSize(18f);

        ArrayList<LineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(lineDataSet1);
        lineDataSets.add(lineDataSet2);

        LineData lineData = new LineData(YearMonthData,lineDataSets);

        myLineChart.setData(lineData);
        myLineChart.setDescription("");
        myLineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
    }
}
