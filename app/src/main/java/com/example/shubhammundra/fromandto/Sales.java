package com.example.shubhammundra.fromandto;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class Sales extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TableLayout TL, TL2, TL3;

    TableRow TR, TR2, TR3;

    TextView TV1,TV2,TV3,TV4,TV5,TV6,TV7,TV8,TV9,TV10,TV11,TV12,TV13,TV14,TV15,TV16;
    TextView TV21,TV22,TV23,TV24,TV25,TV26,TV27,TV28,TV29,TV210,TV211,TV212,TV213,TV214,TV215,TV216;
    TextView TV31,TV32,TV33,TV34,TV35,TV36,TV37,TV38,TV39,TV310,TV311,TV312,TV313,TV314,TV315,TV316;

    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    ImageView refresh;

    ImageView back;

    String[] listItems;
    String[] Months = {"January","February","March","April","May","June","July","August","September","October","November","December"};
    String[] Months1 = {"1","2","3","4","5","6","7","8","9","10","11","12"};

    String[] MonthsNew = {"","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    boolean[] checkedItems1;

    ArrayList<Integer> UserItems1 = new ArrayList<>();

    Button selectCity,selectCity1;

    String CName;

    ArrayList<String> Store;
    ArrayList<String> StoreDescrip;

    ArrayList<String> Year;

    String YearSelect = "2001", dropDownYear;

    GradientDrawable gd;

    String Cities = "''", Cities1 = "''";

    ArrayList<String> StoreSelected = new ArrayList<>();

    ArrayList<String> MonthSelected = new ArrayList<>();

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal = {1,1000,100000,1000000,10000000};

    String dropDownValue;

    int index;

    @SuppressLint("StaticFieldLeak")
    public static Spinner spinnerFilter;
    @SuppressLint("StaticFieldLeak")
    public static Spinner spinner;

    ArrayList<String> getStoreDescrip = new ArrayList<>();

    ArrayList<TextView> myTV = new ArrayList<>();
    ArrayList<TextView> myTV2 = new ArrayList<>();
    ArrayList<TextView> myTV3 = new ArrayList<>();

    ArrayList<TextView> myTV1 = new ArrayList<>();

    ArrayList<String> getStoreNo = new ArrayList<>();
    ArrayList<String> getHr = new ArrayList<>();
    ArrayList<Double> getNetAmt = new ArrayList<>();
    ArrayList<Double> getNoOfRece = new ArrayList<>();

    ArrayList<String> getMonth = new ArrayList<>();
    ArrayList<String> getHr2 = new ArrayList<>();
    ArrayList<Double> getNetAmt2 = new ArrayList<>();

    String NewHr = "",NewHr2 = "",NewHr3 = "";

    ArrayList<ArrayList<Float>> netAmtToGraph = new ArrayList<>();
    ArrayList<ArrayList<Float>> netAmtToGraph2 = new ArrayList<>();
    ArrayList<ArrayList<Float>> netAmtToGraph3 = new ArrayList<>();

    ArrayList<String> HourToGraph = new ArrayList<>();
    ArrayList<String> HourToGraph2 = new ArrayList<>();
    ArrayList<String> HourToGraph3 = new ArrayList<>();

    Button ViewChart;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TL = findViewById(R.id.tl_myTable1);
        TL2 = findViewById(R.id.tl_myTable2);
        TL3 = findViewById(R.id.tl_myTable3);

        TV1 = findViewById(R.id.tv1);        TV2 = findViewById(R.id.tv2);        TV3 = findViewById(R.id.tv3);        TV4 = findViewById(R.id.tv4);        TV5 = findViewById(R.id.tv5);        TV6 = findViewById(R.id.tv6);        TV7 = findViewById(R.id.tv7);        TV8 = findViewById(R.id.tv8);        TV9 = findViewById(R.id.tv9);        TV10 = findViewById(R.id.tv10);        TV11 = findViewById(R.id.tv11);        TV12 = findViewById(R.id.tv12);        TV13 = findViewById(R.id.tv13);        TV14 = findViewById(R.id.tv14);        TV15 = findViewById(R.id.tv15);        TV16 = findViewById(R.id.tv16);
        TV21 = findViewById(R.id.tv1a);        TV22 = findViewById(R.id.tv2a);        TV23 = findViewById(R.id.tv3a);        TV24 = findViewById(R.id.tv4a);        TV25 = findViewById(R.id.tv5a);        TV26 = findViewById(R.id.tv6a);        TV27 = findViewById(R.id.tv7a);        TV28 = findViewById(R.id.tv8a);        TV29 = findViewById(R.id.tv9a);        TV210 = findViewById(R.id.tv10a);        TV211 = findViewById(R.id.tv11a);        TV212 = findViewById(R.id.tv12a);        TV213 = findViewById(R.id.tv13a);        TV214 = findViewById(R.id.tv14a);        TV215 = findViewById(R.id.tv15a);        TV216 = findViewById(R.id.tv16a);
        TV31 = findViewById(R.id.tv1b);        TV32 = findViewById(R.id.tv2b);        TV33 = findViewById(R.id.tv3b);        TV34 = findViewById(R.id.tv4b);        TV35 = findViewById(R.id.tv5b);        TV36 = findViewById(R.id.tv6b);        TV37 = findViewById(R.id.tv7b);        TV38 = findViewById(R.id.tv8b);        TV39 = findViewById(R.id.tv9b);        TV310 = findViewById(R.id.tv10b);        TV311 = findViewById(R.id.tv11b);        TV312 = findViewById(R.id.tv12b);        TV313 = findViewById(R.id.tv13b);        TV314 = findViewById(R.id.tv14b);        TV315 = findViewById(R.id.tv15b);        TV316 = findViewById(R.id.tv16b);

        myTV.add(TV1);        myTV.add(TV2);        myTV.add(TV3);        myTV.add(TV4);        myTV.add(TV5);        myTV.add(TV6);        myTV.add(TV7);        myTV.add(TV8);        myTV.add(TV9);        myTV.add(TV10);        myTV.add(TV11);        myTV.add(TV12);        myTV.add(TV13);        myTV.add(TV14);        myTV.add(TV15);        myTV.add(TV16);
        myTV2.add(TV21);        myTV2.add(TV22);        myTV2.add(TV23);        myTV2.add(TV24);        myTV2.add(TV25);        myTV2.add(TV26);        myTV2.add(TV27);        myTV2.add(TV28);        myTV2.add(TV29);        myTV2.add(TV210);        myTV2.add(TV211);        myTV2.add(TV212);        myTV2.add(TV213);        myTV2.add(TV214);        myTV2.add(TV215);        myTV2.add(TV216);
        myTV3.add(TV31);        myTV3.add(TV32);        myTV3.add(TV33);        myTV3.add(TV34);        myTV3.add(TV35);        myTV3.add(TV36);        myTV3.add(TV37);        myTV3.add(TV38);        myTV3.add(TV39);        myTV3.add(TV310);        myTV3.add(TV311);        myTV3.add(TV312);        myTV3.add(TV313);        myTV3.add(TV314);        myTV3.add(TV315);        myTV3.add(TV316);

        Intent intent = getIntent();
        Store = intent.getStringArrayListExtra("Store Name");
        StoreDescrip = intent.getStringArrayListExtra("Store Descrip");
        Year = intent.getStringArrayListExtra("Unique Year");
        CName = intent.getStringExtra("Company Name");

        back = findViewById(R.id.img1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        spinnerFilter = findViewById(R.id.spin_filter);
        spinnerFilter.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, amtFilter);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapter1);

        Spinner spinner = findViewById(R.id.spin_yearselect);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Year);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final ArrayList<Integer> alreadySelectedItem = new ArrayList<>();

        ArrayList<MultiSelectModel> listOfItems = new ArrayList<>();

        for (int i = 0 ; i < StoreDescrip.size() ; i++)
        {
            listOfItems.add(new MultiSelectModel(i+1,StoreDescrip.get(i)));
        }

        final MultiSelectDialog multiSelectDialog = new MultiSelectDialog();

        multiSelectDialog.title("Select Stores");
        multiSelectDialog.titleSize(24);
        multiSelectDialog.positiveText("Apply");
        multiSelectDialog.negativeText("Cancel");
        multiSelectDialog.clearText("Clear All");
        multiSelectDialog.preSelectIDsList(alreadySelectedItem);
        multiSelectDialog.multiSelectList(listOfItems);

        multiSelectDialog.onSubmit(new MultiSelectDialog.SubmitCallbackListener() {

            @Override
            public void onDismiss(ArrayList<Integer> ids, ArrayList<String> commonSeperatedData) throws PackageManager.NameNotFoundException {
                getStoreDescrip.addAll(commonSeperatedData);

                String txt = "";
                for (int z = 0 ; z < getStoreDescrip.size() ; z++)
                {
                    if (z == 0)
                    {
                        txt = getStoreDescrip.get(z);
                    }
                    else
                        txt = txt + "\n" + getStoreDescrip.get(z);
                }

                selectCity.setText(txt);
            }

            @Override
            public void onCancel() {}

            @Override
            public void onClear() {}

        });

        selectCity = findViewById(R.id.btn_selectYear);

        selectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStoreDescrip.clear();
                multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");
            }
        });

        checkedItems1 = new boolean[Months.length];

        selectCity1 = findViewById(R.id.btn_selectMonths);

        selectCity1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(Sales.this);
                builder1.setTitle("Select Months");

                builder1.setMultiChoiceItems(Months, checkedItems1, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                        if(isChecked)
                        {
                            if (! UserItems1.contains(position)){
                                UserItems1.add(position);
                            }
                        }
                        else
                        if (UserItems1.contains(position))
                        {
                            UserItems1.remove(UserItems1.indexOf(position));
                        }

                    }
                });

                builder1.setCancelable(false);

                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String Month = "";
                        for(int i = 0 ; i < UserItems1.size() ; i++)
                        {
                            Month = Month + Months[UserItems1.get(i)];
                            if(i != UserItems1.size() -1)
                            {
                                Month = Month + "\n";
                            }
                        }
                        selectCity1.setText(Month);
                    }
                });

                builder1.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder1.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for(int i = 0 ; i < checkedItems1.length ; i++)
                        {
                            checkedItems1[i] = false;
                            UserItems1.clear();
                            selectCity1.setText("Select");
                        }
                    }
                });

                AlertDialog dialog1 = builder1.create();
                dialog1.show();

            }
        });

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                PrintTable();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                if (Looper.myLooper() == null)
                {
                    Looper.prepare();
                }
                doInBackground1();
                doInBackground2();
                return null;
            }
        }.execute();


        ViewChart = findViewById(R.id.btn_SalesChart);

        ViewChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Sales.this,SalesChart.class);
                intent.putExtra("Net Amount1",netAmtToGraph);
                intent.putExtra("Hours1",HourToGraph);
                intent.putExtra("Store No",StoreSelected);

                intent.putExtra("Net Amount2",netAmtToGraph2);
                intent.putExtra("Hours2",HourToGraph2);

                intent.putExtra("Net Amount3",netAmtToGraph3);
                intent.putExtra("Hours3",HourToGraph3);
                intent.putExtra("Month",MonthSelected);

                startActivity(intent);
            }
        });

    }

    @SuppressLint("RtlHardcoded")
    public void doInBackground1() {

        YearSelect = dropDownYear;

        refresh = findViewById(R.id.iv_refresh2);

        refresh.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
                refresh.startAnimation(rotation);

                TL = findViewById(R.id.tl_myTable1);
                TL2 = findViewById(R.id.tl_myTable2);
                TL3 = findViewById(R.id.tl_myTable3);

                while (TL.getChildCount() > 1) {
                    TL.removeView(TL.getChildAt(TL.getChildCount() - 1));
                }

                while (TL2.getChildCount() > 1) {
                    TL2.removeView(TL2.getChildAt(TL2.getChildCount() - 1));
                }

                while (TL3.getChildCount() > 1) {
                    TL3.removeView(TL3.getChildAt(TL3.getChildCount() - 1));
                }

                StoreSelected.clear();
                MonthSelected.clear();

                netAmtToGraph.clear();
                netAmtToGraph2.clear();
                netAmtToGraph3.clear();

                Cities = "''";
                for (int i = 0; i < getStoreDescrip.size() ; i++)
                {
                    for (int j = 0 ; j < StoreDescrip.size() ; j++)
                    {
                        if (getStoreDescrip.get(i).equals(StoreDescrip.get(j)))
                        {
                            netAmtToGraph.add(new ArrayList<Float>());
                            netAmtToGraph2.add(new ArrayList<Float>());
                            StoreSelected.add(Store.get(j));
                            Cities = Cities + ", '" + Store.get(j) + "'";
                        }
                    }
                }

                Cities1 = "''";
                for (int i = 0; i < UserItems1.size() ; i++)
                {
                    netAmtToGraph3.add(new ArrayList<Float>());
                    MonthSelected.add(MonthsNew[Integer.parseInt(Months1[UserItems1.get(i)])]);
                    Cities1 = Cities1 + ", '" + Months1[UserItems1.get(i)] + "'";
                }

                getStoreNo.clear();
                getHr.clear();
                getNetAmt.clear();
                getNoOfRece.clear();

                getMonth.clear();
                getHr2.clear();
                getNetAmt2.clear();

                HourToGraph.clear();
                HourToGraph2.clear();
                HourToGraph3.clear();

                final ProgressDialog progressDialog = new ProgressDialog(Sales.this);

                new AsyncTask<Void, Void, Void>()
                {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog.setMessage("Collecting Data...");
                        progressDialog.show();
                    }
                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        PrintTable();
                        progressDialog.dismiss();
                    }
                    @Override
                    protected Void doInBackground(Void... voids) {
                        if (Looper.myLooper() == null)
                        {
                            Looper.prepare();
                        }
                        doInBackground2();
                        doInBackground1();
                        return null;
                    }
                }.execute();
            }
        });

        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query = "Select [Store No_], Datepart(HH,Time) as Hour, sum([Net Amount]) as NetAmt, count([Receipt No_]) as NoOfRece FROM [dbo].["+CName+"$Transaction Header] where [Store No_] in ("+Cities+") and Datepart(MM,Date) in ("+Cities1+") and DATEPART(YYYY,Date) = '"+YearSelect+"' group by [Store No_], Datepart(HH,Time) order by Datepart(HH,Time), [Store No_]";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    getStoreNo.add(rs.getString("Store No_"));
                    getHr.add(rs.getString("Hour"));
                    getNetAmt.add(Math.abs(rs.getDouble("NetAmt")));
                    getNoOfRece.add(rs.getDouble("NoOfRece"));
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @SuppressLint("RtlHardcoded")
    public void doInBackground2() {
        YearSelect = dropDownYear;

        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query = "Select Datepart(MM,Date) as Month, Datepart(HH,Time) as Hour, sum([Net Amount]) as NetAmt FROM [dbo].["+CName+"$Transaction Header] where Datepart(MM,Date) in ("+Cities1+") and [Store No_] in ("+Cities+") and DATEPART(YYYY,Date) = '"+YearSelect+"' group by Datepart(HH,Time),Datepart(MM,Date) order by Datepart(MM,Date), Datepart(HH,Time)";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    getMonth.add(rs.getString("Month"));
                    getHr2.add(rs.getString("Hour"));
                    getNetAmt2.add(Math.abs(Math.round(rs.getDouble("NetAmt") * 100.0) / 100.0));
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @SuppressLint({"RtlHardcoded", "SetTextI18n"})
    public void PrintTable() {

        TV1.setText("Store →\nHour ↓");
        TV1.setTextSize(15);
        TV1.setPadding(10, 0, 10, 10);
        TV1.setGravity(Gravity.LEFT);
        TV1.setTextColor(Color.WHITE);
        TV1.setBackground(getDrawable(R.drawable.rect_border));

        TV21.setText("Store →\nHour ↓");
        TV21.setTextSize(15);
        TV21.setPadding(10, 0, 10, 10);
        TV21.setGravity(Gravity.LEFT);
        TV21.setTextColor(Color.WHITE);
        TV21.setBackground(getDrawable(R.drawable.rect_border));

        TV31.setText("Month →\nHour ↓");
        TV31.setTextSize(15);
        TV31.setPadding(10, 0, 10, 10);
        TV31.setGravity(Gravity.LEFT);
        TV31.setTextColor(Color.WHITE);
        TV31.setBackground(getDrawable(R.drawable.rect_border));

        double GrandTotalValue = 0;
        double GrandTotalValue2 = 0;
        double GrandTotalValue3 = 0;

        for (int c = 1 ; c < myTV.size() ; c++)
        {
            myTV.get(c).setText("");
            myTV.get(c).setBackground(getDrawable(android.R.color.transparent));

            myTV2.get(c).setText("");
            myTV2.get(c).setBackground(getDrawable(android.R.color.transparent));
        }

        for (int i = 0 ; i < StoreSelected.size() ; i++)
        {
            myTV.get(i+1).setText(StoreSelected.get(i) + "\n");

            myTV.get(i+1).setTextSize(15);
            myTV.get(i+1).setPadding(10, 0, 10, 10);
            myTV.get(i+1).setGravity(Gravity.LEFT);
            myTV.get(i+1).setTextColor(Color.WHITE);
            myTV.get(i+1).setBackground(getDrawable(R.drawable.rect_border));

            myTV2.get(i+1).setText(StoreSelected.get(i) + "\n");

            myTV2.get(i+1).setTextSize(15);
            myTV2.get(i+1).setPadding(10, 0, 10, 10);
            myTV2.get(i+1).setGravity(Gravity.LEFT);
            myTV2.get(i+1).setTextColor(Color.WHITE);
            myTV2.get(i+1).setBackground(getDrawable(R.drawable.rect_border));
        }

        for (int p = StoreSelected.size()+1 ; p < StoreSelected.size()+2 ; p++)
        {
            myTV.get(p).setText("Grand\nTotal");

            myTV.get(p).setTextSize(15);
            myTV.get(p).setPadding(10, 0, 10, 10);
            myTV.get(p).setGravity(Gravity.LEFT);
            myTV.get(p).setTextColor(Color.WHITE);
            myTV.get(p).setBackground(getDrawable(R.drawable.rect_border));

            myTV2.get(p).setText("Grand\nTotal");

            myTV2.get(p).setTextSize(15);
            myTV2.get(p).setPadding(10, 0, 10, 10);
            myTV2.get(p).setGravity(Gravity.LEFT);
            myTV2.get(p).setTextColor(Color.WHITE);
            myTV2.get(p).setBackground(getDrawable(R.drawable.rect_border));
        }

        for (int c = 1 ; c < myTV3.size() ; c++)
        {
            myTV3.get(c).setText("");
            myTV3.get(c).setBackground(getDrawable(android.R.color.transparent));
        }

        for (int i = 0 ; i < MonthSelected.size() ; i++)
        {
            myTV3.get(i+1).setText(MonthSelected.get(i) + "\n");

            myTV3.get(i+1).setTextSize(15);
            myTV3.get(i+1).setPadding(10, 0, 10, 10);
            myTV3.get(i+1).setGravity(Gravity.LEFT);
            myTV3.get(i+1).setTextColor(Color.WHITE);
            myTV3.get(i+1).setBackground(getDrawable(R.drawable.rect_border));
        }

        for (int p = MonthSelected.size()+1 ; p < MonthSelected.size()+2 ; p++)
        {
            myTV3.get(p).setText("Grand\nTotal");

            myTV3.get(p).setTextSize(15);
            myTV3.get(p).setPadding(10, 0, 10, 10);
            myTV3.get(p).setGravity(Gravity.LEFT);
            myTV3.get(p).setTextColor(Color.WHITE);
            myTV3.get(p).setBackground(getDrawable(R.drawable.rect_border));
        }

        for (int i = 0 ; i < getStoreNo.size() ; i++)
        {
            if (NewHr.equals(getHr.get(i)))
            {
                GrandTotalValue = GrandTotalValue + getNetAmt.get(i);

                TV1.setText(getHr.get(i));

                for (int k = 0 ; k < StoreSelected.size() ; k++)
                {
                    if (StoreSelected.get(k).equals(getStoreNo.get(i)))
                    {
                        myTV1.get(k+1).setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((getNetAmt.get(i) / intVal[index]) * 100.0) / 100.0));
                        netAmtToGraph.get(k).remove(netAmtToGraph.get(k).size() - 1);
                        netAmtToGraph.get(k).add(Float.parseFloat((myTV1.get(k+1).getText().toString()).replaceAll(",","")));
                    }
                }

                for (int p = StoreSelected.size()+1 ; p < StoreSelected.size()+2 ; p++)
                {
                    myTV1.get(p).setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((GrandTotalValue / intVal[index]) * 100.0) / 100.0));
                }
            }
            else
            {
                TR = new TableRow(this);
                TV1 = new TextView(this);                TV2 = new TextView(this);                TV3 = new TextView(this);                TV4 = new TextView(this);                TV5 = new TextView(this);                TV6 = new TextView(this);                TV7 = new TextView(this);                TV8 = new TextView(this);                TV9 = new TextView(this);                TV10 = new TextView(this);                TV11 = new TextView(this);                TV12 = new TextView(this);                TV13 = new TextView(this);                TV14 = new TextView(this);                TV15 = new TextView(this);                TV16 = new TextView(this);
                TR.addView(TV1);                TR.addView(TV2);                TR.addView(TV3);                TR.addView(TV4);                TR.addView(TV5);                TR.addView(TV6);                TR.addView(TV7);                TR.addView(TV8);                TR.addView(TV9);                TR.addView(TV10);                TR.addView(TV11);                TR.addView(TV12);                TR.addView(TV13);                TR.addView(TV14);                TR.addView(TV15);                TR.addView(TV16);
                TL.addView(TR);
                myTV1.clear();
                myTV1.add(TV1);                myTV1.add(TV2);                myTV1.add(TV3);                myTV1.add(TV4);                myTV1.add(TV5);                myTV1.add(TV6);                myTV1.add(TV7);                myTV1.add(TV8);                myTV1.add(TV9);                myTV1.add(TV10);                myTV1.add(TV11);                myTV1.add(TV12);                myTV1.add(TV13);                myTV1.add(TV14);                myTV1.add(TV15);                myTV1.add(TV16);

                HourToGraph.add(getHr.get(i));

                TV1.setText(getHr.get(i));
                TV1.setTextSize(15);
                TV1.setPadding(10, 0, 10, 0);
                TV1.setGravity(Gravity.LEFT);
                TV1.setTextColor(Color.WHITE);
                TV1.setBackground(getDrawable(R.drawable.rect_border));

                for (int z = 0 ; z < StoreSelected.size() + 1 ; z++)
                {
                    myTV1.get(z + 1).setText("0");

                    myTV1.get(z + 1).setTextSize(15);
                    myTV1.get(z + 1).setPadding(10, 0, 10, 0);
                    myTV1.get(z + 1).setGravity(Gravity.RIGHT);
                    myTV1.get(z + 1).setTextColor(Color.WHITE);
                    myTV1.get(z + 1).setBackground(getDrawable(R.drawable.rect_border));
                }

                for (int k = 0 ; k < StoreSelected.size() ; k++)
                {
                    netAmtToGraph.get(k).add(Float.parseFloat((myTV1.get(k+1).getText().toString()).replaceAll(",","")));
                    if (StoreSelected.get(k).equals(getStoreNo.get(i)))
                    {
                        myTV1.get(k+1).setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((getNetAmt.get(i) / intVal[index]) * 100.0) / 100.0));

                        netAmtToGraph.get(k).remove(netAmtToGraph.get(k).size() - 1);
                        netAmtToGraph.get(k).add(Float.parseFloat((myTV1.get(k+1).getText().toString()).replaceAll(",","")));
                    }
                }

                GrandTotalValue = 0;
                GrandTotalValue = GrandTotalValue + getNetAmt.get(i);
                for (int p = StoreSelected.size()+1 ; p < StoreSelected.size()+2 ; p++)
                {
                    myTV1.get(p).setText(String.valueOf(Math.round((GrandTotalValue / intVal[index]) * 100.0) / 100.0));
                }

            }

            NewHr = getHr.get(i);
        }

        //second table

        for (int i = 0 ; i < getStoreNo.size() ; i++)
        {
            if (NewHr2.equals(getHr.get(i)))
            {
                GrandTotalValue2 = GrandTotalValue2 + getNetAmt.get(i) / getNoOfRece.get(i);

                TV21.setText(getHr.get(i));

                for (int k = 0 ; k < StoreSelected.size() ; k++)
                {
                    if (StoreSelected.get(k).equals(getStoreNo.get(i)))
                    {
                        myTV1.get(k+1).setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round(((getNetAmt.get(i) / getNoOfRece.get(i)) / intVal[index]) * 100.0) / 100.0));
                        netAmtToGraph2.get(k).remove(netAmtToGraph2.get(k).size() - 1);
                        netAmtToGraph2.get(k).add(Float.parseFloat((myTV1.get(k+1).getText().toString()).replaceAll(",","")));
                    }
                }

                for (int p = StoreSelected.size()+1 ; p < StoreSelected.size()+2 ; p++)
                {
                    myTV1.get(p).setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((GrandTotalValue2 / intVal[index]) * 100.0) / 100.0));
                }
            }
            else
            {
                TR2 = new TableRow(this);
                TV21 = new TextView(this);                TV22 = new TextView(this);                TV23 = new TextView(this);                TV24 = new TextView(this);                TV25 = new TextView(this);                TV26 = new TextView(this);                TV27 = new TextView(this);                TV28 = new TextView(this);                TV29 = new TextView(this);                TV210 = new TextView(this);                TV211 = new TextView(this);                TV212 = new TextView(this);                TV213 = new TextView(this);                TV214 = new TextView(this);                TV215 = new TextView(this);                TV216 = new TextView(this);
                TR2.addView(TV21);                TR2.addView(TV22);                TR2.addView(TV23);                TR2.addView(TV24);                TR2.addView(TV25);                TR2.addView(TV26);                TR2.addView(TV27);                TR2.addView(TV28);                TR2.addView(TV29);                TR2.addView(TV210);                TR2.addView(TV211);                TR2.addView(TV212);                TR2.addView(TV213);                TR2.addView(TV214);                TR2.addView(TV215);                TR2.addView(TV216);
                TL2.addView(TR2);
                myTV1.clear();
                myTV1.add(TV21);                myTV1.add(TV22);                myTV1.add(TV23);                myTV1.add(TV24);                myTV1.add(TV25);                myTV1.add(TV26);                myTV1.add(TV27);                myTV1.add(TV28);                myTV1.add(TV29);                myTV1.add(TV210);                myTV1.add(TV211);                myTV1.add(TV212);                myTV1.add(TV213);                myTV1.add(TV214);                myTV1.add(TV215);                myTV1.add(TV216);

                HourToGraph2.add(getHr.get(i));

                TV21.setText(getHr.get(i));
                TV21.setTextSize(15);
                TV21.setPadding(10, 0, 10, 0);
                TV21.setGravity(Gravity.LEFT);
                TV21.setTextColor(Color.WHITE);
                TV21.setBackground(getDrawable(R.drawable.rect_border));

                for (int z = 0 ; z < StoreSelected.size() + 1 ; z++)
                {
                    myTV1.get(z + 1).setText("0");

                    myTV1.get(z + 1).setTextSize(15);
                    myTV1.get(z + 1).setPadding(10, 0, 10, 0);
                    myTV1.get(z + 1).setGravity(Gravity.RIGHT);
                    myTV1.get(z + 1).setTextColor(Color.WHITE);
                    myTV1.get(z + 1).setBackground(getDrawable(R.drawable.rect_border));
                }

                for (int k = 0 ; k < StoreSelected.size() ; k++)
                {
                    netAmtToGraph2.get(k).add(Float.parseFloat((myTV1.get(k+1).getText().toString()).replaceAll(",","")));

                    if (StoreSelected.get(k).equals(getStoreNo.get(i)))
                    {
                        myTV1.get(k+1).setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round(((getNetAmt.get(i) / getNoOfRece.get(i)) / intVal[index]) * 100.0) / 100.0));
                        netAmtToGraph2.get(k).remove(netAmtToGraph2.get(k).size() - 1);
                        netAmtToGraph2.get(k).add(Float.parseFloat((myTV1.get(k+1).getText().toString()).replaceAll(",","")));
                    }
                }

                GrandTotalValue2 = 0;
                GrandTotalValue2 = GrandTotalValue2 + getNetAmt.get(i) / getNoOfRece.get(i);
                for (int p = StoreSelected.size()+1 ; p < StoreSelected.size()+2 ; p++)
                {
                    myTV1.get(p).setText(String.valueOf(Math.round((GrandTotalValue2 / intVal[index]) * 100.0) / 100.0));
                }

            }

            NewHr2 = getHr.get(i);
        }

        //Third Table-----

        for (int i = 0 ; i < getMonth.size() ; i++)
        {
            if (NewHr3.equals(getHr2.get(i)))
            {
                GrandTotalValue3 = GrandTotalValue3 + getNetAmt2.get(i);

                TV31.setText(getHr2.get(i));

                for (int k = 0 ; k < MonthSelected.size() ; k++)
                {
                    if (Months1[Arrays.asList(MonthsNew).indexOf(MonthSelected.get(k)) - 1].equals(getMonth.get(i)))
                    {
                        myTV1.get(k+1).setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((getNetAmt2.get(i) / intVal[index]) * 100.0) / 100.0));
                        netAmtToGraph2.get(k).remove(netAmtToGraph2.get(k).size() - 1);
                        netAmtToGraph2.get(k).add(Float.parseFloat((myTV1.get(k+1).getText().toString()).replaceAll(",","")));
                    }
                }

                for (int p = MonthSelected.size()+1 ; p < MonthSelected.size()+2 ; p++)
                {
                    myTV1.get(p).setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((GrandTotalValue3 / intVal[index]) * 100.0) / 100.0));
                }
            }
            else
            {
                TR3 = new TableRow(this);
                TV31 = new TextView(this);                TV32 = new TextView(this);                TV33 = new TextView(this);                TV34 = new TextView(this);                TV35 = new TextView(this);                TV36 = new TextView(this);                TV37 = new TextView(this);                TV38 = new TextView(this);                TV39 = new TextView(this);                TV310 = new TextView(this);                TV311 = new TextView(this);                TV312 = new TextView(this);                TV313 = new TextView(this);                TV314 = new TextView(this);                TV315 = new TextView(this);                TV316 = new TextView(this);
                TR3.addView(TV31);                TR3.addView(TV32);                TR3.addView(TV33);                TR3.addView(TV34);                TR3.addView(TV35);                TR3.addView(TV36);                TR3.addView(TV37);                TR3.addView(TV38);                TR3.addView(TV39);                TR3.addView(TV310);                TR3.addView(TV311);                TR3.addView(TV312);                TR3.addView(TV313);                TR3.addView(TV314);                TR3.addView(TV315);                TR3.addView(TV316);
                TL3.addView(TR3);
                myTV1.clear();
                myTV1.add(TV31);                myTV1.add(TV32);                myTV1.add(TV33);                myTV1.add(TV34);                myTV1.add(TV35);                myTV1.add(TV36);                myTV1.add(TV37);                myTV1.add(TV38);                myTV1.add(TV39);                myTV1.add(TV310);                myTV1.add(TV311);                myTV1.add(TV312);                myTV1.add(TV313);                myTV1.add(TV314);                myTV1.add(TV315);                myTV1.add(TV316);

                HourToGraph3.add(getHr2.get(i));

                TV31.setText(getHr2.get(i));
                TV31.setTextSize(15);
                TV31.setPadding(10, 0, 10, 0);
                TV31.setGravity(Gravity.LEFT);
                TV31.setTextColor(Color.WHITE);
                TV31.setBackground(getDrawable(R.drawable.rect_border));

                for (int z = 0 ; z < MonthSelected.size() + 1 ; z++)
                {
                    myTV1.get(z + 1).setText("0");

                    myTV1.get(z + 1).setTextSize(15);
                    myTV1.get(z + 1).setPadding(10, 0, 10, 0);
                    myTV1.get(z + 1).setGravity(Gravity.RIGHT);
                    myTV1.get(z + 1).setTextColor(Color.WHITE);
                    myTV1.get(z + 1).setBackground(getDrawable(R.drawable.rect_border));
                }

                for (int k = 0 ; k < MonthSelected.size() ; k++)
                {
                    netAmtToGraph3.get(k).add(Float.parseFloat((myTV1.get(k+1).getText().toString()).replaceAll(",","")));

                    if (Months1[Arrays.asList(MonthsNew).indexOf(MonthSelected.get(k)) - 1].equals(getMonth.get(i)))
                    {
                        myTV1.get(k+1).setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((getNetAmt2.get(i) / intVal[index]) * 100.0) / 100.0));
                        netAmtToGraph3.get(k).remove(netAmtToGraph3.get(k).size() - 1);
                        netAmtToGraph3.get(k).add(Float.parseFloat((myTV1.get(k+1).getText().toString()).replaceAll(",","")));
                    }
                }

                GrandTotalValue3 = 0;
                GrandTotalValue3 = GrandTotalValue3 + getNetAmt2.get(i);
                for (int p = MonthSelected.size()+1 ; p < MonthSelected.size()+2 ; p++)
                {
                    myTV1.get(p).setText(String.valueOf(Math.round((GrandTotalValue3 / intVal[index]) * 100.0) / 100.0));
                }

            }

            NewHr3 = getHr2.get(i);
        }

    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Spinner spinner = (Spinner) adapterView;
        if (spinner.getId() == R.id.spin_yearselect)
        {
            dropDownYear = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(adapterView.getContext(), "Selected Store : " + dropDownYear, Toast.LENGTH_SHORT).show();
        }
        else
            if (spinner.getId() == R.id.spin_filter)
            {
                index = i;

                dropDownValue = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(), "Sorted by: " + dropDownValue, Toast.LENGTH_SHORT).show();

                TL = findViewById(R.id.tl_myTable1);
                TL2 = findViewById(R.id.tl_myTable2);
                TL3 = findViewById(R.id.tl_myTable3);

                while(TL.getChildCount() > 1)
                {
                    TL.removeView(TL.getChildAt(TL.getChildCount() - 1));
                }

                while(TL2.getChildCount() > 1)
                {
                    TL2.removeView(TL2.getChildAt(TL2.getChildCount() - 1));
                }

                while(TL3.getChildCount() > 1)
                {
                    TL3.removeView(TL3.getChildAt(TL3.getChildCount() - 1));
                }

                for (int x = 0 ; x < netAmtToGraph.size() ; x++)
                {
                    netAmtToGraph.get(x).clear();
                }
                HourToGraph.clear();

                for (int x = 0 ; x < netAmtToGraph2.size() ; x++)
                {
                    netAmtToGraph2.get(x).clear();
                }
                HourToGraph2.clear();

                for (int x = 0 ; x < netAmtToGraph3.size() ; x++)
                {
                    netAmtToGraph3.get(x).clear();
                }
                HourToGraph3.clear();

                PrintTable();
            }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}
}
