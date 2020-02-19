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

public class MtdAnalysis extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    TableLayout TL1,TL2;
    TableRow TR1,TR2;

    TextView TV11,TV12,TV13,TV14,TV15,TV16,TV17;
    TextView TV21,TV22,TV23,TV24,TV25,TV26,TV27;

    String CName;

    String[] Month = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

    String[] getMonth = {"","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

    String[] index1 = {"1","2","3","4","5","6","7","8","9","10","11","12"};

    ArrayList<String> Year ;

    ImageView back,refresh;

    @SuppressLint("StaticFieldLeak")
    public static Spinner spinner;
    @SuppressLint("StaticFieldLeak")
    public static Spinner spinner1;

    String selectMonth = "", dropDownMonth = "";
    String selectYear = "", dropDownYear = "", dropDownPreYear = "";

    ArrayList<Integer> Month123 = new ArrayList<>();
    ArrayList<Double> NetAmt123 = new ArrayList<>();

    ArrayList<Double> Profit123 = new ArrayList<>();
    ArrayList<Integer> ProDate123 = new ArrayList<>();

    ArrayList<Double> NewNetAmt1 = new ArrayList<>();
    ArrayList<Integer> NewDate1 = new ArrayList<>();

    ArrayList<Double> NewProfit1 = new ArrayList<>();
    ArrayList<Integer> NewDate2 = new ArrayList<>();

    double B = 0, B1 = 0,B2 = 0, B3 = 0;

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal = {1,1000,100000,1000000,10000000};

    String dropDownValue;

    int index;

    @SuppressLint("StaticFieldLeak")
    public static Spinner spinnerFilter;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mtd_analysis);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        CName = intent.getStringExtra("Company Name");
        Year = intent.getStringArrayListExtra("Unique Year");

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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,Month);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner1 = findViewById(R.id.spin_yearselect);
        spinner1.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,Year);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter2);

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
                doInBackground2();
                return null;
            }
        }.execute();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Spinner spinner = (Spinner) adapterView;
        if (spinner.getId() == R.id.spin_storeselect)
        {
            dropDownMonth = adapterView.getItemAtPosition(i).toString();

            for (int j = 0; j < Month.length ; j++)
            {
                if (dropDownMonth == Month[j])
                {
                    dropDownMonth = index1[j];
                }
            }
            Toast.makeText(adapterView.getContext(), "Selected Month : " + getMonth[Integer.parseInt(dropDownMonth)], Toast.LENGTH_SHORT).show();
        }

        else
        if (spinner.getId() == R.id.spin_yearselect)
        {
            dropDownYear = adapterView.getItemAtPosition(i).toString();

            Double qwe = Double.parseDouble(dropDownYear);
            int qwerty = qwe.intValue();
            qwerty = qwerty - 1;

            dropDownPreYear = String.valueOf(qwerty);

            Toast.makeText(adapterView.getContext(), "Selected Year : " + dropDownYear, Toast.LENGTH_SHORT).show();
        }
        else
            if (spinner.getId() == R.id.spin_filter)
            {
                index = i;

                dropDownValue = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(), "Sorted by: " + dropDownValue, Toast.LENGTH_SHORT).show();

                while(TL1.getChildCount() > 1)
                {
                    TL1.removeView(TL1.getChildAt(TL1.getChildCount() - 1));
                }

                while(TL2.getChildCount() > 1)
                {
                    TL2.removeView(TL2.getChildAt(TL2.getChildCount() - 1));
                }

                PrintTable();
            }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @SuppressLint("RtlHardcoded")
    public void doInBackground1(){
        selectMonth = dropDownMonth;
        selectYear = dropDownYear;

        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query = "SELECT sum([Net Amount]) as NetAmt, DATEPART(DD,Date) as date FROM [dbo].["+CName+"$Transaction Header] where [Net Amount] != 0 and DATEPART(YYYY,Date) = '"+selectYear+"' and DATEPART(MM,Date) = '"+selectMonth+"' group by DATEPART(MM,Date), DATEPART(DD,Date) order by DATEPART(MM,Date), DATEPART(DD,Date)";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next())
                {
                    NewNetAmt1.add(Math.abs(rs.getDouble("NetAmt")));
                    NewDate1.add(rs.getInt("date"));
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void doInBackground2(){

        TL1 = findViewById(R.id.tl_myTable1);
        TL2 = findViewById(R.id.tl_myTable2);

        TR1 = findViewById(R.id.tr_tr);
        TR2 = findViewById(R.id.tr_tr2);

        selectMonth = dropDownMonth;
        selectYear = dropDownPreYear;

        refresh = findViewById(R.id.iv_refresh);

        refresh.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);

                while (TL1.getChildCount() > 1) {
                    TL1.removeView(TL1.getChildAt(TL1.getChildCount() - 1));
                }

                while (TL2.getChildCount() > 1) {
                    TL2.removeView(TL2.getChildAt(TL2.getChildCount() - 1));
                }

                B = 0;
                B1 = 0;
                B2 = 0;
                B3 = 0;

                final ProgressDialog progressDialog = new ProgressDialog(MtdAnalysis.this);

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
                        doInBackground2();
                        doInBackground1();
                        doInBackground3();
                        doInBackground4();
                        return null;
                    }
                }.execute();


                /*String abc = "Initial";

                for (int j = 0 ; j < Month123.size() ; j++)
                {
                    abc = abc + ", " + Month123.get(j);
                    demo.setText(abc.toString());
                }
*/
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
            String query = "SELECT sum([Net Amount]) as NetAmt, DATEPART(DD,Date) as date FROM [dbo].["+CName+"$Transaction Header] where [Net Amount] != 0 and DATEPART(YYYY,Date) = '"+selectYear+"' and DATEPART(MM,Date) = '"+selectMonth+"' group by DATEPART(MM,Date), DATEPART(DD,Date) order by DATEPART(MM,Date), DATEPART(DD,Date)";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    int q = rs.getInt("NetAmt");
                    q = Math.abs(q);

                    B1 = B1 + q;

                    int w = rs.getInt("date");

                    NetAmt123.add(B1);
                    Month123.add(w);
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void doInBackground3() {
        selectMonth = dropDownMonth;
        selectYear = dropDownPreYear;

        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query = "Select (sum(Price) - sum(abs([Cost Amount]))) as Profit, DATEPART(DD,Date) as date FROM [dbo].["+CName+"$Trans_ Sales Entry] where [Cost Amount] < 0 and DATEPART(YYYY,Date) = '"+selectYear+"' and DATEPART(MM,Date) = '"+selectMonth+"' group by DATEPART(DD,Date) order by DATEPART(DD,Date)";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    int q = rs.getInt("Profit");

                    B2 = B2 + q;

                    int w = rs.getInt("date");

                    Profit123.add(B2);
                    ProDate123.add(w);
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @SuppressLint({"RtlHardcoded", "SetTextI18n"})
    public void doInBackground4() {
        selectMonth = dropDownMonth;
        selectYear = dropDownYear;

        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query = "Select (sum(Price) - sum(abs([Cost Amount]))) as Profit, DATEPART(DD,Date) as date FROM [dbo].["+CName+"$Trans_ Sales Entry] where [Cost Amount] < 0 and DATEPART(YYYY,Date) = '"+selectYear+"' and DATEPART(MM,Date) = '"+selectMonth+"' group by DATEPART(DD,Date) order by DATEPART(DD,Date)";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    NewProfit1.add(Math.abs(rs.getDouble("Profit")));
                    NewDate2.add(rs.getInt("date"));
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint({"RtlHardcoded", "SetTextI18n"})
    public void PrintTable() {

        for (int p = 0 ; p < NewNetAmt1.size() ; p++)
        {
            TL1.setStretchAllColumns(true);

            TR1 = new TableRow(this);

            TV11 = new TextView(this);            TV12 = new TextView(this);            TV13 = new TextView(this);            TV14 = new TextView(this);            TV15 = new TextView(this);            TV16 = new TextView(this);            TV17 = new TextView(this);

            TV11.setText(NewDate1.get(p) + " - " + getMonth[Integer.parseInt(selectMonth)]);
            TV12.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((NewNetAmt1.get(p) / intVal[index]) * 100.0) / 100.0));

            B = B + NewNetAmt1.get(p);
            double val2 = B;
            TV13.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((B / intVal[index]) * 100.0) / 100.0));

            double val3 = 0;

            for (int i = 0 ; i < Month123.size() ; i++)
            {
                if (NewDate1.get(p) == Month123.get(i))
                {
                    val3 = NetAmt123.get(i);
                    TV14.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((NetAmt123.get(i) / intVal[index]) * 100.0) / 100.0));
                    break;
                }
                else
                {
                    val3 = 0;
                    TV14.setText("0");
                }
            }

            if (val3 != 0)
            {
                double e = val2;
                double f = val3;

                double g1;

                if (f != 0) { g1 = (e * 100) / f ; }
                else { g1 = e * 100 ; }

                TV15.setText(String.valueOf(Math.round((g1) * 100.0) / 100.0) + "%");

                double h = e - f;
                TV16.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((h / intVal[index]) * 100.0) / 100.0));

                TV17.setText(String.valueOf(Math.round((g1 - 100) * 100.0) / 100.0) + "%");
            }
            else
            {
                TV15.setText("0");
                TV16.setText("0");
                TV17.setText("0");
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

            TV14.setTextSize(15);
            TV14.setPadding(10, 0, 10, 0);
            TV14.setGravity(Gravity.RIGHT);
            TV14.setTextColor(Color.WHITE);
            TV14.setBackground(getDrawable(R.drawable.rect_border));

            TV15.setTextSize(15);
            TV15.setPadding(10, 0, 10, 0);
            TV15.setGravity(Gravity.RIGHT);
            TV15.setTextColor(Color.WHITE);
            TV15.setBackground(getDrawable(R.drawable.rect_border));

            TV16.setTextSize(15);
            TV16.setPadding(10, 0, 10, 0);
            TV16.setGravity(Gravity.RIGHT);
            TV16.setTextColor(Color.WHITE);
            TV16.setBackground(getDrawable(R.drawable.rect_border));

            TV17.setTextSize(15);
            TV17.setPadding(10, 0, 10, 0);
            TV17.setGravity(Gravity.RIGHT);
            TV17.setTextColor(Color.WHITE);
            TV17.setBackground(getDrawable(R.drawable.rect_border));

            TR1.addView(TV11);            TR1.addView(TV12);            TR1.addView(TV13);            TR1.addView(TV14);            TR1.addView(TV15);            TR1.addView(TV16);            TR1.addView(TV17);

            TL1.addView(TR1);
        }

        for (int p = 0 ; p < NewProfit1.size() ; p++)
        {
            TL2.setStretchAllColumns(true);

            TR2 = new TableRow(this);

            TV21 = new TextView(this);                    TV22 = new TextView(this);                    TV23 = new TextView(this);                    TV24 = new TextView(this);                    TV25 = new TextView(this);                    TV26 = new TextView(this);                    TV27 = new TextView(this);

            TV21.setText(NewDate2.get(p) + " - " + getMonth[Integer.parseInt(selectMonth)]);
            TV22.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((NewProfit1.get(p) / intVal[index]) * 100.0) / 100.0));

            B3 = B3 + NewProfit1.get(p);
            double val2 = B3;
            TV23.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((NewProfit1.get(p) / intVal[index]) * 100.0) / 100.0));

            double val3 = 0;
            for (int i = 0 ; i < ProDate123.size() ; i++)
            {
                if (NewDate2.get(p) == ProDate123.get(i))
                {
                    val3 = Profit123.get(i);
                    TV24.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((Profit123.get(i)/ intVal[index]) * 100.0) / 100.0));
                    break;
                }
                else
                {
                    val3 = 0;
                    TV24.setText("0");
                }
            }

            if (val3 != 0)
            {
                double e = val2;
                double f = val3;

                double g1;

                if (f != 0) { g1 = (e * 100) / f ; }
                else { g1 = e * 100; }

                TV25.setText(String.valueOf(Math.round((g1) * 100.0) / 100.0) + "%");

                double h = e - f;
                TV26.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((h / intVal[index]) * 100.0) / 100.0));

                TV27.setText(String.valueOf(Math.round((g1 - 100) * 100.0) / 100.0) + "%");
            }
            else
            {
                TV25.setText("0");
                TV26.setText("0");
                TV27.setText("0");
            }

            TV21.setTextSize(15);
            TV21.setPadding(10, 0, 10, 0);
            TV21.setGravity(Gravity.LEFT);
            TV21.setTextColor(Color.WHITE);
            TV21.setBackground(getDrawable(R.drawable.rect_border));

            TV22.setTextSize(15);
            TV22.setPadding(10, 0, 10, 0);
            TV22.setGravity(Gravity.RIGHT);
            TV22.setTextColor(Color.WHITE);
            TV22.setBackground(getDrawable(R.drawable.rect_border));

            TV23.setTextSize(15);
            TV23.setPadding(10, 0, 10, 0);
            TV23.setGravity(Gravity.RIGHT);
            TV23.setTextColor(Color.WHITE);
            TV23.setBackground(getDrawable(R.drawable.rect_border));

            TV24.setTextSize(15);
            TV24.setPadding(10, 0, 10, 0);
            TV24.setGravity(Gravity.RIGHT);
            TV24.setTextColor(Color.WHITE);
            TV24.setBackground(getDrawable(R.drawable.rect_border));

            TV25.setTextSize(15);
            TV25.setPadding(10, 0, 10, 0);
            TV25.setGravity(Gravity.RIGHT);
            TV25.setTextColor(Color.WHITE);
            TV25.setBackground(getDrawable(R.drawable.rect_border));

            TV26.setTextSize(15);
            TV26.setPadding(10, 0, 10, 0);
            TV26.setGravity(Gravity.RIGHT);
            TV26.setTextColor(Color.WHITE);
            TV26.setBackground(getDrawable(R.drawable.rect_border));

            TV27.setTextSize(15);
            TV27.setPadding(10, 0, 10, 0);
            TV27.setGravity(Gravity.RIGHT);
            TV27.setTextColor(Color.WHITE);
            TV27.setBackground(getDrawable(R.drawable.rect_border));

            TR2.addView(TV21);                    TR2.addView(TV22);                    TR2.addView(TV23);                    TR2.addView(TV24);                    TR2.addView(TV25);                    TR2.addView(TV26);                    TR2.addView(TV27);

            TL2.addView(TR2);
        }
    }
}
