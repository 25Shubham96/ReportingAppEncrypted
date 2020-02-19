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

public class PayByMonth extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TableLayout TL;
    TableRow TR;
    TextView TV1, TV2, TV3;

    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    ImageView refresh, back;

    String CName, Values = "''";

    Button selectYear;

    String[] listItems;
    boolean[] checkedItems;

    ArrayList<Integer> UserItems = new ArrayList<>();
    public static ArrayList<String> Year123 ;

    String Cities = "''";

    String[] Months = {" ","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};


    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal = {1,1000,100000,1000000,10000000};

    String dropDownValue;

    ArrayList<String> YearMonth123 = new ArrayList<>();
    ArrayList<Double> NetAmt123 = new ArrayList<>();
    ArrayList<Double> AvgNetAmt123 = new ArrayList<>();

    ArrayList<Float> AmountToGraph = new ArrayList<>();
    ArrayList<Float> AmountToGraph1 = new ArrayList<>();

    int index;

    @SuppressLint("StaticFieldLeak")
    public static Spinner spinnerFilter;

    Button viewChart;

    ArrayList<String> StoreDescrip;
    ArrayList<String> StoreNm = new ArrayList<>();

    Button selectStore;

    ArrayList<String> getStoreDescrip = new ArrayList<>();

    String Store1234 = "''";

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_by_month);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent getintent = getIntent();
        CName = getintent.getStringExtra("Company Name");
        Year123 = getintent.getStringArrayListExtra("Unique Year");
        StoreNm = getintent.getStringArrayListExtra("Store Name");
        StoreDescrip = getintent.getStringArrayListExtra("Store Descrip");

        TL = findViewById(R.id.tl_myTable1);

        listItems = Year123.toArray(new String[Year123.size()]);
        checkedItems = new boolean[listItems.length];

        spinnerFilter = findViewById(R.id.spin_filter);
        spinnerFilter.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, amtFilter);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapter1);

        selectYear = findViewById(R.id.btn_selectYear);
        selectYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PayByMonth.this);
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

        back = findViewById(R.id.img1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

        viewChart = findViewById(R.id.btn_PayMonChart);
        viewChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PayByMonth.this,PayByMonthChart.class);
                intent.putExtra("YearMonth",YearMonth123);
                intent.putExtra("NetAmt",AmountToGraph);
                intent.putExtra("AvgNetAmt",AmountToGraph1);
                startActivity(intent);
            }
        });
    }

    @SuppressLint({"RtlHardcoded", "SetTextI18n"})
    public void doInBackground1() {
        refresh = findViewById(R.id.iv_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {

                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
                refresh.startAnimation(rotation);

                TL = findViewById(R.id.tl_myTable1);

                while (TL.getChildCount() > 1) {
                    TL.removeView(TL.getChildAt(TL.getChildCount() - 1));
                }

                Cities = "''";
                for (int i = 0; i < UserItems.size() ; i++)
                {
                    Cities = Cities + ", '" + listItems[UserItems.get(i)] + "'";
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

                AmountToGraph.clear();
                AmountToGraph1.clear();
                YearMonth123.clear();
                NetAmt123.clear();
                AvgNetAmt123.clear();

                final ProgressDialog progressDialog = new ProgressDialog(PayByMonth.this);

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

            String query = "Select DATEPART(YYYY,Date) AS YR,DATEPART(MM,Date) AS MON, sum([Amount Tendered]) as NetPayment, (sum([Amount Tendered])/COUNT([Receipt No_])) as AvgPayment FROM [dbo].["+CName+"$Trans_ Payment Entry] where DATEPART(YYYY,Date) in ("+Cities+") and [Store No_] in ("+Store1234+") GROUP BY DATEPART(YYYY,Date),DATEPART(MM,Date) order by DATEPART(YYYY,Date),DATEPART(MM,Date)";

            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    YearMonth123.add(rs.getString("YR") + "-" + Months[Integer.parseInt(rs.getString("MON"))]);
                    NetAmt123.add(Math.abs(rs.getDouble("NetPayment")));
                    AvgNetAmt123.add(Math.abs(rs.getDouble("AvgPayment")));
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

        for (int i = 0 ; i < YearMonth123.size() ; i++)
        {
            TL.setStretchAllColumns(true);

            TR = new TableRow(this);

            TV1 = new TextView(this);
            TV2 = new TextView(this);
            TV3 = new TextView(this);

            TV1.setText(YearMonth123.get(i));
            TV2.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((NetAmt123.get(i) / intVal[index]) * 100.0) / 100.0));
            TV3.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((AvgNetAmt123.get(i) / intVal[index]) * 100.0) / 100.0));

            AmountToGraph.add(Float.parseFloat(String.valueOf(Math.round((NetAmt123.get(i) / intVal[index]) * 100.0) / 100.0)));
            AmountToGraph1.add(Float.parseFloat(String.valueOf(Math.round((AvgNetAmt123.get(i) / intVal[index]) * 100.0) / 100.0)));

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
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        index = i;

        dropDownValue = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), "Sorted by: " + dropDownValue, Toast.LENGTH_SHORT).show();

        while(TL.getChildCount() > 1)
        {
            TL.removeView(TL.getChildAt(TL.getChildCount() - 1));
        }

        AmountToGraph.clear();
        AmountToGraph1.clear();

        PrintTable();
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}
}
