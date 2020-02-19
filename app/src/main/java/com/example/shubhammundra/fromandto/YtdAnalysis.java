package com.example.shubhammundra.fromandto;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class YtdAnalysis extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TableLayout TL1;
    TableRow TR1;

    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    String CName;

    ArrayList<String> Store ;
    ArrayList<String> Year ;

    ArrayList<Integer> UserItems = new ArrayList<>();

    String[] listItems;
    boolean[] checkedItems;

    Button selectYear;

    ImageView back,refresh;

    String YearsSelected = "''", LastYearsSelected = "''";

    ArrayList<Integer> CurrYearData = new ArrayList<>();
    ArrayList<Double> CurrNetAmt = new ArrayList<>();

    ArrayList<Integer> PrevYearData = new ArrayList<>();
    ArrayList<Double> PrevNetAmt = new ArrayList<>();

    ArrayList<Integer> CurrYearYMData = new ArrayList<>();
    ArrayList<Integer> CurrMonthYMData = new ArrayList<>();
    ArrayList<Double> CurrNetYMAmt = new ArrayList<>();

    ArrayList<Integer> PrevYearYMData = new ArrayList<>();
    ArrayList<Integer> PrevMonthYMData = new ArrayList<>();
    ArrayList<Double> PrevNetYMAmt = new ArrayList<>();

    TextView TV11,TV12,TV13,TV14,TV15,TV16,TV17;

    double B = 0;

    String[] getMonth = {"","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal = {1,1000,100000,1000000,10000000};

    String dropDownValue;

    GradientDrawable gd;

    int index;

    @SuppressLint("StaticFieldLeak")
    public static Spinner spinnerFilter;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ytd_analysis);

        TL1 = findViewById(R.id.tl_myTable1);

        Intent getintent = getIntent();
        CName = getintent.getStringExtra("Company Name");
        Store = getintent.getStringArrayListExtra("Store Name");
        Year = getintent.getStringArrayListExtra("Unique Year");

        listItems = Year.toArray(new String[Year.size()]);
        checkedItems = new boolean[listItems.length];

        selectYear = findViewById(R.id.btn_selectYear);
        selectYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(YtdAnalysis.this);
                builder.setTitle("Select Year");

                builder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                        if(isChecked)
                        {
                            if (! UserItems.contains(position)){
                                UserItems.add(position);
                            }
                        }
                        else
                        if (UserItems.contains(position))
                        {
                            UserItems.remove(UserItems.indexOf(position));
                        }

                    }
                });

                builder.setCancelable(false);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String MultiYear = "";
                        for(int i = 0 ; i < UserItems.size() ; i++)
                        {
                            MultiYear = MultiYear + listItems[UserItems.get(i)];
                            if(i != UserItems.size() -1)
                            {
                                MultiYear = MultiYear + "\n";
                            }
                        }
                        selectYear.setText(MultiYear);
                    }
                });

                builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for(int i = 0 ; i < checkedItems.length ; i++)
                        {
                            checkedItems[i] = false;
                            UserItems.clear();
                            selectYear.setText("Select");
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

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
                doInBackground2();
                return null;
            }
        }.execute();
    }

    @SuppressLint({"RtlHardcoded", "SetTextI18n"})
    public void doInBackground1() {
        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        }else{
            Log.w("Android", "Connected");

            String query = "SELECT sum([Net Amount]) as netamt, DATEPART(YYYY,Date) as year, DATEPART(MM,Date) as month FROM [dbo].["+CName+"$Transaction Header] where [Net Amount] != 0 and DATEPART(YYYY,Date) in ("+YearsSelected+") group by DATEPART(YYYY,Date), DATEPART(MM,Date) order by DATEPART(YYYY,Date), DATEPART(MM,Date)";

            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");
                while (rs.next()) {

                    double Currnetamtdata = rs.getDouble("netamt");
                    Currnetamtdata = Math.abs(Currnetamtdata);

                    CurrNetYMAmt.add(Currnetamtdata);

                    int yeardata = rs.getInt("year");
                    CurrYearYMData.add(yeardata);

                    int monthdata = rs.getInt("month");
                    CurrMonthYMData.add(monthdata);
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void doInBackground2() {
        TL1 = findViewById(R.id.tl_myTable1);

        refresh = findViewById(R.id.iv_refresh);

        refresh.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
                refresh.startAnimation(rotation);

                while (TL1.getChildCount() > 1) {
                    TL1.removeView(TL1.getChildAt(TL1.getChildCount() - 1));
                }

                YearsSelected = "''";
                for (int i = 0; i < UserItems.size() ; i++)
                {
                    YearsSelected = YearsSelected + ", '" + listItems[UserItems.get(i)] + "'";
                }

                for (int i = 0; i < UserItems.size() ; i++)
                {
                    String yr = listItems[UserItems.get(i)];
                    int qwerty = Integer.parseInt(yr);
                    qwerty = qwerty - 1;
                    yr = String.valueOf(qwerty);

                    LastYearsSelected = LastYearsSelected + ", '" + yr + "'";
                }

                CurrYearData.clear();
                CurrNetAmt.clear();

                PrevYearData.clear();
                PrevNetAmt.clear();

                CurrYearYMData.clear();
                CurrMonthYMData.clear();
                CurrNetYMAmt.clear();

                PrevYearYMData.clear();
                PrevMonthYMData.clear();
                PrevNetYMAmt.clear();

                final ProgressDialog progressDialog = new ProgressDialog(YtdAnalysis.this);

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
                        doInBackground3();
                        doInBackground4();
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
        }else{
            Log.w("Android", "Connected");

            String query = "SELECT sum([Net Amount]) as netamt, DATEPART(YYYY,Date) as year, DATEPART(MM,Date) as month FROM [dbo].["+CName+"$Transaction Header] where [Net Amount] != 0 and DATEPART(YYYY,Date) in ("+LastYearsSelected+") group by DATEPART(YYYY,Date), DATEPART(MM,Date) order by DATEPART(YYYY,Date), DATEPART(MM,Date)";

            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {

                    double lastnetamtdata = rs.getDouble("netamt");
                    lastnetamtdata = Math.abs(lastnetamtdata);

                    PrevNetYMAmt.add(lastnetamtdata);

                    int yeardata = rs.getInt("year");
                    PrevYearYMData.add(yeardata);

                    int monthdata = rs.getInt("month");
                    PrevMonthYMData.add(monthdata);
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void doInBackground3() {
        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        }else{
            Log.w("Android", "Connected");

            String query = "SELECT sum([Net Amount]) as netamt, DATEPART(YYYY,Date) as year FROM [dbo].["+CName+"$Transaction Header] where [Net Amount] != 0 and DATEPART(YYYY,Date) in ("+LastYearsSelected+") group by DATEPART(YYYY,Date) order by DATEPART(YYYY,Date)";

            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {

                    double lastnetamtdata = rs.getDouble("netamt");
                    lastnetamtdata = Math.abs(lastnetamtdata);

                    PrevNetAmt.add(lastnetamtdata);

                    int yeardata = rs.getInt("year");
                    PrevYearData.add(yeardata);
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void doInBackground4() {
        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        }else{
            Log.w("Android", "Connected");

            String query = "SELECT sum([Net Amount]) as netamt, DATEPART(YYYY,Date) as year FROM [dbo].["+CName+"$Transaction Header] where [Net Amount] != 0 and DATEPART(YYYY,Date) in ("+YearsSelected+") group by DATEPART(YYYY,Date) order by DATEPART(YYYY,Date)";

            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {

                    double Currnetamtdata = rs.getDouble("netamt");
                    Currnetamtdata = Math.abs(Currnetamtdata);

                    CurrNetAmt.add(Currnetamtdata);

                    int yeardata = rs.getInt("year");
                    CurrYearData.add(yeardata);
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint({"SetTextI18n", "RtlHardcoded"})
    public void PrintTable() {

        for (int i = 0 ; i < CurrYearData.size() ; i++)
        {
            TL1.setStretchAllColumns(true);
            TR1 = new TableRow(this);
            TV11 = new TextView(this);            TV12 = new TextView(this);            TV13 = new TextView(this);            TV14 = new TextView(this);            TV15 = new TextView(this);            TV16 = new TextView(this);            TV17 = new TextView(this);

            TV11.setText(String.valueOf(CurrYearData.get(i)));

            TV12.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((CurrNetAmt.get(i) / intVal[index]) * 100.0) / 100.0));

            TV13.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((CurrNetAmt.get(i) / intVal[index]) * 100.0) / 100.0));

            double val1 = 0;

            for (int j = 0 ; j < PrevYearData.size() ; j++)
            {
                if (CurrYearData.get(i) == (PrevYearData.get(j) + 1))
                {
                    val1 = PrevNetAmt.get(j);
                    TV14.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((PrevNetAmt.get(j) / intVal[index]) * 100.0) / 100.0));
                    break;
                }
                else
                {
                    val1 = 0;
                    TV14.setText("0");
                }
            }

            TV15.setText("0");
            TV16.setText("0");
            TV17.setText("0");

            if (val1 != 0)
            {
                Double a = Double.parseDouble(String.valueOf(CurrNetAmt.get(i)));
                Double b = Double.parseDouble(String.valueOf(val1));

                Double c;

                c = (a * 100) / b;

                TV15.setText((Math.round(c * 100.0) / 100.0) + "%");

                TV16.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round(((a.intValue() - b.intValue()) / intVal[index]) * 100.0) / 100.0));

                TV17.setText((Math.round((c - 100) * 100.0) / 100.0) + "%");
            }

            TV11.setTextSize(15);
            TV11.setPadding(10, 0, 10, 0);
            TV11.setGravity(Gravity.LEFT);
            TV11.setTextColor(Color.WHITE);
            TV11.setBackground(getDrawable(R.drawable.rect_border));

            TV12.setTextSize(15);
            TV12.setPadding(10, 0, 10, 0);
            TV12.setGravity(Gravity.RIGHT);
            TV12.setTextColor(Color.WHITE);
            TV12.setBackground(getDrawable(R.drawable.rect_border));

            TV13.setTextSize(15);
            TV13.setPadding(10, 0, 10, 0);
            TV13.setGravity(Gravity.RIGHT);
            TV13.setTextColor(Color.WHITE);
            TV13.setBackground(getDrawable(R.drawable.rect_border));

            TV14.setTextSize(15);
            TV14.setPadding(10, 0, 10, 0);
            TV14.setGravity(Gravity.RIGHT);
            TV14.setTextColor(Color.WHITE);
            TV14.setBackground(getDrawable(R.drawable.rect_border));

            TV15.setTextSize(15);
            TV15.setPadding(10, 0, 10, 0);
            TV15.setGravity(Gravity.RIGHT);
            TV15.setTextColor(Color.WHITE);
            TV15.setBackground(getDrawable(R.drawable.rect_border));

            TV16.setTextSize(15);
            TV16.setPadding(10, 0, 10, 0);
            TV16.setGravity(Gravity.RIGHT);
            TV16.setTextColor(Color.WHITE);
            TV16.setBackground(getDrawable(R.drawable.rect_border));

            TV17.setTextSize(15);
            TV17.setPadding(10, 0, 10, 0);
            TV17.setGravity(Gravity.RIGHT);
            TV17.setTextColor(Color.WHITE);
            TV17.setBackground(getDrawable(R.drawable.rect_border));

            TR1.addView(TV11);            TR1.addView(TV12);            TR1.addView(TV13);            TR1.addView(TV14);            TR1.addView(TV15);            TR1.addView(TV16);            TR1.addView(TV17);

            TL1.addView(TR1);

            B = 0;

            for (int k = 0 ; k < CurrYearYMData.size() ; k++)
            {
                if (CurrYearData.get(i).equals(CurrYearYMData.get(k)))
                {
                    TL1.setStretchAllColumns(true);
                    TR1 = new TableRow(this);
                    TV11 = new TextView(this);                    TV12 = new TextView(this);                    TV13 = new TextView(this);                    TV14 = new TextView(this);                    TV15 = new TextView(this);                    TV16 = new TextView(this);                    TV17 = new TextView(this);

                    TV11.setText(String.valueOf(CurrYearYMData.get(k) + " - " + getMonth[CurrMonthYMData.get(k)]));

                    TV12.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((CurrNetYMAmt.get(k) / intVal[index]) * 100.0) / 100.0));

                    B = B + CurrNetYMAmt.get(k);

                    TV13.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((B / intVal[index]) * 100.0) / 100.0));

                    double val2 = 0;

                    for (int j = 0 ; j < PrevYearYMData.size() ; j++)
                    {
                        if (CurrYearYMData.get(k) == (PrevYearYMData.get(j) + 1) && CurrMonthYMData.get(k).equals(PrevMonthYMData.get(j)))
                        {
                            val2 = PrevNetYMAmt.get(j);
                            TV14.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((PrevNetYMAmt.get(j) / intVal[index]) * 100.0) / 100.0));
                            break;
                        }
                        else
                        {
                            val2 = 0;
                            TV14.setText("0");
                        }
                    }

                    TV15.setText("0");
                    TV16.setText("0");
                    TV17.setText("0");

                    if (val2 != 0)
                    {
                        Double a = Double.parseDouble(String.valueOf(CurrNetYMAmt.get(k)));
                        Double b = Double.parseDouble(String.valueOf(val2));

                        Double c;

                        c = (a * 100) / b;

                        TV15.setText((Math.round(c * 100.0) / 100.0) + "%");

                        TV16.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round(((a.intValue() - b.intValue()) / intVal[index]) * 100.0) / 100.0));

                        TV17.setText(Math.round(((c - 100) * 100.0) / 100.0) + "%");
                    }

                    TV11.setTextSize(15);
                    TV11.setPadding(60, 0, 10, 0);
                    TV11.setGravity(Gravity.LEFT);
                    TV11.setTextColor(Color.WHITE);
                    TV11.setBackground(getDrawable(R.drawable.rect_border));

                    TV12.setTextSize(15);
                    TV12.setPadding(10, 0, 10, 0);
                    TV12.setGravity(Gravity.RIGHT);
                    TV12.setTextColor(Color.WHITE);
                    TV12.setBackground(getDrawable(R.drawable.rect_border));

                    TV13.setTextSize(15);
                    TV13.setPadding(10, 0, 10, 0);
                    TV13.setGravity(Gravity.RIGHT);
                    TV13.setTextColor(Color.WHITE);
                    TV13.setBackground(getDrawable(R.drawable.rect_border));

                    TV14.setTextSize(15);
                    TV14.setPadding(10, 0, 10, 0);
                    TV14.setGravity(Gravity.RIGHT);
                    TV14.setTextColor(Color.WHITE);
                    TV14.setBackground(getDrawable(R.drawable.rect_border));

                    TV15.setTextSize(15);
                    TV15.setPadding(10, 0, 10, 0);
                    TV15.setGravity(Gravity.RIGHT);
                    TV15.setTextColor(Color.WHITE);
                    TV15.setBackground(getDrawable(R.drawable.rect_border));

                    TV16.setTextSize(15);
                    TV16.setPadding(10, 0, 10, 0);
                    TV16.setGravity(Gravity.RIGHT);
                    TV16.setTextColor(Color.WHITE);
                    TV16.setBackground(getDrawable(R.drawable.rect_border));

                    TV17.setTextSize(15);
                    TV17.setPadding(10, 0, 10, 0);
                    TV17.setGravity(Gravity.RIGHT);
                    TV17.setTextColor(Color.WHITE);
                    TV17.setBackground(getDrawable(R.drawable.rect_border));

                    TR1.addView(TV11);                    TR1.addView(TV12);                    TR1.addView(TV13);                    TR1.addView(TV14);                    TR1.addView(TV15);                    TR1.addView(TV16);                    TR1.addView(TV17);

                    TL1.addView(TR1);
                }
            }
        }
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        index = i;

        dropDownValue = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), "Sorted by: " + dropDownValue, Toast.LENGTH_SHORT).show();

        while(TL1.getChildCount() > 1)
        {
            TL1.removeView(TL1.getChildAt(TL1.getChildCount() - 1));
        }

        PrintTable();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}
}
