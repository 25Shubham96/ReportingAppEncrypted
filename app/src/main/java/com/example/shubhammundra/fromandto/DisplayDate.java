package com.example.shubhammundra.fromandto;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
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

public class DisplayDate extends MainActivity implements AdapterView.OnItemSelectedListener {

    TableLayout TL;
    TableRow TR;
    TextView TV1, TV2, TV3, TV4, TV5;
    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    TextView storeStart;
    TextView storeEnd;

    ImageView refresh;

    ImageView back;

    String storeStartDate = "2001-01-01";

    String storeEndDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

    Button pieChart1,pieChart21,storeLineChart;

    Calendar startCalendar = Calendar.getInstance();
    Calendar endCalendar = Calendar.getInstance();

    String CName;

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal= {1,1000,100000,1000000,10000000};

    String dropDownYear;

    ArrayList<String> StoreNo123 = new ArrayList<>();
    ArrayList<String> StoreName123 = new ArrayList<>();
    ArrayList<Double> Amount123 = new ArrayList<>();
    ArrayList<Double> NoOfTrans = new ArrayList<>();
    ArrayList<Double> NoOfItemsSold = new ArrayList<>();

    ArrayList<Float> NoOfTXtoGraph = new ArrayList<>();
    ArrayList<Float> AmountToGraph = new ArrayList<>();

