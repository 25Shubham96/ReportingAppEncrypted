package com.example.shubhammundra.fromandto;

////// Adapter for LineChart ////////

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;

public class MyPieChartAdapter extends PagerAdapter{

    private ArrayList<PieChart> pieCharts;
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<String> Title;

    public MyPieChartAdapter(Context context, ArrayList<PieChart> pieCharts, ArrayList<String> Title) {
        this.context = context;
        this.pieCharts = pieCharts;
        this.Title = Title;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return pieCharts.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, int position) {
        View myPieChartLayout = inflater.inflate(R.layout.pie_slide, view, false);

        TextView tv = myPieChartLayout.findViewById(R.id.tv_1);
        tv.setText(Title.get(position));

        PieChart pieChart = myPieChartLayout.findViewById(R.id.pc_1);

        if(pieCharts.get(position).getParent()!=null)
        {
            ((ViewGroup)pieCharts.get(position).getParent()).removeView(pieCharts.get(position));
        }
        pieChart.addView(pieCharts.get(position));

        view.addView(myPieChartLayout);
        return myPieChartLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

}
