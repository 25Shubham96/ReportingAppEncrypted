package com.example.shubhammundra.fromandto;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
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

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

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

public class ReceiptReport extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TableLayout TL,TL2,TL3,TL4;
    TableRow TR,TR2,TR3,TR4;
    TextView TV1,TV2,TV21,TV22,TV31,TV32,TV41,TV42;

    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    TextView storeStart;
    TextView storeEnd;

    ImageView im1,im2;

    String storeStartDate = "2001-01-01";

    String storeEndDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

    String selectStoreNo = "S0001";

    String dropDownStoreNo;

    public ArrayList<Double> citiessales = new ArrayList<>();
    public ArrayList<String> noofdiffcity = new ArrayList<>();

    public ArrayList<Double> monthsales = new ArrayList<>();
    public ArrayList<String> nodiffmonths = new ArrayList<>();

    public ArrayList<Double> hoursales = new ArrayList<>();
    public ArrayList<String> noofdiffhours = new ArrayList<>();

    public ArrayList<Double> cashiersales = new ArrayList<>();
    public ArrayList<String> noofdiffcashier = new ArrayList<>();

    Button b1;

    Calendar startCalendar = Calendar.getInstance();
    Calendar endCalendar = Calendar.getInstance();
    String[] Months = {" ","January","February","March","April","May","June","July","August","September","October","November","December"};

    String CName;

    int count = 1;

    ArrayList<String> Year,Str;

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal= {1,1000,100000,1000000,10000000};

    String dropDownValue;

    ArrayList<Float> AmountToGraph1 = new ArrayList<>();
    ArrayList<Float> AmountToGraph2 = new ArrayList<>();
    ArrayList<Float> AmountToGraph3 = new ArrayList<>();
    ArrayList<Float> AmountToGraph4 = new ArrayList<>();

    GradientDrawable gd;

    int index;

    @SuppressLint("StaticFieldLeak")
    public static Spinner spinnerFilter;

    @SuppressLint("StaticFieldLeak")
    public static SearchableSpinner spinner;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipt_report_table);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        CName = intent.getStringExtra("Company Name");
        Year = intent.getStringArrayListExtra("Unique Year");
        Str = intent.getStringArrayListExtra("Store Name");

        TL = findViewById(R.id.tl_myReceiptTable1);
        TL2 = findViewById(R.id.tl_myReceiptTable2);
        TL3 = findViewById(R.id.tl_myReceiptTable3);
        TL4 = findViewById(R.id.tl_myReceiptTable4);

        spinnerFilter = findViewById(R.id.spin_filter);
        spinnerFilter.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, amtFilter);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapter1);

        spinner = findViewById(R.id.spin_storeselect);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Str);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setTitle("Select Store");

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
                doInBackground4();
                return null;
            }
        }.execute();

        storeStart = findViewById(R.id.tv_startdate);
        storeEnd = findViewById(R.id.tv_enddate);
        storeStart.setText(storeStartDate);
        storeEnd.setText(storeEndDate);

        im1 = findViewById(R.id.img_bckrecetab);

        im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        b1 = findViewById(R.id.btn_myReceiptchart);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent barIntent = new Intent(ReceiptReport.this,ReceiptBarChart.class);
                barIntent.putExtra("City",noofdiffcity);
                barIntent.putExtra("City Sales",AmountToGraph1);
                barIntent.putExtra("Month",nodiffmonths);
                barIntent.putExtra("Month Sales",AmountToGraph2);
                barIntent.putExtra("Cashier",noofdiffcashier);
                barIntent.putExtra("Cashier Sales",AmountToGraph3);
                barIntent.putExtra("Hour",noofdiffhours);
                barIntent.putExtra("Hour Sales",AmountToGraph4);
                startActivity(barIntent);
            }
        });
    }

    @SuppressLint("RtlHardcoded")
    public void doInBackground1() {
        selectStoreNo = dropDownStoreNo;

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

                while(TL4.getChildCount() > 1)
                {
                    TL4.removeView(TL4.getChildAt(TL4.getChildCount() - 1));
                }

                noofdiffcity.clear();
                citiessales.clear();
                nodiffmonths.clear();
                monthsales.clear();
                noofdiffcashier.clear();
                cashiersales.clear();
                noofdiffhours.clear();
                hoursales.clear();

                AmountToGraph1.clear();
                AmountToGraph2.clear();
                AmountToGraph3.clear();
                AmountToGraph4.clear();

                final ProgressDialog progressDialog = new ProgressDialog(ReceiptReport.this);

                new AsyncTask<Void, Void, Void>() {

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
                        doInBackground4();
                        return null;
                    }
                }.execute();

                im2.startAnimation(rotation);
            }
        });

        storeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ReceiptReport.this, DPstartDate, startCalendar.get(Calendar.YEAR),startCalendar.get(Calendar.MONTH),startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        storeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ReceiptReport.this, DPendDate, endCalendar.get(Calendar.YEAR),endCalendar.get(Calendar.MONTH),endCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
                    noofdiffcity.add(rs.getString("City"));
                    citiessales.add(rs.getDouble("NO"));
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
    public void doInBackground2() {
        count = 1;
        
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
            String query2 = "Select DATEPART(MM,th.[Date]) as month, count(th.[Receipt No_]) as NO from ["+CName+"$Store] as store,["+CName+"$Transaction Header] as th where th.[Store No_] = store.[No_] and th.[Store No_] = '"+selectStoreNo+"' and th.[Date] between '"+storeStartDate+"' and '"+storeEndDate+"' group by DATEPART(MM,th.[Date]) order by DATEPART(MM,th.[Date])";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs2 = stmt.executeQuery(query2);

                Log.w("Android","database Connected");

                while (rs2.next()) {
                    nodiffmonths.add(rs2.getString("month"));
                    monthsales.add(Math.abs(rs2.getDouble("NO")));
                    /*if (count % 2 == 0)
                    {
                        TR2.setBackgroundColor(Color.parseColor("#757575"));
                        count++;
                    }
                    else
                    {
                        TR2.setBackgroundColor(Color.parseColor("#9E9E9E"));
                        count++;
                    }*/
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
    public void doInBackground3() {
        count = 1;
        
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
            String query = "Select DATEPART(HH,th.[Time]) as hour, count(th.[Receipt No_]) as NO from ["+CName+"$Store] as store,["+CName+"$Transaction Header] as th where th.[Store No_] = store.[No_] and th.[Store No_] = '"+selectStoreNo+"' and th.[Date] between '"+storeStartDate+"' and '"+storeEndDate+"' group by DATEPART(HH,th.[Time])";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs3 = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs3.next()) {
                    noofdiffhours.add(rs3.getString("hour"));
                    hoursales.add(Math.abs(rs3.getDouble("NO")));
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
    public void doInBackground4() {
        count = 1;
        
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
            String query = "Select staff.[First Name], count(tse.[Receipt No_]) as NoOfRece from [dbo].["+CName+"$Trans_ Sales Entry] as tse, [dbo].["+CName+"$Staff] as staff where staff.[ID] = tse.[Staff ID] and tse.[Store No_] = '"+selectStoreNo+"' and tse.[Date] between '"+storeStartDate+"' and '"+storeEndDate+"' group by staff.[First Name]";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs4 = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs4.next()) {
                    noofdiffcashier.add(rs4.getString("First Name"));
                    cashiersales.add(Math.abs(rs4.getDouble("NoOfRece")));
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
        for (int i = 0 ; i < noofdiffcity.size() ; i++)
        {
            TR = new TableRow(this);

            TV1 = new TextView(this);
            TV2 = new TextView(this);

            TV1.setText(noofdiffcity.get(i));
            TV2.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((citiessales.get(i) / intVal[index]) * 100.0) / 100.0));

            AmountToGraph1.add(Float.parseFloat(String.valueOf(Math.round((citiessales.get(i) / intVal[index]) * 100.0) / 100.0)));

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
        for (int i = 0 ; i < nodiffmonths.size() ; i++)
        {
            TL2.setStretchAllColumns(true);

            TR2 = new TableRow(this);

            TV21 = new TextView(this);
            TV22 = new TextView(this);

            TV21.setText(Months[Integer.parseInt(nodiffmonths.get(i))]);
            TV22.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((monthsales.get(i) / intVal[index]) * 100.0) / 100.0));

            AmountToGraph2.add(Float.parseFloat(String.valueOf(Math.round((monthsales.get(i) / intVal[index]) * 100.0) / 100.0)));

            TV21.setTextSize(16);
            TV21.setPadding(10,0,10,0);
            TV21.setGravity(Gravity.LEFT);
            TV21.setTextColor(Color.WHITE);
            TV21.setBackground(getDrawable(R.drawable.rect_border));

            TV22.setTextSize(16);
            TV22.setPadding(10,0,10,0);
            TV22.setGravity(Gravity.RIGHT);
            TV22.setTextColor(Color.WHITE);
            TV22.setBackground(getDrawable(R.drawable.rect_border));

            TR2.addView(TV21);
            TR2.addView(TV22);

            TL2.addView(TR2);
        }
        for (int i = 0 ; i < noofdiffhours.size() ; i++)
        {
            TR4 = new TableRow(this);

            TV41 = new TextView(this);
            TV42 = new TextView(this);

            TV41.setText(noofdiffhours.get(i));
            TV42.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((hoursales.get(i) / intVal[index]) * 100.0) / 100.0));

            AmountToGraph4.add(Float.parseFloat(String.valueOf(Math.round((hoursales.get(i) / intVal[index]) * 100.0) / 100.0)));

            TV41.setTextSize(16);
            TV41.setPadding(10,0,10,0);
            TV41.setGravity(Gravity.RIGHT);
            TV41.setTextColor(Color.WHITE);
            TV41.setBackground(getDrawable(R.drawable.rect_border));

            TV42.setTextSize(16);
            TV42.setPadding(10,0,10,0);
            TV42.setGravity(Gravity.RIGHT);
            TV42.setTextColor(Color.WHITE);
            TV42.setBackground(getDrawable(R.drawable.rect_border));

            TR4.addView(TV41);
            TR4.addView(TV42);

            TL4.addView(TR4);
        }
        for (int i = 0 ; i < noofdiffcashier.size() ; i++)
        {
            TR3 = new TableRow(this);

            TV31 = new TextView(this);
            TV32 = new TextView(this);

            TV31.setText(noofdiffcashier.get(i));
            TV32.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((cashiersales.get(i) / intVal[index]) * 100.0) / 100.0));

            AmountToGraph3.add(Float.parseFloat(String.valueOf(Math.round((cashiersales.get(i) / intVal[index]) * 100.0) / 100.0)));

            TV31.setTextSize(16);
            TV31.setPadding(10,0,10,0);
            TV31.setGravity(Gravity.LEFT);
            TV31.setTextColor(Color.WHITE);
            TV31.setBackground(getDrawable(R.drawable.rect_border));

            TV32.setTextSize(16);
            TV32.setPadding(10,0,10,0);
            TV32.setGravity(Gravity.RIGHT);
            TV32.setTextColor(Color.WHITE);
            TV32.setBackground(getDrawable(R.drawable.rect_border));

            TR3.addView(TV31);
            TR3.addView(TV32);

            TL3.addView(TR3);
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
        Spinner spinner = (Spinner) adapterView;
        if (spinner.getId() == R.id.spin_filter)
        {
            index = i;

            dropDownValue = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(adapterView.getContext(), "Sorted by: " + dropDownValue, Toast.LENGTH_SHORT).show();

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
            while(TL4.getChildCount() > 1)
            {
                TL4.removeView(TL4.getChildAt(TL4.getChildCount() - 1));
            }

            AmountToGraph1.clear();
            AmountToGraph2.clear();
            AmountToGraph3.clear();
            AmountToGraph4.clear();

            PrintTable();
        }
        else
        if (spinner.getId() == R.id.spin_storeselect)
        {
            dropDownStoreNo = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(adapterView.getContext(), "Selected Store : " + dropDownStoreNo, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}
}

