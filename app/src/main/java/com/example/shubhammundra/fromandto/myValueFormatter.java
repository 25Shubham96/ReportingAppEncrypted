package com.example.shubhammundra.fromandto;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class myValueFormatter implements ValueFormatter {

    private DecimalFormat myFormat;

    public myValueFormatter(){
        myFormat = new DecimalFormat("##.##");
    }
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return myFormat.format(value);
    }
}
