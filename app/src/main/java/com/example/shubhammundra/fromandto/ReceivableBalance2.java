package com.example.shubhammundra.fromandto;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
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
import android.widget.LinearLayout;
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

@SuppressLint("Registered")
public class ReceivableBalance2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TableLayout TL1,TL2,TL3;
    TableRow TR1,TR2,TR3;
    TextView TV,TV11,TV12,TV21,TV22,TV23,TV31,TV32;

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
    ArrayList<String> PostingGrp = new ArrayList<>();
    ArrayList<String> CountryCode = new ArrayList<>();
    ArrayList<String> Country = new ArrayList<>();

    String Cities = "''", Cities2 = "''", Cities3 = "''";

    Button selectYear, selectPostingGrp, selectCountry;

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal = {1,1000,100000,1000000,10000000};

    String dropDownValue;

    int index;

    @SuppressLint("StaticFieldLeak")
    public static Spinner spinnerFilter;

    ArrayList<String> Year1 = new ArrayList<>();
    ArrayList<Double> ReceBal = new ArrayList<>();

    ArrayList<String> Year2 = new ArrayList<>();
    ArrayList<Double> ReceBeforeDue = new ArrayList<>();
    ArrayList<Double> ReceOverDue = new ArrayList<>();

    ArrayList<String> Year3 = new ArrayList<>();
    ArrayList<Double> ReceOverduePer = new ArrayList<>();

    ArrayList<Float> AmountToGraph1 = new ArrayList<>();
    ArrayList<Float> AmountToGraph2 = new ArrayList<>();
    ArrayList<Float> AmountToGraph3 = new ArrayList<>();
    ArrayList<Float> AmountToGraph4 = new ArrayList<>();

    Button ReceBalChart;

    @SuppressLint("StaticFieldLeak")
    public static Spinner spinner1;

    String selectUniYear = "", dropDownYear = "";

    String[] getMonth = {"","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

    ArrayList<String> getCountryName = new ArrayList<>();

    @SuppressLint({"StaticFieldLeak", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receivable_balance);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TL1 = findViewById(R.id.tl_myTable);
        TL2 = findViewById(R.id.tl_myTable2);
        TL3 = findViewById(R.id.tl_myTable3);

        TV = findViewById(R.id.tv_selectYear);
        TV.setText("Select Year : ");

        Intent intent = getIntent();
        Year = intent.getStringArrayListExtra("Unique Year");
        CName = intent.getStringExtra("Company Name");
        PostingGrp = intent.getStringArrayListExtra("Posting Group");
        CountryCode = intent.getStringArrayListExtra("Country Code");
        Country = intent.getStringArrayListExtra("Country");

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

        spinner1 = new Spinner(this,Spinner.MODE_DROPDOWN);
        spinner1.setBackgroundResource(R.drawable.background_round);
        spinner1.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,Year);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter2);

        LinearLayout ll = findViewById(R.id.linearLay);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,28,0,0);

        ll.addView(spinner1,lp);

        listItems2 = PostingGrp.toArray(new String[PostingGrp.size()]);
        checkedItems2 = new boolean[listItems2.length];

        selectPostingGrp = findViewById(R.id.btn_selectPostingGrp);

        selectPostingGrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ReceivableBalance2.this);
                builder.setTitle("Select Posting Group");

                builder.setMultiChoiceItems(listItems2, checkedItems2, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                        if(isChecked)
                        {
                            if (! UserItems2.contains(position)){
                                UserItems2.add(position);
                            }
                        }
                        else
                        if (UserItems2.contains(position))
                        {
                            UserItems2.remove(UserItems2.indexOf(position));
                        }

                    }
                });

                builder.setCancelable(false);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String Store = "";
                        for(int i = 0 ; i < UserItems2.size() ; i++)
                        {
                            if (i == 0)
                            {
                                Store = listItems2[UserItems2.get(i)];
                            }
                            else
                                Store = Store + "\n" + listItems2[UserItems2.get(i)];
                        }
                        selectPostingGrp.setText(Store);
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
                        for(int i = 0 ; i < checkedItems2.length ; i++)
                        {
                            checkedItems2[i] = false;
                            UserItems2.clear();
                            selectPostingGrp.setText("Select");
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        final ArrayList<Integer> alreadySelectedItem = new ArrayList<>();

        ArrayList<MultiSelectModel> listOfItems = new ArrayList<>();

        for (int i = 0 ; i < Country.size() ; i++)
        {
            listOfItems.add(new MultiSelectModel(i+1,Country.get(i)));
        }

        final MultiSelectDialog multiSelectDialog = new MultiSelectDialog();

        multiSelectDialog.title("Select Countrys");
        multiSelectDialog.titleSize(24);
        multiSelectDialog.positiveText("Apply");
        multiSelectDialog.negativeText("Cancel");
        multiSelectDialog.clearText("Clear All");
        multiSelectDialog.preSelectIDsList(alreadySelectedItem);
        multiSelectDialog.multiSelectList(listOfItems);

        multiSelectDialog.onSubmit(new MultiSelectDialog.SubmitCallbackListener() {

            @Override
            public void onDismiss(ArrayList<Integer> ids, ArrayList<String> commonSeperatedData) throws PackageManager.NameNotFoundException {
                getCountryName.addAll(commonSeperatedData);

                String txt = "";
                for (int z = 0 ; z < getCountryName.size() ; z++)
                {
                    if (z == 0)
                    {
                        txt = getCountryName.get(z);
                    }
                    else
                        txt = txt + "\n" + getCountryName.get(z);
                }

                selectCountry.setText(txt);
            }

            @Override
            public void onCancel() {}

            @Override
            public void onClear() {}

        });

        selectCountry = findViewById(R.id.btn_selectCountry);

        selectCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getCountryName.clear();
                multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");
            }
        });

        ReceBalChart = findViewById(R.id.btn_ReceBal);

        ReceBalChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ReceivableBalance2.this,ReceivableBalanceChart.class);
                intent1.putExtra("ReceBal",AmountToGraph1);
                intent1.putExtra("Year1",Year1);
                intent1.putExtra("ReceBeforeDue",AmountToGraph2);
                intent1.putExtra("ReceOverDue",AmountToGraph3);
                intent1.putExtra("Year2",Year2);
                intent1.putExtra("ReceOverDuePer",AmountToGraph4);
                intent1.putExtra("Year3",Year3);
                startActivity(intent1);
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
        selectUniYear = dropDownYear;

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

                while(TL2.getChildCount() > 1)
                {
                    TL2.removeView(TL2.getChildAt(TL2.getChildCount() - 1));
                }

                while(TL3.getChildCount() > 1)
                {
                    TL3.removeView(TL3.getChildAt(TL3.getChildCount() - 1));
                }

                Cities = "''";
                for (int i = 0; i < UserItems.size() ; i++)
                {
                    Cities = Cities + ", '" + listItems[UserItems.get(i)] + "'";
                }

                Cities2 = "''";
                for (int i = 0; i < UserItems2.size() ; i++)
                {
                    Cities2 = Cities2 + ", '" + listItems2[UserItems2.get(i)] + "'";
                }

                Cities3 = "''";
                for (int i = 0; i < getCountryName.size() ; i++)
                {
                    for (int j = 0 ; j < CountryCode.size() ; j++)
                    {
                        if (Country.get(j).equals(getCountryName.get(i)))
                        {
                            Cities3 = Cities3 + ", '" + CountryCode.get(j) + "'";
                            break;
                        }
                    }
                }

                Year1.clear();
                ReceBal.clear();

                Year2.clear();
                ReceBeforeDue.clear();
                ReceOverDue.clear();

                Year3.clear();
                ReceOverduePer.clear();

                AmountToGraph1.clear();
                AmountToGraph2.clear();
                AmountToGraph3.clear();
                AmountToGraph4.clear();

                final ProgressDialog progressDialog = new ProgressDialog(ReceivableBalance2.this);

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
            String query = "select DATEPART(MM,[Posting Date]) as mon, sum([Receivable Balance]) as ReceBal FROM [dbo].["+CName+"$CustLedgerEntry_Staging] as cle, [dbo].["+CName+"$Customer] as cust where cle.[Customer No_] = cust.No_ and DATEPART(YYYY,cle.[Posting Date]) = '"+selectUniYear+"' and cle.[Customer Posting Group] in ("+Cities2+") and cust.[Country_Region Code] in ("+Cities3+") and cle.[Document Type] = '2' group by DATEPART(MM,cle.[Posting Date])";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {

                    int month1234 = rs.getInt("mon");
                    Year1.add(String.valueOf(getMonth[month1234]));

                    double ReceBalAmt = rs.getDouble("ReceBal");
                    ReceBalAmt = Math.abs(ReceBalAmt);
                    ReceBal.add(ReceBalAmt);
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

        selectUniYear = dropDownYear;

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
            String query = "select DATEPART(MM,cle.[Posting Date]) as mon, sum(cle.[Receivable Before Due]) as ReceBeforeDue, sum(cle.[Receivable After OverDue]) as ReceOverdue FROM [dbo].["+CName+"$CustLedgerEntry_Staging] as cle, [dbo].["+CName+"$Customer] as cust where cle.[Customer No_] = cust.No_ and DATEPART(YYYY,cle.[Posting Date]) = '"+selectUniYear+"' and cle.[Customer Posting Group] in ("+Cities2+") and cust.[Country_Region Code] in ("+Cities3+") and cle.[Document Type] = '2' group by DATEPART(MM,cle.[Posting Date])";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {

                    int month1234 = rs.getInt("mon");
                    Year2.add(String.valueOf(getMonth[month1234]));

                    double ReceBefDue = rs.getDouble("ReceBeforeDue");
                    ReceBefDue = Math.abs(ReceBefDue);
                    ReceBeforeDue.add(ReceBefDue);

                    double ReceOvrDue = rs.getDouble("ReceOverdue");
                    ReceOvrDue = Math.abs(ReceOvrDue);
                    ReceOverDue.add(ReceOvrDue);
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

        selectUniYear = dropDownYear;

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
            String query = "select DATEPART(MM,cle.[Posting Date]) as mon, ((SUM(cle.[Receivable After OverDue]) * 100) / sum(cle.[Receivable Balance])) as OverduePercen FROM [dbo].["+CName+"$CustLedgerEntry_Staging] as cle, [dbo].["+CName+"$Customer] as cust where cle.[Customer No_] = cust.No_ and DATEPART(YYYY,cle.[Posting Date]) = '"+selectUniYear+"' and cle.[Customer Posting Group] in ("+Cities2+") and cust.[Country_Region Code] in ("+Cities3+") and cle.[Document Type] = '2' group by DATEPART(MM,cle.[Posting Date])";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {

                    int month1234 = rs.getInt("mon");
                    Year3.add(String.valueOf(getMonth[month1234]));

                    double OverduePercen = rs.getDouble("OverduePercen");
                    OverduePercen = Math.abs(OverduePercen);
                    ReceOverduePer.add(OverduePercen);
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

        TV11 = findViewById(R.id.tv_no);
        TV21 = findViewById(R.id.tv_no2);
        TV31 = findViewById(R.id.tv_no3);

        TV11.setText("Month");
        TV21.setText("Month");
        TV31.setText("Month");

        for (int i = 0 ; i < Year1.size() ; i++)
        {
            TL1.setStretchAllColumns(true);

            TR1 = new TableRow(this);
            TV11 = new TextView(this);
            TV12 = new TextView(this);

            TV11.setText(String.valueOf(Year1.get(i)));
            TV12.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((ReceBal.get(i) / intVal[index]) * 100.0) / 100.0));

            AmountToGraph1.add(Float.parseFloat(String.valueOf(ReceBal.get(i) / intVal[index])));

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

            TR1.addView(TV11);
            TR1.addView(TV12);

            TL1.addView(TR1);
        }

        for (int j = 0 ; j < Year2.size() ; j++)
        {
            TL2.setColumnStretchable(0, true);
            TL2.setColumnStretchable(1, true);
            TL2.setColumnStretchable(2, true);

            TR2 = new TableRow(this);
            TV21 = new TextView(this);
            TV22 = new TextView(this);
            TV23 = new TextView(this);

            TV21.setText(String.valueOf(Year2.get(j)));
            TV22.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((ReceBeforeDue.get(j) / intVal[index]) * 100.0) / 100.0));
            TV23.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((ReceOverDue.get(j) / intVal[index]) * 100.0) / 100.0));

            AmountToGraph2.add(Float.parseFloat(String.valueOf(ReceBeforeDue.get(j) / intVal[index])));
            AmountToGraph3.add(Float.parseFloat(String.valueOf(ReceOverDue.get(j) / intVal[index])));

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

            TR2.addView(TV21);
            TR2.addView(TV22);
            TR2.addView(TV23);

            TL2.addView(TR2);
        }

        for (int k = 0 ; k < Year3.size() ; k++)
        {
            TL3.setColumnStretchable(0, true);
            TL3.setColumnStretchable(1, true);

            TR3 = new TableRow(this);
            TV31 = new TextView(this);
            TV32 = new TextView(this);

            TV31.setText(String.valueOf(Year3.get(k)));
            TV32.setText(String.valueOf(Math.round((ReceOverduePer.get(k)) * 100.0) / 100.0) + "%");

            AmountToGraph4.add(Float.parseFloat(ReceOverduePer.get(k).toString()));

            TV31.setTextSize(15);
            TV31.setPadding(10, 0, 10, 0);
            TV31.setGravity(Gravity.LEFT);
            TV31.setTextColor(Color.WHITE);
            TV31.setBackground(getDrawable(R.drawable.rect_border));

            TV32.setTextSize(15);
            TV32.setPadding(10, 0, 10, 0);
            TV32.setGravity(Gravity.RIGHT);
            TV32.setTextColor(Color.WHITE);
            TV32.setBackground(getDrawable(R.drawable.rect_border));

            TR3.addView(TV31);
            TR3.addView(TV32);

            TL3.addView(TR3);
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

            while(TL2.getChildCount() > 1)
            {
                TL2.removeView(TL2.getChildAt(TL2.getChildCount() - 1));
            }

            while(TL3.getChildCount() > 1)
            {
                TL3.removeView(TL3.getChildAt(TL3.getChildCount() - 1));
            }

            AmountToGraph1.clear();
            AmountToGraph2.clear();
            AmountToGraph3.clear();
            AmountToGraph4.clear();
            
            PrintTable();
        }
        else
        {
            dropDownYear = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(adapterView.getContext(), "Selected Year : " + dropDownYear, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
