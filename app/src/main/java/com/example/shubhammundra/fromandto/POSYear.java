package com.example.shubhammundra.fromandto;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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

public class POSYear extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TableLayout TL, TL1;
    TableRow TR, TR1;
    TextView TV1, TV2, TV3, TV5, TV6, TV7, TV8, TV9, TV11, TV12;

    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    TextView storeStart;
    TextView storeEnd;

    ImageView refresh;

    String storeStartDate = "2001-01-01";

    String storeEndDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    String selectStoreNo = "S0001";

    String dropDownStoreNo, dropDownYear;

    String Values = "''";

    public ArrayList<String> Yearq = new ArrayList<>();
    public ArrayList<String> Yearq1 = new ArrayList<>();
    public ArrayList<Double> NetSale = new ArrayList<>();
    public ArrayList<Double> Income1 = new ArrayList<>();
    public ArrayList<Double> ProfitPer = new ArrayList<>();
    public ArrayList<Double> DisPer = new ArrayList<>();
    public ArrayList<Double> Cost1 = new ArrayList<>();
    public ArrayList<Double> NOR1 = new ArrayList<>();
    public ArrayList<Double> ANAmtPR1 = new ArrayList<>();
    public ArrayList<Double> UniqC = new ArrayList<>();

    public ArrayList<String> Str = new ArrayList<>();
    public ArrayList<String> Year = new ArrayList<>();

    Button btnChart;

    Calendar startCalendar = Calendar.getInstance();
    Calendar endCalendar = Calendar.getInstance();

    TextView selectYear;

    String[] listItems;
    boolean[] checkedItems;

    ArrayList<Integer> UserItems = new ArrayList<>();

    String Cities = "' '";

    String CName;

    ImageView back;

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal = {1,1000,100000,1000000,10000000};

    String dropDownValue;

    ArrayList<Float> AmountToGraph1 = new ArrayList<>();
    ArrayList<Float> AmountToGraph2 = new ArrayList<>();
    ArrayList<Float> AmountToGraph3 = new ArrayList<>();
    ArrayList<Float> AmountToGraph4 = new ArrayList<>();
    ArrayList<Float> AmountToGraph5 = new ArrayList<>();
    ArrayList<Float> AmountToGraph6 = new ArrayList<>();
    ArrayList<Float> AmountToGraph7 = new ArrayList<>();

    ArrayList<Float> AmountToGraph8 = new ArrayList<>();

    int index;

    @SuppressLint("StaticFieldLeak")
    public static Spinner spinnerFilter;
    @SuppressLint("StaticFieldLeak")
    public static SearchableSpinner spinner;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posyear);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent I1 = getIntent();
        CName = I1.getStringExtra("Company Name");
        Year = I1.getStringArrayListExtra("Unique Year");
        Str = I1.getStringArrayListExtra("Store Name");

        TL = findViewById(R.id.tl_myTable);
        TL1 = findViewById(R.id.tl_myTable1);

        listItems = Year.toArray(new String[Year.size()]);
        checkedItems = new boolean[listItems.length];

        selectYear = findViewById(R.id.btn_selectyear);

        selectYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(POSYear.this);
                builder.setTitle("Select Years");

                builder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                        if (isChecked) {
                            if (!UserItems.contains(position)) {
                                UserItems.add(position);
                            }
                        } else if (UserItems.contains(position)) {
                            UserItems.remove(UserItems.indexOf(position));
                        }

                    }
                });

                builder.setCancelable(false);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String Store = "";
                        for (int i = 0; i < UserItems.size(); i++) {
                            Store = Store + listItems[UserItems.get(i)];
                            if (i != UserItems.size() - 1) {
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
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                            UserItems.clear();
                            selectYear.setText("Select Years");
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                for (int i = 0; i < UserItems.size(); i++) {
                    Values = Values + ",'" + UserItems.get(i) + "'";
                }

            }
        });

        spinnerFilter = findViewById(R.id.spin_filter);
        spinnerFilter.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, amtFilter);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapter1);

        spinner = findViewById(R.id.spin_storeselect1);
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
                return null;
            }
        }.execute();

        btnChart = findViewById(R.id.btn_POSChart);
        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chartIntent = new Intent(POSYear.this, POsYearChart.class);
                chartIntent.putExtra("Year", Yearq);
                chartIntent.putExtra("Year1", Yearq1);
                chartIntent.putExtra("Net Sale", AmountToGraph1);
                chartIntent.putExtra("Income", AmountToGraph2);
                chartIntent.putExtra("Profit Percent", AmountToGraph3);
                chartIntent.putExtra("Discount", AmountToGraph4);
                chartIntent.putExtra("cost", AmountToGraph5);
                chartIntent.putExtra("No.of Receipts", AmountToGraph6);
                chartIntent.putExtra("Avg No.of Stores Per Receipts", AmountToGraph7);
                chartIntent.putExtra("Unique Customers", AmountToGraph8);

                startActivity(chartIntent);
            }
        });

        back = findViewById(R.id.img1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Spinner spinner = (Spinner) adapterView;
        if (spinner.getId() == R.id.spin_storeselect1)
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
                while(TL1.getChildCount() > 1)
                {
                    TL1.removeView(TL1.getChildAt(TL1.getChildCount() - 1));
                }

                AmountToGraph1.clear();
                AmountToGraph2.clear();
                AmountToGraph3.clear();
                AmountToGraph4.clear();
                AmountToGraph5.clear();
                AmountToGraph6.clear();
                AmountToGraph7.clear();
                AmountToGraph8.clear();

                PrintTable();
            }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @SuppressLint("RtlHardcoded")
    private void doInBackground1() {
        selectStoreNo = dropDownStoreNo;

        refresh = findViewById(R.id.iv_refresh);

        refresh.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
                refresh.startAnimation(rotation);

                while (TL.getChildCount() > 1)
                {
                    TL.removeView(TL.getChildAt(TL.getChildCount() - 1));
                }
                while (TL1.getChildCount() > 1) {
                    TL1.removeView(TL1.getChildAt(TL1.getChildCount() - 1));
                }
                Cities = "''";
                for (int i = 0; i < UserItems.size() ; i++)
                {
                    Cities = Cities + ", '" + listItems[UserItems.get(i)] + "'";
                }

                Yearq.clear();
                Yearq1.clear();
                NetSale.clear();
                Income1.clear();
                ProfitPer.clear();
                DisPer.clear();
                Cost1.clear();
                NOR1.clear();
                ANAmtPR1.clear();
                UniqC.clear();

                final ProgressDialog progressDialog = new ProgressDialog(POSYear.this);

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
            String query = "Select datepart(YYYY,[Date]) as year, sum([Net Amount]) as NetAmt, SUM([Gross Amount]) as Income, sum([Cost Amount]) as Cost, sum([Discount Amount]) as Discount, count([Receipt No_]) as NoOfRece from ["+CName+"$Transaction Header] where datepart(YYYY,[Date]) in ("+Cities+") and [Store No_] = '"+selectStoreNo+"' group by datepart(YY,[Date])";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    Yearq.add(rs.getString("year"));
                    double netAmt123 = Math.abs(rs.getDouble("NetAmt"));
                    NetSale.add(netAmt123);
                    double costAmt123 = Math.abs(rs.getDouble("Cost"));
                    Cost1.add(costAmt123);
                    Income1.add(Math.abs(rs.getDouble("Income")));
                    double noOfRece123 = rs.getDouble("NoOfRece");
                    NOR1.add(noOfRece123);
                    double profirPer123 = 0;
                    double discount123 = Math.abs(rs.getDouble("Discount"));
                    double discountPer123 = 0;
                    if (netAmt123 != 0)
                    {
                        profirPer123 = ((netAmt123 - costAmt123)*100) / netAmt123;
                        discountPer123 = (discount123 * 100) / netAmt123;
                    }
                    ProfitPer.add(profirPer123);
                    DisPer.add(discountPer123);
                    double avgAmtPerRece = 0;
                    if (noOfRece123 != 0)
                    {
                        avgAmtPerRece = netAmt123 / noOfRece123;
                    }
                    ANAmtPR1.add(avgAmtPerRece);
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
    private void doInBackground2() {
        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query = "select DATEPART(YYYY,Date) as year, COUNT([Customer No_]) as UniqCust FROM [dbo].["+CName+"$Transaction Header] where DATEPART(YYYY,Date) in ("+Cities+") and [Store No_] = '"+selectStoreNo+"' and [Customer No_] != ' ' group by DATEPART(YYYY,Date)";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    Yearq1.add(rs.getString("year"));
                    UniqC.add(Math.abs(rs.getDouble("UniqCust")));
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
        for (int i = 0 ; i < Yearq.size() ; i++)
        {
            TL.setStretchAllColumns(true);

            TR = new TableRow(this);

            TV1 = new TextView(this);
            TV2 = new TextView(this);
            TV3 = new TextView(this);
            TV5 = new TextView(this);
            TV6 = new TextView(this);
            TV7 = new TextView(this);
            TV8 = new TextView(this);
            TV9 = new TextView(this);

            TV1.setText(Yearq.get(i));

            double val1 = NetSale.get(i) / Double.parseDouble(String.valueOf(intVal[index]));
            val1 = Math.round(val1 * 100.0) / 100.0;
            TV2.setText(NumberFormat.getNumberInstance(Locale.US).format(val1));

            double val2 = Income1.get(i) / Double.parseDouble(String.valueOf(intVal[index]));
            val2 = Math.round(val2 * 100.0) / 100.0;
            TV3.setText(NumberFormat.getNumberInstance(Locale.US).format(val2));

            double val3 = ProfitPer.get(i);
            val3 = Math.round(val3 * 100.0) / 100.0;
            TV5.setText(String.valueOf(val3));

            double val4 = DisPer.get(i);
            val4 = Math.round(val4 * 100.0) / 100.0;
            TV6.setText(String.valueOf(val4));

            double val5 = Cost1.get(i) / Double.parseDouble(String.valueOf(intVal[index]));
            val5 = Math.round(val5 * 100.0) / 100.0;
            TV7.setText(NumberFormat.getNumberInstance(Locale.US).format(val5));

            double val6 = NOR1.get(i) / Double.parseDouble(String.valueOf(intVal[index]));
            val6 = Math.round(val6 * 100.0) / 100.0;
            TV8.setText(NumberFormat.getNumberInstance(Locale.US).format(val6));

            double val7 = ANAmtPR1.get(i) / Double.parseDouble(String.valueOf(intVal[index]));
            val7 = Math.round(val7 * 100.0) / 100.0;
            TV9.setText(NumberFormat.getNumberInstance(Locale.US).format(val7));

            AmountToGraph1.add(Float.parseFloat(String.valueOf(val1)));
            AmountToGraph2.add(Float.parseFloat(String.valueOf(val2)));
            AmountToGraph3.add(Float.parseFloat(String.valueOf(val3)));
            AmountToGraph4.add(Float.parseFloat(String.valueOf(val4)));
            AmountToGraph5.add(Float.parseFloat(String.valueOf(val5)));
            AmountToGraph6.add(Float.parseFloat(String.valueOf(val6)));
            AmountToGraph7.add(Float.parseFloat(String.valueOf(val7)));

            TV1.setTextSize(15);
            TV1.setPadding(10, 0, 10, 0);
            TV1.setGravity(Gravity.RIGHT);
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

            TV6.setTextSize(15);
            TV6.setPadding(10, 0, 10, 0);
            TV6.setGravity(Gravity.RIGHT);
            TV6.setTextColor(Color.WHITE);
            TV6.setBackground(getDrawable(R.drawable.rect_border));

            TV7.setTextSize(15);
            TV7.setPadding(10, 0, 10, 0);
            TV7.setGravity(Gravity.RIGHT);
            TV7.setTextColor(Color.WHITE);
            TV7.setBackground(getDrawable(R.drawable.rect_border));

            TV8.setTextSize(15);
            TV8.setPadding(10, 0, 10, 0);
            TV8.setGravity(Gravity.RIGHT);
            TV8.setTextColor(Color.WHITE);
            TV8.setBackground(getDrawable(R.drawable.rect_border));

            TV9.setTextSize(15);
            TV9.setPadding(10, 0, 10, 0);
            TV9.setGravity(Gravity.RIGHT);
            TV9.setTextColor(Color.WHITE);
            TV9.setBackground(getDrawable(R.drawable.rect_border));

            TR.addView(TV1);
            TR.addView(TV2);
            TR.addView(TV3);
            TR.addView(TV5);
            TR.addView(TV6);
            TR.addView(TV7);
            TR.addView(TV8);
            TR.addView(TV9);

            TL.addView(TR);
        }

        for (int i = 0 ; i < Yearq1.size() ; i++)
        {
            TL1.setStretchAllColumns(true);

            TR1 = new TableRow(this);

            TV11 = new TextView(this);
            TV12 = new TextView(this);

            TV11.setText(Yearq1.get(i));
            TV12.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((UniqC.get(i) / intVal[index]) * 100.0) / 100.0));

            AmountToGraph8.add(Float.parseFloat(String.valueOf(Math.round((UniqC.get(i) / intVal[index]) * 100.0) / 100.0)));

            TV11.setTextSize(15);
            TV11.setPadding(10, 0, 10, 0);
            TV11.setGravity(Gravity.RIGHT);
            TV11.setTextColor(Color.WHITE);
            TV11.setBackground(getDrawable(R.drawable.rect_border));

            TV12.setTextSize(15);
            TV12.setPadding(10, 0, 10, 0);
            TV12.setGravity(Gravity.RIGHT);
            TV12.setTextColor(Color.WHITE);
            TV12.setBackground(getDrawable(R.drawable.rect_border));

            TR1.addView(TV11);
            TR1.addView(TV12);

            TL1.addView(TR1);
        }
    }
}
