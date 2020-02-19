package com.example.shubhammundra.fromandto;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ScrollView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.util.ArrayList;
import me.relex.circleindicator.CircleIndicator;

public class PaymentBarchart extends AppCompatActivity
{
    BarChart b1,b2,b3,b4,b5,b6,b7,b8;

//    ScrollView scrollView;

    private static ViewPager mPager;
//    private static int currentPage = 0;
    private ArrayList<BarChart> ChartArray = new ArrayList<>();
    private ArrayList<String> Title = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_report);

        /*this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);*/

        /*scrollView = findViewById(R.id.sv_payment);

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

        /*b1 = findViewById(R.id.bc_1);
        b2 = findViewById(R.id.bc_2);
        b3 = findViewById(R.id.bc_3);
        b4 = findViewById(R.id.bc_4);
        b5 = findViewById(R.id.bc_5);
        b6 = findViewById(R.id.bc_6);
        b7 = findViewById(R.id.bc_7);
        b8 = findViewById(R.id.bc_8);*/

        /*textView = findViewById(R.id.tv_1);*/

        b1 = new BarChart(this);
        b2 = new BarChart(this);
        b3 = new BarChart(this);
        b4 = new BarChart(this);
        b5 = new BarChart(this);
        b6 = new BarChart(this);
        b7 = new BarChart(this);
        b8 = new BarChart(this);

        ArrayList<String> Netpaymonth1;
        ArrayList<Float> NetpayAmt1;
        ArrayList<Float> AvgpayAmt1;
        ArrayList<String> Netpaytype1;
        ArrayList<Float> NetpaytypeAmt1;
        ArrayList<Float> AvgNetpaytypeAmt1;
        ArrayList<Float> NetcardAmt1;
        ArrayList<String> Months11;
        ArrayList<Float> NetcashAmt1;
        ArrayList<String> Months12;
        ArrayList<Float> NetchequeAmt1;
        ArrayList<String> Months13;
        ArrayList<Float> NetGiftcardAmt1;
        ArrayList<String> Months14;

        Intent Dataforchart = getIntent();

        Netpaymonth1 =(ArrayList<String>) Dataforchart.getSerializableExtra("NPM");
        NetpayAmt1 =(ArrayList<Float>) Dataforchart.getSerializableExtra("NPA");
        AvgpayAmt1 =(ArrayList<Float>) Dataforchart.getSerializableExtra("APA");

        Netpaytype1 =(ArrayList<String>) Dataforchart.getSerializableExtra("NPT");
        NetpaytypeAmt1 =(ArrayList<Float>) Dataforchart.getSerializableExtra("NPTA");
        AvgNetpaytypeAmt1 =(ArrayList<Float>) Dataforchart.getSerializableExtra("ANPTA");

        NetcardAmt1 =(ArrayList<Float>) Dataforchart.getSerializableExtra("NCA1");
        Months11 =(ArrayList<String>) Dataforchart.getSerializableExtra("M1");

        NetcashAmt1 =(ArrayList<Float>) Dataforchart.getSerializableExtra("NCA2");
        Months12 =(ArrayList<String>) Dataforchart.getSerializableExtra("M2");

        NetchequeAmt1 =(ArrayList<Float>) Dataforchart.getSerializableExtra("NCA3");
        Months13=(ArrayList<String>) Dataforchart.getSerializableExtra("M3");

        NetGiftcardAmt1 =(ArrayList<Float>) Dataforchart.getSerializableExtra("NGCA");
        Months14=(ArrayList<String>) Dataforchart.getSerializableExtra("M4");

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
        for(int i = 0; i < NetpayAmt1.size(); i++){
            NPC.add(new BarEntry(NetpayAmt1.get(i),i));
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

        while(myColor.size() < Netpaymonth1.size())
        {
            myColor.add(myColor.get(c));
            c++;
        }

        ArrayList<Integer> newColor1 = new ArrayList<>();

        for (int k = 0 ; k < Netpaymonth1.size() ; k++)
        {
            newColor1.add(myColor.get(k));
        }
///////////

        Legend l1 = b1.getLegend();
        l1.setCustom(newColor1,Netpaymonth1);
        l1.setWordWrapEnabled(true);

        BarData bd1 = new BarData(Netpaymonth1,bds1);
        bd1.setValueFormatter(new myValueFormatter());

        b1.setData(bd1);
        b1.animateY(2000, Easing.EasingOption.EaseInOutBack);

        b1.setDescription(" ");
        b1.setDescriptionTextSize(18f);

        ArrayList<BarEntry> ANPC = new ArrayList<>();
        for(int i = 0; i < AvgpayAmt1.size(); i++){
            ANPC.add(new BarEntry(AvgpayAmt1.get(i),i));
        }

        BarDataSet bds2 = new BarDataSet(ANPC,"");
        bds2.setColors(Paycolor);
        bds2.setValueTextSize(14f);
        bds2.setValueFormatter(new myValueFormatter());

        Legend l2 = b2.getLegend();
        l2.setCustom(newColor1,Netpaymonth1);
        l2.setWordWrapEnabled(true);

        BarData bd2 = new BarData(Netpaymonth1,bds2);
        bd2.setValueFormatter(new myValueFormatter());

        b2.setData(bd2);
        b2.animateY(2000, Easing.EasingOption.EaseInOutBack);

        b2.setDescription("");
        b2.setDescriptionTextSize(18f);

        ArrayList<BarEntry> NPTC = new ArrayList<>();
        for(int i = 0; i < NetpaytypeAmt1.size(); i++){
            NPTC.add(new BarEntry(NetpaytypeAmt1.get(i),i));
        }

        BarDataSet bds3 = new BarDataSet(NPTC,"");
        bds3.setColors(Paycolor);
        bds3.setValueTextSize(14f);
        bds3.setValueFormatter(new myValueFormatter());

        ArrayList<Integer> myColor2 = new ArrayList<>();
        for (int w = 0 ; w < Paycolor.length ; w++)
        {
            myColor2.add(Paycolor[w]);
        }

        int c2 = 0;

        while(myColor2.size() < Netpaytype1.size())
        {
            myColor2.add(myColor2.get(c2));
            c2++;
        }

        ArrayList<Integer> newColor2 = new ArrayList<>();

        for (int k = 0 ; k < Netpaytype1.size() ; k++)
        {
            newColor2.add(myColor2.get(k));
        }

        Legend l3 = b3.getLegend();
        l3.setCustom(newColor2,Netpaytype1);
        l3.setWordWrapEnabled(true);

        BarData bd3 = new BarData(Netpaytype1,bds3);
        bd3.setValueFormatter(new myValueFormatter());

        b3.setData(bd3);
        b3.animateY(2000, Easing.EasingOption.EaseInOutBack);

        b3.setDescription("");
        b3.setDescriptionTextSize(18f);

        ArrayList<BarEntry> ANPTC = new ArrayList<>();
        for(int i = 0; i < AvgNetpaytypeAmt1.size(); i++){
            ANPTC.add(new BarEntry(AvgNetpaytypeAmt1.get(i),i));
        }

        BarDataSet bds4 = new BarDataSet(ANPTC,"");
        bds4.setColors(Paycolor);
        bds4.setValueTextSize(14f);
        bds4.setValueFormatter(new myValueFormatter());

        Legend l4 = b4.getLegend();
        l4.setCustom(newColor2,Netpaytype1);
        l4.setWordWrapEnabled(true);

        BarData bd4 = new BarData(Netpaytype1,bds4);
        bd4.setValueFormatter(new myValueFormatter());

        b4.setData(bd4);
        b4.animateY(2000, Easing.EasingOption.EaseInOutBack);

        b4.setDescription("");
        b4.setDescriptionTextSize(18f);

        ArrayList<BarEntry> CPC1 = new ArrayList<>();
        for(int i = 0; i < NetcardAmt1.size(); i++){
            CPC1.add(new BarEntry(NetcardAmt1.get(i),i));
        }

        BarDataSet bds5 = new BarDataSet(CPC1,"");
        bds5.setColors(Paycolor);
        bds5.setValueTextSize(14f);
        bds5.setValueFormatter(new myValueFormatter());

///////////
        ArrayList<Integer> myColor3 = new ArrayList<>();
        for (int w = 0 ; w < Paycolor.length ; w++)
        {
            myColor3.add(Paycolor[w]);
        }

        int c3 = 0;

        while(myColor3.size() < Months11.size())
        {
            myColor3.add(myColor3.get(c3));
            c3++;
        }

        ArrayList<Integer> newColor3 = new ArrayList<>();

        for (int k = 0 ; k < Months11.size() ; k++)
        {
            newColor3.add(myColor3.get(k));
        }
///////////

        Legend l5 = b5.getLegend();
        l5.setCustom(newColor3,Months11);
        l5.setWordWrapEnabled(true);

        BarData bd5 = new BarData(Months11,bds5);
        bd5.setValueFormatter(new myValueFormatter());

        b5.setData(bd5);
        b5.animateY(2000, Easing.EasingOption.EaseInOutBack);

        b5.setDescription("");
        b5.setDescriptionTextSize(18f);

        ArrayList<BarEntry> CPC2 = new ArrayList<>();
        for(int i = 0; i < NetcashAmt1.size(); i++){
            CPC2.add(new BarEntry(NetcashAmt1.get(i),i));
        }

        BarDataSet bds6 = new BarDataSet(CPC2,"");
        bds6.setColors(Paycolor);
        bds6.setValueTextSize(14f);
        bds6.setValueFormatter(new myValueFormatter());

///////////
        ArrayList<Integer> myColor4 = new ArrayList<>();
        for (int w = 0 ; w < Paycolor.length ; w++)
        {
            myColor4.add(Paycolor[w]);
        }

        int c4 = 0;

        while(myColor4.size() < Months12.size())
        {
            myColor4.add(myColor4.get(c4));
            c4++;
        }

        ArrayList<Integer> newColor4 = new ArrayList<>();

        for (int k = 0 ; k < Months12.size() ; k++)
        {
            newColor4.add(myColor4.get(k));
        }
///////////

        Legend l6 = b6.getLegend();
        l6.setCustom(newColor4,Months12);
        l6.setWordWrapEnabled(true);

        BarData bd6 = new BarData(Months12,bds6);
        bd6.setValueFormatter(new myValueFormatter());

        b6.setData(bd6);
        b6.animateY(2000, Easing.EasingOption.EaseInOutBack);

        b6.setDescription("");
        b6.setDescriptionTextSize(18f);

        ArrayList<BarEntry> CPC3 = new ArrayList<>();
        for(int i = 0; i < NetchequeAmt1.size(); i++){
            CPC3.add(new BarEntry(NetchequeAmt1.get(i),i));
        }

        BarDataSet bds7 = new BarDataSet(CPC3,"");
        bds7.setColors(Paycolor);
        bds7.setValueTextSize(14f);
        bds7.setValueFormatter(new myValueFormatter());

///////////
        ArrayList<Integer> myColor5 = new ArrayList<>();
        for (int w = 0 ; w < Paycolor.length ; w++)
        {
            myColor5.add(Paycolor[w]);
        }

        int c5 = 0;

        while(myColor5.size() < Months13.size())
        {
            myColor5.add(myColor5.get(c5));
            c5++;
        }

        ArrayList<Integer> newColor5 = new ArrayList<>();

        for (int k = 0 ; k < Months13.size() ; k++)
        {
            newColor5.add(myColor5.get(k));
        }
///////////

        Legend l7 = b7.getLegend();
        l7.setCustom(newColor5,Months13);
        l7.setWordWrapEnabled(true);

        BarData bd7 = new BarData(Months13,bds7);
        bd7.setValueFormatter(new myValueFormatter());

        b7.setData(bd7);
        b7.animateY(2000, Easing.EasingOption.EaseInOutBack);

        b7.setDescription("");
        b7.setDescriptionTextSize(18f);

        ArrayList<BarEntry> GPC = new ArrayList<>();
        for(int i = 0; i < NetGiftcardAmt1.size(); i++){
            GPC.add(new BarEntry(NetGiftcardAmt1.get(i),i));
        }

        BarDataSet bds8 = new BarDataSet(GPC,"");
        bds8.setColors(Paycolor);
        bds8.setValueTextSize(14f);
        bds8.setValueFormatter(new myValueFormatter());

///////////
        ArrayList<Integer> myColor6 = new ArrayList<>();
        for (int w = 0 ; w < Paycolor.length ; w++)
        {
            myColor6.add(Paycolor[w]);
        }

        int c6 = 0;

        while(myColor6.size() < Months14.size())
        {
            myColor6.add(myColor6.get(c6));
            c6++;
        }

        ArrayList<Integer> newColor6 = new ArrayList<>();

        for (int k = 0 ; k < Months14.size() ; k++)
        {
            newColor6.add(myColor6.get(k));
        }
///////////

        Legend l8 = b8.getLegend();
        l8.setCustom(newColor6,Months14);
        l8.setWordWrapEnabled(true);

        BarData bd8 = new BarData(Months14,bds8);
        bd8.setValueFormatter(new myValueFormatter());

        b8.setData(bd8);
        b8.animateY(2000, Easing.EasingOption.EaseInOutBack);

        b8.setDescription("");
        b8.setDescriptionTextSize(18f);



        ChartArray.add(b1);
        ChartArray.add(b2);
        ChartArray.add(b3);
        ChartArray.add(b4);
        ChartArray.add(b5);
        ChartArray.add(b6);
        ChartArray.add(b7);
        ChartArray.add(b8);

        Title.add("Net Payment Chart");
        Title.add("AvgNet Payment Chart");
        Title.add("NetPayment By Type Chart");
        Title.add("AvgNet Payment By Type Chart");
        Title.add("Card Payment Chart");
        Title.add("Cash Payment Chart");
        Title.add("Cheque Payment Chart");
        Title.add("Giftcard Payment Chart");

        mPager = findViewById(R.id.pager);
        mPager.setAdapter(new MyAdapter(PaymentBarchart.this,ChartArray,Title));

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        /*// Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == ChartArray.size()) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);*/


    }
}
