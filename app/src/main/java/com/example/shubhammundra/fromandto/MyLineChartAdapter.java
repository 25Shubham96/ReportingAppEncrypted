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

import java.util.ArrayList;

public class MyLineChartAdapter extends PagerAdapter {

    private ArrayList<LineChart> lineCharts;
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<String> Title;

    public MyLineChartAdapter(Context context, ArrayList<LineChart> lineCharts, ArrayList<String> Title) {
        this.context = context;
        this.lineCharts=lineCharts;
        this.Title = Title;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return lineCharts.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, int position) {
        View myLineChartLayout = inflater.inflate(R.layout.line_slide, view, false);

        TextView tv = myLineChartLayout.findViewById(R.id.tv_1);
        tv.setText(Title.get(position));

        LineChart lineChart = myLineChartLayout.findViewById(R.id.lc_1);

        if(lineCharts.get(position).getParent()!=null)
        {
            ((ViewGroup)lineCharts.get(position).getParent()).removeView(lineCharts.get(position));
        }
        lineChart.addView(lineCharts.get(position));

        view.addView(myLineChartLayout);
        return myLineChartLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}
