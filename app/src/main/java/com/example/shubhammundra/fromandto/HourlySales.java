package com.example.shubhammundra.fromandto;

import android.annotation.SuppressLint;
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

public class HourlySales extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TableLayout TL;
    TableRow TR;
    TextView TV1, TV2, TV3;

    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    ImageView back;
    ImageView Refresh;

    String dropDownYear = "";

    public ArrayList<String> Hour = new ArrayList<>();
    public ArrayList<Double> NetAmt = new ArrayList<>();
    public ArrayList<Double> AvgNetAmt = new ArrayList<>();

    ArrayList<String> Year;

    String CName;

    String SelectYear = "";

    Button HourChart;

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal = {1,1000,100000,1000000,10000000};

    String dropDownValue;

    ArrayList<Float> AmountToGraph1 = new ArrayList<>();
    ArrayList<Float> AmountToGraph2 = new ArrayList<>();

    int index;

    @SuppressLint("StaticFieldLeak")
    public static Spinner spinnerFilter;
    @SuppressLint("StaticFieldLeak")
    public static Spinner spinner;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_sales);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        CName = intent.getStringExtra("Company Name");
        Year = intent.getStringArrayListExtra("Unique Year");

        TL = findViewById(R.id.tl_myTable);

        back = findViewById(R.id.img_back);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Year);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        HourChart = findViewById(R.id.btn_hourchart);

        HourChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HourlySales.this,HourlySalesChart.class);
                intent.putExtra("Time",Hour);
                intent.putExtra("Net Amt",AmountToGraph1);
                intent.putExtra("Avg Net Amt",AmountToGraph2);
                startActivity(intent);
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
                return null;
            }
        }.execute();
    }

    @SuppressLint("RtlHardcoded")
    private void doInBackground1() {
        SelectYear = dropDownYear;

        Refresh = findViewById(R.id.img3);
        Refresh.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
                Refresh.startAnimation(rotation);

                while (TL.getChildCount() > 1) {
                    TL.removeView(TL.getChildAt(TL.getChildCount() - 1));
                }

                NetAmt.clear();
                AvgNetAmt.clear();
                Hour.clear();

                AmountToGraph1.clear();
                AmountToGraph2.clear();

                final ProgressDialog progressDialog = new ProgressDialog(HourlySales.this);
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

        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query = "select sum([Net Amount]) as netAmt,count([Receipt No_]) as NoOfRece,DATEPART(hh,Time) as Hrs, (DATEPART(MINUTE,Time) / 30) as Mins from [dbo].["+CName+"$Transaction Header] where DATEPART(YYYY,Date) = '"+SelectYear+"' group by DATEPART(hh,Time),(DATEPART(MINUTE,Time) / 30) order by DATEPART(hh,Time)";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {

                    String hour = rs.getString("Hrs");
                    int mint = rs.getInt("Mins");
                    if (mint == 1) { hour = hour + ":30" + " - " + String.valueOf(Integer.parseInt(hour) + 1) + ":00"; }
                    else { hour = hour + ":00" + " - " + hour + ":30"; }
                    Hour.add(hour);

                    double netAmt = Math.abs(rs.getDouble("NetAmt"));
                    NetAmt.add(netAmt);

                    double noOfRece = Math.abs(rs.getDouble("NoOfRece"));
                    double avgNetAmt = 0 ;
                    if (noOfRece != 0) { avgNetAmt = netAmt / noOfRece; }
                    AvgNetAmt.add(avgNetAmt);
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
        for (int i = 0 ; i < Hour.size(); i++ )
        {
            TL.setStretchAllColumns(true);

            TR = new TableRow(this);

            TV1 = new TextView(this);
            TV2 = new TextView(this);
            TV3 = new TextView(this);

            TV1.setText(Hour.get(i));
            double val = NetAmt.get(i) / Double.parseDouble(String.valueOf(intVal[index]));
            val = Math.round(val * 100.0) / 100.0;
            TV2.setText(NumberFormat.getNumberInstance(Locale.US).format(val));
            double val2 = AvgNetAmt.get(i) / Double.parseDouble(String.valueOf(intVal[index]));
            val2 = Math.round(val2 * 100.0) / 100.0;
            TV3.setText(NumberFormat.getNumberInstance(Locale.US).format(val2));

            AmountToGraph1.add(Float.parseFloat(String.valueOf(val)));
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

            TV3.setTextSize(15);
            TV3.setPadding(10, 0, 10, 0);
            TV3.setGravity(Gravity.RIGHT);
            TV3.setTextColor(Color.WHITE);
            TV3.setBackground(getDrawable(R.drawable.rect_border));

            TR.addView(TV1);
            TR.addView(TV2);
            TR.addView(TV3);

            TL.addView(TR);
        }
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Spinner spinner = (Spinner) adapterView;
        if (spinner.getId() == R.id.spin_yearselect)
        {
            dropDownYear = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(adapterView.getContext(), "Selected Year : " + dropDownYear, Toast.LENGTH_SHORT).show();
        }
        else
            if (spinner.getId() == R.id.spin_filter)
            {
                index = i;

                dropDownValue = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(), "Sorted by: " + dropDownValue, Toast.LENGTH_SHORT).show();

                while(TL.getChildCount() > 1)
                {
                    TL.removeView(TL.getChildAt(TL.getChildCount() - 1));
                }

                AmountToGraph1.clear();
                AmountToGraph2.clear();

                PrintTable();
            }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}
}
