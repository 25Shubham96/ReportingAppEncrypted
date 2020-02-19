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
import java.util.Locale;

public class InventryOverview extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TableLayout TL1;
    TableRow TR1;
    TextView TV1,TV2,TV3;

    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    ImageView refresh;

    ImageView back;

    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> UserItems = new ArrayList<>();

    String[] listItems3;
    boolean[] checkedItems3;
    ArrayList<Integer> UserItems3 = new ArrayList<>();

    String CName;

    ArrayList<String> Year = new ArrayList<>();
    ArrayList<String> ItemCategory = new ArrayList<>();
    ArrayList<String> ItemCategoryDescrip = new ArrayList<>();

    String Cities = "''", Cities3 = "''";

    Button selectYear, selectCategory;

    GradientDrawable gd;

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal = {1,1000,100000,1000000,10000000};

    String dropDownValue;

    int index;

    @SuppressLint("StaticFieldLeak")
    public static Spinner spinnerFilter;

    Button InvenOverviewChart;

    ArrayList<Integer> QuaterYear = new ArrayList<>();
    ArrayList<String> Year123 = new ArrayList<>();
    ArrayList<Float> RotationDays = new ArrayList<>();
    ArrayList<Double> CostAmt = new ArrayList<>();

    ArrayList<String> YearQuat = new ArrayList<>();

    ArrayList<Integer> QuaterYear2 = new ArrayList<>();
    ArrayList<String> Year1232 = new ArrayList<>();
    ArrayList<Double> CostAmt2 = new ArrayList<>();

    ArrayList<Integer> QuaterYear3 = new ArrayList<>();
    ArrayList<String> Year1233 = new ArrayList<>();
    ArrayList<Double> FirstValue = new ArrayList<>();
    ArrayList<Double> LastValue = new ArrayList<>();

    ArrayList<Float> CostAmtToGraph = new ArrayList<>();

    ArrayList<String> getItemCategory = new ArrayList<>();

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventry_overview);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TL1 = findViewById(R.id.tl_myTable);

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

        spinnerFilter = findViewById(R.id.spin_filter);
        spinnerFilter.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, amtFilter);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapter1);

        listItems = Year.toArray(new String[Year.size()]);
        checkedItems = new boolean[listItems.length];

        selectYear = findViewById(R.id.btn_selectYear);

        selectYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(InventryOverview.this);
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

        InvenOverviewChart = findViewById(R.id.btn_InventoryOverviewChart);

        InvenOverviewChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent chartIntent = new Intent(InventryOverview.this,InventryOverviewChart.class);
                chartIntent.putExtra("Year Quat",YearQuat);
                chartIntent.putExtra("Cost Amount",CostAmtToGraph);
                chartIntent.putExtra("Rotation Days",RotationDays);
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

                Cities3 = "";
                for (int i = 0; i < getItemCategory.size() ; i++)
                {
                    for (int j = 0 ; j < ItemCategory.size() ; j++)
                    {
                        if (ItemCategoryDescrip.get(j).equals(getItemCategory.get(i)))
                        {
                            if (i == 0)
                            {
                                Cities3 = " '" + ItemCategory.get(j) + "'";
                            }
                            else
                                Cities3 = Cities3 + ", '" + ItemCategory.get(j) + "'";
                            break;
                        }
                    }
                }

                Year123.clear();
                QuaterYear.clear();
                CostAmt.clear();
                RotationDays.clear();

                Year1232.clear();
                QuaterYear2.clear();
                CostAmt2.clear();

                Year1233.clear();
                QuaterYear3.clear();
                FirstValue.clear();
                LastValue.clear();

                YearQuat.clear();

                CostAmtToGraph.clear();

                final ProgressDialog progressDialog = new ProgressDialog(InventryOverview.this);

                new AsyncTask<Void, Void, Void>()
                {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog.setMessage("Collecting Data");
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
            String query = "Select DATEPART(YYYY, [Posting Date]) as Yr, DATEPART(QUARTER, [Posting Date]) as Quat, sum([Cost Amount (Actual)]) as CostAmt FROM [dbo].["+CName+"$Inventory Staging] where DATEPART(YYYY, [Posting Date]) in ("+Cities+") and [Item Category Code] in ("+Cities3+") group by DATEPART(YYYY, [Posting Date]), DATEPART(QUARTER, [Posting Date]) order by DATEPART(YYYY, [Posting Date]), DATEPART(QUARTER, [Posting Date])";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {

                    String a = rs.getString("Yr");
                    Year123.add(String.valueOf(a));

                    Integer b = rs.getInt("Quat");
                    QuaterYear.add(b);

                    YearQuat.add(a + " - " + b);

                    double c = rs.getDouble("CostAmt");
                    c = Math.abs(c);
                    CostAmt.add(c);
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
            String query = "Select DATEPART(YYYY, [Posting Date]) as Yr, DATEPART(QUARTER, [Posting Date]) as Quat, sum([Cost Amount (Actual)]) as CostAmt FROM [dbo].["+CName+"$Inventory Staging] where [Entry Type] = '1' and DATEPART(YYYY, [Posting Date]) in ("+Cities+") and [Item Category Code] in ("+Cities3+") group by DATEPART(YYYY, [Posting Date]), DATEPART(QUARTER, [Posting Date]) order by DATEPART(YYYY, [Posting Date]), DATEPART(QUARTER, [Posting Date])";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {

                    String a = rs.getString("Yr");
                    Year1232.add(String.valueOf(a));

                    Integer b = rs.getInt("Quat");
                    QuaterYear2.add(b);

                    double c = rs.getDouble("CostAmt");
                    c = Math.abs(c);
                    CostAmt2.add(c);
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
    public void doInBackground3() {
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
            String query = "Select DATEPART(YYYY, [Posting Date]) as Yr, [Posting Date], DATEPART(QUARTER, [Posting Date]) as Quat, [Cost Amount (Actual)], FIRST_VALUE([Cost Amount (Actual)]) over (Partition by DATEPART(YYYY, [Posting Date]), DATEPART(QUARTER, [Posting Date]) order by [Posting Date]) FirstVal, LAST_VALUE([Cost Amount (Actual)]) over (Partition By DATEPART(YYYY, [Posting Date]), DATEPART(QUARTER, [Posting Date]) order by DATEPART(YYYY, [Posting Date])) lastVal FROM [dbo].["+CName+"$Inventory Staging] where DATEPART(YYYY, [Posting Date]) in ("+Cities+") and [Item Category Code] in ("+Cities3+") order by DATEPART(YYYY, [Posting Date]), [Posting Date], DATEPART(QUARTER, [Posting Date])";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                String NewYear = "" ;
                int QuatYr = 0 ;

                while (rs.next()) {

                    String a = rs.getString("Yr");
                    Integer b = rs.getInt("Quat");
                    double c = rs.getLong("FirstVal");
                    c = Math.abs(c);
                    double d = rs.getLong("LastVal");
                    d = Math.abs(d);

                    if (!(NewYear.equals(a) && QuatYr == b))
                    {
                        Year1233.add(String.valueOf(a));
                        QuaterYear3.add(b);
                        FirstValue.add(c);
                        LastValue.add(d);
                    }

                    NewYear = a;
                    QuatYr = b;

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

        for (int i = 0 ; i < Year123.size() ; i++)
        {
            TL1.setStretchAllColumns(true);

            TR1 = new TableRow(this);

            TV1 = new TextView(this);
            TV2 = new TextView(this);
            TV3 = new TextView(this);

            TV1.setText(Year123.get(i) + " - " + QuaterYear.get(i));
            TV2.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((CostAmt.get(i) / intVal[index]) * 100.0) / 100.0));

            CostAmtToGraph.add(Float.parseFloat(String.valueOf(Math.round((CostAmt.get(i) / intVal[index]) * 100.0) / 100.0)));

            for (int j = 0 ; j < Year1232.size() ; j++)
            {
                for (int k = 0 ; k < Year1233.size() ; k++ )
                {
                    if (Year123.get(i).equals(Year1232.get(j)) && QuaterYear.get(i) == QuaterYear2.get(j) && Year1232.get(j).equals(Year1233.get(k)) && QuaterYear2.get(j) == QuaterYear3.get(k))
                    {
                        double COGS = CostAmt2.get(j);

                        double AvgInven = (FirstValue.get(k) + LastValue.get(k)) / 2;

                        Double Value;

                        if (AvgInven != 0 && COGS != 0 )
                        {
                            Value = COGS / AvgInven;
                            Value = 365 / Value;
                        }
                        else
                        {
                            Value = 0.0;
                        }

                        long RotDays = Value.longValue();

                        RotationDays.add(Float.parseFloat(String.valueOf(RotDays)));

                        TV3.setText(String.valueOf(RotDays));
                    }
                }
            }

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

            TR1.addView(TV1);
            TR1.addView(TV2);
            TR1.addView(TV3);

            TL1.addView(TR1);
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

            CostAmtToGraph.clear();
            RotationDays.clear();

            PrintTable();
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}
}
