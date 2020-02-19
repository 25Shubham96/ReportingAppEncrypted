package com.example.shubhammundra.fromandto;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
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

public class StrOvrYrReport extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TableLayout TL;
    TableRow TR;
    TextView TV1, TV2, TV3, TV4;

    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    ImageView refresh;

    String CName;

    ArrayList<String> StoreNm = new ArrayList<>();
    ArrayList<String> UniYear = new ArrayList<>();
    
    String selectStoreNo = "", dropDownStoreNo = "";
    String selectUniYear = "", dropDownYear = "";

    String[] Months = {" ","Jan","Feb","Mar","Apr","May","June","July","Aug","Sep","Oct","Nov","Dec"};

    ArrayList<String> Month123 = new ArrayList<>();
    ArrayList<Double> NetAmt123 = new ArrayList<>();
    ArrayList<Double> Profit123 = new ArrayList<>();
    ArrayList<Double> ProfitPer123 = new ArrayList<>();

    Button btn;

    ImageView back;

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal= {1,1000,100000,1000000,10000000};

    String dropDownValue;

    ArrayList<Float> AmountToGraph1 = new ArrayList<>();
    ArrayList<Float> AmountToGraph2 = new ArrayList<>();
    ArrayList<Float> AmountToGraph3 = new ArrayList<>();

    int index;

    @SuppressLint("StaticFieldLeak")
    public static Spinner spinnerFilter;
    @SuppressLint("StaticFieldLeak")
    public static Spinner spinner;
    @SuppressLint("StaticFieldLeak")
    public static Spinner spinner1;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_over_year_report);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        back = findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        CName = intent.getStringExtra("Company Name");
        StoreNm = intent.getStringArrayListExtra("Store Name");
        UniYear = intent.getStringArrayListExtra("Unique Year");

        TL = findViewById(R.id.tl_myTable);

        spinnerFilter = findViewById(R.id.spin_filter);
        spinnerFilter.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, amtFilter);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapter1);

        spinner = findViewById(R.id.spin_storeselect);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,StoreNm);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner1 = findViewById(R.id.spin_yearselect);
        spinner1.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,UniYear);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter2);

        btn  = findViewById(R.id.btn_StrOvrYrChart);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent StrOvrYrintent = new Intent(StrOvrYrReport.this,StrOvrYrChart.class);
                StrOvrYrintent.putExtra("Months",Month123);
                StrOvrYrintent.putExtra("Net Amount",AmountToGraph1);
                StrOvrYrintent.putExtra("Profit",AmountToGraph2);
                StrOvrYrintent.putExtra("Profit %",AmountToGraph3);
                startActivity(StrOvrYrintent);
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
        selectStoreNo = dropDownStoreNo;
        selectUniYear = dropDownYear;
        
        refresh = findViewById(R.id.iv_refresh);
        
        refresh.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);

                while (TL.getChildCount() > 1) {
                    TL.removeView(TL.getChildAt(TL.getChildCount() - 1));
                }

                Month123.clear();
                NetAmt123.clear();
                Profit123.clear();
                ProfitPer123.clear();

                AmountToGraph1.clear();
                AmountToGraph2.clear();

                final ProgressDialog progressDialog = new ProgressDialog(StrOvrYrReport.this);

                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog.setMessage("Collecting  Data...");
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

                refresh.startAnimation(rotation);
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
            String query = "select datepart(MM,Date) as Month, sum([Net Amount]) as NetAmt, sum([Gross Amount]) as Gamt FROM [dbo].["+CName+"$Transaction Header] where [Store No_] = '"+selectStoreNo+"' and DATEPART(YYYY,Date) = '"+selectUniYear+"' group by datepart(MM,Date)";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    Month123.add(Months[Integer.parseInt(rs.getString("Month"))]);

                    double netAmt = rs.getDouble("NetAmt");
                    netAmt = Math.abs(netAmt);
                    double grsAmt = rs.getDouble("Gamt");
                    grsAmt = Math.abs(grsAmt);
                    double Profit = grsAmt - netAmt;
                    double ProfitPer = 0;

                    if (grsAmt == 0)
                    {
                        ProfitPer = 0;
                    }
                    else
                    {
                        ProfitPer = ((Profit * 100) / grsAmt);
                        ProfitPer = Math.abs(ProfitPer);
                    }

                    NetAmt123.add(netAmt);
                    Profit123.add(Profit);
                    ProfitPer123.add(ProfitPer);
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
        for (int i = 0 ; i < NetAmt123.size() ; i++)
        {
            TL.setStretchAllColumns(true);

            TR = new TableRow(this);

            TV1 = new TextView(this);
            TV2 = new TextView(this);
            TV3 = new TextView(this);
            TV4 = new TextView(this);

            TV1.setText(Month123.get(i));
            TV2.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((NetAmt123.get(i) / intVal[index]) * 100.0) / 100.0 ));
            TV3.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((Profit123.get(i) / intVal[index]) * 100.0) / 100.0 ));
            TV4.setText(String.valueOf(Math.round(ProfitPer123.get(i) * 100.0) / 100.0));

            AmountToGraph1.add(Float.parseFloat(String.valueOf(Math.round((NetAmt123.get(i) / intVal[index]) * 100.0) / 100.0)));
            AmountToGraph2.add(Float.parseFloat(String.valueOf(Math.round((Profit123.get(i) / intVal[index]) * 100.0) / 100.0)));
            AmountToGraph3.add(Float.parseFloat(String.valueOf(Math.round(ProfitPer123.get(i) * 100.0) / 100.0)));

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

            TR.addView(TV1);
            TR.addView(TV2);
            TR.addView(TV3);
            TR.addView(TV4);

            TL.addView(TR);
        }
    }
    
    @SuppressLint("RtlHardcoded")
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Spinner spinner = (Spinner) adapterView;
        if (spinner.getId() == R.id.spin_storeselect)
        {
            dropDownStoreNo = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(adapterView.getContext(), "Selected Store : " + dropDownStoreNo, Toast.LENGTH_SHORT).show();
        }
        else
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
