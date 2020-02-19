package com.example.shubhammundra.fromandto;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class KPIAnalysisChart extends AppCompatActivity {

    BarChart b1,b2,b3,b4;
    Button Scroll,ScrollUp;

    ScrollView scrollView;

    ImageView im2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kpi_analysis_chart);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        scrollView = findViewById(R.id.sv_payment);

        Scroll = findViewById(R.id.btn_scroll);
        ScrollUp = findViewById(R.id.btn_scrollup);

        Scroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.scrollTo(scrollView.getScrollX(),scrollView.getScrollY() + 855);
            }
        });

        ScrollUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.scrollTo(scrollView.getScrollX(),scrollView.getScrollY() - 855);
            }
        });

        im2 = findViewById(R.id.img11);

        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        b1 = findViewById(R.id.barchart1);
        b2 = findViewById(R.id.barchart2);
        b3 = findViewById(R.id.barchart3);
        b4 = findViewById(R.id.barchart4);

        Intent getReceiptintent = getIntent();

        ArrayList<String> Hour = (ArrayList<String>) getReceiptintent.getSerializableExtra("Hour");
        ArrayList<String> Hour2 = (ArrayList<String>) getReceiptintent.getSerializableExtra("Hour 2");
        ArrayList<String> Hour3 = (ArrayList<String>) getReceiptintent.getSerializableExtra("Hour 3");
        ArrayList<Float> NetAmt = (ArrayList<Float>) getReceiptintent.getSerializableExtra("Net Amount");
        ArrayList<Float> AvgNetAmt = (ArrayList<Float>) getReceiptintent.getSerializableExtra("Avg Net Amount");
        ArrayList<Float> ReturnSale = (ArrayList<Float>) getReceiptintent.getSerializableExtra("Return Sales");
        ArrayList<Float> ItemLine = (ArrayList<Float>) getReceiptintent.getSerializableExtra("Avg Item Lines");

        int[] ReceiptColor = {Color.parseColor("#e6194b"),
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
//1
        ArrayList<BarEntry> CWS = new ArrayList<>();
        for(int i = 0; i < NetAmt.size(); i++){
            CWS.add(new BarEntry(NetAmt.get(i),i));
        }

        BarDataSet bds1 = new BarDataSet(CWS,"");
        bds1.setColors(ReceiptColor);
        bds1.setValueTextSize(14f);
        bds1.setValueFormatter(new myValueFormatter());

///////////
        ArrayList<Integer> myColor = new ArrayList<>();
        for (int w = 0 ; w < ReceiptColor.length ; w++)
        {
            myColor.add(ReceiptColor[w]);
        }

        int c = 0;

        while(myColor.size() < Hour.size())
        {
            myColor.add(myColor.get(c));
            c++;
        }

        ArrayList<Integer> newColor = new ArrayList<>();

        for (int k = 0 ; k < Hour.size() ; k++)
        {
            newColor.add(myColor.get(k));
        }
///////////

        Legend l = b1.getLegend();
        l.setCustom(newColor,Hour);
        l.setWordWrapEnabled(true);

        BarData bd1 = new BarData(Hour,bds1);
        bd1.setValueFormatter(new myValueFormatter());

        b1.setData(bd1);
        b1.animateY(2000, Easing.EasingOption.EaseInOutBack);

        b1.setDescription(" ");
        b1.setDescriptionTextSize(18f);
//2
        ArrayList<BarEntry> MWS = new ArrayList<>();
        for(int i = 0; i < AvgNetAmt.size(); i++){
            MWS.add(new BarEntry(AvgNetAmt.get(i),i));
        }

        BarDataSet bds2 = new BarDataSet(MWS,"");
        bds2.setColors(ReceiptColor);
        bds2.setValueTextSize(14f);
        bds2.setValueFormatter(new myValueFormatter());

        Legend l2 = b2.getLegend();
        l2.setCustom(newColor,Hour);
        l2.setWordWrapEnabled(true);

        BarData bd2 = new BarData(Hour,bds2);
        bd2.setValueFormatter(new myValueFormatter());

        b2.setData(bd2);
        b2.animateY(2000, Easing.EasingOption.EaseInOutBack);

        b2.setDescription(" ");
        b2.setDescriptionTextSize(18f);
//3
        ArrayList<BarEntry> CaWS = new ArrayList<>();
        for(int i = 0; i < ReturnSale.size(); i++){
            CaWS.add(new BarEntry(ReturnSale.get(i),i));
        }

        BarDataSet bds3 = new BarDataSet(CaWS,"");
        bds3.setColors(ReceiptColor);
        bds3.setValueTextSize(14f);
        bds3.setValueFormatter(new myValueFormatter());

///////////
        ArrayList<Integer> myColor2 = new ArrayList<>();
        for (int w = 0 ; w < ReceiptColor.length ; w++)
        {
            myColor2.add(ReceiptColor[w]);
        }

        int c2 = 0;

        while(myColor2.size() < Hour2.size())
        {
            myColor2.add(myColor2.get(c2));
            c2++;
        }

        ArrayList<Integer> newColor2 = new ArrayList<>();

        for (int k = 0 ; k < Hour2.size() ; k++)
        {
            newColor2.add(myColor2.get(k));
        }
///////////

        Legend l3 = b3.getLegend();
        l3.setCustom(newColor2,Hour2);
        l3.setWordWrapEnabled(true);

        BarData bd3 = new BarData(Hour2,bds3);
        bd3.setValueFormatter(new myValueFormatter());

        b3.setData(bd3);
        b3.animateY(2000, Easing.EasingOption.EaseInOutBack);

        b3.setDescription(" ");
        b3.setDescriptionTextSize(18f);
//4
        ArrayList<BarEntry> HWS = new ArrayList<>();
        for(int i = 0; i < ItemLine.size(); i++){
            HWS.add(new BarEntry(ItemLine.get(i),i));
        }

        BarDataSet bds4 = new BarDataSet(HWS,"");
        bds4.setColors(ReceiptColor);
        bds4.setValueTextSize(14f);
        bds4.setValueFormatter(new myValueFormatter());

///////////
        ArrayList<Integer> myColor3 = new ArrayList<>();
        for (int w = 0 ; w < ReceiptColor.length ; w++)
        {
            myColor3.add(ReceiptColor[w]);
        }

        int c3 = 0;

        while(myColor3.size() < Hour3.size())
        {
            myColor3.add(myColor3.get(c3));
            c3++;
        }

        ArrayList<Integer> newColor3 = new ArrayList<>();

        for (int k = 0 ; k < Hour3.size() ; k++)
        {
            newColor3.add(myColor3.get(k));
        }
///////////

        Legend l4 = b4.getLegend();
        l4.setCustom(newColor3,Hour3);
        l4.setWordWrapEnabled(true);

        BarData bd4 = new BarData(Hour3,bds4);
        bd4.setValueFormatter(new myValueFormatter());

        b4.setData(bd4);
        b4.animateY(2000, Easing.EasingOption.EaseInOutBack);

        b4.setDescription(" ");
        b4.setDescriptionTextSize(18f);
    }
}
