package com.example.shubhammundra.fromandto;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Receiveable extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TableLayout TL;
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

    public ArrayList<Float> Amount = new ArrayList<>();
    public ArrayList<String> Year = new ArrayList<>();

    public float a = 0;
    public String c = "", d = "";

    Button b1;

    Calendar startCalendar = Calendar.getInstance();
    Calendar endCalendar = Calendar.getInstance();

    String CName;

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal= {1,1000,100000,1000000,10000000};

    String dropDownYear;

    ArrayList<String> StoreNo123 = new ArrayList<>();
    ArrayList<Long> Amount123 = new ArrayList<>();

    ArrayList<Float> AmountToGraph = new ArrayList<>();

    GradientDrawable gd;

    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiveable);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Spinner spinner = findViewById(R.id.spin_filter);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, amtFilter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Intent intent = getIntent();
        CName = intent.getStringExtra("Company Name");

        doInBackground();

        im1 = findViewById(R.id.img1);

        im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        b1 = findViewById(R.id.btn_receivableChart);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent barIntent = new Intent(Receiveable.this,ReceivableChart.class);
                barIntent.putExtra("Year",Year);
                barIntent.putExtra("Amount",AmountToGraph);
                startActivity(barIntent);
            }
        });
    }

    @SuppressLint("RtlHardcoded")
    public void doInBackground()
    {
        TL = findViewById(R.id.tl_myTable);
        TR = findViewById(R.id.tr_index);
        TV1 = findViewById(R.id.tv_no);
        TV2 = findViewById(R.id.tv_name);

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
            @Override
            public void onClick(View view) {

                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);

                while(TL.getChildCount() > 1)
                {
                    TL.removeView(TL.getChildAt(TL.getChildCount() - 1));
                }
                Year.clear();
                Amount.clear();

                doInBackground();

                im2.startAnimation(rotation);
            }
        });

        storeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Receiveable.this, DPstartDate, startCalendar.get(Calendar.YEAR),startCalendar.get(Calendar.MONTH),startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        storeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Receiveable.this, DPendDate, endCalendar.get(Calendar.YEAR),endCalendar.get(Calendar.MONTH),endCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        storeStart.setText(storeStartDate);
        storeEnd.setText(storeEndDate);

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
            String query = "SELECT DATEPART(YYYY,[Posting Date]) as Year, abs(sum(Amount)) as Amount FROM [dbo].["+CName+"$Detailed Cust_ Ledg_ Entry] where [Entry Type] = '1' and [Document Type] in ('2','3') and [Posting Date] between '"+storeStartDate+"' and '"+storeEndDate+"' group by DATEPART(YYYY,[Posting Date])";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {
                    TL.setColumnStretchable(0, true);
                    TL.setColumnStretchable(1, true);

                    TR = new TableRow(this);

                    gd = new GradientDrawable();
                    gd.setStroke(2, 0xFFFFFFFF);

                    TV1 = new TextView(this);
                    TV2 = new TextView(this);

                    Double netAmt = rs.getDouble("Amount");
                    long roundAmt = netAmt.intValue();
                    roundAmt = Math.abs(roundAmt);

                    TV1.setText(rs.getString("Year"));
                    StoreNo123.add(TV1.getText().toString());

                    Amount123.add(roundAmt);
                    long val = roundAmt / intVal[index];
                    TV2.setText(String.valueOf(val));

                    TV1.setTextSize(16);
                    TV1.setPadding(10,0,10,0);
                    TV1.setGravity(Gravity.LEFT);
                    TV1.setTextColor(Color.WHITE);
                    TV1.setBackground(gd);

                    TV2.setTextSize(16);
                    TV2.setPadding(10,0,10,0);
                    TV2.setGravity(Gravity.RIGHT);
                    TV2.setTextColor(Color.WHITE);
                    TV2.setBackground(gd);

                    TR.addView(TV1);
                    TR.addView(TV2);

                    TL.addView(TR);

                    c = TV2.getText().toString();
                    a = Float.parseFloat(c);
                    d = TV1.getText().toString();

                    Year.add(d);
                    Amount.add(a);
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

        AmountToGraph.clear();

        for (int j = 0 ; j < Amount123.size() ; j++)
        {
            TR = new TableRow(this);

            TV1 = new TextView(this);
            TV2 = new TextView(this);

            TV1.setText(String.valueOf(StoreNo123.get(j)));
            TV2.setText(String.valueOf(Amount123.get(j) / intVal[i]));

            AmountToGraph.add(Float.parseFloat(TV2.getText().toString()));

            TV1.setTextSize(15);
            TV1.setPadding(10,0,10,0);
            TV1.setGravity(Gravity.LEFT);
            TV1.setTextColor(Color.WHITE);
            TV1.setBackground(gd);

            TV2.setTextSize(15);
            TV2.setPadding(10,0,10,0);
            TV2.setGravity(Gravity.RIGHT);
            TV2.setTextColor(Color.WHITE);
            TV2.setBackground(gd);

            TR.addView(TV1);
            TR.addView(TV2);

            TL.addView(TR);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
