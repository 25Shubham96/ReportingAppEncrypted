package com.example.shubhammundra.fromandto;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class ReceivableSalesChart extends AppCompatActivity {

    LineChart lineChart1,lineChart2,lineChart3,lineChart4;
    ImageView im2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receivable_sales_chart);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        im2 = findViewById(R.id.img11);

        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lineChart1 = findViewById(R.id.linechart1);
        lineChart2 = findViewById(R.id.linechart2);
        lineChart3 = findViewById(R.id.linechart3);
        lineChart4 = findViewById(R.id.linechart4);

        Intent intent = getIntent();

        ArrayList<Float> ReceSale = (ArrayList<Float>) intent.getSerializableExtra("ReceSales");
        ArrayList<String> Year1 = (ArrayList<String>) intent.getSerializableExtra("Year1");

        ArrayList<Float> ReceSaleCre = (ArrayList<Float>) intent.getSerializableExtra("ReceSalesCre");
        ArrayList<String> Year2 = (ArrayList<String>) intent.getSerializableExtra("Year2");

        ArrayList<Float> ReceSalesCrePer = (ArrayList<Float>) intent.getSerializableExtra("ReceSaleCrePer");

        ArrayList<Float> ReceSalesPer = (ArrayList<Float>) intent.getSerializableExtra("ReceSalesPer");

        ArrayList<Entry> ReceivableSales = new ArrayList<>();
        for (int i = 0 ; i < ReceSale.size() ; i++)
        {
            ReceivableSales.add(new Entry(ReceSale.get(i),i));
        }

        LineDataSet lineDataSet1 = new LineDataSet(ReceivableSales,"");
        lineDataSet1.setColor(Color.GREEN);
        lineDataSet1.setLineWidth(2f);
        lineDataSet1.setValueTextSize(16f);
        lineDataSet1.setValueTextColor(Color.BLACK);

        LineData lineData1 = new LineData(Year1,lineDataSet1);

        lineChart1.setData(lineData1);
        lineChart1.setDescription("");
        lineChart1.animateY(2000, Easing.EasingOption.EaseInCubic);

        Legend l = lineChart1.getLegend();
        l.setEnabled(false);

        ArrayList<Entry> ReceivableSAlesCredit = new ArrayList<>();
        for (int i = 0 ; i < ReceSaleCre.size() ; i++)
        {
            ReceivableSAlesCredit.add(new Entry(ReceSaleCre.get(i),i));
        }

        LineDataSet lineDataSet2 = new LineDataSet(ReceivableSAlesCredit,"");
        lineDataSet2.setColor(Color.GREEN);
        lineDataSet2.setLineWidth(2f);
        lineDataSet2.setValueTextSize(16f);
        lineDataSet2.setValueTextColor(Color.BLACK);

        LineData lineData2 = new LineData(Year2,lineDataSet2);

        lineChart2.setData(lineData2);
        lineChart2.setDescription("");
        lineChart2.animateY(2000, Easing.EasingOption.EaseInCubic);

        Legend l2 = lineChart2.getLegend();
        l2.setEnabled(false);

        ArrayList<Entry> ReceivableSAlesCreditPercen = new ArrayList<>();
        for (int i = 0 ; i < ReceSalesCrePer.size() ; i++)
        {
            ReceivableSAlesCreditPercen.add(new Entry(ReceSalesCrePer.get(i),i));
        }

        LineDataSet lineDataSet3 = new LineDataSet(ReceivableSAlesCreditPercen,"");
        lineDataSet3.setColor(Color.GREEN);
        lineDataSet3.setLineWidth(2f);
        lineDataSet3.setValueTextSize(16f);
        lineDataSet3.setValueTextColor(Color.BLACK);

        LineData lineData3 = new LineData(Year1,lineDataSet3);

        lineChart3.setData(lineData3);
        lineChart3.setDescription("");
        lineChart3.animateY(2000, Easing.EasingOption.EaseInCubic);

        Legend l3 = lineChart3.getLegend();
        l3.setEnabled(false);

        ArrayList<Entry> ReceivableSalesPercen = new ArrayList<>();
        for (int i = 0 ; i < ReceSalesPer.size() ; i++)
        {
            ReceivableSalesPercen.add(new Entry(ReceSalesPer.get(i),i));
        }

        LineDataSet lineDataSet4 = new LineDataSet(ReceivableSalesPercen,"");
        lineDataSet4.setColor(Color.GREEN);
        lineDataSet4.setLineWidth(2f);
        lineDataSet4.setValueTextSize(16f);
        lineDataSet4.setValueTextColor(Color.BLACK);

        LineData lineData4 = new LineData(Year1,lineDataSet4);

        lineChart4.setData(lineData4);
        lineChart4.setDescription("");
        lineChart4.animateY(2000, Easing.EasingOption.EaseInCubic);

        Legend l4 = lineChart4.getLegend();
        l4.setEnabled(false);
    }
}
