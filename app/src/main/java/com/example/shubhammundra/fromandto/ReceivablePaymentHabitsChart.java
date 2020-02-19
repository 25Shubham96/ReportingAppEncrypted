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

public class ReceivablePaymentHabitsChart extends AppCompatActivity {

    LineChart lineChart1,lineChart2;
    ImageView im2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receivable_payment_habits_chart);

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

        Intent intent = getIntent();

        ArrayList<Float> ReceAvgPayTrm = (ArrayList<Float>) intent.getSerializableExtra("ReceAvgPayTrm");
        ArrayList<String> Year1 = (ArrayList<String>) intent.getSerializableExtra("Year1");

        ArrayList<Float> ReceAvgOverDue = (ArrayList<Float>) intent.getSerializableExtra("ReceAvgOvrdue");
        ArrayList<String> Year2 = (ArrayList<String>) intent.getSerializableExtra("Year2");

        ArrayList<Entry> ReceivableAveragePayTerm = new ArrayList<>();
        for (int i = 0 ; i < ReceAvgPayTrm.size() ; i++)
        {
            ReceivableAveragePayTerm.add(new Entry(ReceAvgPayTrm.get(i),i));
        }

        LineDataSet lineDataSet1 = new LineDataSet(ReceivableAveragePayTerm,"");
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

        ArrayList<Entry> ReceivableAverageOverDue = new ArrayList<>();
        for (int i = 0 ; i < ReceAvgOverDue.size() ; i++)
        {
            ReceivableAverageOverDue.add(new Entry(ReceAvgOverDue.get(i),i));
        }

        LineDataSet lineDataSet2 = new LineDataSet(ReceivableAverageOverDue,"");
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
    }
}
