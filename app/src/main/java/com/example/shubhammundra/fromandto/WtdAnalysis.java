package com.example.shubhammundra.fromandto;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
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

public class WtdAnalysis extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TableLayout TL;
    TableRow TR;
    TextView TV1,TV2,TV3,TV4,TV5,TV6,TV7;

    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    ImageView refresh, back;

    String CurrentYear = "2001", PreviousYear = "2001";

    String dropDownYear;

    int dropDownPreviousYear;

    String CName;

    Button selectWeek;

    String[] listItems = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54"};
    boolean[] checkedItems;

    ArrayList<Integer> UserItems = new ArrayList<>();
    ArrayList<String> Year;

    String Cities = "''";

    double B = 0, B1 = 0;

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal = {1,1000,100000,1000000,10000000};

    String dropDownValue;

    int index;

    @SuppressLint("StaticFieldLeak")
    public static Spinner spinnerFilter;
    @SuppressLint("StaticFieldLeak")
    public static Spinner spinner;

    ArrayList<Double> CurrentNetAmtData = new ArrayList<>();
    ArrayList<String> CurrentDateData = new ArrayList<>();
    ArrayList<Integer> CurrentWeekNoData = new ArrayList<>();

    ArrayList<Integer> year1231 = new ArrayList<>();
    ArrayList<Integer> month1231 = new ArrayList<>();
    ArrayList<Integer> date1231 = new ArrayList<>();

    ArrayList<Integer> year1232 = new ArrayList<>();
    ArrayList<Integer> month1232 = new ArrayList<>();
    ArrayList<Integer> date1232 = new ArrayList<>();

    ArrayList<Double> LastNetAmtData = new ArrayList<>();
    ArrayList<String> LastDateData = new ArrayList<>();
    ArrayList<Integer> LastWeekNoData = new ArrayList<>();

    ArrayList<Double> YearTotalNetAmtData = new ArrayList<>();
    ArrayList<String> YearSel = new ArrayList<>();

    ArrayList<Double> WeekNetAmtData = new ArrayList<>();
    ArrayList<String> WeekYear = new ArrayList<>();
    ArrayList<Integer> WeekNo = new ArrayList<>();

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wtd_analysis);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent getintent = getIntent();
        CName = getintent.getStringExtra("Company Name");
        Year = getintent.getStringArrayListExtra("Unique Year");

        checkedItems = new boolean[listItems.length];

        selectWeek = findViewById(R.id.btn_selectcity);

        selectWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WtdAnalysis.this);
                builder.setTitle("Select Week Numbers");

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
                        String Store = "";
                        for(int i = 0 ; i < UserItems.size() ; i++)
                        {
                            Store = Store + listItems[UserItems.get(i)];
                            if(i != UserItems.size() -1)
                            {
                                Store = Store + "\n";
                            }
                        }
                        selectWeek.setText(Store);
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
                            selectWeek.setText("Select");
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

        spinner = findViewById(R.id.spin_yearselect);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Year);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

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

    public void doInBackground1() {
        CurrentYear = dropDownYear;

        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        }else{
            Log.w("Android", "Connected");

            String query = "SELECT DATEPART(YYYY,Date) as yr, DATEPART(MM,Date) as mon, DATEPART(DD,Date) as dt, Format(Convert(date, Date), 'dd-MM-yyyy') as date, DATEPART(wk, Date) as weekNo, sum(round([Net Amount],0,1)) as totalAmt FROM [dbo].["+CName+"$Transaction Header] where [Net Amount] != 0 and DATEPART(YYYY, Date) = '"+CurrentYear+"' and DATEPART(wk, Date) in ("+Cities+") group by Date, DATEPART(YYYY, Date), DATEPART(wk, Date) order by Date, DATEPART(YYYY, Date), DATEPART(wk, Date)";

            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    double netamtdata = rs.getDouble("totalAmt");
                    netamtdata = Math.abs(netamtdata);
                    CurrentNetAmtData.add(netamtdata);

                    String Date = rs.getString("date");
                    CurrentDateData.add(Date);

                    int WeekNo = rs.getInt("weekNo");
                    CurrentWeekNoData.add(WeekNo);

                    year1231.add(rs.getInt("yr"));
                    month1231.add(rs.getInt("mon"));
                    date1231.add(rs.getInt("dt"));
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

        TL = findViewById(R.id.tl_myTable1);

        PreviousYear = String.valueOf(dropDownPreviousYear);

        refresh  = findViewById(R.id.iv_refresh);

        refresh.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
                refresh.startAnimation(rotation);

                while (TL.getChildCount() > 1) {
                    TL.removeView(TL.getChildAt(TL.getChildCount() - 1));
                }

                B = 0; B1 = 0;

                Cities = "''";
                for (int i = 0; i < UserItems.size() ; i++)
                {
                    Cities = Cities + ", '" + listItems[UserItems.get(i)] + "'";
                }

                CurrentNetAmtData.clear();
                CurrentDateData.clear();
                CurrentWeekNoData.clear();

                LastNetAmtData.clear();
                LastDateData.clear();
                LastWeekNoData.clear();

                WeekNetAmtData.clear();
                WeekYear.clear();
                WeekNo.clear();

                YearTotalNetAmtData.clear();
                YearSel.clear();

                year1231.clear();
                month1231.clear();
                date1231.clear();

                year1232.clear();
                month1232.clear();
                date1232.clear();

                final ProgressDialog progressDialog = new ProgressDialog(WtdAnalysis.this);

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

            String query = "SELECT DATEPART(YYYY,Date) as yr, DATEPART(MM,Date) as mon, DATEPART(DD,Date) as dt, Format(Convert(date, Date), 'dd-MM-yyyy') as date, DATEPART(wk, Date) as weekNo, sum(round([Net Amount],0,1)) as totalAmt FROM [dbo].["+CName+"$Transaction Header] where [Net Amount] != 0 and DATEPART(YYYY, Date) = '"+PreviousYear+"' and DATEPART(wk, Date) in ("+Cities+") group by Date, DATEPART(YYYY, Date), DATEPART(wk, Date) order by Date, DATEPART(YYYY, Date), DATEPART(wk, Date)";

            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    double lastnetamtdata = rs.getLong("totalAmt");
                    lastnetamtdata = Math.abs(lastnetamtdata);
                    LastNetAmtData.add(lastnetamtdata);

                    String Date = rs.getString("date");
                    LastDateData.add(Date);

                    int WeekNo = rs.getInt("weekNo");
                    LastWeekNoData.add(WeekNo);

                    year1232.add(rs.getInt("yr"));
                    month1232.add(rs.getInt("mon"));
                    date1232.add(rs.getInt("dt"));
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
        CurrentYear = dropDownYear;

        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        }else{
            Log.w("Android", "Connected");

            String query = "SELECT DATEPART(YYYY, Date) as year, sum(round([Net Amount],0,1)) as totalAmt FROM [dbo].["+CName+"$Transaction Header] where [Net Amount] != 0 and DATEPART(YYYY, Date) = '"+CurrentYear+"' and DATEPART(wk, Date) in ("+Cities+") group by DATEPART(YYYY, Date) order by DATEPART(YYYY, Date)";

            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    double netamtdata = rs.getLong("totalAmt");
                    netamtdata = Math.abs(netamtdata);
                    YearTotalNetAmtData.add(netamtdata);

                    String Year = rs.getString("year");
                    YearSel.add(Year);
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
        CurrentYear = dropDownYear;

        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        }else{
            Log.w("Android", "Connected");

            String query = "SELECT DATEPART(YYYY, Date) as year, DATEPART(wk, Date) as weekNo, sum(round([Net Amount],0,1)) as totalAmt FROM [dbo].["+CName+"$Transaction Header] where [Net Amount] != 0 and DATEPART(YYYY, Date) = '"+CurrentYear+"' and DATEPART(wk, Date) in ("+Cities+") group by DATEPART(YYYY, Date), DATEPART(wk, Date) order by DATEPART(YYYY, Date), DATEPART(wk, Date)";

            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    double netamtdata = rs.getLong("totalAmt");
                    netamtdata = Math.abs(netamtdata);
                    WeekNetAmtData.add(netamtdata);

                    String Year = rs.getString("year");
                    WeekYear.add(Year);

                    int WeekNoNew = rs.getInt("weekNo");
                    WeekNo.add(WeekNoNew);
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

        TR = new TableRow(this);

        TV1 = new TextView(this);        TV2 = new TextView(this);        TV3 = new TextView(this);        TV4 = new TextView(this);        TV5 = new TextView(this);        TV6 = new TextView(this);        TV7 = new TextView(this);

        for (int q = 0 ; q < YearSel.size() ; q++)
        {
            TV1.setText(YearSel.get(q));

            TV2.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((YearTotalNetAmtData.get(q) / intVal[index]) * 100.0) / 100.0));
        }

        TV1.setTextSize(15);
        TV1.setPadding(60, 0, 10, 0);
        TV1.setGravity(Gravity.LEFT);
        TV1.setTextColor(Color.WHITE);
        TV1.setBackground(getDrawable(R.drawable.rect_border));

        TV2.setTextSize(15);
        TV2.setPadding(10, 0, 10, 0);
        TV2.setGravity(Gravity.RIGHT);
        TV2.setTextColor(Color.WHITE);
        TV2.setBackground(getDrawable(R.drawable.rect_border));

        TV3.setTextSize(15);
        TV3.setPadding(10, 0, 10, 0);
        TV3.setGravity(Gravity.RIGHT);
        TV3.setTextColor(Color.WHITE);
        TV3.setBackground(getDrawable(R.drawable.rect_border));

        TV4.setTextSize(15);
        TV4.setPadding(10, 0, 10, 0);
        TV4.setGravity(Gravity.RIGHT);
        TV4.setTextColor(Color.WHITE);
        TV4.setBackground(getDrawable(R.drawable.rect_border));

        TV5.setTextSize(15);
        TV5.setPadding(10, 0, 10, 0);
        TV5.setGravity(Gravity.RIGHT);
        TV5.setTextColor(Color.WHITE);
        TV5.setBackground(getDrawable(R.drawable.rect_border));

        TV6.setTextSize(15);
        TV6.setPadding(10, 0, 10, 0);
        TV6.setGravity(Gravity.RIGHT);
        TV6.setTextColor(Color.WHITE);
        TV6.setBackground(getDrawable(R.drawable.rect_border));

        TV7.setTextSize(15);
        TV7.setPadding(10, 0, 10, 0);
        TV7.setGravity(Gravity.RIGHT);
        TV7.setTextColor(Color.WHITE);
        TV7.setBackground(getDrawable(R.drawable.rect_border));

        TR.addView(TV1);        TR.addView(TV2);        TR.addView(TV3);        TR.addView(TV4);        TR.addView(TV5);        TR.addView(TV6);        TR.addView(TV7);
        TL.addView(TR);

        for (int i = 0 ; i < WeekNetAmtData.size() ; i++)
        {
            TR = new TableRow(this);
            TV1 = new TextView(this);            TV2 = new TextView(this);            TV3 = new TextView(this);            TV4 = new TextView(this);            TV5 = new TextView(this);            TV6 = new TextView(this);            TV7 = new TextView(this);
            TV1.setText(String.valueOf(WeekYear.get(i) + "-" + WeekNo.get(i)));

            TV2.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((WeekNetAmtData.get(i) / intVal[index]) * 100.0) / 100.0));

            TV1.setTextSize(15);
            TV1.setPadding(10, 0, 10, 0);
            TV1.setGravity(Gravity.LEFT);
            TV1.setTextColor(Color.WHITE);

            TV2.setTextSize(15);
            TV2.setPadding(10, 0, 10, 0);
            TV2.setGravity(Gravity.RIGHT);
            TV2.setTextColor(Color.WHITE);

            TR.addView(TV1);
            TR.addView(TV2);

            TL.addView(TR);

            B = 0;

            for (int j = 0 ; j < CurrentNetAmtData.size() ; j++)
            {
                if (CurrentWeekNoData.get(j).equals(WeekNo.get(i)))
                {
                    TR = new TableRow(this);
                    TV1 = new TextView(this);                    TV2 = new TextView(this);                    TV3 = new TextView(this);                    TV4 = new TextView(this);                    TV5 = new TextView(this);                    TV6 = new TextView(this);                    TV7 = new TextView(this);
                    TV1.setText("0");                    TV2.setText("0");                    TV3.setText("0");                    TV4.setText("0");                    TV5.setText("0");                    TV6.setText("0");                    TV7.setText("0");

                    TV1.setText(String.valueOf(CurrentDateData.get(j)));

                    TV2.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((CurrentNetAmtData.get(j) / intVal[index]) * 100.0) / 100.0));

                    B = B + CurrentNetAmtData.get(j);
                    TV3.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((B / intVal[index]) * 100.0) / 100.0));

                    for (int k = 0; k < LastNetAmtData.size(); k++) {
                        if (year1231.get(j).equals(year1232.get(k) + 1) && month1231.get(j).equals(month1232.get(k)) && date1231.get(j).equals(date1232.get(k))) {
                            B1 = B1 + LastNetAmtData.get(k);
                            TV4.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((B1 / intVal[index]) * 100.0) / 100.0));
                            break;
                        }
                    }

                    if (B1 != 0) {
                        Double e = Double.parseDouble(String.valueOf(B));
                        Double f = Double.parseDouble(String.valueOf(B1));

                        Double g;

                        if (f != 0) {
                            g = (e / f) * 100;
                        } else
                            g = e * 100;

                        TV5.setText(String.valueOf(Math.round((g) * 100.0) / 100.0) + "%");

                        long h = e.longValue() - f.longValue();
                        TV6.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((h / intVal[index]) * 100.0) / 100.0));

                        TV7.setText(String.valueOf(Math.round((g - 100) * 100.0) / 100.0) + "%");
                    }

                    TV1.setTextSize(15);
                    TV1.setPadding(10, 0, 10, 0);
                    TV1.setGravity(Gravity.LEFT);
                    TV1.setTextColor(Color.WHITE);
                    TV1.setBackground(getDrawable(R.drawable.rect_border));

                    TV2.setTextSize(15);
                    TV2.setPadding(10, 0, 10, 0);
                    TV2.setGravity(Gravity.RIGHT);
                    TV2.setTextColor(Color.WHITE);
                    TV2.setBackground(getDrawable(R.drawable.rect_border));

                    TV3.setTextSize(15);
                    TV3.setPadding(10, 0, 10, 0);
                    TV3.setGravity(Gravity.RIGHT);
                    TV3.setTextColor(Color.WHITE);
                    TV3.setBackground(getDrawable(R.drawable.rect_border));

                    TV4.setTextSize(15);
                    TV4.setPadding(10, 0, 10, 0);
                    TV4.setGravity(Gravity.RIGHT);
                    TV4.setTextColor(Color.WHITE);
                    TV4.setBackground(getDrawable(R.drawable.rect_border));

                    TV5.setTextSize(15);
                    TV5.setPadding(10, 0, 10, 0);
                    TV5.setGravity(Gravity.RIGHT);
                    TV5.setTextColor(Color.WHITE);
                    TV5.setBackground(getDrawable(R.drawable.rect_border));

                    TV6.setTextSize(15);
                    TV6.setPadding(10, 0, 10, 0);
                    TV6.setGravity(Gravity.RIGHT);
                    TV6.setTextColor(Color.WHITE);
                    TV6.setBackground(getDrawable(R.drawable.rect_border));

                    TV7.setTextSize(15);
                    TV7.setPadding(10, 0, 10, 0);
                    TV7.setGravity(Gravity.RIGHT);
                    TV7.setTextColor(Color.WHITE);
                    TV7.setBackground(getDrawable(R.drawable.rect_border));

                    TR.addView(TV1);                    TR.addView(TV2);                    TR.addView(TV3);                    TR.addView(TV4);                    TR.addView(TV5);                    TR.addView(TV6);                    TR.addView(TV7);
                    TL.addView(TR);
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner) adapterView;

        if (spinner.getId() == R.id.spin_yearselect)
        {
            dropDownYear = adapterView.getItemAtPosition(i).toString();

            dropDownPreviousYear = Integer.parseInt(adapterView.getItemAtPosition(i).toString()) - 1;

            Toast.makeText(adapterView.getContext(), "Selected Year : " + dropDownYear, Toast.LENGTH_SHORT).show();
        }
        else
            if (spinner.getId() == R.id.spin_filter)
            {
                TL = findViewById(R.id.tl_myTable1);

                index = i;

                dropDownValue = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(), "Sorted by: " + dropDownValue, Toast.LENGTH_SHORT).show();

                while (TL.getChildCount() > 1)
                {
                    TL.removeView(TL.getChildAt(TL.getChildCount() - 1));
                }

                PrintTable();
            }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}
}