    int index;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_date);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TL = findViewById(R.id.tl_myTable);

        Spinner spinner = findViewById(R.id.spin_filter);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, amtFilter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        back = findViewById(R.id.img10);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        CName = intent.getStringExtra("Company Name");

        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                if (Looper.myLooper() == null)
                {
                    Looper.prepare();
                }

                doInBackground1();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                PrintTable();
            }
        }.execute();

        storeStart = findViewById(R.id.tv_startdate);
        storeEnd = findViewById(R.id.tv_enddate);
        storeStart.setText(storeStartDate);
        storeEnd.setText(storeEndDate);

        pieChart1 = findViewById(R.id.btn_piechart);
        pieChart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pieIntent = new Intent(DisplayDate.this,DisplayPiechart.class);
                pieIntent.putExtra("X Values",StoreNo123);
                pieIntent.putExtra("Y Values",AmountToGraph);
                pieIntent.putExtra("No of TX", NoOfTXtoGraph);
                startActivity(pieIntent);
            }
        });

        pieChart21 = findViewById(R.id.btn_piechart2);
        pieChart21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent storeIntent = new Intent(DisplayDate.this,StorePieChart.class);
                storeIntent.putExtra("Store No",StoreNo123);
                storeIntent.putExtra("Store Amount",AmountToGraph);
                storeIntent.putExtra("No of TX", NoOfTXtoGraph);
                startActivity(storeIntent);
            }
        });

        storeLineChart = findViewById(R.id.btn_storeLinechart);
        storeLineChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent storeLineIntent = new Intent(DisplayDate.this,StoreLineChart.class);
                storeLineIntent.putExtra("Store Line No",StoreNo123);
                storeLineIntent.putExtra("Store Line Amount",AmountToGraph);
                storeLineIntent.putExtra("No of TX", NoOfTXtoGraph);
                startActivity(storeLineIntent);
            }
        });

        /*Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Toast.makeText(DisplayDate.this, "Network Speed: __" + "B/s", Toast.LENGTH_SHORT).show();
            }
        },0,5000);*/

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

        refresh = findViewById(R.id.iv_refresh);
        storeStart = findViewById(R.id.tv_startdate);
        storeEnd = findViewById(R.id.tv_enddate);

        refresh.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {

                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);

                while(TL.getChildCount() > 1)
                {
                    TL.removeView(TL.getChildAt(TL.getChildCount() - 1));
                }

                StoreNo123.clear();
                StoreName123.clear();
                NoOfTrans.clear();
                NoOfItemsSold.clear();
                Amount123.clear();

                AmountToGraph.clear();
                NoOfTXtoGraph.clear();

                final ProgressDialog progressDialog = new ProgressDialog(DisplayDate.this);

                new AsyncTask<Void, Void, Void>(){

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog.setMessage("Collecting data...");
                        progressDialog.show();
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

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        storeStart.setText(storeStartDate);
                        storeEnd.setText(storeEndDate);
                        PrintTable();
                        progressDialog.dismiss();
                    }

                }.execute();

                refresh.startAnimation(rotation);
            }
        });

        storeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(DisplayDate.this, DPstartDate, startCalendar.get(Calendar.YEAR),startCalendar.get(Calendar.MONTH),startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        storeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(DisplayDate.this, DPendDate, endCalendar.get(Calendar.YEAR),endCalendar.get(Calendar.MONTH),endCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
            String query = "Select No_, Name, sum([Net Amount]) as sumNetAmt, COUNT([Transaction No_]) as NoOfTran, Sum([No_ of Items]) as TotlItemsSold from ["+CName+"$Store] as str,["+CName+"$Transaction Header] as trans where str.[No_] = trans.[Store No_] and trans.[Date] between '"+storeStartDate+"' and '"+storeEndDate+"' group by str.[No_],str.[Name],trans.[Store No_] order by No_";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {
                    StoreNo123.add(rs.getString("No_"));
                    StoreName123.add(rs.getString("Name"));
                    NoOfTrans.add(rs.getDouble("NoOfTran"));
                    NoOfItemsSold.add(rs.getDouble("TotlItemsSold"));
                    Amount123.add(rs.getDouble("sumNetAmt"));
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

        for (int i = 0 ; i < StoreNo123.size() ; i++){

            TL.setStretchAllColumns(true);

            TR = new TableRow(this);

            TV1 = new TextView(this);
            TV2 = new TextView(this);
            TV3 = new TextView(this);
            TV4 = new TextView(this);
            TV5 = new TextView(this);

            TV1.setText(StoreNo123.get(i));

            String str = StoreName123.get(i);
            /*StringBuilder sb = new StringBuilder();
            for (int j = 0; j < str.length(); j++) {
                if (j > 0 && (j % 20 == 0)) {
                    sb.append("\n");
                }
                sb.append(str.charAt(j));
            }
            str = sb.toString();*/

            TV2.setText(str);

            double val = Math.abs(Amount123.get(i)) / Double.parseDouble(String.valueOf(intVal[index]));
            val = Math.round(val * 100.0) / 100.0;

            TV3.setText(NumberFormat.getInstance(Locale.US).format(Math.round((NoOfTrans.get(i) / intVal[index]) * 100.0) / 100.0));

            NoOfTXtoGraph.add(Float.parseFloat(String.valueOf(Math.round((NoOfTrans.get(i) / intVal[index]) * 100.0) / 100.0)));

            TV4.setText(NumberFormat.getInstance(Locale.US).format(Math.round((NoOfItemsSold.get(i) / intVal[index]) * 100.0) / 100.0));

            TV5.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(val)));

            AmountToGraph.add(Float.parseFloat(String.valueOf(val)));

            TV1.setTextSize(15);
            TV1.setPadding(10,0,10,0);
            TV1.setGravity(Gravity.LEFT);
            TV1.setTextColor(Color.WHITE);
            TV1.setBackground(getDrawable(R.drawable.rect_border));

            TV2.setTextSize(15);
            TV2.setPadding(10,0,10,0);
            TV2.setGravity(Gravity.LEFT);
            TV2.setTextColor(Color.WHITE);
            TV2.setBackground(getDrawable(R.drawable.rect_border));

            TV3.setTextSize(15);
            TV3.setPadding(10,0,10,0);
            TV3.setGravity(Gravity.RIGHT);
            TV3.setTextColor(Color.WHITE);
            TV3.setBackground(getDrawable(R.drawable.rect_border));

            TV4.setTextSize(15);
            TV4.setPadding(10,0,10,0);
            TV4.setGravity(Gravity.RIGHT);
            TV4.setTextColor(Color.WHITE);
            TV4.setBackground(getDrawable(R.drawable.rect_border));

            TV5.setTextSize(15);
            TV5.setPadding(10,0,10,0);
            TV5.setGravity(Gravity.RIGHT);
            TV5.setTextColor(Color.WHITE);
            TV5.setBackground(getDrawable(R.drawable.rect_border));

            TR.addView(TV1);
            TR.addView(TV2);
            TR.addView(TV3);
            TR.addView(TV4);
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

        NoOfTXtoGraph.clear();
        AmountToGraph.clear();

        PrintTable();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}
}
