package com.example.shubhammundra.fromandto;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.charts.LineChart;
import java.util.ArrayList;


public class SalesChart extends AppCompatActivity {

    ArrayList<ArrayList<Float>> GetNetAmount = new ArrayList<>();
    ArrayList<String> GetStoreNo = new ArrayList<>();
    ArrayList<String> GetHours = new ArrayList<>();

    ArrayList<ArrayList<Float>> GetNetAmount2 = new ArrayList<>();
    ArrayList<String> GetHours2 = new ArrayList<>();

    ArrayList<ArrayList<Float>> GetNetAmount3 = new ArrayList<>();
    ArrayList<String> GetMonth = new ArrayList<>();
    ArrayList<String> GetHours3 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_chart);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Intent intent = getIntent();

        GetNetAmount = (ArrayList<ArrayList<Float>>) intent.getSerializableExtra("Net Amount1");
        GetHours = (ArrayList<String>) intent.getSerializableExtra("Hours1");
        GetStoreNo = (ArrayList<String>) intent.getSerializableExtra("Store No");

        GetNetAmount2 = (ArrayList<ArrayList<Float>>) intent.getSerializableExtra("Net Amount2");
        GetHours2 = (ArrayList<String>) intent.getSerializableExtra("Hours2");

        GetNetAmount3 = (ArrayList<ArrayList<Float>>) intent.getSerializableExtra("Net Amount3");
        GetHours3 = (ArrayList<String>) intent.getSerializableExtra("Hours3");
        GetMonth = (ArrayList<String>) intent.getSerializableExtra("Month");

        LineChart lineChart = findViewById(R.id.linechart1);

        ArrayList<ArrayList<Entry>> diffStores = new ArrayList<>();

        ArrayList<LineDataSet> multiLine = new ArrayList<>();

        int[] BarColor = {Color.parseColor("#e6194b"),
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
                Color.parseColor("#808080"),
                Color.parseColor("#e6194b"),
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

        for (int i = 0 ; i < GetNetAmount.size() ; i++)
        {
            diffStores.add(new ArrayList<Entry>());

            for (int j = 0 ; j < GetNetAmount.get(i).size() ; j++)
            {
                diffStores.get(i).add(new Entry(GetNetAmount.get(i).get(j),j));
            }

            LineDataSet lineDataSet = new LineDataSet(diffStores.get(i),"" + GetStoreNo.get(i));
            lineDataSet.setValueTextSize(12f);
            lineDataSet.setLineWidth(3f);
            lineDataSet.setColor(BarColor[i]);
            lineDataSet.setValueFormatter(new myValueFormatter());
            multiLine.add(lineDataSet);
        }

        LineData lineData = new LineData(GetHours, multiLine);
        lineData.setValueFormatter(new myValueFormatter());

        lineChart.setData(lineData);
        lineChart.setDescription("");
        lineChart.getXAxis().setTextSize(14f);
        lineChart.animateY(2000, Easing.EasingOption.EaseInOutBack);



        LineChart lineChart2 = findViewById(R.id.linechart2);

        ArrayList<ArrayList<Entry>> diffStores2 = new ArrayList<>();

        ArrayList<LineDataSet> multiLine2 = new ArrayList<>();

        for (int i = 0 ; i < GetNetAmount2.size() ; i++)
        {
            diffStores2.add(new ArrayList<Entry>());

            for (int j = 0 ; j < GetNetAmount2.get(i).size() ; j++)
            {
                diffStores2.get(i).add(new Entry(GetNetAmount2.get(i).get(j),j));
            }

            LineDataSet lineDataSet2 = new LineDataSet(diffStores2.get(i),"" + GetStoreNo.get(i));
            lineDataSet2.setValueTextSize(12f);
            lineDataSet2.setLineWidth(3f);
            lineDataSet2.setColor(BarColor[i]);
            lineDataSet2.setValueFormatter(new myValueFormatter());
            multiLine2.add(lineDataSet2);
        }

        LineData lineData2 = new LineData(GetHours2, multiLine2);
        lineData2.setValueFormatter(new myValueFormatter());

        lineChart2.setData(lineData2);
        lineChart2.setDescription("");
        lineChart2.getXAxis().setTextSize(14f);
        lineChart2.animateY(2000, Easing.EasingOption.EaseInOutBack);



        LineChart lineChart3 = findViewById(R.id.linechart3);

        ArrayList<ArrayList<Entry>> diffMonth = new ArrayList<>();

        ArrayList<LineDataSet> multiLine3 = new ArrayList<>();

        for (int i = 0 ; i < GetNetAmount3.size() ; i++)
        {
            diffMonth.add(new ArrayList<Entry>());

            for (int j = 0 ; j < GetNetAmount3.get(i).size() ; j++)
            {
                diffMonth.get(i).add(new Entry(GetNetAmount3.get(i).get(j),j));
            }

            LineDataSet lineDataSet3 = new LineDataSet(diffMonth.get(i),"" + GetMonth.get(i));
            lineDataSet3.setValueTextSize(12f);
            lineDataSet3.setLineWidth(3f);
            lineDataSet3.setColor(BarColor[i]);
            lineDataSet3.setValueFormatter(new myValueFormatter());
            multiLine3.add(lineDataSet3);
        }

        LineData lineData3 = new LineData(GetHours3, multiLine3);
        lineData3.setValueFormatter(new myValueFormatter());

        lineChart3.setData(lineData3);
        lineChart3.setDescription("");
        lineChart3.getXAxis().setTextSize(14f);
        lineChart3.animateY(2000, Easing.EasingOption.EaseInOutBack);
    }
}
