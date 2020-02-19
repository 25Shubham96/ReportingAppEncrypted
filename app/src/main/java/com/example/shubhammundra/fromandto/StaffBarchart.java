package com.example.shubhammundra.fromandto;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class StaffBarchart extends AppCompatActivity{

    CombinedChart cc1,cc2;

    ScrollView scrollView;

    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_bar_chart);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        imageView = findViewById(R.id.img11);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cc1 = findViewById(R.id.CombiChart1);
        cc2 = findViewById(R.id.CombiChart2);

        ArrayList<String> Cashier;
        ArrayList<Float> CashNetAmt;
        ArrayList<Float> CashDisc;
        ArrayList<String> Manager;
        ArrayList<Float> ManNetAmt;
        ArrayList<Float> AvgNetAmt;

        Intent getReceiptintent = getIntent();

        Cashier = (ArrayList<String>) getReceiptintent.getSerializableExtra("Cashier Name");
        CashNetAmt = (ArrayList<Float>) getReceiptintent.getSerializableExtra("Net Amount");
        CashDisc = (ArrayList<Float>) getReceiptintent.getSerializableExtra("Discount Percen");

        Manager = (ArrayList<String>) getReceiptintent.getSerializableExtra("Mani Name");
        ManNetAmt = (ArrayList<Float>) getReceiptintent.getSerializableExtra("Mani Net Amount");
        AvgNetAmt = (ArrayList<Float>) getReceiptintent.getSerializableExtra("Avg Net Amt");

        ArrayList<Entry> CashierNetAmount = new ArrayList<>();
        ArrayList<BarEntry> Discount = new ArrayList<>();

        for (int i = 0 ; i < CashNetAmt.size() ; i++)
        {
            CashierNetAmount.add(new Entry(CashNetAmt.get(i),i));
        }

        for (int i = 0 ; i < CashDisc.size() ; i++)
        {
            Discount.add(new BarEntry(CashDisc.get(i),i));
        }

        LineDataSet lineDataSet = new LineDataSet(CashierNetAmount,"Net Amount");
        lineDataSet.setColor(Color.BLUE);
        lineDataSet.setValueFormatter(new myValueFormatter());

        LineData lineData = new LineData(Cashier, lineDataSet);
        lineData.setValueTextSize(16f);
        lineData.setValueFormatter(new myValueFormatter());

        BarDataSet barDataSet = new BarDataSet(Discount,"Discount");
        barDataSet.setColor(Color.parseColor("#388E3C"));
        barDataSet.setValueFormatter(new myValueFormatter());

        BarData barData = new BarData(Cashier,barDataSet);
        barData.setValueTextSize(16f);
        barData.setValueFormatter(new myValueFormatter());

        CombinedData data = new CombinedData(Cashier);
        data.setData(lineData);
        data.setData(barData);

        cc1.setData(data);

        cc1.setDescription("");

        ArrayList<Entry> ManagerNetAmount = new ArrayList<>();
        ArrayList<BarEntry> AverageNetAmt = new ArrayList<>();

        for (int i = 0 ; i < ManNetAmt.size() ; i++)
        {
            ManagerNetAmount.add(new Entry(ManNetAmt.get(i),i));
        }

        for (int i = 0 ; i < AvgNetAmt.size() ; i++)
        {
            AverageNetAmt.add(new BarEntry(AvgNetAmt.get(i),i));
        }

        LineDataSet lineDataSet1 = new LineDataSet(ManagerNetAmount,"Net Amount");
        lineDataSet1.setColor(Color.BLUE);
        lineDataSet1.setValueFormatter(new myValueFormatter());

        LineData lineData1 = new LineData(Manager, lineDataSet1);
        lineData1.setValueTextSize(16f);
        lineData1.setValueFormatter(new myValueFormatter());

        BarDataSet barDataSet1 = new BarDataSet(AverageNetAmt,"Avg Net Amt");
        barDataSet1.setColor(Color.parseColor("#388E3C"));
        barDataSet1.setValueFormatter(new myValueFormatter());

        BarData barData1 = new BarData(Manager,barDataSet1);
        barData1.setValueTextSize(16f);
        barData1.setValueFormatter(new myValueFormatter());

        CombinedData data1 = new CombinedData(Manager);
        data1.setData(lineData1);
        data1.setData(barData1);

        cc2.setData(data1);

        cc2.setDescription("");

    }
}
