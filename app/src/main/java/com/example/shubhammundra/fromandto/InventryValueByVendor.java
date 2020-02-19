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
import java.util.ArrayList;
import java.util.Collections;

public class InventryValueByVendor extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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

    String CName;

    ArrayList<String> Year = new ArrayList<>();
    ArrayList<String> ItemCategory = new ArrayList<>();
    ArrayList<String> ItemCategoryDescrip = new ArrayList<>();

    Button selectYear, selectCategory;

    String Cities = "''",Cities2 = "''";

    GradientDrawable gd;

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal = {1,1000,100000,1000000,10000000};

    String dropDownValue;

    int index;

    @SuppressLint("StaticFieldLeak")
    public static Spinner spinnerFilter;

    ArrayList<String> Year1 = new ArrayList<>();
    ArrayList<String> VendorName = new ArrayList<>();
    ArrayList<Double> CostAmt = new ArrayList<>();

    ArrayList<String> SelectedYears = new ArrayList<>();

    String NewVendor = "";

    ArrayList<String> VendorNameToGraph = new ArrayList<>();
    ArrayList<ArrayList<Float>> CostAmtToGraph = new ArrayList<>();

    Button btnChart;

    ArrayList<String> getItemCategory = new ArrayList<>();

    @SuppressLint({"RtlHardcoded", "StaticFieldLeak"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventry_value_by_vendor);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TL1 = findViewById(R.id.tl_myTable);

        TV0 = findViewById(R.id.tv0);        TV1 = findViewById(R.id.tv1);        TV2 = findViewById(R.id.tv2);        TV3 = findViewById(R.id.tv3);        TV4 = findViewById(R.id.tv4);        TV5 = findViewById(R.id.tv5);        TV6 = findViewById(R.id.tv6);        TV7 = findViewById(R.id.tv7);        TV8 = findViewById(R.id.tv8);        TV9 = findViewById(R.id.tv9);        TV10 = findViewById(R.id.tv10);        TV11 = findViewById(R.id.tv11);        TV12 = findViewById(R.id.tv12);        TV13 = findViewById(R.id.tv13);        TV14 = findViewById(R.id.tv14);        TV15 = findViewById(R.id.tv15);        TV16 = findViewById(R.id.tv16);

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

                AlertDialog.Builder builder = new AlertDialog.Builder(InventryValueByVendor.this);
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
                            Store = Store + ",'" + listItems[UserItems.get(i)] + "'";
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

        btnChart = findViewById(R.id.btn_InventoryValueByVendor);

        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chartIntent = new Intent(InventryValueByVendor.this,InventryValueByVendorChart.class);
                chartIntent.putExtra("Cost Amount",CostAmtToGraph);
                chartIntent.putExtra("Vendor Name",VendorNameToGraph);
                chartIntent.putExtra("Selected Years",SelectedYears);
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

                SelectedYears.clear();
                CostAmtToGraph.clear();

                Cities = "''";
                for (int i = 0; i < UserItems.size() ; i++)
                {
                    SelectedYears.add(listItems[UserItems.get(i)]);
                    CostAmtToGraph.add(new ArrayList<Float>());
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

                Year1.clear();
                CostAmt.clear();
                VendorName.clear();

                VendorNameToGraph.clear();

                final ProgressDialog progressDialog = new ProgressDialog(InventryValueByVendor.this);

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
            String query = "select DATEPART(YYYY, [Posting Date]) as Yr, Name, SUM([Cost Amount (Actual)]) as Amt FROM [dbo].["+CName+"$Inventory Staging] as InvenStg, [dbo].["+CName+"$Vendor] as vendorTab where InvenStg.[Source No_] = vendorTab.No_ and DATEPART(YYYY, [Posting Date]) in ("+Cities+") and [Item Category Code] in ("+Cities2+") and [Entry Type] = '0' group by DATEPART(YYYY, [Posting Date]), Name order by Name, DATEPART(YYYY, [Posting Date])";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {

                    String Year = rs.getString("Yr");
                    Year1.add(String.valueOf(Year));

                    String a = rs.getString("Name");
                    VendorName.add(a);

                    double Amount = rs.getLong("Amt");
                    Amount = Math.abs(Amount);
                    CostAmt.add(Amount);
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

        Collections.sort(SelectedYears);

        TV0.setText("Year →\nVendor Name ↓");
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

        for (int i = 0 ; i < SelectedYears.size() ; i++)
        {
            myTV.get(i+1).setText(SelectedYears.get(i) + "\n");

            myTV.get(i+1).setTextSize(15);
            myTV.get(i+1).setPadding(10, 0, 10, 10);
            myTV.get(i+1).setGravity(Gravity.LEFT);
            myTV.get(i+1).setTextColor(Color.WHITE);
            myTV.get(i+1).setBackground(getDrawable(R.drawable.rect_border));
        }

        for (int p = SelectedYears.size()+1 ; p < SelectedYears.size()+2 ; p++)
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

            if (NewVendor.equals(VendorName.get(j)))
            {
                GrandTotalValue = GrandTotalValue + CostAmt.get(j);

                TV0.setText(VendorName.get(j));

                for (int k = 0 ; k < SelectedYears.size() ; k++)
                {
                    if (SelectedYears.get(k).equals(Year1.get(j)))
                    {
                        myTV1.get(k+1).setText(String.valueOf(CostAmt.get(j) / intVal[index]));
                        CostAmtToGraph.get(k).remove(CostAmtToGraph.get(k).size() - 1);
                        CostAmtToGraph.get(k).add(Float.parseFloat(String.valueOf(CostAmt.get(j) / intVal[index])));
                    }
                }

                for (int p = SelectedYears.size()+1 ; p < SelectedYears.size()+2 ; p++)
                {
                    myTV1.get(p).setText(String.valueOf(GrandTotalValue / intVal[index]));
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

                TV0.setText(VendorName.get(j));
                TV0.setTextSize(15);
                TV0.setPadding(10, 0, 10, 0);
                TV0.setGravity(Gravity.LEFT);
                TV0.setTextColor(Color.WHITE);
                TV0.setBackground(getDrawable(R.drawable.rect_border));

                VendorNameToGraph.add(TV0.getText().toString());

                for (int z = 0 ; z < SelectedYears.size() + 1 ; z++)
                {
                    myTV1.get(z + 1).setText("0");

                    myTV1.get(z + 1).setTextSize(15);
                    myTV1.get(z + 1).setPadding(10, 0, 10, 0);
                    myTV1.get(z + 1).setGravity(Gravity.RIGHT);
                    myTV1.get(z + 1).setTextColor(Color.WHITE);
                    myTV1.get(z + 1).setBackground(getDrawable(R.drawable.rect_border));
                }

                for (int k = 0 ; k < SelectedYears.size() ; k++)
                {
                    CostAmtToGraph.get(k).add(Float.parseFloat(String.valueOf(CostAmt.get(j) / intVal[index])));

                    if (SelectedYears.get(k).equals(Year1.get(j)))
                    {
                        myTV1.get(k+1).setText(String.valueOf(CostAmt.get(j) / intVal[index]));
                        CostAmtToGraph.get(k).remove(CostAmtToGraph.get(k).size() - 1);
                        CostAmtToGraph.get(k).add(Float.parseFloat(String.valueOf(CostAmt.get(j) / intVal[index])));
                    }
                }

                GrandTotalValue = 0;
                GrandTotalValue = GrandTotalValue + CostAmt.get(j);
                for (int p = SelectedYears.size()+1 ; p < SelectedYears.size()+2 ; p++)
                {
                    myTV1.get(p).setText(String.valueOf(GrandTotalValue / intVal[index]));
                }

            }

            NewVendor = VendorName.get(j);
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

            VendorNameToGraph.clear();

            for (int z = 0 ; z < CostAmtToGraph.size() ; z++)
            {
                CostAmtToGraph.get(z).clear();
            }


            PrintTable();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
