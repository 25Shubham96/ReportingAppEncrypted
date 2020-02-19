package com.example.shubhammundra.fromandto;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.annotation.Nullable;
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

public class DiscountType extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TableLayout TL, TL2;
    TableRow TR, TR2;
    TextView TV1, TV2, TV3, TV4, TV5, TV6, TV7, TV21, TV22;
    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    TextView storeStart;
    TextView storeEnd;

    ImageView im1,im2;

    String storeStartDate = "2001-01-01";

    String storeEndDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

    public ArrayList<String> diffCity = new ArrayList<>();
    public ArrayList<Double> discAmt = new ArrayList<>();
    public ArrayList<Double> periDisc = new ArrayList<>();
    public ArrayList<Double> lineDisc = new ArrayList<>();
    public ArrayList<Double> CustDisc = new ArrayList<>();
    public ArrayList<Double> InfoCodeDisc = new ArrayList<>();
    public ArrayList<Double> CustInvDisc = new ArrayList<>();

    public ArrayList<String> diffCity2 = new ArrayList<>();
    public ArrayList<Double> DiscPercen = new ArrayList<>();

    Button b1;

    Calendar startCalendar = Calendar.getInstance();
    Calendar endCalendar = Calendar.getInstance();
    
    String CName;

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal= {1,1000,100000,1000000,10000000};

    String dropDownYear;

    ArrayList<Float> AmountToGraph1 = new ArrayList<>();
    ArrayList<Float> AmountToGraph2 = new ArrayList<>();
    ArrayList<Float> AmountToGraph3 = new ArrayList<>();
    ArrayList<Float> AmountToGraph4 = new ArrayList<>();
    ArrayList<Float> AmountToGraph5 = new ArrayList<>();
    ArrayList<Float> AmountToGraph6 = new ArrayList<>();
    ArrayList<Float> AmountToGraph7 = new ArrayList<>();

    GradientDrawable gd;

    int index;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_type);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        CName = intent.getStringExtra("Company Name");

        TL = findViewById(R.id.tl_myTable);
        TL2 = findViewById(R.id.tl_myTable1);

        Spinner spinner = findViewById(R.id.spin_filter);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, amtFilter);
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
                doInBackground1();
                doInBackground2();
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

        b1 = findViewById(R.id.btn_discountTypChart);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent barIntent = new Intent(DiscountType.this,DiscountBarChart.class);
                barIntent.putExtra("City",diffCity);
                barIntent.putExtra("Disc Amt",AmountToGraph1);
                barIntent.putExtra("Periodic Disc",AmountToGraph2);
                barIntent.putExtra("Line Disc",AmountToGraph3);
                barIntent.putExtra("Customer Disc",AmountToGraph4);
                barIntent.putExtra("Info Code Disc",AmountToGraph5);
                barIntent.putExtra("Cust Invoice Disc",AmountToGraph6);

                barIntent.putExtra("Diff City",diffCity2);
                barIntent.putExtra("Discount per",AmountToGraph7);
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

        im2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {

                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
                im2.startAnimation(rotation);

                while(TL.getChildCount() > 1)
                {
                    TL.removeView(TL.getChildAt(TL.getChildCount() - 1));
                }

                while(TL2.getChildCount() > 1)
                {
                    TL2.removeView(TL2.getChildAt(TL2.getChildCount() - 1));
                }

                diffCity.clear();                discAmt.clear();                periDisc.clear();                lineDisc.clear();                CustDisc.clear();                discAmt.clear();                CustInvDisc.clear();                diffCity2.clear();                DiscPercen.clear();
                AmountToGraph1.clear();                AmountToGraph2.clear();                AmountToGraph3.clear();                AmountToGraph4.clear();                AmountToGraph5.clear();                AmountToGraph6.clear();                AmountToGraph7.clear();

                final ProgressDialog progressDialog = new ProgressDialog(DiscountType.this);

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
                        return null;
                    }
                }.execute();
            }
        });

        storeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(DiscountType.this, DPstartDate, startCalendar.get(Calendar.YEAR),startCalendar.get(Calendar.MONTH),startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        storeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(DiscountType.this, DPendDate, endCalendar.get(Calendar.YEAR),endCalendar.get(Calendar.MONTH),endCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
            String query = "Select City, sum([Discount Amount])as DiscountAmount, sum([Periodic Discount])as PeriodicDiscount, sum([Line Discount]) as LineDiscount, sum([Customer Discount])as CustomerDiscount, sum([Infocode Discount]) as InfocodeDiscount, sum([Cust_ Invoice Discount])as Cust_InvoiceDiscount from [dbo].["+CName+"$Trans_ Sales Entry] as TSE,[dbo].["+CName+"$Store] as store  where TSE.[Store No_] = store.No_ and Date between '"+storeStartDate+"' and '"+storeEndDate+"' group by City";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {
                    diffCity.add(rs.getString("City"));
                    discAmt.add(Math.abs(rs.getDouble("DiscountAmount")));
                    periDisc.add(Math.abs(rs.getDouble("PeriodicDiscount")));
                    lineDisc.add(Math.abs(rs.getDouble("LineDiscount")));
                    CustDisc.add(Math.abs(rs.getDouble("CustomerDiscount")));
                    InfoCodeDisc.add(Math.abs(rs.getDouble("InfocodeDiscount")));
                    CustInvDisc.add(Math.abs(rs.getDouble("Cust_InvoiceDiscount")));
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("RtlHardcoded")
    public void doInBackground2() {
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
            String query1 = "Select City,sum([Price]) as price, sum([Net Price]) as np from [dbo].["+CName+"$Trans_ Sales Entry] as TSE,[dbo].["+CName+"$Store] as store where TSE.[Store No_] = store.No_ and Date between '"+storeStartDate+"' and '"+storeEndDate+"' group by City";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs1 = stmt.executeQuery(query1);

                Log.w("Android","database Connected");

                while (rs1.next()) {
                    diffCity2.add(rs1.getString("City"));
                    double netAmt = Math.abs(rs1.getDouble("price"));
                    double netAmt1 = Math.abs(rs1.getDouble("np"));
                    double dis = 0;
                    if (netAmt != 0) {
                        dis = ((netAmt - netAmt1) * 100) / netAmt;
                    }
                    DiscPercen.add(dis);
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
        for (int i = 0 ; i < diffCity.size() ; i++)
        {
            TR = new TableRow(this);

            TV1 = new TextView(this);
            TV2 = new TextView(this);
            TV3 = new TextView(this);
            TV4 = new TextView(this);
            TV5 = new TextView(this);
            TV6 = new TextView(this);
            TV7 = new TextView(this);

            TV1.setText(diffCity.get(i));

            double val11 = discAmt.get(i) / Double.parseDouble(String.valueOf(intVal[index]));
            val11 = Math.round(val11 * 100.0) / 100.0;
            TV2.setText(NumberFormat.getNumberInstance(Locale.US).format(val11));

            double val12 = periDisc.get(i) / Double.parseDouble(String.valueOf(intVal[index]));
            val12 = Math.round(val12 * 100.0) / 100.0;
            TV3.setText(NumberFormat.getNumberInstance(Locale.US).format(val12));

            double val13 = lineDisc.get(i) / Double.parseDouble(String.valueOf(intVal[index]));
            val13 = Math.round(val13 * 100.0) / 100.0;
            TV4.setText(NumberFormat.getNumberInstance(Locale.US).format(val13));

            double val14 = CustDisc.get(i) / Double.parseDouble(String.valueOf(intVal[index]));
            val14 = Math.round(val14 * 100.0) / 100.0;
            TV5.setText(NumberFormat.getNumberInstance(Locale.US).format(val14));

            double val15 = InfoCodeDisc.get(i) / Double.parseDouble(String.valueOf(intVal[index]));
            val15 = Math.round(val15 * 100.0) / 100.0;
            TV6.setText(NumberFormat.getNumberInstance(Locale.US).format(val15));

            double val16 = CustInvDisc.get(i) / Double.parseDouble(String.valueOf(intVal[index]));
            val16 = Math.round(val16 * 100.0) / 100.0;
            TV7.setText(NumberFormat.getNumberInstance(Locale.US).format(val16));

            AmountToGraph1.add(Float.parseFloat(String.valueOf(val11)));
            AmountToGraph2.add(Float.parseFloat(String.valueOf(val12)));
            AmountToGraph3.add(Float.parseFloat(String.valueOf(val13)));
            AmountToGraph4.add(Float.parseFloat(String.valueOf(val14)));
            AmountToGraph5.add(Float.parseFloat(String.valueOf(val15)));
            AmountToGraph6.add(Float.parseFloat(String.valueOf(val16)));

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

            TV3.setTextSize(16);
            TV3.setPadding(10,0,10,0);
            TV3.setGravity(Gravity.RIGHT);
            TV3.setTextColor(Color.WHITE);
            TV3.setBackground(getDrawable(R.drawable.rect_border));

            TV4.setTextSize(16);
            TV4.setPadding(10,0,10,0);
            TV4.setGravity(Gravity.RIGHT);
            TV4.setTextColor(Color.WHITE);
            TV4.setBackground(getDrawable(R.drawable.rect_border));

            TV5.setTextSize(16);
            TV5.setPadding(10,0,10,0);
            TV5.setGravity(Gravity.RIGHT);
            TV5.setTextColor(Color.WHITE);
            TV5.setBackground(getDrawable(R.drawable.rect_border));

            TV6.setTextSize(16);
            TV6.setPadding(10,0,10,0);
            TV6.setGravity(Gravity.RIGHT);
            TV6.setTextColor(Color.WHITE);
            TV6.setBackground(getDrawable(R.drawable.rect_border));

            TV7.setTextSize(16);
            TV7.setPadding(10,0,10,0);
            TV7.setGravity(Gravity.RIGHT);
            TV7.setTextColor(Color.WHITE);
            TV7.setBackground(getDrawable(R.drawable.rect_border));

            TR.addView(TV1);
            TR.addView(TV2);
            TR.addView(TV3);
            TR.addView(TV4);
            TR.addView(TV5);
            TR.addView(TV6);
            TR.addView(TV7);

            TL.addView(TR);
        }

        for (int i = 0 ; i < diffCity2.size() ; i++)
        {
            TL2.setColumnStretchable(0, true);
            TL2.setColumnStretchable(1, true);

            TR2 = new TableRow(this);

            TV21 = new TextView(this);
            TV22 = new TextView(this);

            TV21.setText(diffCity2.get(i));
            double val7 = DiscPercen.get(i);
            val7 = Math.round(val7 * 100.0) / 100.0;
            TV22.setText(String.valueOf(val7));

            AmountToGraph7.add(Float.parseFloat(String.valueOf(val7)));

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

        AmountToGraph1.clear();
        AmountToGraph2.clear();
        AmountToGraph3.clear();
        AmountToGraph4.clear();
        AmountToGraph5.clear();
        AmountToGraph6.clear();
        AmountToGraph7.clear();

        PrintTable();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}
}
