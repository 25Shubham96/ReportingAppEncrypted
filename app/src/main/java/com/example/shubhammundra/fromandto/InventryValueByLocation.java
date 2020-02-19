package com.example.shubhammundra.fromandto;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class InventryValueByLocation extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TableLayout TL1;
    TableRow TR1;
    TextView TV0,TV1,TV2,TV3,TV4,TV5,TV6,TV7,TV8,TV9,TV10,TV11,TV12,TV13,TV14,TV15,TV16;

    ArrayList<TextView> myTV = new ArrayList<>();

    ArrayList<TextView> myTV1 = new ArrayList<>();

    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    ImageView refresh;

    ImageView back;

    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> UserItems = new ArrayList<>();

    String[] listItems2;
    boolean[] checkedItems2;
    ArrayList<Integer> UserItems2 = new ArrayList<>();

    String[] listItems3;
    boolean[] checkedItems3;
    ArrayList<Integer> UserItems3 = new ArrayList<>();

    String CName;

    ArrayList<String> Year = new ArrayList<>();
    ArrayList<String> ItemCategory = new ArrayList<>();
    ArrayList<String> ItemCategoryDescrip = new ArrayList<>();
    ArrayList<String> LocationCode = new ArrayList<>();
    ArrayList<String> LocationDescrip = new ArrayList<>();

    Button selectYear, selectCategory, selectLocation;

    String Cities = "''", Cities2 = "''", Cities3 = "''";

    GradientDrawable gd;

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal = {1,1000,100000,1000000,10000000};

    String dropDownValue;

    int index;

    @SuppressLint("StaticFieldLeak")
    public static Spinner spinnerFilter;

    ArrayList<String> Year1 = new ArrayList<>();
    ArrayList<Integer> QuatYear = new ArrayList<>();
    ArrayList<String> QuatLocation = new ArrayList<>();
    ArrayList<Double> CostAmt1 = new ArrayList<>();

    ArrayList<String> Year2 = new ArrayList<>();
    ArrayList<String> YearLocation = new ArrayList<>();
    ArrayList<Double> CostAmt2 = new ArrayList<>();

    ArrayList<String> SelectedLocations = new ArrayList<>();

    String NewYear = "";

    int NewQuatYear = 0;

    ArrayList<String> QuaterYearToGraph = new ArrayList<>();
    ArrayList<ArrayList<Float>> CostAmtToGraph = new ArrayList<>();

    Button btnChart;

    String[] QuaterYear = {"","Q1","Q2","Q3","Q4"};

    ArrayList<String> getItemCategory = new ArrayList<>();

    ArrayList<String> getLocation = new ArrayList<>();

    @SuppressLint({"RtlHardcoded", "StaticFieldLeak"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventry_value_by_location);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TL1 = findViewById(R.id.tl_myTable);

        TV0 = findViewById(R.id.tv0);        TV1 = findViewById(R.id.tv1);        TV2 = findViewById(R.id.tv2);        TV3 = findViewById(R.id.tv3);       TV4 = findViewById(R.id.tv4);        TV5 = findViewById(R.id.tv5);        TV6 = findViewById(R.id.tv6);        TV7 = findViewById(R.id.tv7);        TV8 = findViewById(R.id.tv8);        TV9 = findViewById(R.id.tv9);        TV10 = findViewById(R.id.tv10);        TV11 = findViewById(R.id.tv11);        TV12 = findViewById(R.id.tv12);        TV13 = findViewById(R.id.tv13);        TV14 = findViewById(R.id.tv14);        TV15 = findViewById(R.id.tv15);        TV16 = findViewById(R.id.tv16);

        TV0.setTextSize(15);
        TV0.setPadding(10, 0, 10, 0);
        TV0.setGravity(Gravity.LEFT);
        TV0.setTextColor(Color.WHITE);
        TV0.setBackground(getDrawable(R.drawable.rect_border));

        myTV.add(TV0);        myTV.add(TV1);        myTV.add(TV2);        myTV.add(TV3);        myTV.add(TV4);        myTV.add(TV5);        myTV.add(TV6);        myTV.add(TV7);        myTV.add(TV8);        myTV.add(TV9);        myTV.add(TV10);        myTV.add(TV11);        myTV.add(TV12);        myTV.add(TV13);        myTV.add(TV14);        myTV.add(TV15);        myTV.add(TV16);

        Intent intent = getIntent();
        Year = intent.getStringArrayListExtra("Unique Year");
        CName = intent.getStringExtra("Company Name");
        ItemCategory = intent.getStringArrayListExtra("Item Category Group");
        ItemCategoryDescrip = intent.getStringArrayListExtra("Item Category Description");

        back = findViewById(R.id.img1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Location();

        spinnerFilter = findViewById(R.id.spin_filter);
        spinnerFilter.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, amtFilter);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapter1);

        listItems = Year.toArray(new String[Year.size()]);
        checkedItems = new boolean[listItems.length];

        selectYear = findViewById(R.id.btn_selectYears);

        selectYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(InventryValueByLocation.this);
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

            }
        });

        final ArrayList<Integer> alreadySelectedItem = new ArrayList<>();

        ArrayList<MultiSelectModel> listOfItems = new ArrayList<>();

        for (int i = 0 ; i < ItemCategoryDescrip.size() ; i++)
        {
            listOfItems.add(new MultiSelectModel(i+1,ItemCategoryDescrip.get(i)));
        }

        final MultiSelectDialog multiSelectDialog = new MultiSelectDialog();

        multiSelectDialog.title("Select Items");
        multiSelectDialog.titleSize(24);
        multiSelectDialog.positiveText("Apply");
        multiSelectDialog.negativeText("Cancel");
        multiSelectDialog.clearText("Clear All");
        multiSelectDialog.preSelectIDsList(alreadySelectedItem);
        multiSelectDialog.multiSelectList(listOfItems);

        multiSelectDialog.onSubmit(new MultiSelectDialog.SubmitCallbackListener() {

            @Override
            public void onDismiss(ArrayList<Integer> ids, ArrayList<String> commonSeperatedData) throws PackageManager.NameNotFoundException {
                getItemCategory.addAll(commonSeperatedData);

                String txt = "";
                for (int z = 0 ; z < getItemCategory.size() ; z++)
                {
                    if (z == 0)
                    {
                        txt = getItemCategory.get(z);
                    }
                    else
                        txt = txt + "\n" + getItemCategory.get(z);
                }

                selectCategory.setText(txt);
            }

            @Override
            public void onCancel() {}

            @Override
            public void onClear() {}

        });

        selectCategory = findViewById(R.id.btn_selectItemCategory);

        selectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getItemCategory.clear();
                multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");

            }
        });

        final ArrayList<Integer> alreadySelectedItem2 = new ArrayList<>();

        ArrayList<MultiSelectModel> listOfItems2 = new ArrayList<>();

        for (int i = 0 ; i < LocationDescrip.size() ; i++)
        {
            listOfItems2.add(new MultiSelectModel(i+1,LocationDescrip.get(i)));
        }

        final MultiSelectDialog multiSelectDialog2 = new MultiSelectDialog();

        multiSelectDialog2.title("Select Items");
        multiSelectDialog2.titleSize(24);
        multiSelectDialog2.positiveText("Apply");
        multiSelectDialog2.negativeText("Cancel");
        multiSelectDialog2.clearText("Clear All");
        multiSelectDialog2.preSelectIDsList(alreadySelectedItem2);
        multiSelectDialog2.multiSelectList(listOfItems2);

        multiSelectDialog2.onSubmit(new MultiSelectDialog.SubmitCallbackListener() {

            @Override
            public void onDismiss(ArrayList<Integer> ids, ArrayList<String> commonSeperatedData) throws PackageManager.NameNotFoundException {
                getLocation.addAll(commonSeperatedData);

                String txt = "";
                for (int z = 0 ; z < getLocation.size() ; z++)
                {
                    if (z == 0)
                    {
                        txt = getLocation.get(z);
                    }
                    else
                        txt = txt + "\n" + getLocation.get(z);
                }

                selectLocation.setText(txt);
            }

            @Override
            public void onCancel() {}

            @Override
            public void onClear() {}

        });

        selectLocation = findViewById(R.id.btn_selectLocation);

        selectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SelectedLocations.clear();
                getLocation.clear();

                multiSelectDialog2.show(getSupportFragmentManager(), "multiSelectDialog2");

            }
        });

        btnChart = findViewById(R.id.btn_InventoryValueByLocation);

        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent chartIntent = new Intent(InventryValueByLocation.this,InventryValueByLocationChart.class);
                chartIntent.putExtra("Cost Amount",CostAmtToGraph);
                chartIntent.putExtra("Quater Year",QuaterYearToGraph);
                chartIntent.putExtra("Selected Location",SelectedLocations);
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
                return null;
            }
        }.execute();
    }

    public void doInBackground1() {
        refresh = findViewById(R.id.iv_refresh);

        refresh.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);

                while(TL1.getChildCount() > 1)
                {
                    TL1.removeView(TL1.getChildAt(TL1.getChildCount() - 1));
                }

                NewYear = "";
                NewQuatYear = 0;

                SelectedLocations.clear();
                CostAmtToGraph.clear();

                Cities = "";
                for (int i = 0; i < UserItems.size() ; i++)
                {
                    if (i == 0)
                    {
                        Cities = " '" + listItems[UserItems.get(i)] + "'";
                    }
                    else
                        Cities = Cities + ", '" + listItems[UserItems.get(i)] + "'";
                }

                Cities2 = "";
                for (int i = 0; i < getItemCategory.size() ; i++)
                {
                    for (int j = 0 ; j < ItemCategory.size() ; j++)
                    {
                        if (ItemCategoryDescrip.get(j).equals(getItemCategory.get(i)))
                        {
                            if (i == 0)
                            {
                                Cities2 = " '" + ItemCategory.get(j) + "'";
                            }
                            else
                                Cities2 = Cities2 + ", '" + ItemCategory.get(j) + "'";
                            break;
                        }
                    }
                }

                Cities3 = "";
                for (int i = 0; i < getLocation.size() ; i++)
                {
                    for (int j = 0 ; j < LocationCode.size() ; j++)
                    {
                        if (LocationDescrip.get(j).equals(getLocation.get(i)))
                        {
                            SelectedLocations.add(LocationCode.get(j));
                            CostAmtToGraph.add(new ArrayList<Float>());
                            if (i == 0)
                            {
                                Cities3 = " '" + LocationCode.get(j) + "'";
                            }
                            else
                                Cities3 = Cities3 + ", '" + LocationCode.get(j) + "'";
                            break;
                        }
                    }
                }

                Year1.clear();
                QuatYear.clear();
                QuatLocation.clear();
                CostAmt1.clear();
                Year2.clear();
                YearLocation.clear();
                CostAmt2.clear();

                /*getLocation.clear();
                getItemCategory.clear();*/

                QuaterYearToGraph.clear();

                final ProgressDialog progressDialog = new ProgressDialog(InventryValueByLocation.this);

                new AsyncTask<Void, Void, Void>()
                {
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

                refresh.startAnimation(rotation);
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
            String query = "select DATEPART(YYYY, [Posting Date]) as Yr, [Location Code], SUM([Cost Amount (Actual)]) as Amt FROM [dbo].["+CName+"$Inventory Staging] where DATEPART(YYYY, [Posting Date]) in ("+Cities+") and [Location Code] in ("+Cities3+") and [Item Category Code] in ("+Cities2+") group by DATEPART(YYYY, [Posting Date]), [Location Code] order by DATEPART(YYYY, [Posting Date])";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {

                    String Year = rs.getString("Yr");
                    Year1.add(String.valueOf(Year));

                    String a = rs.getString("Location Code");
                    YearLocation.add(a);

                    double Amount = rs.getDouble("Amt");
                    Amount = Math.abs(Amount);
                    CostAmt1.add(Amount);
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
            String query = "select DATEPART(YYYY, [Posting Date]) as Yr, DATEPART(QUARTER, [Posting Date]) as Quat, [Location Code], SUM([Cost Amount (Actual)]) as Amt FROM [dbo].["+CName+"$Inventory Staging] where DATEPART(YYYY, [Posting Date]) in ("+Cities+") and [Location Code] in ("+Cities3+") and [Item Category Code] in ("+Cities2+") group by DATEPART(YYYY, [Posting Date]), DATEPART(QUARTER, [Posting Date]), [Location Code] order by DATEPART(YYYY, [Posting Date]), DATEPART(QUARTER, [Posting Date])";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {

                    String a = rs.getString("Yr");
                    Year2.add(a);

                    int b = rs.getInt("Quat");
                    QuatYear.add(b);

                    String c = rs.getString("Location Code");
                    QuatLocation.add(c);

                    double Amount = rs.getDouble("Amt");
                    Amount = Math.abs(Amount);
                    CostAmt2.add(Amount);
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

    @SuppressLint({"RtlHardcoded", "SetTextI18n"})
    public void PrintTable() {

        Collections.sort(SelectedLocations);

        TV0.setText("Location →\nYear ↓");
        TV0.setTextSize(15);
        TV0.setPadding(10, 0, 10, 10);
        TV0.setGravity(Gravity.LEFT);
        TV0.setTextColor(Color.WHITE);
        TV0.setBackground(getDrawable(R.drawable.rect_border));

        double GrandTotalValue = 0;

        for (int c = 1 ; c < myTV.size() ; c++)
        {
            myTV.get(c).setText("");
            myTV.get(c).setBackground(getDrawable(android.R.color.transparent));
        }

        for (int i = 0 ; i < SelectedLocations.size() ; i++)
        {
            myTV.get(i+1).setText(SelectedLocations.get(i) + "\n");

            myTV.get(i+1).setTextSize(15);
            myTV.get(i+1).setPadding(10, 0, 10, 10);
            myTV.get(i+1).setGravity(Gravity.LEFT);
            myTV.get(i+1).setTextColor(Color.WHITE);
            myTV.get(i+1).setBackground(getDrawable(R.drawable.rect_border));
        }

        for (int p = SelectedLocations.size()+1 ; p < SelectedLocations.size()+2 ; p++)
        {
            myTV.get(p).setText("Grand\nTotal");

            myTV.get(p).setTextSize(15);
            myTV.get(p).setPadding(10, 0, 10, 10);
            myTV.get(p).setGravity(Gravity.LEFT);
            myTV.get(p).setTextColor(Color.WHITE);
            myTV.get(p).setBackground(getDrawable(R.drawable.rect_border));
        }

        for (int j = 0 ; j < Year1.size() ; j++)
        {
            if (NewYear.equals(Year1.get(j)))
            {
                GrandTotalValue = GrandTotalValue + CostAmt1.get(j);

                TV0.setText(Year1.get(j));

                for (int k = 0 ; k < SelectedLocations.size() ; k++)
                {
                    if (SelectedLocations.get(k).equals(YearLocation.get(j)))
                    {
                        myTV1.get(k+1).setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((CostAmt1.get(j) / intVal[index]) * 100.0) / 100.0));
                    }
                }

                for (int p = SelectedLocations.size()+1 ; p < SelectedLocations.size()+2 ; p++)
                {
                    myTV1.get(p).setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((GrandTotalValue / intVal[index]) * 100.0) / 100.0));
                }
            }
            else
            {
                for (int x = 0 ; x < Year2.size() ; x++)
                {
                    if (NewYear.equals(Year2.get(x)))
                    {
                        if (NewQuatYear == QuatYear.get(x))
                        {
                            GrandTotalValue = GrandTotalValue + CostAmt2.get(x);

                            TV0.setText(NewYear + " - " + QuaterYear[QuatYear.get(x)]);

                            for (int k = 0 ; k < SelectedLocations.size() ; k++)
                            {
                                if (SelectedLocations.get(k).equals(QuatLocation.get(x)))
                                {
                                    myTV1.get(k+1).setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((CostAmt2.get(x) / intVal[index]) * 100.0) / 100.0));
                                    CostAmtToGraph.get(k).remove(CostAmtToGraph.get(k).size() - 1);
                                    CostAmtToGraph.get(k).add(Float.parseFloat(String.valueOf(CostAmt2.get(x) / intVal[index])));
                                }
                            }

                            for (int p = SelectedLocations.size()+1 ; p < SelectedLocations.size()+2 ; p++)
                            {
                                myTV1.get(p).setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((GrandTotalValue / intVal[index]) * 100.0) / 100.0));
                            }
                        }
                        else
                        {
                            TR1 = new TableRow(this);
                            TV0 = new TextView(this);                TV1 = new TextView(this);                TV2 = new TextView(this);                TV3 = new TextView(this);                TV4 = new TextView(this);                TV5 = new TextView(this);                TV6 = new TextView(this);                TV7 = new TextView(this);                TV8 = new TextView(this);                TV9 = new TextView(this);                TV10 = new TextView(this);                TV11 = new TextView(this);                TV12 = new TextView(this);                TV13 = new TextView(this);                TV14 = new TextView(this);                TV15 = new TextView(this);                TV16 = new TextView(this);
                            TR1.addView(TV0);                TR1.addView(TV1);                TR1.addView(TV2);                TR1.addView(TV3);                TR1.addView(TV4);                TR1.addView(TV5);                TR1.addView(TV6);                TR1.addView(TV7);                TR1.addView(TV8);                TR1.addView(TV9);                TR1.addView(TV10);                TR1.addView(TV11);                TR1.addView(TV12);                TR1.addView(TV13);                TR1.addView(TV14);                TR1.addView(TV15);                TR1.addView(TV16);
                            TL1.addView(TR1);
                            myTV1.clear();
                            myTV1.add(TV0);                myTV1.add(TV1);                myTV1.add(TV2);                myTV1.add(TV3);                myTV1.add(TV4);                myTV1.add(TV5);                myTV1.add(TV6);                myTV1.add(TV7);                myTV1.add(TV8);                myTV1.add(TV9);                myTV1.add(TV10);                myTV1.add(TV11);                myTV1.add(TV12);                myTV1.add(TV13);                myTV1.add(TV14);                myTV1.add(TV15);                myTV1.add(TV16);

                            TV0.setText(NewYear + " - " + QuaterYear[QuatYear.get(x)]);
                            TV0.setTextSize(15);
                            TV0.setPadding(60, 0, 10, 0);
                            TV0.setGravity(Gravity.LEFT);
                            TV0.setTextColor(Color.WHITE);
                            TV0.setBackground(getDrawable(R.drawable.rect_border));

                            QuaterYearToGraph.add(TV0.getText().toString());

                            for (int z = 0 ; z < SelectedLocations.size() + 1 ; z++)
                            {
                                myTV1.get(z + 1).setText("0");

                                myTV1.get(z + 1).setTextSize(15);
                                myTV1.get(z + 1).setPadding(10, 0, 10, 0);
                                myTV1.get(z + 1).setGravity(Gravity.RIGHT);
                                myTV1.get(z + 1).setTextColor(Color.WHITE);
                                myTV1.get(z + 1).setBackground(getDrawable(R.drawable.rect_border));
                            }

                            for (int k = 0 ; k < SelectedLocations.size() ; k++)
                            {
                                CostAmtToGraph.get(k).add(Float.parseFloat(String.valueOf(CostAmt2.get(x) / intVal[index])));

                                if (SelectedLocations.get(k).equals(QuatLocation.get(x)))
                                {
                                    myTV1.get(k+1).setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((CostAmt2.get(x) / intVal[index]) * 100.0) / 100.0));
                                    CostAmtToGraph.get(k).remove(CostAmtToGraph.get(k).size() - 1);
                                    CostAmtToGraph.get(k).add(Float.parseFloat(String.valueOf(CostAmt2.get(x) / intVal[index])));
                                }
                            }

                            GrandTotalValue = 0;
                            GrandTotalValue = GrandTotalValue + CostAmt2.get(x);
                            for (int p = SelectedLocations.size()+1 ; p < SelectedLocations.size()+2 ; p++)
                            {
                                myTV1.get(p).setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((GrandTotalValue / intVal[index]) * 100.0) / 100.0));
                            }
                        }
                        NewQuatYear = QuatYear.get(x);
                    }
                }

                TR1 = new TableRow(this);
                TV0 = new TextView(this);                TV1 = new TextView(this);                TV2 = new TextView(this);                TV3 = new TextView(this);                TV4 = new TextView(this);                TV5 = new TextView(this);                TV6 = new TextView(this);                TV7 = new TextView(this);                TV8 = new TextView(this);                TV9 = new TextView(this);                TV10 = new TextView(this);                TV11 = new TextView(this);                TV12 = new TextView(this);                TV13 = new TextView(this);                TV14 = new TextView(this);                TV15 = new TextView(this);                TV16 = new TextView(this);
                TR1.addView(TV0);                TR1.addView(TV1);                TR1.addView(TV2);                TR1.addView(TV3);                TR1.addView(TV4);                TR1.addView(TV5);                TR1.addView(TV6);                TR1.addView(TV7);                TR1.addView(TV8);                TR1.addView(TV9);                TR1.addView(TV10);                TR1.addView(TV11);                TR1.addView(TV12);                TR1.addView(TV13);                TR1.addView(TV14);                TR1.addView(TV15);                TR1.addView(TV16);
                TL1.addView(TR1);
                myTV1.clear();
                myTV1.add(TV0);                myTV1.add(TV1);                myTV1.add(TV2);                myTV1.add(TV3);                myTV1.add(TV4);                myTV1.add(TV5);                myTV1.add(TV6);                myTV1.add(TV7);                myTV1.add(TV8);                myTV1.add(TV9);                myTV1.add(TV10);                myTV1.add(TV11);                myTV1.add(TV12);                myTV1.add(TV13);                myTV1.add(TV14);                myTV1.add(TV15);                myTV1.add(TV16);

                TV0.setText(Year1.get(j));
                TV0.setTextSize(15);
                TV0.setPadding(10, 0, 10, 0);
                TV0.setGravity(Gravity.LEFT);
                TV0.setTextColor(Color.WHITE);
                TV0.setBackground(getDrawable(R.drawable.rect_border));

                for (int z = 0 ; z < SelectedLocations.size() + 1 ; z++)
                {
                    myTV1.get(z + 1).setText("0");

                    myTV1.get(z + 1).setTextSize(15);
                    myTV1.get(z + 1).setPadding(10, 0, 10, 0);
                    myTV1.get(z + 1).setGravity(Gravity.RIGHT);
                    myTV1.get(z + 1).setTextColor(Color.WHITE);
                    myTV1.get(z + 1).setBackground(getDrawable(R.drawable.rect_border));
                }

                for (int k = 0 ; k < SelectedLocations.size() ; k++)
                {
//                    CostAmtToGraph.get(k).add(Float.parseFloat(myTV1.get(k+1).getText().toString()));

                    if (SelectedLocations.get(k).equals(YearLocation.get(j)))
                    {
                        myTV1.get(k+1).setText(NumberFormat.getNumberInstance(Locale.US).format(CostAmt1.get(j) / intVal[index]));
//                        CostAmtToGraph.get(k).remove(CostAmtToGraph.get(k).size() - 1);
//                        CostAmtToGraph.get(k).add(Float.parseFloat(myTV1.get(k+1).getText().toString()));
                    }
                }

                GrandTotalValue = 0;
                GrandTotalValue = GrandTotalValue + CostAmt1.get(j);
                for (int p = SelectedLocations.size()+1 ; p < SelectedLocations.size()+2 ; p++)
                {
                    myTV1.get(p).setText(NumberFormat.getNumberInstance(Locale.US).format(GrandTotalValue / intVal[index]));
                }
            }
            NewYear = Year1.get(j);

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            if (j == Year1.size() - 1)
            {
                for (int x = 0 ; x < Year2.size() ; x++)
                {
                    if (NewYear.equals(Year2.get(x)))
                    {
                        if (NewQuatYear == QuatYear.get(x))
                        {
                            GrandTotalValue = GrandTotalValue + CostAmt2.get(x);

                            TV0.setText(NewYear + " - " + QuaterYear[QuatYear.get(x)]);

                            for (int k = 0 ; k < SelectedLocations.size() ; k++)
                            {
                                if (SelectedLocations.get(k).equals(QuatLocation.get(x)))
                                {
                                    myTV1.get(k+1).setText(NumberFormat.getNumberInstance(Locale.US).format(CostAmt2.get(x) / intVal[index]));
                                    CostAmtToGraph.get(k).remove(CostAmtToGraph.get(k).size() - 1);
                                    CostAmtToGraph.get(k).add(Float.parseFloat(String.valueOf(CostAmt2.get(x) / intVal[index])));
                                }
                            }

                            for (int p = SelectedLocations.size()+1 ; p < SelectedLocations.size()+2 ; p++)
                            {
                                myTV1.get(p).setText(NumberFormat.getNumberInstance(Locale.US).format(GrandTotalValue / intVal[index]));
                            }
                        }
                        else
                        {
                            TR1 = new TableRow(this);
                            TV0 = new TextView(this);                TV1 = new TextView(this);                TV2 = new TextView(this);                TV3 = new TextView(this);                TV4 = new TextView(this);                TV5 = new TextView(this);                TV6 = new TextView(this);                TV7 = new TextView(this);                TV8 = new TextView(this);                TV9 = new TextView(this);                TV10 = new TextView(this);                TV11 = new TextView(this);                TV12 = new TextView(this);                TV13 = new TextView(this);                TV14 = new TextView(this);                TV15 = new TextView(this);                TV16 = new TextView(this);
                            TR1.addView(TV0);                TR1.addView(TV1);                TR1.addView(TV2);                TR1.addView(TV3);                TR1.addView(TV4);                TR1.addView(TV5);                TR1.addView(TV6);                TR1.addView(TV7);                TR1.addView(TV8);                TR1.addView(TV9);                TR1.addView(TV10);                TR1.addView(TV11);                TR1.addView(TV12);                TR1.addView(TV13);                TR1.addView(TV14);                TR1.addView(TV15);                TR1.addView(TV16);
                            TL1.addView(TR1);
                            myTV1.clear();
                            myTV1.add(TV0);                myTV1.add(TV1);                myTV1.add(TV2);                myTV1.add(TV3);                myTV1.add(TV4);                myTV1.add(TV5);                myTV1.add(TV6);                myTV1.add(TV7);                myTV1.add(TV8);                myTV1.add(TV9);                myTV1.add(TV10);                myTV1.add(TV11);                myTV1.add(TV12);                myTV1.add(TV13);                myTV1.add(TV14);                myTV1.add(TV15);                myTV1.add(TV16);

                            TV0.setText(NewYear + " - " + QuaterYear[QuatYear.get(x)]);
                            TV0.setTextSize(15);
                            TV0.setPadding(60, 0, 10, 0);
                            TV0.setGravity(Gravity.LEFT);
                            TV0.setTextColor(Color.WHITE);
                            TV0.setBackground(getDrawable(R.drawable.rect_border));

                            QuaterYearToGraph.add(TV0.getText().toString());

                            for (int z = 0 ; z < SelectedLocations.size() + 1 ; z++)
                            {
                                myTV1.get(z + 1).setText("0");

                                myTV1.get(z + 1).setTextSize(15);
                                myTV1.get(z + 1).setPadding(10, 0, 10, 0);
                                myTV1.get(z + 1).setGravity(Gravity.RIGHT);
                                myTV1.get(z + 1).setTextColor(Color.WHITE);
                                myTV1.get(z + 1).setBackground(getDrawable(R.drawable.rect_border));
                            }

                            for (int k = 0 ; k < SelectedLocations.size() ; k++)
                            {
                                CostAmtToGraph.get(k).add(Float.parseFloat(String.valueOf(CostAmt2.get(x) / intVal[index])));

                                if (SelectedLocations.get(k).equals(QuatLocation.get(x)))
                                {
                                    myTV1.get(k+1).setText(NumberFormat.getNumberInstance(Locale.US).format(CostAmt2.get(x) / intVal[index]));

                                    CostAmtToGraph.get(k).remove(CostAmtToGraph.get(k).size() - 1);
                                    CostAmtToGraph.get(k).add(Float.parseFloat(String.valueOf(CostAmt2.get(x) / intVal[index])));
                                }
                            }

                            GrandTotalValue = 0;
                            GrandTotalValue = GrandTotalValue + CostAmt2.get(x);
                            for (int p = SelectedLocations.size()+1 ; p < SelectedLocations.size()+2 ; p++)
                            {
                                myTV1.get(p).setText(NumberFormat.getNumberInstance(Locale.US).format(GrandTotalValue / intVal[index]));
                            }
                        }
                        NewQuatYear = QuatYear.get(x);
                    }
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner) adapterView;

        if (spinner.getId() == R.id.spin_filter)
        {
            index = i;

            dropDownValue = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(adapterView.getContext(), "Sorted by: " + dropDownValue, Toast.LENGTH_SHORT).show();

            while(TL1.getChildCount() > 1)
            {
                TL1.removeView(TL1.getChildAt(TL1.getChildCount() - 1));
            }

            NewYear = "";
            NewQuatYear = 0;

            for (int x = 0 ; x < CostAmtToGraph.size() ; x++)
            {
                CostAmtToGraph.get(x).clear();
            }
            QuaterYearToGraph.clear();

            PrintTable();
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    public void Location() {
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
            String query = "select Code, Name FROM [dbo].["+CName+"$Location]";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                LocationCode.add("");
                LocationDescrip.add("Blank");

                while (rs.next()) {

                    String a = rs.getString("Code");
                    LocationCode.add(a);

                    String b = rs.getString("Name");
                    LocationDescrip.add(a + "-" + b);
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
}
