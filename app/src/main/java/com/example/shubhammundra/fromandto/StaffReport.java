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

public class StaffReport extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TableLayout TL,TL1;
    TableRow TR,TR1;
    TextView TV1, TV2, TV3, TV5, TV11, TV12, TV13, TV15;
    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    TextView storeStart;
    TextView storeEnd;

    ImageView refresh;

    ImageView back;

    String storeStartDate = "2001-01-01";

    String storeEndDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

    String selectStoreNo = "";

    String dropDownStoreNo;

    public float a = 0,b = 0,c = 0;
    public String a1 = "",b1 = "",c1 = "";

    Button btnChart;

    Calendar startCalendar = Calendar.getInstance();
    Calendar endCalendar = Calendar.getInstance();

    String CName;

    int count1 = 1;

    ArrayList<String> Str,Year;

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal= {1,1000,100000,1000000,10000000};

    String dropDownValue;

    ArrayList<String> CashierName = new ArrayList<>();
    ArrayList<Double> CashierNetAmt = new ArrayList<>();
    ArrayList<Double> CashierRece = new ArrayList<>();
    ArrayList<Double> CashierDiscount = new ArrayList<>();

    ArrayList<Float> AmountToGraph = new ArrayList<>();
    ArrayList<Float> AmountToGraph2 = new ArrayList<>();

    ArrayList<String> ManagerName = new ArrayList<>();
    ArrayList<Double> ManagerNetAmt = new ArrayList<>();
    ArrayList<Double> ManagerRece = new ArrayList<>();
    ArrayList<Double> ManagerDiscount = new ArrayList<>();

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_report_table);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TL = findViewById(R.id.tl_myTableCashi);
        TL1 = findViewById(R.id.tl_myTableMana);

        Intent intent = getIntent();
        CName = intent.getStringExtra("Company Name");
        Year = intent.getStringArrayListExtra("Unique Year");
        Str = intent.getStringArrayListExtra("Store Name");

        spinnerFilter = findViewById(R.id.spin_filter);
        spinnerFilter.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, amtFilter);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapter1);

        spinner = findViewById(R.id.spin_storeselect);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,Str);
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
                return null;
            }
        }.execute();

        storeStart = findViewById(R.id.tv_startdate);
        storeEnd = findViewById(R.id.tv_enddate);
        storeStart.setText(storeStartDate);
        storeEnd.setText(storeEndDate);

        back = findViewById(R.id.img1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnChart = findViewById(R.id.btn_staffchart);

        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chartIntent = new Intent(StaffReport.this,StaffBarchart.class);
                chartIntent.putExtra("Cashier Name",CashierName);
                chartIntent.putExtra("Net Amount",AmountToGraph);
                chartIntent.putExtra("Discount Percen",AmountToGraph2);
                chartIntent.putExtra("Mani Name",ManagerName);
                chartIntent.putExtra("Mani Net Amount",AmountToGraph3);
                chartIntent.putExtra("Avg Net Amt",AmountToGraph4);
                startActivity(chartIntent);
            }
        });
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

            while(TL1.getChildCount() > 1)
            {
                TL1.removeView(TL1.getChildAt(TL1.getChildCount() - 1));
            }
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

    @SuppressLint("RtlHardcoded")
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

                while (TL.getChildCount() > 1) {
                    TL.removeView(TL.getChildAt(TL.getChildCount() - 1));
                }

                while (TL1.getChildCount() > 1) {
                    TL1.removeView(TL1.getChildAt(TL1.getChildCount() - 1));
                }

                CashierName.clear();
                CashierNetAmt.clear();
                CashierRece.clear();
                CashierDiscount.clear();

                ManagerName.clear();
                ManagerNetAmt.clear();
                ManagerRece.clear();
                ManagerDiscount.clear();

                AmountToGraph.clear();
                AmountToGraph2.clear();
                AmountToGraph3.clear();
                AmountToGraph4.clear();

                final ProgressDialog progressDialog = new ProgressDialog(StaffReport.this);

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
                        doInBackground2();
                        return null;
                    }
                }.execute();

                refresh.startAnimation(rotation);
            }
        });

        storeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(StaffReport.this, DPstartDate, startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        storeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(StaffReport.this, DPendDate, endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
            String query = "Select a.[First Name], sum([Net Amount]) as NetAmount, count(b.[Receipt No_]) as NoRec, sum([Discount Amount]) as Discount from  [dbo].["+CName+"$Transaction Header] as b, [dbo].["+CName+"$Staff] as a where b.[Staff ID] = a.[ID] and b.[Date] between '"+storeStartDate+"' and '"+storeEndDate+"' and b.[Store No_] = '"+selectStoreNo+"' and a.[Permission Group] = 'CASHIER' group by a.[First Name]";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                Log.w("Android", "database Connected");
                while (rs.next()) {
                    CashierName.add(rs.getString("First Name"));
                    CashierNetAmt.add(rs.getDouble("NetAmount"));
                    CashierRece.add(rs.getDouble("NoRec"));
                    CashierDiscount.add(rs.getDouble("Discount"));
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
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
    public void doInBackground2() {
        count1 = 1;

        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query2 = "Select a.[First Name], sum([Net Amount]) as NetAmount, count(b.[Receipt No_]) as NoRec, sum([Discount Amount]) as Discount from [dbo].["+CName+"$Transaction Header] as b, [dbo].["+CName+"$Staff] as a where b.[Staff ID] = a.[ID] and b.[Date] between '"+storeStartDate+"' and '"+storeEndDate+"' and b.[Store No_] = '"+selectStoreNo+"' and a.[Permission Group] = 'MANAGER' group by a.[First Name]";

            try {
                Statement stmt = connect.createStatement();
                ResultSet rs2 = stmt.executeQuery(query2);

                Log.w("Android", "database Connected");

                while(rs2.next())
                {
                    ManagerName.add(rs2.getString("First Name"));
                    ManagerNetAmt.add(rs2.getDouble("NetAmount"));
                    ManagerRece.add(rs2.getDouble("NoRec"));
                    ManagerDiscount.add(rs2.getDouble("Discount"));
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
        for (int i = 0 ; i < CashierName.size() ; i++)
        {
            TL.setStretchAllColumns(true);

            TR = new TableRow(this);

            TV1 = new TextView(this);
            TV2 = new TextView(this);
            TV3 = new TextView(this);
            TV5 = new TextView(this);

            TV1.setText(CashierName.get(i));

            double val01 = Math.abs(CashierNetAmt.get(i)) / Double.parseDouble(String.valueOf(intVal[index]));
            val01 = Math.round(val01 * 100.0) / 100.0;

            TV2.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(val01)));

            AmountToGraph.add(Float.parseFloat(String.valueOf(Math.abs(CashierNetAmt.get(i)) / intVal[index])));

            double val02 = (Math.abs(CashierNetAmt.get(i)) / Math.abs(CashierRece.get(i))) / Double.parseDouble(String.valueOf(intVal[index]));
            val02 = Math.round(val02 * 100.0) / 100.0;

            if (TV2.getText() != "0") {
                TV3.setText(String.valueOf(NumberFormat.getInstance(Locale.US).format(val02)));
//                AmountToGraph4.add(Float.valueOf(TV3.getText().toString()));
            }
            else {
                TV3.setText("0");
//                AmountToGraph4.add(Float.parseFloat("0"));
            }

            double val03 = (Math.abs(CashierDiscount.get(i)) * 100) / Math.abs(CashierNetAmt.get(i));
            val03 = Math.round(val03 * 100.0) / 100.0;

            if (TV2.getText() != "0") {
                TV5.setText(String.valueOf(val03));
            }
            else {
                TV5.setText("0");
            }

            AmountToGraph2.add(Float.valueOf(String.valueOf(val03)));

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

            TV5.setTextSize(15);
            TV5.setPadding(10, 0, 10, 0);
            TV5.setGravity(Gravity.RIGHT);
            TV5.setTextColor(Color.WHITE);
            TV5.setBackground(getDrawable(R.drawable.rect_border));

            TR.addView(TV1);
            TR.addView(TV2);
            TR.addView(TV3);
            TR.addView(TV5);

            TL.addView(TR);
        }

        for (int k = 0 ; k < ManagerName.size() ; k++)
        {
            TL1.setStretchAllColumns(true);

            TR1 = new TableRow(this);

            TV11 = new TextView(this);
            TV12 = new TextView(this);
            TV13 = new TextView(this);
            TV15 = new TextView(this);

            TV11.setText(ManagerName.get(k));

            double val11 = Math.abs(ManagerNetAmt.get(k)) / Double.parseDouble(String.valueOf(intVal[index]));
            val11 = Math.round(val11 * 100.0) / 100.0;

            TV12.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(val11)));

            AmountToGraph3.add(Float.parseFloat(String.valueOf(val11)));

            double val12 = (Math.abs(ManagerNetAmt.get(k)) / Math.abs(ManagerRece.get(k))) / Double.parseDouble(String.valueOf(intVal[index]));
            val12 = Math.round(val12 * 100.0) / 100.0;

            if (TV12.getText() != "0") {
                TV13.setText(String.valueOf(NumberFormat.getInstance(Locale.US).format(val12)));
                AmountToGraph4.add(Float.valueOf(String.valueOf(val12)));
            }
            else {
                TV13.setText("0");
                AmountToGraph4.add(Float.parseFloat("0"));
            }

            double val13 = (Math.abs(ManagerDiscount.get(k)) * 100) / Math.abs(ManagerNetAmt.get(k));
            val13 = Math.round(val13 * 100.0) / 100.0;

            if (TV12.getText() != "0") {
                TV15.setText(String.valueOf(val13));
            }
            else {
                TV15.setText("0");
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

            TV15.setTextSize(15);
            TV15.setPadding(10, 0, 10, 0);
            TV15.setGravity(Gravity.RIGHT);
            TV15.setTextColor(Color.WHITE);
            TV15.setBackground(getDrawable(R.drawable.rect_border));

            TR1.addView(TV11);
            TR1.addView(TV12);
            TR1.addView(TV13);
            TR1.addView(TV15);

            TL1.addView(TR1);
        }
    }
}
