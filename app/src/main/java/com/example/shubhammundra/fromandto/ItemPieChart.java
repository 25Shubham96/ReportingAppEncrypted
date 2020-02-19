package com.example.shubhammundra.fromandto;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;

public class ItemPieChart extends AppCompatActivity{
    PieChart viewPieChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_piechart);

        viewPieChart = findViewById(R.id.piechart);

        Intent getItemPieIntent = getIntent();

        ArrayList<Float> getamount = (ArrayList<Float>) getItemPieIntent.getSerializableExtra("amount");
        ArrayList<String> itemCode = (ArrayList<String>) getItemPieIntent.getSerializableExtra("itemCode");

        ArrayList<Entry> amount = new ArrayList<>();

        for (int i = 0 ; i < getamount.size() ; i++){
            amount.add(new Entry(getamount.get(i),i));
        }

        PieDataSet itemPieDataSet = new PieDataSet(amount,"");
        int[] itemPieColor = {Color.parseColor("#e6194b"),
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

        itemPieDataSet.setColors(itemPieColor);
        itemPieDataSet.setValueTextSize(16f);
        itemPieDataSet.setValueTextColor(Color.WHITE);
        itemPieDataSet.setSliceSpace(0.5f);

        Legend l = viewPieChart.getLegend();
        l.setWordWrapEnabled(true);

        PieData itemPieData = new PieData(itemCode,itemPieDataSet);
        viewPieChart.setData(itemPieData);

        viewPieChart.setHoleColor(Color.TRANSPARENT);
        viewPieChart.setCenterText("Item Category");
        viewPieChart.setCenterTextColor(Color.WHITE);
        viewPieChart.setDragDecelerationFrictionCoef(0.90f);

        viewPieChart.animateY(2000, Easing.EasingOption.EaseInCirc);

        viewPieChart.setDescription("Item Category Wise Entry");
        viewPieChart.setDescriptionTextSize(18f);
    }
}
