package com.example.shubhammundra.fromandto;

////// Adapter for BarChart ////////

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;

public class MyAdapter extends PagerAdapter {

    private ArrayList<BarChart> barcharts;
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<String> Title;

    public MyAdapter(Context context, ArrayList<BarChart> barcharts, ArrayList<String> Title) {
        this.context = context;
        this.barcharts=barcharts;
        this.Title = Title;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return barcharts.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, int position) {
        View myBarChartLayout = inflater.inflate(R.layout.slide, view, false);

        TextView tv = myBarChartLayout.findViewById(R.id.tv_1);
        tv.setText(Title.get(position));

        BarChart barChart = myBarChartLayout.findViewById(R.id.bc_1);

        if(barcharts.get(position).getParent()!=null)
        {
            ((ViewGroup)barcharts.get(position).getParent()).removeView(barcharts.get(position));
        }
        barChart.addView(barcharts.get(position));

        view.addView(myBarChartLayout);
        return myBarChartLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}
