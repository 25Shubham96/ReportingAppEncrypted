package com.example.shubhammundra.fromandto;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
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
import android.widget.DatePicker;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FrequencyReport extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TableLayout TL,TL2,TL3;
    TableRow TR;
    TextView TV1, TV2;
    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    TextView storeStart;
    TextView storeEnd;

    ImageView im1,im2;

    String storeStartDate = "2001-01-01";

    String storeEndDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

    Button b1;

    Calendar startCalendar = Calendar.getInstance();
    Calendar endCalendar = Calendar.getInstance();

    String CName;

    int count = 1;

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal= {1,1000,100000,1000000,10000000};

    String dropDownYear;

    ArrayList<String> NoOfSalesCity = new ArrayList<>();
    ArrayList<Double> NoOfSales = new ArrayList<>();

    ArrayList<Float> AmountToGraph = new ArrayList<>();

    ArrayList<String> LineDiscountCity = new ArrayList<>();
    ArrayList<Double> LineDiscount = new ArrayList<>();

    ArrayList<Float> AmountToGraph2 = new ArrayList<>();

    ArrayList<String> ReturnSalesCity = new ArrayList<>();
    ArrayList<Double> ReturnSales = new ArrayList<>();

    ArrayList<Float> AmountToGraph3 = new ArrayList<>();

    int index;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequency_report);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        CName = intent.getStringExtra("Company Name");

        TL = findViewById(R.id.tl_myFreqTable);
        TL2 = findViewById(R.id.tl2);
        TL3 = findViewById(R.id.tl3);

        Spinner spinner = findViewById(R.id.spin_filter);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, amtFilter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        new AsyncTask<Void, Void, Void>(){

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
                doInBackground3();
                return null;
            }
        }.execute();

        storeStart = findViewById(R.id.tv_startdate);
        storeEnd = findViewById(R.id.tv_enddate);
        storeStart.setText(storeStartDate);
        storeEnd.setText(storeEndDate);

        im1 = findViewById(R.id.img1);
        im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        b1 = findViewById(R.id.btn_frequencychart);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent barIntent = new Intent(FrequencyReport.this,FreqBar.class);
                barIntent.putExtra("X Values",NoOfSalesCity);
                barIntent.putExtra("Y Values",AmountToGraph);
                barIntent.putExtra("Bar2String",LineDiscountCity);
                barIntent.putExtra("Bar2",AmountToGraph2);
                barIntent.putExtra("Bar3String",ReturnSalesCity);
                barIntent.putExtra("Bar3",AmountToGraph3);
                startActivity(barIntent);
            }
        });
    }

    @SuppressLint("RtlHardcoded")
    public void doInBackground1() {

        final DatePickerDialog.OnDateSetListener DPstartDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                startCalendar.set(Calendar.YEAR, year);
                startCalendar.set(Calendar.MONTH, month);
                startCalendar.set(Calendar.DAY_OF_MONTH, day);
                StartUpdateLabel();
                storeStartDate = storeStart.getText().toString();
            }
        };

        final DatePickerDialog.OnDateSetListener DPendDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                endCalendar.set(Calendar.YEAR, year);
                endCalendar.set(Calendar.MONTH, month);
                endCalendar.set(Calendar.DAY_OF_MONTH, day);
                EndUpdateLabel();
                storeEndDate = storeEnd.getText().toString();
            }
        };

        im2 = findViewById(R.id.iv_refresh);
        storeStart = findViewById(R.id.tv_startdate);
        storeEnd = findViewById(R.id.tv_enddate);

        im2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {

                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);

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
                NoOfSalesCity.clear();
                NoOfSales.clear();
                LineDiscountCity.clear();
                LineDiscount.clear();
                ReturnSalesCity.clear();
                ReturnSales.clear();
                AmountToGraph.clear();
                AmountToGraph2.clear();
                AmountToGraph3.clear();

                final ProgressDialog progressDialog = new ProgressDialog(FrequencyReport.this);
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
                        storeStart.setText(storeStartDate);
                        storeEnd.setText(storeEndDate);
                        PrintTable();
                        progressDialog.dismiss();
                    }
                    @Override
                    protected Void doInBackground(Void... voids) {
                        if (Looper.myLooper() == null)
                        {
                            Looper.prepare();
                        }
                        doInBackground1();
                        doInBackground2();
                        doInBackground3();
                        return null;
                    }
                }.execute();
                im2.startAnimation(rotation);
            }
        });

        storeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(FrequencyReport.this, DPstartDate, startCalendar.get(Calendar.YEAR),startCalendar.get(Calendar.MONTH),startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        storeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(FrequencyReport.this, DPendDate, endCalendar.get(Calendar.YEAR),endCalendar.get(Calendar.MONTH),endCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android","Connecting....");

        if (connect == null)
        {
            Log.w("Android","Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        }
        else
        {
            Log.w("Android","Connected");
            String query = "Select store.City, count(th.[Receipt No_]) as NO from ["+CName+"$Store] as store,["+CName+"$Transaction Header] as th where th.[Store No_] = store.[No_] and th.[Date] between '"+storeStartDate+"' and '"+storeEndDate+"' group by store.City";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {
                    NoOfSalesCity.add(rs.getString("City"));
                    NoOfSales.add(rs.getDouble("NO"));
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("RtlHardcoded")
    public void  doInBackground2() {
        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android","Connecting....");

        if (connect == null)
        {
            Log.w("Android","Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        }
        else
        {
            Log.w("Android","Connected");
            String query = "Select store.City, count(Distinct tse.[Receipt No_]) as NO from ["+CName+"$Store] as store,["+CName+"$Trans_ Sales Entry] as tse where tse.[Store No_] = store.[No_] and tse.[Date] between '"+storeStartDate+"' and '"+storeEndDate+"' and [Line was Discounted] != 0 group by store.City";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {
                    LineDiscountCity.add(rs.getString("City"));
                    LineDiscount.add(rs.getDouble("NO"));
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("RtlHardcoded")
    public void  doInBackground3() {
        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android","Connecting....");

        if (connect == null)
        {
            Log.w("Android","Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        }
        else
        {
            Log.w("Android","Connected");
            String query = "Select store.City, count(Distinct tse.[Receipt No_]) as NO from ["+CName+"$Store] as store,["+CName+"$Transaction Header] as tse where tse.[Store No_] = store.[No_] and tse.[Date] between '"+storeStartDate+"' and '"+storeEndDate+"' and [Sale Is Return Sale] != 0 group by store.City";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {
                    ReturnSalesCity.add(rs.getString("City"));
                    ReturnSales.add(rs.getDouble("NO"));
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("RtlHardcoded")
    public void PrintTable() {
        for (int i = 0 ; i < NoOfSales.size() ; i++)
        {
            TL.setStretchAllColumns(true);
            TR = new TableRow(this);
            TV1 = new TextView(this);
            TV2 = new TextView(this);

            TV1.setText(NoOfSalesCity.get(i));
            TV2.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((NoOfSales.get(i) / intVal[index]) * 100.0) / 100.0 ));
            AmountToGraph.add(Float.parseFloat(String.valueOf(Math.round((NoOfSales.get(i) / intVal[index]) * 100.0) / 100.0)));

            TV1.setTextSize(16);
            TV1.setPadding(10,0,10,0);
            TV1.setGravity(Gravity.LEFT);
            TV1.setTextColor(Color.WHITE);
            TV1.setBackground(getDrawable(R.drawable.rect_border));

            TV2.setTextSize(16);
            TV2.setPadding(10,0,10,0);
            TV2.setGravity(Gravity.RIGHT);
            TV2.setTextColor(Color.WHITE);
            TV2.setBackground(getDrawable(R.drawable.rect_border));

            TR.addView(TV1);
            TR.addView(TV2);
            TL.addView(TR);
        }
        for (int i = 0 ; i < LineDiscount.size() ; i++)
        {
            TL2.setStretchAllColumns(true);
            TR = new TableRow(this);
            TV1 = new TextView(this);
            TV2 = new TextView(this);

            TV1.setText(LineDiscountCity.get(i));
            TV2.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((LineDiscount.get(i) / intVal[index]) * 100.0) / 100.0));
            AmountToGraph2.add(Float.parseFloat(String.valueOf(Math.round((LineDiscount.get(i) / intVal[index]) * 100.0) / 100.0)));

            TV1.setTextSize(16);
            TV1.setPadding(10,0,10,0);
            TV1.setGravity(Gravity.LEFT);
            TV1.setTextColor(Color.WHITE);
            TV1.setBackground(getDrawable(R.drawable.rect_border));

            TV2.setTextSize(16);
            TV2.setPadding(10,0,10,0);
            TV2.setGravity(Gravity.RIGHT);
            TV2.setTextColor(Color.WHITE);
            TV2.setBackground(getDrawable(R.drawable.rect_border));

            TR.addView(TV1);
            TR.addView(TV2);
            TL2.addView(TR);
        }
        for (int i = 0 ; i < ReturnSales.size() ; i++)
        {
            TL3.setStretchAllColumns(true);
            TR = new TableRow(this);
            TV1 = new TextView(this);
            TV2 = new TextView(this);

            TV1.setText(ReturnSalesCity.get(i));
            TV2.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((ReturnSales.get(i) / intVal[index]) * 100.0) / 100.0));
            AmountToGraph3.add(Float.parseFloat(String.valueOf(Math.round((ReturnSales.get(i) / intVal[index]) * 100.0) / 100.0)));

            TV1.setTextSize(16);
            TV1.setPadding(10,0,10,0);
            TV1.setGravity(Gravity.LEFT);
            TV1.setTextColor(Color.WHITE);
            TV1.setBackground(getDrawable(R.drawable.rect_border));

            TV2.setTextSize(16);
            TV2.setPadding(10,0,10,0);
            TV2.setGravity(Gravity.RIGHT);
            TV2.setTextColor(Color.WHITE);
            TV2.setBackground(getDrawable(R.drawable.rect_border));

            TR.addView(TV1);
            TR.addView(TV2);
            TL3.addView(TR);
        }
    }

    public void StartUpdateLabel(){
        String myStartFormat = "yyyy-MM-dd";
        SimpleDateFormat STARTsdf = new SimpleDateFormat(myStartFormat,Locale.US);
        storeStart.setText(STARTsdf.format(startCalendar.getTime()));
    }
    public void EndUpdateLabel(){
        String myEndFormat = "yyyy-MM-dd";
        SimpleDateFormat ENDsdf = new SimpleDateFormat(myEndFormat,Locale.US);
        storeEnd.setText(ENDsdf.format(endCalendar.getTime()));
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        index = i;

        dropDownYear = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), "Sorted by: " + dropDownYear, Toast.LENGTH_SHORT).show();

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
        AmountToGraph.clear();
        AmountToGraph2.clear();
        AmountToGraph3.clear();

        PrintTable();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
