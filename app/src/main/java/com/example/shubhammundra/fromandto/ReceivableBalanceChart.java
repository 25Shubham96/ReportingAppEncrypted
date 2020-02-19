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

public class ReceivableBalanceChart extends AppCompatActivity {

    LineChart lineChart1,lineChart2,lineChart3;
    ImageView im2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receivable_balance_chart);

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

        Intent intent = getIntent();

        ArrayList<Float> ReceBal = (ArrayList<Float>) intent.getSerializableExtra("ReceBal");
        ArrayList<String> Year1 = (ArrayList<String>) intent.getSerializableExtra("Year1");

        ArrayList<Float> ReceBeforeDue = (ArrayList<Float>) intent.getSerializableExtra("ReceBeforeDue");
        ArrayList<Float> ReceOverDue = (ArrayList<Float>) intent.getSerializableExtra("ReceOverDue");
        ArrayList<String> Year2 = (ArrayList<String>) intent.getSerializableExtra("Year2");

        ArrayList<Float> ReceOverDuePer = (ArrayList<Float>) intent.getSerializableExtra("ReceOverDuePer");
        ArrayList<String> Year3 = (ArrayList<String>) intent.getSerializableExtra("Year3");

        ArrayList<Entry> ReceivableBalance = new ArrayList<>();
        for (int i = 0 ; i < ReceBal.size() ; i++)
        {
            ReceivableBalance.add(new Entry(ReceBal.get(i),i));
        }

        LineDataSet lineDataSet1 = new LineDataSet(ReceivableBalance,"");
        lineDataSet1.setColor(Color.RED);
        lineDataSet1.setLineWidth(2f);
        lineDataSet1.setValueTextSize(16f);
        lineDataSet1.setValueTextColor(Color.BLACK);

        LineData lineData1 = new LineData(Year1,lineDataSet1);

        lineChart1.setData(lineData1);
        lineChart1.setDescription("");
        lineChart1.animateY(2000, Easing.EasingOption.EaseInCubic);

        Legend l = lineChart1.getLegend();
        l.setEnabled(false);
///////////////////
        ArrayList<Entry> ReceivableBeforeDue = new ArrayList<>();
        for (int i = 0 ; i < ReceBeforeDue.size() ; i++)
        {
            ReceivableBeforeDue.add(new Entry(ReceBeforeDue.get(i),i));
        }

        LineDataSet lineDataSet21 = new LineDataSet(ReceivableBeforeDue,"Before Due");
        lineDataSet21.setColor(Color.GREEN);
        lineDataSet21.setLineWidth(2f);
        lineDataSet21.setDrawFilled(true);
        lineDataSet21.setFillColor(Color.GREEN);
        lineDataSet21.setFillAlpha(255);
        lineDataSet21.setValueTextSize(16f);
        lineDataSet21.setValueTextColor(Color.BLACK);

        ArrayList<Entry> ReceivableOverDue = new ArrayList<>();
        for (int i = 0 ; i < ReceOverDue.size() ; i++)
        {
            ReceivableOverDue.add(new Entry(ReceOverDue.get(i),i));
        }

        LineDataSet lineDataSet22 = new LineDataSet(ReceivableOverDue,"OverDue");
        lineDataSet22.setColor(Color.BLUE);
        lineDataSet22.setLineWidth(2f);
        lineDataSet22.setDrawFilled(true);
        lineDataSet22.setFillColor(Color.BLUE);
        lineDataSet22.setFillAlpha(255);
        lineDataSet22.setValueTextSize(16f);
        lineDataSet22.setValueTextColor(Color.BLACK);

        ArrayList<LineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(lineDataSet22);
        lineDataSets.add(lineDataSet21);

        LineData lineData2 = new LineData(Year2,lineDataSets);

        lineChart2.setData(lineData2);
        lineChart2.setDescription("");
        lineChart2.animateY(2000, Easing.EasingOption.EaseInCubic);
////////////
        ArrayList<Entry> ReceivableOverDuePer = new ArrayList<>();
        for (int i = 0 ; i < ReceOverDuePer.size() ; i++)
        {
            ReceivableOverDuePer.add(new Entry(ReceOverDuePer.get(i),i));
        }

        LineDataSet lineDataSet3 = new LineDataSet(ReceivableOverDuePer,"");
        lineDataSet3.setColor(Color.RED);
        lineDataSet3.setLineWidth(2f);
        lineDataSet3.setValueTextSize(16f);
        lineDataSet3.setValueTextColor(Color.BLACK);

        LineData lineData3 = new LineData(Year3,lineDataSet3);

        lineChart3.setData(lineData3);
        lineChart3.setDescription("");
        lineChart3.animateY(2000, Easing.EasingOption.EaseInCubic);

        Legend l3 = lineChart3.getLegend();
        l3.setEnabled(false);
    }
}
