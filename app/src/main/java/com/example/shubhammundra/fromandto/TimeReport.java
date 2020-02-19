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
import android.support.annotation.Nullable;
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

import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

public class TimeReport extends AppCompatActivity implements OnItemSelectedListener {

    TableLayout TL;
    TableRow TR;
    TextView TV1, TV2, TV5;

    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    TextView storeStart;
    TextView storeEnd;

    ImageView refresh;

    String storeStartDate = "2001-01-01";

    String storeEndDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

    String selectStoreNo = "S0001";

    String dropDownStoreNo;

    String Values = "''" ;

    Button btnChart;

    Calendar startCalendar = Calendar.getInstance();
    Calendar endCalendar = Calendar.getInstance();

    String Cities = "' '", CName;

    ImageView back;

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal= {1,1000,100000,1000000,10000000};

    String dropDownValue;

    ArrayList<String> TimeInHr = new ArrayList<>();
    ArrayList<Double> NetAmtOfTime = new ArrayList<>();
    ArrayList<Double> AvgNetAmtofTime = new ArrayList<>();

    ArrayList<Float> AmountToGraph = new ArrayList<>();
    ArrayList<Float> AmountToGraph2 = new ArrayList<>();

    GradientDrawable gd;

    int index;

    @SuppressLint("StaticFieldLeak")
    public static Spinner spinnerFilter;

    @SuppressLint("StaticFieldLeak")
    public static SearchableSpinner spinner;

    ArrayList<String> Year, Str;
    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_report_table);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TL = findViewById(R.id.tl_myTable);

        Intent intent = getIntent();
        CName = intent.getStringExtra("Company Name");
        Year = intent.getStringArrayListExtra("Unique Year");
        Str = intent.getStringArrayListExtra("Store Name");

        back = findViewById(R.id.img1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        spinnerFilter = findViewById(R.id.spin_filter);
        spinnerFilter.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, amtFilter);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapter1);

        spinner = findViewById(R.id.spin_storeselect);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new  ArrayAdapter(this, android.R.layout.simple_spinner_item, Str);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setTitle("Select Store");

        btnChart = findViewById(R.id.btn_timewisechart);

        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chartIntent = new Intent(TimeReport.this,DisplayChart.class);
                chartIntent.putExtra("Net Amount",AmountToGraph);
                chartIntent.putExtra("Avg Net Amount",AmountToGraph2);
                chartIntent.putExtra("Hour",TimeInHr);
                chartIntent.putExtra("Store No.",selectStoreNo);
                startActivity(chartIntent);
            }
        });

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
                return null;
            }
        }.execute();

        storeStart = findViewById(R.id.tv_startdate);
        storeEnd = findViewById(R.id.tv_enddate);
        storeStart.setText(storeStartDate);
        storeEnd.setText(storeEndDate);
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

            AmountToGraph.clear();
            AmountToGraph2.clear();

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


    private void doInBackground1() {
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

        refresh = findViewById(R.id.iv_refresh);
        storeStart = findViewById(R.id.tv_startdate);
        storeEnd = findViewById(R.id.tv_enddate);

        refresh.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
                refresh.startAnimation(rotation);

                while (TL.getChildCount() > 1) {
                    TL.removeView(TL.getChildAt(TL.getChildCount() - 1));
                }

                TimeInHr.clear();
                NetAmtOfTime.clear();
                AvgNetAmtofTime.clear();

                AmountToGraph.clear();
                AmountToGraph2.clear();

                final ProgressDialog progressDialog = new ProgressDialog(TimeReport.this);

                new AsyncTask<Void, Void, Void>(){

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
                        return null;
                    }
                }.execute();


            }
        });

        storeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(TimeReport.this, DPstartDate, startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        storeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(TimeReport.this, DPendDate, endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
            String query = "Select datepart(hh,[Time]) as POS, sum([Net Amount]) as sumNetAmt, count([Receipt No_]) as noOfReceipt from ["+CName+"$Transaction Header] where [Store No_] = '"+selectStoreNo+"' and [Date] between '" + storeStartDate + "' and '" + storeEndDate + "' group by datepart(hh,[Time])";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    int val = rs.getInt("POS");
                    TimeInHr.add(val + ":00" + "-" + String.valueOf(val + 1) + ":00");
                    NetAmtOfTime.add(Math.abs(rs.getDouble("sumNetAmt")));
                    AvgNetAmtofTime.add(Math.abs(rs.getDouble("sumNetAmt") / rs.getDouble("noOfReceipt")));

                    /*if (count1 % 2 == 0)
                    {
                        TR.setBackgroundColor(Color.parseColor("#757575"));
                        count1++;
                    }
                    else
                    {
                        TR.setBackgroundColor(Color.parseColor("#9E9E9E"));
                        count1++;
                    }*/
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
    public void PrintTable() {
        for (int i = 0 ; i < TimeInHr.size() ; i++)
        {
            TL.setStretchAllColumns(true);

            TR = new TableRow(this);

            TV1 = new TextView(this);
            TV2 = new TextView(this);
            TV5 = new TextView(this);

            TV1.setText(String.valueOf(TimeInHr.get(i)));
            double val1 = NetAmtOfTime.get(i) / Double.parseDouble(String.valueOf(intVal[index]));
            val1 = Math.round(val1 * 100.0) / 100.0;

            TV2.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(val1)));

            double val2 = AvgNetAmtofTime.get(i) / Double.parseDouble(String.valueOf(intVal[index]));
            val2 = Math.round(val2 * 100.0) / 100.0;

            TV5.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(val2)));

            AmountToGraph.add(Float.parseFloat(String.valueOf(val1)));
            AmountToGraph2.add(Float.parseFloat(String.valueOf(val2)));

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

            TV5.setTextSize(15);
            TV5.setPadding(10, 0, 10, 0);
            TV5.setGravity(Gravity.RIGHT);
            TV5.setTextColor(Color.WHITE);
            TV5.setBackground(getDrawable(R.drawable.rect_border));

            TR.addView(TV1);
            TR.addView(TV2);
            TR.addView(TV5);

            TL.addView(TR);
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
}
