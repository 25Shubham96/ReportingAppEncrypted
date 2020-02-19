package com.example.shubhammundra.fromandto;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class PayByTenderChart extends AppCompatActivity {

    LineChart lineChart;

    ArrayList<String> YearData = new ArrayList<>();
    ArrayList<Float> NormalData = new ArrayList<>();
    ArrayList<Float> CardData = new ArrayList<>();
    ArrayList<Float> CustomerData = new ArrayList<>();

    ArrayList<ArrayList<Float>> GetCostAmount = new ArrayList<>();
    ArrayList<String> GetQuaterYear = new ArrayList<>();
    ArrayList<String> GetTenderTypCode = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_by_tender_chart);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        lineChart = findViewById(R.id.linechart);

        Intent i = getIntent();

        YearData = (ArrayList<String>) i.getSerializableExtra("Year");
        NormalData = (ArrayList<Float>) i.getSerializableExtra("Normal");
        CardData = (ArrayList<Float>) i.getSerializableExtra("Card");
        CustomerData = (ArrayList<Float>) i.getSerializableExtra("Customer");

        GetTenderTypCode = (ArrayList<String>) i.getSerializableExtra("TenderTyp");
        GetCostAmount = (ArrayList<ArrayList<Float>>) i.getSerializableExtra("Amount");
        GetQuaterYear = (ArrayList<String>) i.getSerializableExtra("QuatYear");

        ArrayList<Entry> PayNormalAmt = new ArrayList<>();
        ArrayList<Entry> PayCardAmt = new ArrayList<>();
        ArrayList<Entry> PayCustomerAmt = new ArrayList<>();

        for (int j = 0 ; j < NormalData.size() ; j++)
        {
            PayNormalAmt.add(new Entry(NormalData.get(j),j));
        }

        for (int k = 0 ; k < CardData.size() ; k ++)
        {
            PayCardAmt.add(new Entry(CardData.get(k),k));
        }

        for (int m = 0 ; m < CustomerData.size() ; m ++)
        {
            PayCustomerAmt.add(new Entry(CustomerData.get(m),m));
        }

        LineDataSet lineDataSet1 = new LineDataSet(PayNormalAmt,"Normal");
        lineDataSet1.setColor(Color.BLUE);
        lineDataSet1.setLineWidth(3f);
        lineDataSet1.setValueTextSize(18f);

        LineDataSet lineDataSet2 = new LineDataSet(PayCardAmt,"Card");
        lineDataSet2.setColor(Color.GREEN);
        lineDataSet2.setLineWidth(3f);
        lineDataSet2.setValueTextSize(18f);

        LineDataSet lineDataSet3 = new LineDataSet(PayCustomerAmt,"Customer");
        lineDataSet3.setColor(Color.RED);
        lineDataSet3.setLineWidth(3f);
        lineDataSet3.setValueTextSize(18f);

        ArrayList<LineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(lineDataSet1);
        lineDataSets.add(lineDataSet2);
        lineDataSets.add(lineDataSet3);

        LineData lineData = new LineData(YearData,lineDataSets);

        lineChart.setData(lineData);
        lineChart.setDescription("");
        lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);

        LineChart lineChart1 = findViewById(R.id.linechart1);

        ArrayList<ArrayList<Entry>> diffLocations = new ArrayList<>();

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

        for (int k = 0 ; k < GetCostAmount.size() ; k++)
        {
            diffLocations.add(new ArrayList<Entry>());

            for (int j = 0 ; j < GetCostAmount.get(k).size() ; j++)
            {
                diffLocations.get(k).add(new BarEntry(GetCostAmount.get(k).get(j),j));
            }

            LineDataSet lineDataSet = new LineDataSet(diffLocations.get(k),"" + GetTenderTypCode.get(k));
            lineDataSet.setValueTextSize(12f);
            lineDataSet.setLineWidth(3f);
            lineDataSet.setColor(BarColor[k]);
            multiLine.add(lineDataSet);
        }

        Legend l = lineChart1.getLegend();
        l.setWordWrapEnabled(true);

        LineData lineData1 = new LineData(GetQuaterYear, multiLine);
        lineChart1.setData(lineData1);
        lineChart1.setDescription("");
        lineChart1.getXAxis().setTextSize(14f);
        lineChart1.animateY(2000, Easing.EasingOption.EaseInOutBack);

    }
}
