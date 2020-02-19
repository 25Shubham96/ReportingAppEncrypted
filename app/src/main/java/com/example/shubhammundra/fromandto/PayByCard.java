package com.example.shubhammundra.fromandto;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import android.widget.ListView;
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

public class PayByCard extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TableLayout TL;
    TableRow TR;
    TextView TV,TV1,TV2,TV3,TV4,TV5,TV6,TV7,TV8,TV9,TV10;

    ArrayList<TextView> myTV = new ArrayList<>();
    ArrayList<TextView> myTV1 = new ArrayList<>();

    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    ImageView refresh, back;

    String[] listItems;

    ArrayList<String> Year;

    boolean[] checkedItems;

    public static String CName;

    Button selectYear;

    GradientDrawable gd;

    String Values = "''";

    String Years;

    ArrayList<Integer> UserItems = new ArrayList<>();

    ArrayList<Integer> year1 = new ArrayList<>();
    ArrayList<Integer> month1 = new ArrayList<>();
    ArrayList<Double> amount1 = new ArrayList<>();
    ArrayList<String> describe1 = new ArrayList<>();

    ArrayList<String> originalDescribe = new ArrayList<>();

    ListView listView;

    int yearCompare = 0, newyearCompare = 0, monthCompare = 0, newmonthCompare = 0;

    int value = 0;

    String[] getMonth = {"","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal= {1,1000,100000,1000000,10000000};

    String dropDownYear;

    int index;

    ArrayList<String> StoreDescrip;
    ArrayList<String> StoreNm = new ArrayList<>();

    Button selectStore;

    ArrayList<String> getStoreDescrip = new ArrayList<>();

    String Store1234 = "''";

    @SuppressLint({"RtlHardcoded", "StaticFieldLeak"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_by_card);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TL = findViewById(R.id.tl_myTable1);
        TV = findViewById(R.id.tv_yearmonth);        TV1 = findViewById(R.id.tv_1);        TV2 = findViewById(R.id.tv_2);        TV3 = findViewById(R.id.tv_3);        TV4 = findViewById(R.id.tv_4);        TV5 = findViewById(R.id.tv_5);        TV6 = findViewById(R.id.tv_6);        TV7 = findViewById(R.id.tv_7);        TV8 = findViewById(R.id.tv_8);        TV9 = findViewById(R.id.tv_9);        TV10 = findViewById(R.id.tv_10);

        TV.setTextSize(15);
        TV.setPadding(10, 0, 10, 0);
        TV.setGravity(Gravity.LEFT);
        TV.setTextColor(Color.WHITE);
        TV.setBackground(getDrawable(R.drawable.rect_border));

        myTV.add(TV);        myTV.add(TV1);        myTV.add(TV2);        myTV.add(TV3);        myTV.add(TV4);        myTV.add(TV5);        myTV.add(TV6);        myTV.add(TV7);        myTV.add(TV8);        myTV.add(TV9);        myTV.add(TV10);

        Spinner spinner = findViewById(R.id.spin_filter);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, amtFilter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        listView = new ListView(this);

        Intent intent = getIntent();
        CName = intent.getStringExtra("Company Name");
        Year = intent.getStringArrayListExtra("Unique Year");
        StoreNm = intent.getStringArrayListExtra("Store Name");
        StoreDescrip = intent.getStringArrayListExtra("Store Descrip");

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

                AlertDialog.Builder builder = new AlertDialog.Builder(PayByCard.this);
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

        final ProgressDialog progressDialog = new ProgressDialog(PayByCard.this);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setMessage("data loading please wait...");
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                PrintTable2();
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
                refresh.startAnimation(rotation);

                while (TL.getChildCount() > 1) {
                    TL.removeView(TL.getChildAt(TL.getChildCount() - 1));
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

                year1.clear();
                month1.clear();
                describe1.clear();
                amount1.clear();

                final ProgressDialog progressDialog = new ProgressDialog(PayByCard.this);

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
                        PrintTable2();
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

        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
                String query = "Select DATEPART(YYYY,TPE.Date) as Year, DATEPART(MM,TPE.Date) as month, TTCS.Description, sum(TPE.[Amount Tendered]) as Amount FROM [dbo].["+CName+"$Trans_ Payment Entry] as TPE, [dbo].["+CName+"$Tender Type Card Setup] as TTCS, [dbo].["+CName+"$Tender Type] as TT  where TT.[Store No_] = TPE.[Store No_] and TT.Code = TPE.[Tender Type] and TTCS.[Store No_] = TT.[Store No_] and TTCS.[Tender Type Code] = TT.Code and TTCS.[Card No_] = TPE.[Card No_] and TT.[Function] = '1' and DATEPART(YYYY,TPE.Date) in ("+Years+") and TPE.[Store No_] in ("+Store1234+") group by DATEPART(YYYY,TPE.Date), DATEPART(MM,TPE.Date), TTCS.Description order by DATEPART(YYYY,TPE.Date), DATEPART(MM,TPE.Date), TTCS.Description";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    int yearData = rs.getInt("Year");
                    int monthData = rs.getInt("month");
                    double amountData = rs.getLong("Amount");
                    String describeData = rs.getString("Description");

                    year1.add(yearData);
                    month1.add(monthData);
                    amount1.add(amountData);
                    describe1.add(describeData);
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void doInBackground2() {
        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query = "Select Distinct Description FROM [dbo].["+CName+"$Tender Type Card Setup]";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    String originalDes = rs.getString("Description");
                    originalDescribe.add(originalDes);
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
    public void PrintTable2() {
        for (int i = 0 ; i < originalDescribe.size() ; i++)
        {
            myTV.get(i+1).setText(originalDescribe.get(i) + "\n");
            myTV.get(i+1).setTextSize(15);
            myTV.get(i+1).setPadding(10, 0, 10, 0);
            myTV.get(i+1).setGravity(Gravity.LEFT);
            myTV.get(i+1).setTextColor(Color.WHITE);
            myTV.get(i+1).setBackground(getDrawable(R.drawable.rect_border));
        }

        double val1231 = 0;
        for (int j = 0 ; j < year1.size() ; j++)
        {
            newyearCompare = year1.get(j);
            newmonthCompare = month1.get(j);

            if (newyearCompare == yearCompare && newmonthCompare == monthCompare)
            {
                for (int k = 0 ; k < originalDescribe.size() ; k++) {
                    if (describe1.get(j) == originalDescribe.get(k)) {
                        value = k + 1;
                        switch (value) {
                            case 1: {
                                val1231 = Math.round((amount1.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV1.setText(NumberFormat.getNumberInstance(Locale.US).format(val1231));
                                TV1.setTextSize(15);
                                TV1.setPadding(10, 0, 10, 0);
                                TV1.setGravity(Gravity.RIGHT);
                                TV1.setTextColor(Color.WHITE);
                                TV1.setBackground(getDrawable(R.drawable.rect_border));
                                break;
                            }
                            case 2: {
                                val1231 = Math.round((amount1.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV2.setText(NumberFormat.getNumberInstance(Locale.US).format(val1231));
                                TV2.setTextSize(15);
                                TV2.setPadding(10, 0, 10, 0);
                                TV2.setGravity(Gravity.RIGHT);
                                TV2.setTextColor(Color.WHITE);
                                TV2.setBackground(getDrawable(R.drawable.rect_border));
                                break;
                            }
                            case 3: {
                                val1231 = Math.round((amount1.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV3.setText(NumberFormat.getNumberInstance(Locale.US).format(val1231));
                                TV3.setTextSize(15);
                                TV3.setPadding(10, 0, 10, 0);
                                TV3.setGravity(Gravity.RIGHT);
                                TV3.setTextColor(Color.WHITE);
                                TV3.setBackground(getDrawable(R.drawable.rect_border));
                                break;
                            }
                            case 4: {
                                val1231 = Math.round((amount1.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV4.setText(NumberFormat.getNumberInstance(Locale.US).format(val1231));
                                TV4.setTextSize(15);
                                TV4.setPadding(10, 0, 10, 0);
                                TV4.setGravity(Gravity.RIGHT);
                                TV4.setTextColor(Color.WHITE);
                                TV4.setBackground(getDrawable(R.drawable.rect_border));
                                break;
                            }
                            case 5: {
                                val1231 = Math.round((amount1.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV5.setText(NumberFormat.getNumberInstance(Locale.US).format(val1231));
                                TV5.setTextSize(15);
                                TV5.setPadding(10, 0, 10, 0);
                                TV5.setGravity(Gravity.RIGHT);
                                TV5.setTextColor(Color.WHITE);
                                TV5.setBackground(getDrawable(R.drawable.rect_border));
                                break;
                            }
                            case 6: {
                                val1231 = Math.round((amount1.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV6.setText(NumberFormat.getNumberInstance(Locale.US).format(val1231));
                                TV6.setTextSize(15);
                                TV6.setPadding(10, 0, 10, 0);
                                TV6.setGravity(Gravity.RIGHT);
                                TV6.setTextColor(Color.WHITE);
                                TV6.setBackground(getDrawable(R.drawable.rect_border));
                                break;
                            }
                            case 7: {
                                val1231 = Math.round((amount1.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV7.setText(NumberFormat.getNumberInstance(Locale.US).format(val1231));
                                TV7.setTextSize(15);
                                TV7.setPadding(10, 0, 10, 0);
                                TV7.setGravity(Gravity.RIGHT);
                                TV7.setTextColor(Color.WHITE);
                                TV7.setBackground(getDrawable(R.drawable.rect_border));
                                break;
                            }
                            case 8: {
                                val1231 = Math.round((amount1.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV8.setText(NumberFormat.getNumberInstance(Locale.US).format(val1231));
                                TV8.setTextSize(15);
                                TV8.setPadding(10, 0, 10, 0);
                                TV8.setGravity(Gravity.RIGHT);
                                TV8.setTextColor(Color.WHITE);
                                TV8.setBackground(getDrawable(R.drawable.rect_border));
                                break;
                            }
                            case 9: {
                                val1231 = Math.round((amount1.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV9.setText(NumberFormat.getNumberInstance(Locale.US).format(val1231));
                                TV9.setTextSize(15);
                                TV9.setPadding(10, 0, 10, 0);
                                TV9.setGravity(Gravity.RIGHT);
                                TV9.setTextColor(Color.WHITE);
                                TV9.setBackground(getDrawable(R.drawable.rect_border));
                                break;
                            }
                            case 10: {
                                val1231 = Math.round((amount1.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV10.setText(NumberFormat.getNumberInstance(Locale.US).format(val1231));
                                TV10.setTextSize(15);
                                TV10.setPadding(10, 0, 10, 0);
                                TV10.setGravity(Gravity.RIGHT);
                                TV10.setTextColor(Color.WHITE);
                                TV10.setBackground(getDrawable(R.drawable.rect_border));
                                break;
                            }
                        }
                    }
                }
            }
            else
            {
                TR = new TableRow(this);
                TV = new TextView(this);                TV1 = new TextView(this);                TV2 = new TextView(this);                TV3 = new TextView(this);                TV4 = new TextView(this);                TV5 = new TextView(this);                TV6 = new TextView(this);                TV7 = new TextView(this);                TV8 = new TextView(this);                TV9 = new TextView(this);                TV10 = new TextView(this);
                myTV1.clear();

                myTV1.add(TV);                myTV1.add(TV1);                myTV1.add(TV2);                myTV1.add(TV3);                myTV1.add(TV4);                myTV1.add(TV5);                myTV1.add(TV6);                myTV1.add(TV7);                myTV1.add(TV8);                myTV1.add(TV9);                myTV1.add(TV10);
                TR.addView(TV);                TR.addView(TV1);                TR.addView(TV2);                TR.addView(TV3);                TR.addView(TV4);                TR.addView(TV5);                TR.addView(TV6);                TR.addView(TV7);                TR.addView(TV8);                TR.addView(TV9);                TR.addView(TV10);

                TL.addView(TR);

                TV.setText(year1.get(j).toString() + " - " + getMonth[month1.get(j)]);

                for (int z = 0 ; z < originalDescribe.size() + 1 ; z++)
                {
                    myTV1.get(z).setTextSize(15);
                    myTV1.get(z).setPadding(10, 0, 10, 0);
                    myTV1.get(z).setGravity(Gravity.LEFT);
                    myTV1.get(z).setTextColor(Color.WHITE);
                    myTV1.get(z).setBackground(getDrawable(R.drawable.rect_border));
                }

                String des = "", newdes = "";
                double val123 = 0;

                for (int k = 0 ; k < originalDescribe.size() ; k++)
                {
                    newdes = describe1.get(j);
                    des = originalDescribe.get(k);

                    if (newdes.equals(des)) {
                        value = k + 1;
                        switch (value) {
                            case 1: {
                                val123 = Math.round((amount1.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV1.setText(NumberFormat.getNumberInstance(Locale.US).format(val123));
                                TV1.setTextSize(15);
                                TV1.setPadding(10, 0, 10, 0);
                                TV1.setGravity(Gravity.RIGHT);
                                TV1.setTextColor(Color.WHITE);
                                TV1.setBackground(getDrawable(R.drawable.rect_border));
                                break;
                            }
                            case 2: {
                                val123 = Math.round((amount1.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV2.setText(NumberFormat.getNumberInstance(Locale.US).format(val123));
                                TV2.setTextSize(15);
                                TV2.setPadding(10, 0, 10, 0);
                                TV2.setGravity(Gravity.RIGHT);
                                TV2.setTextColor(Color.WHITE);
                                TV2.setBackground(getDrawable(R.drawable.rect_border));
                                break;
                            }
                            case 3: {
                                val123 = Math.round((amount1.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV3.setText(NumberFormat.getNumberInstance(Locale.US).format(val123));
                                TV3.setTextSize(15);
                                TV3.setPadding(10, 0, 10, 0);
                                TV3.setGravity(Gravity.RIGHT);
                                TV3.setTextColor(Color.WHITE);
                                TV3.setBackground(getDrawable(R.drawable.rect_border));
                                break;
                            }
                            case 4: {
                                val123 = Math.round((amount1.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV4.setText(NumberFormat.getNumberInstance(Locale.US).format(val123));
                                TV4.setTextSize(15);
                                TV4.setPadding(10, 0, 10, 0);
                                TV4.setGravity(Gravity.RIGHT);
                                TV4.setTextColor(Color.WHITE);
                                TV4.setBackground(getDrawable(R.drawable.rect_border));
                                break;
                            }
                            case 5: {
                                val123 = Math.round((amount1.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV5.setText(NumberFormat.getNumberInstance(Locale.US).format(val123));
                                TV5.setTextSize(15);
                                TV5.setPadding(10, 0, 10, 0);
                                TV5.setGravity(Gravity.RIGHT);
                                TV5.setTextColor(Color.WHITE);
                                TV5.setBackground(getDrawable(R.drawable.rect_border));
                                break;
                            }
                            case 6: {
                                val123 = Math.round((amount1.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV6.setText(NumberFormat.getNumberInstance(Locale.US).format(val123));
                                TV6.setTextSize(15);
                                TV6.setPadding(10, 0, 10, 0);
                                TV6.setGravity(Gravity.RIGHT);
                                TV6.setTextColor(Color.WHITE);
                                TV6.setBackground(getDrawable(R.drawable.rect_border));
                                break;
                            }
                            case 7: {
                                val123 = Math.round((amount1.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV7.setText(NumberFormat.getNumberInstance(Locale.US).format(val123));
                                TV7.setTextSize(15);
                                TV7.setPadding(10, 0, 10, 0);
                                TV7.setGravity(Gravity.RIGHT);
                                TV7.setTextColor(Color.WHITE);
                                TV7.setBackground(getDrawable(R.drawable.rect_border));
                                break;
                            }
                            case 8: {
                                val123 = Math.round((amount1.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV8.setText(NumberFormat.getNumberInstance(Locale.US).format(val123));
                                TV8.setTextSize(15);
                                TV8.setPadding(10, 0, 10, 0);
                                TV8.setGravity(Gravity.RIGHT);
                                TV8.setTextColor(Color.WHITE);
                                TV8.setBackground(getDrawable(R.drawable.rect_border));
                                break;
                            }
                            case 9: {
                                val123 = Math.round((amount1.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV9.setText(NumberFormat.getNumberInstance(Locale.US).format(val123));
                                TV9.setTextSize(15);
                                TV9.setPadding(10, 0, 10, 0);
                                TV9.setGravity(Gravity.RIGHT);
                                TV9.setTextColor(Color.WHITE);
                                TV9.setBackground(getDrawable(R.drawable.rect_border));
                                break;
                            }
                            case 10: {
                                val123 = Math.round((amount1.get(j) / intVal[index]) * 100.0) / 100.0;
                                TV10.setText(NumberFormat.getNumberInstance(Locale.US).format(val123));
                                TV10.setTextSize(15);
                                TV10.setPadding(10, 0, 10, 0);
                                TV10.setGravity(Gravity.RIGHT);
                                TV10.setTextColor(Color.WHITE);
                                TV10.setBackground(getDrawable(R.drawable.rect_border));
                                break;
                            }
                        }
                    }
                }
            }
            yearCompare = newyearCompare;
            monthCompare = newmonthCompare;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        index = i;

        dropDownYear = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), "Sorted by: " + dropDownYear, Toast.LENGTH_SHORT).show();

        while(TL.getChildCount() > 1)
        {
            TL.removeView(TL.getChildAt(TL.getChildCount() - 1));
        }

        PrintTable2();
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}
}
