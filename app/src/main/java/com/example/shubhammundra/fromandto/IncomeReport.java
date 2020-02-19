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

public class IncomeReport extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TableLayout TL,TL2;
    TableRow TR,TR2;
    TextView TV1, TV2, TV12, TV22;
    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    TextView storeStart;
    TextView storeEnd;

    ImageView refresh;

    ImageView back;

    String storeStartDate = "2001-01-01";

    String storeEndDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

    String selectStoreNo = "S0001";

    String dropDownStoreNo;

    Button btnChart;

    Calendar startCalendar = Calendar.getInstance();
    Calendar endCalendar = Calendar.getInstance();

    String CName;

    int count = 1;

    ArrayList<String> Year, Str;

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal= {1,1000,100000,1000000,10000000};

    String dropDownValue;

    ArrayList<String> IncomeType = new ArrayList<>();
    ArrayList<Double> Amount1 = new ArrayList<>();

    ArrayList<String> CashiName = new ArrayList<>();
    ArrayList<Double> Amount2 = new ArrayList<>();

    ArrayList<Float> AmountToGraph1 = new ArrayList<>();
    ArrayList<Float> AmountToGraph2 = new ArrayList<>();

    int index;

    @SuppressLint("StaticFieldLeak")
    public static Spinner spinnerFilter;
    @SuppressLint("StaticFieldLeak")
    public static SearchableSpinner spinner;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_report_table);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        CName = intent.getStringExtra("Company Name");
        Year = intent.getStringArrayListExtra("Unique Year");
        Str = intent.getStringArrayListExtra("Store Name");

        TL = findViewById(R.id.tl_myTableInc);
        TL2 = findViewById(R.id.tl_myTableInc2);

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

        back = findViewById(R.id.img1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnChart = findViewById(R.id.btn_incomechart);
        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chartIntent = new Intent(IncomeReport.this,IncomeTypChart.class);
                chartIntent.putExtra("Expense Type",IncomeType);
                chartIntent.putExtra("Exp Net Amount",AmountToGraph1);
                chartIntent.putExtra("Expense City",CashiName);
                chartIntent.putExtra("City Net Amount",AmountToGraph2);
                startActivity(chartIntent);
            }
        });
    }

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

                IncomeType.clear();
                CashiName.clear();

                Amount1.clear();
                Amount2.clear();

                AmountToGraph1.clear();
                AmountToGraph2.clear();

                final ProgressDialog progressDialog = new ProgressDialog(IncomeReport.this);

                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog.setMessage("Colleting Data...");
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
                new DatePickerDialog(IncomeReport.this, DPstartDate, startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        storeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(IncomeReport.this, DPendDate, endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
            String query = "select IEa.[Description], sum(tIE.[Amount]) as sumNetAmt from [dbo].["+CName+"$Trans_ Income_Expense Entry] as tIE, [dbo].["+CName+"$Income_Expense Account] as IEa  where tIE.No_ = IEa.No_ and tIE.[Store No_] = IEa.[Store No_] and IEa.[Account Type] = 0 and tIE.Date between '"+storeStartDate+"' and '"+storeEndDate+"' and tIE.[Store No_] = '"+selectStoreNo+"' group by IEa.[Description]";
                try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    IncomeType.add(rs.getString("Description"));
                    Amount1.add(Math.abs(rs.getDouble("sumNetAmt")));
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
    public void doInBackground2() {
        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query2 = "select staff.[First Name], sum(tIE.Amount) as sumNetAmt from [dbo].["+CName+"$Trans_ Income_Expense Entry] as tIE, [dbo].["+CName+"$Staff] as staff where  tIE.[Staff ID] = staff.[ID] and tIE.Date between '"+storeStartDate+"' and '"+storeEndDate+"' and tIE.[Account Type] = 0 and tIE.[Store No_] = '"+selectStoreNo+"' group by staff.[First Name]";

            try {
                Statement stmt = connect.createStatement();
                ResultSet rs2 = stmt.executeQuery(query2);

                Log.w("Android", "database Connected");

                while(rs2.next()) {
                    CashiName.add(rs2.getString("First Name"));
                    Amount2.add(Math.abs(rs2.getDouble("sumNetAmt")));
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
        for (int i = 0 ; i < Amount1.size() ; i++)
        {
            TL.setStretchAllColumns(true);

            TR = new TableRow(this);

            TV1 = new TextView(this);
            TV2 = new TextView(this);

            TV1.setText(IncomeType.get(i));
            TV2.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((Amount1.get(i) / intVal[index]) * 100.0) / 100.0));

            AmountToGraph1.add(Float.parseFloat(String.valueOf(Math.round((Amount1.get(i) / intVal[index]) * 100.0) / 100.0)));

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

            TR.addView(TV1);
            TR.addView(TV2);

            TL.addView(TR);
        }

        for (int i = 0 ; i < Amount2.size() ; i++)
        {
            TL2.setStretchAllColumns(true);

            TR2 = new TableRow(this);

            TV12 = new TextView(this);
            TV22 = new TextView(this);

            TV12.setText(CashiName.get(i));
            TV22.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((Amount2.get(i) / intVal[index]) * 100.0) / 100.0));

            AmountToGraph2.add(Float.parseFloat(String.valueOf(Math.round((Amount2.get(i) / intVal[index]) * 100.0) / 100.0)));

            TV12.setTextSize(15);
            TV12.setPadding(10, 0, 10, 0);
            TV12.setGravity(Gravity.LEFT);
            TV12.setTextColor(Color.WHITE);
            TV12.setBackground(getDrawable(R.drawable.rect_border));

            TV22.setTextSize(15);
            TV22.setPadding(10, 0, 10, 0);
            TV22.setGravity(Gravity.RIGHT);
            TV22.setTextColor(Color.WHITE);
            TV22.setBackground(getDrawable(R.drawable.rect_border));

            TR2.addView(TV12);
            TR2.addView(TV22);

            TL2.addView(TR2);
        }
    }

    public void StartUpdateLabel() {
        String myStartFormat = "yyyy-MM-dd";
        SimpleDateFormat STARTsdf = new SimpleDateFormat(myStartFormat,Locale.US);
        storeStart.setText(STARTsdf.format(startCalendar.getTime()));
    }
    public void EndUpdateLabel() {
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
            AmountToGraph1.clear();
            while(TL2.getChildCount() > 1)
            {
                TL2.removeView(TL2.getChildAt(TL2.getChildCount() - 1));
            }
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
}
