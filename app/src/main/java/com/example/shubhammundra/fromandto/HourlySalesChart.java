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
public class HourlySalesChart extends AppCompatActivity {

    CombinedChart chartAmtAvgamt;

    ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hourly_sales_chart);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        chartAmtAvgamt = findViewById(R.id.combiChart);

        Intent newIntent = getIntent();

        ArrayList<String> Time123 = (ArrayList<String>) newIntent.getSerializableExtra("Time");
        ArrayList<Float> NetAmt123 = (ArrayList<Float>) newIntent.getSerializableExtra("Net Amt");
        ArrayList<Float> AvgNetAmt123 = (ArrayList<Float>) newIntent.getSerializableExtra("Avg Net Amt");

        ArrayList<BarEntry> NetAmount = new ArrayList<>();
        ArrayList<Entry> AvgNetAmount = new ArrayList<>();

        back = findViewById(R.id.img11);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        for (int i = 0 ; i < AvgNetAmt123.size() ; i++)
        {
            AvgNetAmount.add(new Entry(AvgNetAmt123.get(i),i));
        }

        for (int i = 0 ; i < NetAmt123.size() ; i++)
        {
            NetAmount.add(new BarEntry(NetAmt123.get(i),i));
        }

        LineDataSet lineDataSet = new LineDataSet(AvgNetAmount,"Net Amount");
        lineDataSet.setColor(Color.BLUE);
        lineDataSet.setValueFormatter(new myValueFormatter());

        LineData lineData = new LineData(Time123, lineDataSet);
        lineData.setValueTextSize(16f);
        lineData.setValueFormatter(new myValueFormatter());

        BarDataSet barDataSet = new BarDataSet(NetAmount,"Avg Net Amount");
        barDataSet.setColor(Color.parseColor("#388E3C"));
        barDataSet.setValueFormatter(new myValueFormatter());

        BarData barData = new BarData(Time123,barDataSet);
        barData.setValueTextSize(16f);
        barData.setValueFormatter(new myValueFormatter());

        CombinedData data = new CombinedData(Time123);
        data.setData(lineData);
        data.setData(barData);

        chartAmtAvgamt.setData(data);

        chartAmtAvgamt.setDescription("");
    }
}
