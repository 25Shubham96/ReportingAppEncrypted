package com.example.shubhammundra.fromandto;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
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

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import static android.view.Gravity.RIGHT;

public class Tender extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TableLayout TL, TL2;
    TableRow TR, TR2;
    TextView TV1,TV2,TV3,TV4;

    TextView TV21,TV22,TV23,TV24,TV25,TV26,TV27,TV28,TV29,TV210,TV211,TV212,TV213,TV214,TV215;

    Connection connect;
    String ConnectionResult = "", code;
    double Amount123;
    Boolean isSuccess = false;

    ImageView refresh, back;

    String[] listItems;

    ArrayList<String> Year;

    boolean[] checkedItems;

    public static String CName;
    String Years = "''";

    Button selectYear;

    String dropDownYear;

    String Values = "''";

    ArrayList<String> YearSelected = new ArrayList<>();

    ArrayList<Integer> UserItems = new ArrayList<>();

    ArrayList<Integer> year1 = new ArrayList<>();

    ArrayList<String> OriginalTenderType = new ArrayList<>();

    String[] Quater = {"","Q1","Q2","Q3","Q4"};

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal= {1,1000,100000,1000000,10000000};

    String dropDownValue;

    ArrayList<String> NewYear1234 = new ArrayList<>();
    ArrayList<String> NewCode1234 = new ArrayList<>();
    ArrayList<Double> NewNetAmt1234 = new ArrayList<>();

    ArrayList<String> Year123 = new ArrayList<>();
    ArrayList<Double> Amount1 = new ArrayList<>();
    ArrayList<Double> Amount2 = new ArrayList<>();
    ArrayList<Double> Amount3 = new ArrayList<>();

    ArrayList<Float> AmountToGraph1 = new ArrayList<>();
    ArrayList<Float> AmountToGraph2 = new ArrayList<>();
    ArrayList<Float> AmountToGraph3 = new ArrayList<>();

    int index;

    Button TenderChart;

    ArrayList<String> QuaterYrWithYr = new ArrayList<>();
    ArrayList<String> TenderTypeWithCode = new ArrayList<>();
    ArrayList<Double> AmountForTab = new ArrayList<>();

    String NewYr1, NewQuatYr1, Code1, Des1;
    double Amt1 = 0;

    ArrayList<TextView> myTV = new ArrayList<>();

    String yearCompare = "''", newyearCompare = "''";

    int value = 0;

    ArrayList<String> QuatYrToGraph = new ArrayList<>();

    ArrayList<ArrayList<Float>> AmountToGraph = new ArrayList<>();

    ArrayList<String> StoreDescrip;
    ArrayList<String> StoreNm = new ArrayList<>();

    Button selectStore;

    ArrayList<String> getStoreDescrip = new ArrayList<>();

    String Store1234 = "''";

    @SuppressLint({"RtlHardcoded", "StaticFieldLeak"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tender);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TV2 = findViewById(R.id.tv_yearmonth);        TV21 = findViewById(R.id.tv_1);        TV22 = findViewById(R.id.tv_2);        TV23 = findViewById(R.id.tv_3);        TV24 = findViewById(R.id.tv_4);        TV25 = findViewById(R.id.tv_5);        TV26 = findViewById(R.id.tv_6);        TV27 = findViewById(R.id.tv_7);        TV28 = findViewById(R.id.tv_8);        TV29 = findViewById(R.id.tv_9);        TV210 = findViewById(R.id.tv_10);        TV211 = findViewById(R.id.tv_11);        TV212 = findViewById(R.id.tv_12);        TV213 = findViewById(R.id.tv_13);        TV214 = findViewById(R.id.tv_14);        TV215 = findViewById(R.id.tv_15);

        TV2.setTextSize(15);
        TV2.setPadding(10, 0, 10, 0);
        TV2.setGravity(Gravity.LEFT);
        TV2.setTextColor(Color.WHITE);
        TV2.setBackground(getDrawable(R.drawable.rect_border));

        myTV.add(TV2);        myTV.add(TV21);        myTV.add(TV22);        myTV.add(TV23);        myTV.add(TV24);        myTV.add(TV25);        myTV.add(TV26);        myTV.add(TV27);        myTV.add(TV28);        myTV.add(TV29);        myTV.add(TV210);        myTV.add(TV211);        myTV.add(TV212);        myTV.add(TV213);        myTV.add(TV214);        myTV.add(TV215);

        final Intent intent = getIntent();
        CName = intent.getStringExtra("Company Name");
        Year = intent.getStringArrayListExtra("Unique Year");
        StoreNm = intent.getStringArrayListExtra("Store Name");
        StoreDescrip = intent.getStringArrayListExtra("Store Descrip");

        TL = findViewById(R.id.tl_myTable1);
        TL2 = findViewById(R.id.tl_myTable2);

        Spinner spinnerFilter = findViewById(R.id.spin_filter);
        spinnerFilter.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, amtFilter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapter);

        back = findViewById(R.id.img1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listItems = Year.toArray(new String[Year.size()]);
        checkedItems = new boolean[listItems.length];

        selectYear = findViewById(R.id.btn_selectYear);

        selectYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Tender.this);
                builder.setTitle("Select Years");

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
                            if (i == 0)
                            {
                                Store = listItems[UserItems.get(i)];
                            }
                            else
                                Store = Store + "\n" + listItems[UserItems.get(i)];
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

        final ArrayList<Integer> alreadySelectedItem = new ArrayList<>();

        ArrayList<MultiSelectModel> listOfItems = new ArrayList<>();

        for (int i = 0 ; i < StoreDescrip.size() ; i++)
        {
            listOfItems.add(new MultiSelectModel(i+1,StoreDescrip.get(i)));
        }

        final MultiSelectDialog multiSelectDialog = new MultiSelectDialog();

        multiSelectDialog.title("Select Stores");
        multiSelectDialog.titleSize(24);
        multiSelectDialog.positiveText("Apply");
        multiSelectDialog.negativeText("Cancel");
        multiSelectDialog.clearText("Clear All");
        multiSelectDialog.preSelectIDsList(alreadySelectedItem);
        multiSelectDialog.multiSelectList(listOfItems);

        multiSelectDialog.onSubmit(new MultiSelectDialog.SubmitCallbackListener() {

            @Override
            public void onDismiss(ArrayList<Integer> ids, ArrayList<String> commonSeperatedData) throws PackageManager.NameNotFoundException {
                getStoreDescrip.addAll(commonSeperatedData);

                String txt = "";
                for (int z = 0 ; z < getStoreDescrip.size() ; z++)
                {
                    if (z == 0)
                    {
                        txt = getStoreDescrip.get(z);
                    }
                    else
                        txt = txt + "\n" + getStoreDescrip.get(z);
                }

                selectStore.setText(txt);
            }

            @Override
            public void onCancel() {}

            @Override
            public void onClear() {}

        });

        selectStore = findViewById(R.id.btn_selectStore);
        selectStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStoreDescrip.clear();
                multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");
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

        TenderChart = findViewById(R.id.btn_tenderchart);
        TenderChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Tender.this,PayByTenderChart.class);
                intent1.putExtra("Year",Year123);
                intent1.putExtra("Normal",AmountToGraph1);
                intent1.putExtra("Card",AmountToGraph2);
                intent1.putExtra("Customer",AmountToGraph3);

                intent1.putExtra("Amount",AmountToGraph);
                intent1.putExtra("QuatYear",QuatYrToGraph);
                intent1.putExtra("TenderTyp",OriginalTenderType);
                startActivity(intent1);
            }
        });
    }

    @SuppressLint("RtlHardcoded")
    public void doInBackground1(){

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

                Years = "''";
                for (int i = 0; i < UserItems.size() ; i++)
                {
                    Years = Years + ", '" + listItems[UserItems.get(i)] + "'";
                }

                Store1234 = "''";
                for (int i = 0; i < getStoreDescrip.size() ; i++)
                {
                    for (int j = 0 ; j < StoreDescrip.size() ; j++)
                    {
                        if (getStoreDescrip.get(i).equals(StoreDescrip.get(j)))
                        {
                            Store1234 = Store1234 + ", '" + StoreNm.get(j) + "'";
                        }
                    }
                }

                Year123.clear();
                Amount1.clear();
                Amount2.clear();
                Amount3.clear();

                AmountToGraph1.clear();
                AmountToGraph2.clear();
                AmountToGraph3.clear();

                AmountToGraph.clear();
                QuatYrToGraph.clear();

                QuaterYrWithYr.clear();
                TenderTypeWithCode.clear();
                AmountForTab.clear();

                OriginalTenderType.clear();

                NewYear1234.clear();
                NewCode1234.clear();
                NewNetAmt1234.clear();

                final ProgressDialog progressDialog = new ProgressDialog(Tender.this);

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
                        TableEntry2();
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
                        doInBackground4();
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
            String query = "Select DATEPART(YYYY,Date) as Year, sum([Amount Tendered]) as Amount, TT.[Function] FROM [dbo].["+CName+"$Trans_ Payment Entry] as TPE, [dbo].["+CName+"$Tender Type] as TT where TT.Code = TPE.[Tender Type] and TT.[Store No_] = TPE.[Store No_] and TT.[Function] in ('0','1','3') and DATEPART(YYYY,Date) in ("+Years+") and TPE.[Store No_] in ("+Store1234+") group by DATEPART(YYYY,Date), TT.[Function] order by DATEPART(YYYY,Date), TT.[Function]";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    NewYear1234.add(rs.getString("Year"));
                    NewCode1234.add(rs.getString("Function"));
                    NewNetAmt1234.add(Math.abs(rs.getDouble("Amount")));
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

        String Yr = "''", NewYear = "''";

        for (int i = 0 ; i < NewYear1234.size() ; i++) {

            TL.setStretchAllColumns(true);

            NewYear = NewYear1234.get(i);

            code = NewCode1234.get(i);

            Amount123 = NewNetAmt1234.get(i);

            if (NewYear.equals(Yr)) {
                TableEntry();
            } else {
                YearSelected.add(NewYear);

                TV1 = new TextView(this);
                TV2 = new TextView(this);
                TV3 = new TextView(this);
                TV4 = new TextView(this);

                TR = new TableRow(this);

                TV1.setText("0");

                TV2.setText("0");
                Amount1.add(Double.parseDouble(TV2.getText().toString()));

                TV3.setText("0");
                Amount2.add(Double.parseDouble(TV3.getText().toString()));

                TV4.setText("0");
                Amount3.add(Double.parseDouble(TV4.getText().toString()));

                AmountToGraph1.add(Float.parseFloat(TV2.getText().toString()));
                AmountToGraph2.add(Float.parseFloat(TV3.getText().toString()));
                AmountToGraph3.add(Float.parseFloat(TV4.getText().toString()));

                TR.addView(TV1);
                TR.addView(TV2);
                TR.addView(TV3);
                TR.addView(TV4);

                TL.addView(TR);

                TV1.setText(NewYear);
                Year123.add(TV1.getText().toString());
                TV1.setTextSize(15);
                TV1.setPadding(10, 0, 10, 0);
                TV1.setGravity(Gravity.LEFT);
                TV1.setTextColor(Color.WHITE);
                TV1.setBackground(getDrawable(R.drawable.rect_border));

                TV2.setTextSize(15);
                TV2.setPadding(10, 0, 10, 0);
                TV2.setGravity(RIGHT);
                TV2.setTextColor(Color.WHITE);

                TV3.setTextSize(15);
                TV3.setPadding(10, 0, 10, 0);
                TV3.setGravity(RIGHT);
                TV3.setTextColor(Color.WHITE);

                TV4.setTextSize(15);
                TV4.setPadding(10, 0, 10, 0);
                TV4.setGravity(RIGHT);
                TV4.setTextColor(Color.WHITE);

                TV2.setBackground(getDrawable(R.drawable.rect_border));
                TV3.setBackground(getDrawable(R.drawable.rect_border));
                TV4.setBackground(getDrawable(R.drawable.rect_border));

                TableEntry();
            }
            Yr = NewYear;
        }
    }
    @SuppressLint("RtlHardcoded")
    public void TableEntry() {
        switch (code) {
            case "0":
                Amount1.remove(Amount1.size() - 1);
                Amount1.add(Amount123);

                TV2.setText(NumberFormat.getInstance(Locale.US).format(Math.round((Amount123 / intVal[index]) * 100.0) / 100.0));

                AmountToGraph1.remove(AmountToGraph1.size() - 1);
                AmountToGraph1.add(Float.parseFloat(String.valueOf(Math.round((Amount123 / intVal[index]) * 100.0) / 100.0)));

                TV2.setTextSize(15);
                TV2.setPadding(10, 0, 10, 0);
                TV2.setGravity(RIGHT);
                TV2.setTextColor(Color.WHITE);
                TV2.setBackground(getDrawable(R.drawable.rect_border));
                break;

            case "1":
                Amount2.remove(Amount2.size()-1);
                Amount2.add(Amount123);

                TV3.setText(NumberFormat.getInstance(Locale.US).format(Math.round((Amount123 / intVal[index]) * 100.0) / 100.0));

                AmountToGraph2.remove(AmountToGraph2.size() - 1);
                AmountToGraph2.add(Float.parseFloat(String.valueOf(Math.round((Amount123 / intVal[index]) * 100.0) / 100.0)));

                TV3.setTextSize(15);
                TV3.setPadding(10, 0, 10, 0);
                TV3.setGravity(RIGHT);
                TV3.setTextColor(Color.WHITE);
                TV3.setBackground(getDrawable(R.drawable.rect_border));
                break;

            case "3":
                Amount3.remove(Amount3.size()-1);
                Amount3.add(Amount123);

                TV4.setText(String.valueOf(NumberFormat.getInstance(Locale.US).format(Math.round((Amount123 / intVal[index]) * 100.0) / 100.0)));

                AmountToGraph3.remove(AmountToGraph3.size() - 1);
                AmountToGraph3.add(Float.parseFloat(String.valueOf(Math.round((Amount123 / intVal[index]) * 100.0) / 100.0)));

                TV4.setTextSize(15);
                TV4.setPadding(10, 0, 10, 0);
                TV4.setGravity(RIGHT);
                TV4.setTextColor(Color.WHITE);
                TV4.setBackground(getDrawable(R.drawable.rect_border));
                break;
        }
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

        AmountToGraph.clear();
        QuatYrToGraph.clear();

        TableEntry2();

        AmountToGraph1.clear();
        AmountToGraph2.clear();
        AmountToGraph3.clear();

        for (int j = 0 ; j < Amount1.size() ; j++) {
            TR = new TableRow(this);

            TV1 = new TextView(this);
            TV2 = new TextView(this);
            TV3 = new TextView(this);
            TV4 = new TextView(this);

            TV1.setText(String.valueOf(Year123.get(j)));
            TV2.setText(String.valueOf(Math.round((Amount1.get(j) / intVal[i]) * 100.0) / 100.0));
            TV3.setText(String.valueOf(Math.round((Amount2.get(j) / intVal[i]) * 100.0) / 100.0));
            TV4.setText(String.valueOf(Math.round((Amount3.get(j) / intVal[i]) * 100.0) / 100.0));

            AmountToGraph1.add(Float.parseFloat(String.valueOf(Math.round((Amount1.get(j) / intVal[i]) * 100.0) / 100.0)));
            AmountToGraph2.add(Float.parseFloat(String.valueOf(Math.round((Amount2.get(j) / intVal[i]) * 100.0) / 100.0)));
            AmountToGraph3.add(Float.parseFloat(String.valueOf(Math.round((Amount3.get(j) / intVal[i]) * 100.0) / 100.0)));

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
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    public void doInBackground2() {
        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query = "Select DATEPART(YYYY,Date) as Yr, DATEPART(QUARTER,Date) as QuatYr, TT.Code, TT.Description, sum(TPE.[Amount Tendered]) as Amount FROM [dbo].["+CName+"$Trans_ Payment Entry] as TPE, [dbo].["+CName+"$Tender Type] as TT where TT.Code = TPE.[Tender Type] and TT.[Store No_] = TPE.[Store No_] and DATEPART(YYYY,Date) in ("+Years+") and TPE.[Store No_] in ("+Store1234+") group by DATEPART(YYYY,Date), DATEPART(QUARTER,Date), TT.Code, TT.Description order by DATEPART(YYYY,Date), DATEPART(QUARTER,Date), TT.Code, TT.Description";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                TL2 = findViewById(R.id.tl_myTable2);
                TR2 = findViewById(R.id.tr_tr2);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    NewYr1 = rs.getString("Yr");
                    NewQuatYr1 = rs.getString("QuatYr");
                    Code1 = rs.getString("Code");
                    Des1 = rs.getString("Description");
                    Amt1 = rs.getDouble("Amount");
                    Amt1 = Math.abs(Amt1);

                    QuaterYrWithYr.add(NewYr1 + "-" + Quater[Integer.parseInt(NewQuatYr1)]);
                    TenderTypeWithCode.add(Code1 + "-" + Des1);
                    AmountForTab.add(Amt1);
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @SuppressLint("SetTextI18n")
    public  void  doInBackground4() {
        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query = "Select Distinct [Tender Type], Description FROM [dbo].["+CName+"$Trans_ Payment Entry] as TPE, [dbo].["+CName+"$Tender Type] as TT where TT.Code = TPE.[Tender Type]";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                String TenTyp = "''", Description = "''";

                while (rs.next())
                {
                    TenTyp = rs.getString("Tender Type");
                    Description = rs.getString("Description");

                    OriginalTenderType.add(TenTyp + "-" + Description);
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
    public void TableEntry2() {
        TL2 = findViewById(R.id.tl_myTable2);

        for (int i = 0 ; i < OriginalTenderType.size() ; i++)
        {
            AmountToGraph.add(new ArrayList<Float>());
            myTV.get(i+1).setText(OriginalTenderType.get(i) + "\n");

            myTV.get(i+1).setTextSize(15);
            myTV.get(i+1).setPadding(10, 0, 10, 0);
            myTV.get(i+1).setGravity(Gravity.LEFT);
            myTV.get(i+1).setTextColor(Color.WHITE);
            myTV.get(i+1).setBackground(getDrawable(R.drawable.rect_border));
        }

        double val1231,val1232;

        for (int j = 0 ; j < QuaterYrWithYr.size() ; j++)
        {
            newyearCompare = QuaterYrWithYr.get(j);

            if (newyearCompare.equals(yearCompare))
            {
                for (int k = 0 ; k < OriginalTenderType.size() ; k++) {
                    if (TenderTypeWithCode.get(j).equals(OriginalTenderType.get(k))) {

                        value = k + 1;
                        switch (value) {
                            case 1: {
                                val1231 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV21.setText(NumberFormat.getNumberInstance(Locale.US).format(val1231));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1231)));

                                TV21.setTextSize(15);
                                TV21.setPadding(10, 0, 10, 0);
                                TV21.setGravity(Gravity.RIGHT);
                                TV21.setTextColor(Color.WHITE);
                                TV21.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 2: {
                                val1231 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV22.setText(NumberFormat.getNumberInstance(Locale.US).format(val1231));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1231)));

                                TV22.setTextSize(15);
                                TV22.setPadding(10, 0, 10, 0);
                                TV22.setGravity(Gravity.RIGHT);
                                TV22.setTextColor(Color.WHITE);
                                TV22.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 3: {
                                val1231 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV23.setText(NumberFormat.getNumberInstance(Locale.US).format(val1231));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1231)));

                                TV23.setTextSize(15);
                                TV23.setPadding(10, 0, 10, 0);
                                TV23.setGravity(Gravity.RIGHT);
                                TV23.setTextColor(Color.WHITE);
                                TV23.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 4: {
                                val1231 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV24.setText(NumberFormat.getNumberInstance(Locale.US).format(val1231));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1231)));

                                TV24.setTextSize(15);
                                TV24.setPadding(10, 0, 10, 0);
                                TV24.setGravity(Gravity.RIGHT);
                                TV24.setTextColor(Color.WHITE);
                                TV24.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 5: {
                                val1231 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV25.setText(NumberFormat.getNumberInstance(Locale.US).format(val1231));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1231)));

                                TV25.setTextSize(15);
                                TV25.setPadding(10, 0, 10, 0);
                                TV25.setGravity(Gravity.RIGHT);
                                TV25.setTextColor(Color.WHITE);
                                TV25.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 6: {
                                val1231 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV26.setText(NumberFormat.getNumberInstance(Locale.US).format(val1231));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1231)));

                                TV26.setTextSize(15);
                                TV26.setPadding(10, 0, 10, 0);
                                TV26.setGravity(Gravity.RIGHT);
                                TV26.setTextColor(Color.WHITE);
                                TV26.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 7: {
                                val1231 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV27.setText(NumberFormat.getNumberInstance(Locale.US).format(val1231));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1231)));

                                TV27.setTextSize(15);
                                TV27.setPadding(10, 0, 10, 0);
                                TV27.setGravity(Gravity.RIGHT);
                                TV27.setTextColor(Color.WHITE);
                                TV27.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 8: {
                                val1231 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV28.setText(NumberFormat.getNumberInstance(Locale.US).format(val1231));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1231)));

                                TV28.setTextSize(15);
                                TV28.setPadding(10, 0, 10, 0);
                                TV28.setGravity(Gravity.RIGHT);
                                TV28.setTextColor(Color.WHITE);
                                TV28.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 9: {
                                val1231 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV29.setText(NumberFormat.getNumberInstance(Locale.US).format(val1231));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1231)));

                                TV29.setTextSize(15);
                                TV29.setPadding(10, 0, 10, 0);
                                TV29.setGravity(Gravity.RIGHT);
                                TV29.setTextColor(Color.WHITE);
                                TV29.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 10: {
                                val1231 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV210.setText(NumberFormat.getNumberInstance(Locale.US).format(val1231));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1231)));

                                TV210.setTextSize(15);
                                TV210.setPadding(10, 0, 10, 0);
                                TV210.setGravity(Gravity.RIGHT);
                                TV210.setTextColor(Color.WHITE);
                                TV210.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 11: {
                                val1231 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV211.setText(NumberFormat.getNumberInstance(Locale.US).format(val1231));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1231)));

                                TV211.setTextSize(15);
                                TV211.setPadding(10, 0, 10, 0);
                                TV211.setGravity(Gravity.RIGHT);
                                TV211.setTextColor(Color.WHITE);
                                TV211.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 12: {
                                val1231 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV212.setText(NumberFormat.getNumberInstance(Locale.US).format(val1231));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1231)));

                                TV212.setTextSize(15);
                                TV212.setPadding(10, 0, 10, 0);
                                TV212.setGravity(Gravity.RIGHT);
                                TV212.setTextColor(Color.WHITE);
                                TV212.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 13: {
                                val1231 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV213.setText(NumberFormat.getNumberInstance(Locale.US).format(val1231));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1231)));

                                TV213.setTextSize(15);
                                TV213.setPadding(10, 0, 10, 0);
                                TV213.setGravity(Gravity.RIGHT);
                                TV213.setTextColor(Color.WHITE);
                                TV213.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 14: {
                                val1231 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV214.setText(NumberFormat.getNumberInstance(Locale.US).format(val1231));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1231)));

                                TV214.setTextSize(15);
                                TV214.setPadding(10, 0, 10, 0);
                                TV214.setGravity(Gravity.RIGHT);
                                TV214.setTextColor(Color.WHITE);
                                TV214.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 15: {
                                val1231 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV215.setText(NumberFormat.getNumberInstance(Locale.US).format(val1231));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1231)));

                                TV215.setTextSize(15);
                                TV215.setPadding(10, 0, 10, 0);
                                TV215.setGravity(Gravity.RIGHT);
                                TV215.setTextColor(Color.WHITE);
                                TV215.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                        }
                    }
                }
            }
            else
            {
                TR2 = new TableRow(this);

                TV2 = new TextView(this);                TV21 = new TextView(this);                TV22 = new TextView(this);                TV23 = new TextView(this);                TV24 = new TextView(this);                TV25 = new TextView(this);                TV26 = new TextView(this);                TV27 = new TextView(this);                TV28 = new TextView(this);                TV29 = new TextView(this);                TV210 = new TextView(this);                TV211 = new TextView(this);                TV212 = new TextView(this);                TV213 = new TextView(this);                TV214 = new TextView(this);                TV215 = new TextView(this);

                myTV.clear();

                myTV.add(TV2);                myTV.add(TV21);                myTV.add(TV22);                myTV.add(TV23);                myTV.add(TV24);                myTV.add(TV25);                myTV.add(TV26);                myTV.add(TV27);                myTV.add(TV28);                myTV.add(TV29);                myTV.add(TV210);                myTV.add(TV211);                myTV.add(TV212);                myTV.add(TV213);                myTV.add(TV214);                myTV.add(TV215);

                for (int z = 0 ; z < OriginalTenderType.size() + 1 ; z++)
                {
                    myTV.get(z).setTextSize(15);
                    myTV.get(z).setPadding(10, 0, 10, 0);
                    myTV.get(z).setGravity(Gravity.LEFT);
                    myTV.get(z).setTextColor(Color.WHITE);
                    myTV.get(z).setBackground(getDrawable(R.drawable.rect_border));
                }

                TR2.addView(TV2);                TR2.addView(TV21);                TR2.addView(TV22);                TR2.addView(TV23);                TR2.addView(TV24);                TR2.addView(TV25);                TR2.addView(TV26);                TR2.addView(TV27);                TR2.addView(TV28);                TR2.addView(TV29);                TR2.addView(TV210);                TR2.addView(TV211);                TR2.addView(TV212);                TR2.addView(TV213);                TR2.addView(TV214);                TR2.addView(TV215);

                TL2.addView(TR2);

                TV2.setText(QuaterYrWithYr.get(j));

                QuatYrToGraph.add(TV2.getText().toString());

                String des = "", newdes = "";

                for (int z = 0 ; z < AmountToGraph.size() ; z++)
                {
                    AmountToGraph.get(z).add(Float.parseFloat(String.valueOf("0")));
                }

                for (int k = 0 ; k < OriginalTenderType.size() ; k++)
                {
                    newdes = TenderTypeWithCode.get(j);
                    des = OriginalTenderType.get(k);

                    if (newdes.equals(des))
                    {
                        value = k + 1;
                        switch (value)
                        {
                            case 1: {
                                val1232 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV21.setText(NumberFormat.getNumberInstance(Locale.US).format(val1232));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1232)));

                                TV21.setTextSize(15);
                                TV21.setPadding(10, 0, 10, 0);
                                TV21.setGravity(Gravity.RIGHT);
                                TV21.setTextColor(Color.WHITE);
                                TV21.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 2: {
                                val1232 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV22.setText(NumberFormat.getNumberInstance(Locale.US).format(val1232));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1232)));

                                TV22.setTextSize(15);
                                TV22.setPadding(10, 0, 10, 0);
                                TV22.setGravity(Gravity.RIGHT);
                                TV22.setTextColor(Color.WHITE);
                                TV22.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 3: {
                                val1232 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV23.setText(NumberFormat.getNumberInstance(Locale.US).format(val1232));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1232)));

                                TV23.setTextSize(15);
                                TV23.setPadding(10, 0, 10, 0);
                                TV23.setGravity(Gravity.RIGHT);
                                TV23.setTextColor(Color.WHITE);
                                TV23.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 4: {
                                val1232 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV24.setText(NumberFormat.getNumberInstance(Locale.US).format(val1232));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1232)));

                                TV24.setTextSize(15);
                                TV24.setPadding(10, 0, 10, 0);
                                TV24.setGravity(Gravity.RIGHT);
                                TV24.setTextColor(Color.WHITE);
                                TV24.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 5: {
                                val1232 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV25.setText(NumberFormat.getNumberInstance(Locale.US).format(val1232));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1232)));

                                TV25.setTextSize(15);
                                TV25.setPadding(10, 0, 10, 0);
                                TV25.setGravity(Gravity.RIGHT);
                                TV25.setTextColor(Color.WHITE);
                                TV25.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 6: {
                                val1232 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV26.setText(NumberFormat.getNumberInstance(Locale.US).format(val1232));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1232)));

                                TV26.setTextSize(15);
                                TV26.setPadding(10, 0, 10, 0);
                                TV26.setGravity(Gravity.RIGHT);
                                TV26.setTextColor(Color.WHITE);
                                TV26.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 7: {
                                val1232 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV27.setText(NumberFormat.getNumberInstance(Locale.US).format(val1232));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1232)));

                                TV27.setTextSize(15);
                                TV27.setPadding(10, 0, 10, 0);
                                TV27.setGravity(Gravity.RIGHT);
                                TV27.setTextColor(Color.WHITE);
                                TV27.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 8: {
                                val1232 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV28.setText(NumberFormat.getNumberInstance(Locale.US).format(val1232));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1232)));

                                TV28.setTextSize(15);
                                TV28.setPadding(10, 0, 10, 0);
                                TV28.setGravity(Gravity.RIGHT);
                                TV28.setTextColor(Color.WHITE);
                                TV28.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 9: {
                                val1232 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV29.setText(NumberFormat.getNumberInstance(Locale.US).format(val1232));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1232)));

                                TV29.setTextSize(15);
                                TV29.setPadding(10, 0, 10, 0);
                                TV29.setGravity(Gravity.RIGHT);
                                TV29.setTextColor(Color.WHITE);
                                TV29.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 10: {
                                val1232 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV210.setText(NumberFormat.getNumberInstance(Locale.US).format(val1232));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1232)));

                                TV210.setTextSize(15);
                                TV210.setPadding(10, 0, 10, 0);
                                TV210.setGravity(Gravity.RIGHT);
                                TV210.setTextColor(Color.WHITE);
                                TV210.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 11: {
                                val1232 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV211.setText(NumberFormat.getNumberInstance(Locale.US).format(val1232));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1232)));

                                TV211.setTextSize(15);
                                TV211.setPadding(10, 0, 10, 0);
                                TV211.setGravity(Gravity.RIGHT);
                                TV211.setTextColor(Color.WHITE);
                                TV211.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 12: {
                                val1232 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV212.setText(NumberFormat.getNumberInstance(Locale.US).format(val1232));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1232)));

                                TV212.setTextSize(15);
                                TV212.setPadding(10, 0, 10, 0);
                                TV212.setGravity(Gravity.RIGHT);
                                TV212.setTextColor(Color.WHITE);
                                TV212.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 13: {
                                val1232 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV213.setText(NumberFormat.getNumberInstance(Locale.US).format(val1232));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1232)));

                                TV213.setTextSize(15);
                                TV213.setPadding(10, 0, 10, 0);
                                TV213.setGravity(Gravity.RIGHT);
                                TV213.setTextColor(Color.WHITE);
                                TV213.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 14: {
                                val1232 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV214.setText(NumberFormat.getNumberInstance(Locale.US).format(val1232));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1232)));

                                TV214.setTextSize(15);
                                TV214.setPadding(10, 0, 10, 0);
                                TV214.setGravity(Gravity.RIGHT);
                                TV214.setTextColor(Color.WHITE);
                                TV214.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                            case 15: {
                                val1232 = Math.round((AmountForTab.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV215.setText(NumberFormat.getNumberInstance(Locale.US).format(val1232));

                                AmountToGraph.get(value - 1).remove(AmountToGraph.get(value - 1).size() - 1);
                                AmountToGraph.get(value - 1).add(Float.parseFloat(String.valueOf(val1232)));

                                TV215.setTextSize(15);
                                TV215.setPadding(10, 0, 10, 0);
                                TV215.setGravity(Gravity.RIGHT);
                                TV215.setTextColor(Color.WHITE);
                                TV215.setBackground(getDrawable(R.drawable.rect_border));

                                break;
                            }
                        }
                    }
                }
            }
            yearCompare = newyearCompare;
        }
    }
}
