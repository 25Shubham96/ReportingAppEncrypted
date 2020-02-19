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
import com.github.mikephil.charting.charts.BarChart;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class KPIAnalysis extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    TableLayout TL,TL2,TL3;
    TableRow TR,TR2,TR3;
    TextView TV1, TV2, TV3, TV4, TV5, TV6, TV7;

    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    BarChart barChart, barChart1, barChart2, barChart3;

    ImageView refresh, back;

    String selectStoreNo = "S0001", CName;

    String dropDownStoreNo, Values = "''";

    Button btn, selectYear;

    String[] listItems;
    boolean[] checkedItems;

    ArrayList<Integer> UserItems = new ArrayList<>();
    ArrayList<String> Store ;
    public static ArrayList<String> Year ;

    public ArrayList<String> YearMonth = new ArrayList<>();
    public ArrayList<String> YearMonth2 = new ArrayList<>();
    public ArrayList<String> YearMonth3 = new ArrayList<>();
    public ArrayList<Double> NetAmt = new ArrayList<>();
    public ArrayList<Double> AvgNetAmt = new ArrayList<>();
    public ArrayList<Double> NoOfReturnSales = new ArrayList<>();
    public ArrayList<Double> AvgItemLines = new ArrayList<>();

    String Cities = "''";

    String[] Months = {" ","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal = {1,1000,100000,1000000,10000000};

    String dropDownValue;

    ArrayList<Float> AmountToGraph1 = new ArrayList<>();
    ArrayList<Float> AmountToGraph2 = new ArrayList<>();
    ArrayList<Float> AmountToGraph3 = new ArrayList<>();
    ArrayList<Float> AmountToGraph4 = new ArrayList<>();

    int index;

    @SuppressLint("StaticFieldLeak")
    public static Spinner spinnerFilter;
    @SuppressLint("StaticFieldLeak")
    public static SearchableSpinner spinner;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kpianalysis);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent getintent = getIntent();
        CName = getintent.getStringExtra("Company Name");
        Store = getintent.getStringArrayListExtra("Store Name");
        Year = getintent.getStringArrayListExtra("Unique Year");

        TL = findViewById(R.id.tl_myTable1);
        TL2 = findViewById(R.id.tl_myTable2);
        TL3 = findViewById(R.id.tl_myTable3);

        listItems = Year.toArray(new String[Year.size()]);
        checkedItems = new boolean[listItems.length];

        selectYear = findViewById(R.id.btn_selectYear);
        selectYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(KPIAnalysis.this);
                builder.setTitle("Select Year");

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
                        selectYear.setText(Store);
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
                            selectYear.setText("Select");
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                for(int i=0 ; i < UserItems.size() ; i++) {
                    Values = Values + ",'" + UserItems.get(i) + "'";
                }
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

        spinner = findViewById(R.id.spin_storeselect);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Store);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setTitle("Select Store");

        btn = findViewById(R.id.btn_hourchart);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chartIntent = new Intent(KPIAnalysis.this,KPIAnalysisChart.class);
                chartIntent.putExtra("Hour",YearMonth);
                chartIntent.putExtra("Hour 2",YearMonth2);
                chartIntent.putExtra("Hour 3",YearMonth3);
                chartIntent.putExtra("Net Amount",AmountToGraph1);
                chartIntent.putExtra("Avg Net Amount",AmountToGraph2);
                chartIntent.putExtra("Return Sales",  AmountToGraph3);
                chartIntent.putExtra("Avg Item Lines", AmountToGraph4);
                startActivity(chartIntent);
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
                doInBackground2();
                doInBackground3();
                return null;
            }
        }.execute();
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

                AmountToGraph1.clear();
                AmountToGraph2.clear();
                AmountToGraph3.clear();
                AmountToGraph4.clear();

                PrintTable();
            }

    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @SuppressLint({"SetTextI18n", "RtlHardcoded"})
    private void doInBackground1(){
        selectStoreNo = dropDownStoreNo;

        refresh = findViewById(R.id.iv_refresh);

        refresh.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
                refresh.startAnimation(rotation);

                while (TL.getChildCount() > 1) {
                    TL.removeView(TL.getChildAt(TL.getChildCount() - 1));
                }
                while (TL2.getChildCount() > 1) {
                    TL2.removeView(TL2.getChildAt(TL2.getChildCount() - 1));
                }
                while (TL3.getChildCount() > 1) {
                    TL3.removeView(TL3.getChildAt(TL3.getChildCount() - 1));
                }
                Cities = "''";
                for (int i = 0; i < UserItems.size() ; i++) {
                    Cities = Cities + ", '" + listItems[UserItems.get(i)] + "'";
                }

                YearMonth.clear();                YearMonth2.clear();                YearMonth3.clear();                NetAmt.clear();                AvgNetAmt.clear();                NoOfReturnSales.clear();                AvgItemLines.clear();

                final ProgressDialog progressDialog = new ProgressDialog(KPIAnalysis.this);

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
                        doInBackground2();
                        doInBackground3();
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

            String query = "SELECT DATEPART(YYYY,Date) as Year, DATEPART(MM,Date) as Month, SUM([Net Amount]) as NetAmt, COUNT([Receipt No_]) as NoOfRece FROM [dbo].["+CName+"$Transaction Header] where DATEPART(YYYY,Date) in ("+Cities+") and [Store No_] = '"+selectStoreNo+"' group by DATEPART(YYYY,Date), DATEPART(MM,Date) order by DATEPART(YYYY,Date), DATEPART(MM,Date)";

            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    YearMonth.add(rs.getString("Year") + "-" + Months[Integer.parseInt(rs.getString("Month"))]);

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
    @SuppressLint({"SetTextI18n", "RtlHardcoded"})
    private void doInBackground2(){

        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        }else{
            Log.w("Android", "Connected");

            String query = "Select DATEPART(YYYY,Date) as Year, DATEPART(MM,Date) as Month, COUNT([Net Amount]) as ReturnSale FROM [dbo].["+CName+"$Transaction Header] where DATEPART(YYYY,Date) in ("+Cities+") and [Store No_] = '"+selectStoreNo+"' and [Net Amount] > 0 group by DATEPART(YYYY,Date), DATEPART(MM,Date) order by DATEPART(YYYY,Date), DATEPART(MM,Date)";

            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    YearMonth2.add(rs.getString("Year") + "-" + Months[Integer.parseInt(rs.getString("Month"))]);
                    NoOfReturnSales.add(rs.getDouble("ReturnSale"));
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
    private void doInBackground3(){

        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        }else{
            Log.w("Android", "Connected");

            String query = "Select DATEPART(YYYY,Date) as Year, DATEPART(MM,Date) as Month, SUM([No_ of Item Lines]) as SumItemLines, COUNT([Receipt No_]) as NoOfRece FROM [dbo].["+CName+"$Transaction Header] where DATEPART(YYYY,Date) in ("+Cities+") and [Store No_] = '"+selectStoreNo+"' group by DATEPART(YYYY,Date), DATEPART(MM,Date) order by DATEPART(YYYY,Date), DATEPART(MM,Date)";

            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    YearMonth3.add(rs.getString("Year") + "-" + Months[Integer.parseInt(rs.getString("Month"))]);
                    AvgItemLines.add(rs.getDouble("SumItemLines"));
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
        for (int i = 0 ; i < YearMonth.size() ; i++)
        {
            TL.setStretchAllColumns(true);

            TR = new TableRow(this);

            TV1 = new TextView(this);
            TV2 = new TextView(this);
            TV3 = new TextView(this);

            TV1.setText(YearMonth.get(i));
            TV2.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((NetAmt.get(i) / intVal[index]) * 100.0) / 100.0 ));
            TV3.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((AvgNetAmt.get(i) / intVal[index]) * 100.0) / 100.0 ));

            AmountToGraph1.add(Float.parseFloat(String.valueOf(Math.round((NetAmt.get(i) / intVal[index]) * 100.0 ) / 100.0)));
            AmountToGraph2.add(Float.parseFloat(String.valueOf(Math.round((AvgNetAmt.get(i) / intVal[index]) * 100.0 ) / 100.0)));

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

        for (int i = 0 ; i < YearMonth2.size() ; i++)
        {
            TL2.setStretchAllColumns(true);

            TR2 = new TableRow(this);

            TV4 = new TextView(this);
            TV5 = new TextView(this);

            TV4.setText(YearMonth2.get(i));
            TV5.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((NoOfReturnSales.get(i) / intVal[index]) * 100.0) / 100.0 ));

            AmountToGraph3.add(Float.parseFloat(String.valueOf(Math.round((NoOfReturnSales.get(i) / intVal[index]) * 100.0) / 100.0)));

            TV4.setPadding(10, 0, 10, 0);
            TV4.setTextSize(15);
            TV4.setGravity(Gravity.LEFT);
            TV4.setTextColor(Color.WHITE);
            TV4.setBackground(getDrawable(R.drawable.rect_border));

            TV5.setTextSize(15);
            TV5.setPadding(10, 0, 10, 0);
            TV5.setGravity(Gravity.RIGHT);
            TV5.setTextColor(Color.WHITE);
            TV5.setBackground(getDrawable(R.drawable.rect_border));

            TR2.addView(TV4);
            TR2.addView(TV5);

            TL2.addView(TR2);
        }

        for (int i = 0 ; i < YearMonth3.size() ; i++)
        {
            TL3.setStretchAllColumns(true);

            TR3 = new TableRow(this);

            TV6 = new TextView(this);
            TV7 = new TextView(this);

            TV6.setText(YearMonth3.get(i));
            TV7.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((AvgItemLines.get(i) / intVal[index]) * 100.0 ) / 100.0 ));

            AmountToGraph4.add(Float.parseFloat(String.valueOf(Math.round((AvgItemLines.get(i) / intVal[index]) * 100.0 ) / 100.0)));

            TV6.setTextSize(15);
            TV6.setPadding(10, 0, 10, 0);
            TV6.setGravity(Gravity.LEFT);
            TV6.setTextColor(Color.WHITE);
            TV6.setBackground(getDrawable(R.drawable.rect_border));

            TV7.setTextSize(15);
            TV7.setPadding(10, 0, 10, 0);
            TV7.setGravity(Gravity.RIGHT);
            TV7.setTextColor(Color.WHITE);
            TV7.setBackground(getDrawable(R.drawable.rect_border));

            TR3.addView(TV6);
            TR3.addView(TV7);

            TL3.addView(TR3);
        }
    }
}
