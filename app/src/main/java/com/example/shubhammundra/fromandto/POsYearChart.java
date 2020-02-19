package com.example.shubhammundra.fromandto;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class POsYearChart extends AppCompatActivity {

    BarChart b1,b2,b3,b4,b5,b6,b7,b8;
    Button Scroll,ScrollUp;

    ScrollView scrollView;

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_year_chart);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        back = findViewById(R.id.img1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*scrollView = findViewById(R.id.sv_POS);

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
        });*/

        b1 = findViewById(R.id.barchart1);
        b2 = findViewById(R.id.barchart2);
        b3 = findViewById(R.id.barchart3);
        b4 = findViewById(R.id.barchart4);
        b5 = findViewById(R.id.barchart5);
        b6 = findViewById(R.id.barchart6);
        b7 = findViewById(R.id.barchart7);
        b8 = findViewById(R.id.barchart8);

        Intent Dataforchart = getIntent();

        ArrayList<String> Year =(ArrayList<String>) Dataforchart.getSerializableExtra("Year");
        ArrayList<String> Year1 =(ArrayList<String>) Dataforchart.getSerializableExtra("Year1");
        ArrayList<Float> NetAmount =(ArrayList<Float>) Dataforchart.getSerializableExtra("Net Sale");
        ArrayList<Float> Income =(ArrayList<Float>) Dataforchart.getSerializableExtra("Income");
        ArrayList<Float> Profit =(ArrayList<Float>) Dataforchart.getSerializableExtra("Profit Percent");
        ArrayList<Float> Discount =(ArrayList<Float>) Dataforchart.getSerializableExtra("Discount");
        ArrayList<Float> Cost =(ArrayList<Float>) Dataforchart.getSerializableExtra("cost");
        ArrayList<Float> UniqueCostumer =(ArrayList<Float>) Dataforchart.getSerializableExtra("Unique Customers");
        ArrayList<Float> NoReceipts =(ArrayList<Float>) Dataforchart.getSerializableExtra("No.of Receipts");
        ArrayList<Float> AvgNet =(ArrayList<Float>) Dataforchart.getSerializableExtra("Avg No.of Stores Per Receipts");

        int[] Paycolor = {Color.parseColor("#e6194b"),
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

        ArrayList<BarEntry> NPC = new ArrayList<>();
        for(int i = 0; i < NetAmount.size(); i++){
            NPC.add(new BarEntry(NetAmount.get(i),i));
        }

        BarDataSet bds1 = new BarDataSet(NPC,"");
        bds1.setColors(Paycolor);
        bds1.setValueTextSize(14f);
        bds1.setValueFormatter(new myValueFormatter());

///////////
        ArrayList<Integer> myColor = new ArrayList<>();
        for (int w = 0 ; w < Paycolor.length ; w++)
        {
            myColor.add(Paycolor[w]);
        }

        int c = 0;

        while(myColor.size() < Year.size())
        {
            myColor.add(myColor.get(c));
            c++;
        }

        ArrayList<Integer> newColor = new ArrayList<>();

        for (int k = 0 ; k < Year.size() ; k++)
        {
            newColor.add(myColor.get(k));
        }
///////////

        Legend l1 = b1.getLegend();
        l1.setCustom(newColor,Year);
        l1.setWordWrapEnabled(true);

        BarData bd1 = new BarData(Year,bds1);
        bd1.setValueFormatter(new myValueFormatter());

        b1.setData(bd1);
        b1.animateY(2000, Easing.EasingOption.EaseInOutBack);

        b1.setDescription(" ");
        b1.setDescriptionTextSize(18f);

        ArrayList<BarEntry> ANPC = new ArrayList<>();
        for(int i = 0; i < Income.size(); i++){
            ANPC.add(new BarEntry(Income.get(i),i));
        }

        BarDataSet bds2 = new BarDataSet(ANPC,"");
        bds2.setColors(Paycolor);
        bds2.setValueTextSize(14f);
        bds2.setValueFormatter(new myValueFormatter());

        Legend l2 = b2.getLegend();
        l2.setCustom(newColor,Year);
        l2.setWordWrapEnabled(true);

        BarData bd2 = new BarData(Year,bds2);
        bd2.setValueFormatter(new myValueFormatter());

        b2.setData(bd2);
        b2.animateY(2000, Easing.EasingOption.EaseInOutBack);

        b2.setDescription("");
        b2.setDescriptionTextSize(18f);

        ArrayList<BarEntry> NPTC = new ArrayList<>();
        for(int i = 0; i < Profit.size(); i++){
            NPTC.add(new BarEntry(Profit.get(i),i));
        }

        BarDataSet bds3 = new BarDataSet(NPTC,"");
        bds3.setColors(Paycolor);
        bds3.setValueTextSize(14f);
        bds3.setValueFormatter(new myValueFormatter());

        Legend l3 = b3.getLegend();
        l3.setCustom(newColor,Year);
        l3.setWordWrapEnabled(true);

        BarData bd3 = new BarData(Year,bds3);
        bd3.setValueFormatter(new myValueFormatter());

        b3.setData(bd3);
        b3.animateY(2000, Easing.EasingOption.EaseInOutBack);

        b3.setDescription("");
        b3.setDescriptionTextSize(18f);

        ArrayList<BarEntry> ANPTC = new ArrayList<>();
        for(int i = 0; i < Discount.size(); i++){
            ANPTC.add(new BarEntry(Discount.get(i),i));
        }

        BarDataSet bds4 = new BarDataSet(ANPTC,"");
        bds4.setColors(Paycolor);
        bds4.setValueTextSize(14f);
        bds4.setValueFormatter(new myValueFormatter());

        Legend l4 = b4.getLegend();
        l4.setCustom(newColor,Year);
        l4.setWordWrapEnabled(true);

        BarData bd4 = new BarData(Year,bds4);
        bd4.setValueFormatter(new myValueFormatter());

        b4.setData(bd4);
        b4.animateY(2000, Easing.EasingOption.EaseInOutBack);

        b4.setDescription("");
        b4.setDescriptionTextSize(18f);

        ArrayList<BarEntry> CPC1 = new ArrayList<>();
        for(int i = 0; i < Cost.size(); i++){
            CPC1.add(new BarEntry(Cost.get(i),i));
        }

        BarDataSet bds5 = new BarDataSet(CPC1,"");
        bds5.setColors(Paycolor);
        bds5.setValueTextSize(14f);
        bds5.setValueFormatter(new myValueFormatter());

        Legend l5 = b5.getLegend();
        l5.setCustom(newColor,Year);
        l5.setWordWrapEnabled(true);

        BarData bd5 = new BarData(Year,bds5);
        bd5.setValueFormatter(new myValueFormatter());

        b5.setData(bd5);
        b5.animateY(2000, Easing.EasingOption.EaseInOutBack);

        b5.setDescription("");
        b5.setDescriptionTextSize(18f);

        ArrayList<BarEntry> CPC2 = new ArrayList<>();
        for(int i = 0; i < UniqueCostumer.size(); i++){
            CPC2.add(new BarEntry(UniqueCostumer.get(i),i));
        }

        BarDataSet bds6 = new BarDataSet(CPC2,"");
        bds6.setColors(Paycolor);
        bds6.setValueTextSize(14f);
        bds6.setValueFormatter(new myValueFormatter());

///////////
        ArrayList<Integer> myColor2 = new ArrayList<>();
        for (int w = 0 ; w < Paycolor.length ; w++)
        {
            myColor2.add(Paycolor[w]);
        }

        int c2 = 0;

        while(myColor2.size() < Year1.size())
        {
            myColor2.add(myColor2.get(c2));
            c2++;
        }

        ArrayList<Integer> newColor2 = new ArrayList<>();

        for (int k = 0 ; k < Year1.size() ; k++)
        {
            newColor2.add(myColor2.get(k));
        }
///////////

        Legend l6 = b6.getLegend();
        l6.setCustom(newColor2,Year1);
        l6.setWordWrapEnabled(true);

        BarData bd6 = new BarData(Year1,bds6);
        bd6.setValueFormatter(new myValueFormatter());

        b6.setData(bd6);
        b6.animateY(2000, Easing.EasingOption.EaseInOutBack);

        b6.setDescription("");
        b6.setDescriptionTextSize(18f);

        ArrayList<BarEntry> CPC3 = new ArrayList<>();
        for(int i = 0; i < NoReceipts.size(); i++){
            CPC3.add(new BarEntry(NoReceipts.get(i),i));
        }

        BarDataSet bds7 = new BarDataSet(CPC3,"");
        bds7.setColors(Paycolor);
        bds7.setValueTextSize(14f);
        bds7.setValueFormatter(new myValueFormatter());

        Legend l7 = b7.getLegend();
        l7.setCustom(newColor,Year);
        l7.setWordWrapEnabled(true);

        BarData bd7 = new BarData(Year,bds7);
        bd7.setValueFormatter(new myValueFormatter());

        b7.setData(bd7);
        b7.animateY(2000, Easing.EasingOption.EaseInOutBack);

        b7.setDescription("");
        b7.setDescriptionTextSize(18f);

        ArrayList<BarEntry> GPC = new ArrayList<>();
        for(int i = 0; i < AvgNet.size(); i++){
            GPC.add(new BarEntry(AvgNet.get(i),i));
        }

        BarDataSet bds8 = new BarDataSet(GPC,"");
        bds8.setColors(Paycolor);
        bds8.setValueTextSize(14f);
        bds8.setValueFormatter(new myValueFormatter());

        Legend l8 = b8.getLegend();
        l8.setCustom(newColor,Year);
        l8.setWordWrapEnabled(true);

        BarData bd8 = new BarData(Year,bds8);
        bd8.setValueFormatter(new myValueFormatter());

        b8.setData(bd8);
        b8.animateY(2000, Easing.EasingOption.EaseInOutBack);

        b8.setDescription("");
        b8.setDescriptionTextSize(18f);
    }
}
