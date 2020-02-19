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

public class ItemCategoryReport extends MainActivity implements AdapterView.OnItemSelectedListener {
    
    TableLayout TL1;
    TableRow TR1;
    TextView TV11, TV12, TV13;
    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    TextView storeStartIC;
    TextView storeEndIC;

    ImageView refreshIC;
    ImageView back;

    String storeStartDateIC = "2001-01-01";

    String storeEndDateIC = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

    Button pieChart,itemPieChart,itemLineChart;

    Calendar startCalendarIC = Calendar.getInstance();
    Calendar endCalendarIC = Calendar.getInstance();

    String CName;

    int count = 1;

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal= {1,1000,100000,1000000,10000000};

    String selectStoreNo = "";

    String dropDownYear;

    ArrayList<String> ItemNo123 = new ArrayList<>();
    ArrayList<String> ItemName123 = new ArrayList<>();
    ArrayList<Double> Amount123 = new ArrayList<>();
    ArrayList<Float> AmountToGraph = new ArrayList<>();

    int index;

    @SuppressLint("StaticFieldLeak")
    public static SearchableSpinner spinner;

    ArrayList<String> Str;

    String dropDownStoreNo;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_categorty_report);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TL1 = findViewById(R.id.tl_myTable1);

        Intent intent = getIntent();
        CName = intent.getStringExtra("Company Name");
        Str = intent.getStringArrayListExtra("Store Name");

        Spinner spinnerFilter = findViewById(R.id.spin_filter);
        spinnerFilter.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, amtFilter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapter);

        spinner = findViewById(R.id.spin_storeselect);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,Str);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);
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
                return null;
            }
        }.execute();

        storeStartIC = findViewById(R.id.tv_startdate1);
        storeEndIC = findViewById(R.id.tv_enddate1);
        storeStartIC.setText(storeStartDateIC);
        storeEndIC.setText(storeEndDateIC);

        back = findViewById(R.id.img1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pieChart = findViewById(R.id.btn_piechart1);
        pieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pieIntent = new Intent(ItemCategoryReport.this,DisplayBarchart.class);
                pieIntent.putExtra("X1 Values",ItemNo123);
                pieIntent.putExtra("Y1 Values",AmountToGraph);
                startActivity(pieIntent);
            }
        });

        itemPieChart = findViewById(R.id.btn_itemPiechart);
        itemPieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itemPieIntent = new Intent(ItemCategoryReport.this,ItemPieChart.class);
                itemPieIntent.putExtra("itemCode",ItemNo123);
                itemPieIntent.putExtra("amount",AmountToGraph);
                startActivity(itemPieIntent);
            }
        });

        itemLineChart = findViewById(R.id.btn_itemLinechart);
        itemLineChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent storeLineIntent = new Intent(ItemCategoryReport.this,ItemLineChart.class);
                storeLineIntent.putExtra("Item Line Code",ItemNo123);
                storeLineIntent.putExtra("Item Line Amount",AmountToGraph);
                startActivity(storeLineIntent);
            }
        });
    }

    @SuppressLint("RtlHardcoded")
    public void doInBackground1() {
        selectStoreNo = dropDownStoreNo;

        final DatePickerDialog.OnDateSetListener DPstartDateIC = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                startCalendarIC.set(Calendar.YEAR, year);
                startCalendarIC.set(Calendar.MONTH, month);
                startCalendarIC.set(Calendar.DAY_OF_MONTH, day);
                StartUpdateLabelIC();
                storeStartDateIC = storeStartIC.getText().toString();
            }
        };

        final DatePickerDialog.OnDateSetListener DPendDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                endCalendarIC.set(Calendar.YEAR, year);
                endCalendarIC.set(Calendar.MONTH, month);
                endCalendarIC.set(Calendar.DAY_OF_MONTH, day);
                EndUpdateLabelIC();
                storeEndDateIC = storeEndIC.getText().toString();
            }
        };

        refreshIC = findViewById(R.id.iv_refresh);
        refreshIC.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
                refreshIC.startAnimation(rotation);

                while(TL1.getChildCount() > 1)
                {
                    TL1.removeView(TL1.getChildAt(TL1.getChildCount() - 1));
                }

                ItemNo123.clear();
                ItemName123.clear();
                Amount123.clear();

                AmountToGraph.clear();

                final ProgressDialog progressDialog = new ProgressDialog(ItemCategoryReport.this);

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
                        storeStartIC.setText(storeStartDateIC);
                        storeEndIC.setText(storeEndDateIC);
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

        storeStartIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ItemCategoryReport.this, DPstartDateIC, startCalendarIC.get(Calendar.YEAR),startCalendarIC.get(Calendar.MONTH),startCalendarIC.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        storeEndIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ItemCategoryReport.this, DPendDate, endCalendarIC.get(Calendar.YEAR),endCalendarIC.get(Calendar.MONTH),endCalendarIC.get(Calendar.DAY_OF_MONTH)).show();
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
            String query = "Select itemC.[Code],itemC.[Description],sum(transSE.[Net Amount]) as sumPrice from ["+CName+"$Trans_ Sales Entry] as transSE,["+CName+"$Item Category] as itemC where transSE.[Item Category Code] = itemC.[Code] and transSE.[Date] between '"+storeStartDateIC+"' and '"+storeEndDateIC+"' and transSE.[Store No_] = '"+selectStoreNo+"' group by transSE.[Item Category Code],itemC.[Code],itemC.[Description]";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {
                    ItemNo123.add(rs.getString("Code"));
                    ItemName123.add(rs.getString("Description"));
                    Amount123.add(rs.getDouble("sumPrice"));
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
        for (int i = 0 ; i < ItemNo123.size() ; i++)
        {
            TL1.setStretchAllColumns(true);
            TR1 = new TableRow(this);
            TV11 = new TextView(this);
            TV12 = new TextView(this);
            TV13 = new TextView(this);

            TV11.setText(ItemNo123.get(i));

            String str = ItemName123.get(i);
            /*StringBuilder sb = new StringBuilder();
            for (int j = 0; j < str.length(); j++) {
                if (j > 0 && (j % 20 == 0)) {
                    sb.append("\n");
                }
                sb.append(str.charAt(j));
            }
            str = sb.toString();*/

            TV12.setText(str);

            double val = Math.abs(Amount123.get(i)) / Double.parseDouble(String.valueOf(intVal[index]));
            val = Math.round(val * 100.0) / 100.0;

            TV13.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(val)));

            AmountToGraph.add(Float.valueOf(String.valueOf(val)));

            TV11.setTextSize(15);
            TV11.setPadding(10,0,10,0);
            TV11.setGravity(Gravity.LEFT);
            TV11.setTextColor(Color.WHITE);
            TV11.setBackground(getDrawable(R.drawable.rect_border));

            TV12.setTextSize(15);
            TV12.setPadding(10,0,10,0);
            TV12.setGravity(Gravity.LEFT);
            TV12.setTextColor(Color.WHITE);
            TV12.setBackground(getDrawable(R.drawable.rect_border));

            TV13.setTextSize(15);
            TV13.setPadding(10,0,10,0);
            TV13.setGravity(Gravity.RIGHT);
            TV13.setTextColor(Color.WHITE);
            TV13.setBackground(getDrawable(R.drawable.rect_border));

            TR1.addView(TV11);
            TR1.addView(TV12);
            TR1.addView(TV13);

            TL1.addView(TR1);
        }
    }

    public void StartUpdateLabelIC(){
        String myStartFormat = "yyyy-MM-dd";
        SimpleDateFormat STARTsdf = new SimpleDateFormat(myStartFormat,Locale.US);
        storeStartIC.setText(STARTsdf.format(startCalendarIC.getTime()));
    }
    public void EndUpdateLabelIC(){
        String myEndFormat = "yyyy-MM-dd";
        SimpleDateFormat ENDsdf = new SimpleDateFormat(myEndFormat,Locale.US);
        storeEndIC.setText(ENDsdf.format(endCalendarIC.getTime()));
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner) adapterView;

        if (spinner.getId() == R.id.spin_filter)
        {
            index = i;

            dropDownYear = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(adapterView.getContext(), "Sorted by: " + dropDownYear, Toast.LENGTH_SHORT).show();

            while(TL1.getChildCount() > 1)
            {
                TL1.removeView(TL1.getChildAt(TL1.getChildCount() - 1));
            }

            AmountToGraph.clear();

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
