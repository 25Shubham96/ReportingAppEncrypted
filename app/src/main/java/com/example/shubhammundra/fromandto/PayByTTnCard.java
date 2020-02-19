package com.example.shubhammundra.fromandto;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
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

public class PayByTTnCard extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TableLayout TL;
    TableRow TR;
    TextView TV1, TV2, TV3, TV4;

    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    ImageView refresh;

    String CName;

    ArrayList<String> StoreNm = new ArrayList<>();
    ArrayList<String> UniYear = new ArrayList<>();

    String selectUniYear = "", dropDownYear = "";
    String selectUniLastYear = "", dropDownLastYear = "";

    ArrayList<String> TenTyp = new ArrayList<>();
    ArrayList<String> Descrip = new ArrayList<>();
    ArrayList<Double> Amount1234 = new ArrayList<>();

    ArrayList<String> TenTyp2 = new ArrayList<>();
    ArrayList<String> Descrip2 = new ArrayList<>();
    ArrayList<Double> Amount12342 = new ArrayList<>();

    ArrayList<String> CardTenderCode1 = new ArrayList<>();
    ArrayList<String> CardDescriptn1 = new ArrayList<>();
    ArrayList<Double> CardAmount12341 = new ArrayList<>();

    ArrayList<String> CardTenderCode2 = new ArrayList<>();
    ArrayList<String> CardDescriptn2 = new ArrayList<>();
    ArrayList<Double> CardAmount12342 = new ArrayList<>();

    String a, b, c, d;
    Float a1 ,b1 ,c1 ,d1;

    Button btn;

    ArrayList<String> TTDescribe = new ArrayList<>();
    ArrayList<Float> Amt1 = new ArrayList<>();
    ArrayList<Float> Amt2 = new ArrayList<>();

    ImageView imageView;

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal = {1,1000,100000,1000000,10000000};

    String dropDownValue;

    int index;

    @SuppressLint("StaticFieldLeak")
    public static Spinner spinnerFilter;
    @SuppressLint("StaticFieldLeak")
    public static Spinner spinner1;

    int cntr = 0;

    ArrayList<String> StoreDescrip;

    Button selectStore,selectTendorTyp;

    ArrayList<String> getStoreDescrip = new ArrayList<>();
    ArrayList<String> getTenderTypeDescrip = new ArrayList<>();

    String Store1234 = "''";
    String TenderTyp1234 = "''";

    ArrayList<String> TenderTypeCode = new ArrayList<>();
    ArrayList<String> TenderTypeDescrip = new ArrayList<>();

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_by_ttn_card);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        imageView = findViewById(R.id.img1);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TL = findViewById(R.id.tl_myTable);
        TR = findViewById(R.id.tr_index);

        TV1 = findViewById(R.id.tv_TT);
        TV2 = findViewById(R.id.tv_currYear);
        TV3 = findViewById(R.id.tv_preYear);
        TV4 = findViewById(R.id.tv_totalAmt);

        Intent intent = getIntent();
        CName = intent.getStringExtra("Company Name");
        StoreNm = intent.getStringArrayListExtra("Store Name");
        StoreDescrip = intent.getStringArrayListExtra("Store Descrip");
        UniYear = intent.getStringArrayListExtra("Unique Year");

        spinnerFilter = findViewById(R.id.spin_filter);
        spinnerFilter.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, amtFilter);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapter1);

        spinner1 = findViewById(R.id.spin_yearSelect);
        spinner1.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,UniYear);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

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

        btn  = findViewById(R.id.btn_TTnCardChart);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ChartIntent = new Intent(PayByTTnCard.this,DualBarChart.class);
                ChartIntent.putExtra("Tender Type Des",TTDescribe);
                ChartIntent.putExtra("Current Amount",Amt1);
                ChartIntent.putExtra("Previous Amount",Amt2);
                ChartIntent.putExtra("Current Year", dropDownYear);
                ChartIntent.putExtra("Previous Year", dropDownLastYear);
                startActivity(ChartIntent);
            }
        });

        final ProgressDialog progressDialog = new ProgressDialog(PayByTTnCard.this);

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setMessage("loading content please wait...");
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                PrintTable();

                final ArrayList<Integer> alreadySelectedItem1 = new ArrayList<>();

                ArrayList<MultiSelectModel> listOfItems1 = new ArrayList<>();

                for (int i = 0 ; i < TenderTypeDescrip.size() ; i++)
                {
                    listOfItems1.add(new MultiSelectModel(i+1,TenderTypeDescrip.get(i)));
                }

                final MultiSelectDialog multiSelectDialog1 = new MultiSelectDialog();

                multiSelectDialog1.title("Select Tender Type/s");
                multiSelectDialog1.titleSize(24);
                multiSelectDialog1.positiveText("Apply");
                multiSelectDialog1.negativeText("Cancel");
                multiSelectDialog1.clearText("Clear All");
                multiSelectDialog1.preSelectIDsList(alreadySelectedItem1);
                multiSelectDialog1.multiSelectList(listOfItems1);

                multiSelectDialog1.onSubmit(new MultiSelectDialog.SubmitCallbackListener() {

                    @Override
                    public void onDismiss(ArrayList<Integer> ids, ArrayList<String> commonSeperatedData) throws PackageManager.NameNotFoundException {
                        getTenderTypeDescrip.addAll(commonSeperatedData);

                        String txt = "";
                        for (int z = 0 ; z < getTenderTypeDescrip.size() ; z++)
                        {
                            if (z == 0)
                            {
                                txt = getTenderTypeDescrip.get(z);
                            }
                            else
                                txt = txt + "\n" + getTenderTypeDescrip.get(z);
                        }

                        selectTendorTyp.setText(txt);
                    }
                    @Override
                    public void onCancel() {}
                    @Override
                    public void onClear() {}

                });

                selectTendorTyp = findViewById(R.id.btn_selectTender);
                selectTendorTyp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getTenderTypeDescrip.clear();
                        multiSelectDialog1.show(getSupportFragmentManager(), "multiSelectDialog1");
                    }
                });

                progressDialog.dismiss();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                if (Looper.myLooper() == null)
                {
                    Looper.prepare();
                }
                doInBackground1();
                TenderType();
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

                while (TL.getChildCount() > 1) {
                    TL.removeView(TL.getChildAt(TL.getChildCount() - 1));
                }

                TenTyp.clear();
                Descrip.clear();
                Amount1234.clear();

                TenTyp2.clear();
                Descrip2.clear();
                Amount12342.clear();

                CardTenderCode1.clear();
                CardDescriptn1.clear();
                CardAmount12341.clear();

                CardTenderCode2.clear();
                CardDescriptn2.clear();
                CardAmount12342.clear();

                TTDescribe.clear();
                Amt1.clear();
                Amt2.clear();

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

                TenderTyp1234 = "''";
                for (int i = 0; i < getTenderTypeDescrip.size() ; i++)
                {
                    for (int j = 0 ; j < TenderTypeDescrip.size() ; j++)
                    {
                        if (getTenderTypeDescrip.get(i).equals(TenderTypeDescrip.get(j)))
                        {
                            TenderTyp1234 = TenderTyp1234 + ", '" + TenderTypeCode.get(j) + "'";
                        }
                    }
                }

                final ProgressDialog progressDialog = new ProgressDialog(PayByTTnCard.this);

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
                        doInBackground3();
                        doInBackground4();
                        return null;
                    }
                }.execute();

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
            String query = "Select TPE.[Tender Type], TT.[Description], sum(TPE.[Amount Tendered]) as Amount FROM [dbo].["+CName+"$Trans_ Payment Entry] as TPE, [dbo].["+CName+"$Tender Type] as TT where TPE.[Tender Type] = TT.Code and TPE.[Store No_] = TT.[Store No_] and DATEPART(YYYY,Date) = '"+selectUniYear+"' and TPE.[Store No_] in ("+Store1234+") and TPE.[Tender Type] in ("+TenderTyp1234+") group by TPE.[Tender Type], TT.[Description] order by TPE.[Tender Type], TT.[Description]";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {

                    String TT1 = rs.getString("Tender Type");
                    String Des1 = rs.getString("Description");

                    double Amt1 = rs.getLong("Amount");
                    Amt1 = Math.abs(Amt1);

                    TenTyp.add(TT1);
                    Descrip.add(Des1);
                    Amount1234.add(Amt1);
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void  doInBackground2() {
        selectUniLastYear = dropDownLastYear;

        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query = "Select TPE.[Tender Type], TT.[Description], sum(TPE.[Amount Tendered]) as Amount FROM [dbo].["+CName+"$Trans_ Payment Entry] as TPE, [dbo].["+CName+"$Tender Type] as TT where TPE.[Tender Type] = TT.Code and TPE.[Store No_] = TT.[Store No_] and DATEPART(YYYY,Date) = '"+selectUniLastYear+"' and TPE.[Store No_] in ("+Store1234+") and TPE.[Tender Type] in ("+TenderTyp1234+") group by TPE.[Tender Type], TT.[Description] order by TPE.[Tender Type], TT.[Description]";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    String TT1 = rs.getString("Tender Type");
                    String Des1 = rs.getString("Description");

                    double Amt1 = rs.getLong("Amount");
                    Amt1 = Math.abs(Amt1);

                    TenTyp2.add(TT1);
                    Descrip2.add(Des1);
                    Amount12342.add(Amt1);
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void  doInBackground3() {
        selectUniYear = dropDownYear;

        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query = "Select [Tender Type Code], Description, sum([Amount Tendered]) as Amount FROM [dbo].["+CName+"$Tender Type Card Setup] as TTCS, [dbo].["+CName+"$Trans_ Payment Entry] as TPE where TPE.[Card No_] = TTCS.[Card No_] and TPE.[Store No_] = TTCS.[Store No_] and DATEPART(YYYY,Date) = '"+selectUniYear+"' and TPE.[Store No_] in ("+Store1234+") and TPE.[Tender Type] in ("+TenderTyp1234+") group by [Tender Type Code], Description order by [Tender Type Code], Description";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    String a = rs.getString("Tender Type Code");
                    String b = rs.getString("Description");
                    double c = rs.getLong("Amount");
                    c = Math.abs(c);

                    CardTenderCode1.add(a);
                    CardDescriptn1.add(b);
                    CardAmount12341.add(c);
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void  doInBackground4() {
        selectUniLastYear = dropDownLastYear;

        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query = "Select [Tender Type Code], Description, sum([Amount Tendered]) as Amount FROM [dbo].["+CName+"$Tender Type Card Setup] as TTCS, [dbo].["+CName+"$Trans_ Payment Entry] as TPE where TPE.[Card No_] = TTCS.[Card No_] and TPE.[Store No_] = TTCS.[Store No_] and DATEPART(YYYY,Date) = '"+selectUniLastYear+"' and TPE.[Store No_] in ("+Store1234+") and TPE.[Tender Type] in ("+TenderTyp1234+") group by [Tender Type Code], Description order by [Tender Type Code], Description";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    String a = rs.getString("Tender Type Code");
                    String b = rs.getString("Description");
                    double c = rs.getLong("Amount");
                    c = Math.abs(c);

                    CardTenderCode2.add(a);
                    CardDescriptn2.add(b);
                    CardAmount12342.add(c);
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void TenderType(){
        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query = "select distinct Code, Description FROM [dbo].["+CName+"$Tender Type] order by Description";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    TenderTypeCode.add(rs.getString("Code"));
                    TenderTypeDescrip.add(rs.getString("Description"));
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
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Spinner spinner = (Spinner) adapterView;
        if (spinner.getId() == R.id.spin_yearSelect)
        {
            dropDownYear = adapterView.getItemAtPosition(i).toString();

            int dummy = Integer.parseInt(dropDownYear);
            dummy = dummy - 1 ;
            dropDownLastYear = String.valueOf(dummy);

            Toast.makeText(adapterView.getContext(), "Selected Year : " + dropDownYear, Toast.LENGTH_SHORT).show();
        }
        else
            if (spinner.getId() == R.id.spin_filter) {
                index = i;

                dropDownValue = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(), "Sorted by: " + dropDownValue, Toast.LENGTH_SHORT).show();

                while (TL.getChildCount() > 1) {
                    TL.removeView(TL.getChildAt(TL.getChildCount() - 1));
                }
                TTDescribe.clear();
                Amt1.clear();
                Amt2.clear();
                PrintTable();
            }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @SuppressLint({"RtlHardcoded", "SetTextI18n"})
    public void PrintTable() {
        TL = findViewById(R.id.tl_myTable);
        TR = findViewById(R.id.tr_index);

        TV1 = findViewById(R.id.tv_TT);
        TV2 = findViewById(R.id.tv_currYear);
        TV3 = findViewById(R.id.tv_preYear);
        TV4 = findViewById(R.id.tv_totalAmt);

        if (cntr != 0)
        {
            TV2.setText(String.valueOf(selectUniYear));
            TV3.setText(String.valueOf(selectUniLastYear));
        }
        cntr++;

        TL.setStretchAllColumns(true);

        for (int i = 0 ; i < TenTyp.size() ; i++)
        {
            TR = new TableRow(this);
            TV1 = new TextView(this);            TV2 = new TextView(this);            TV3 = new TextView(this);            TV4 = new TextView(this);

            TV1.setText("0");            TV2.setText("0");            TV3.setText("0");            TV4.setText("0");

            TV1.setText(String.valueOf(TenTyp.get(i) + "-" + Descrip.get(i)));

            double val1 = Amount1234.get(i);
            TV2.setText(NumberFormat.getNumberInstance().format(Math.round((val1 / intVal[index]) * 100.0) / 100.0));

            double val2 = 0 ;
            for (int j = 0 ; j < TenTyp2.size() ; j++)
            {
                if (Integer.parseInt(TenTyp.get(i)) == Integer.parseInt(TenTyp2.get(j)))
                {
                    val2 = Amount12342.get(j);
                    TV3.setText(NumberFormat.getNumberInstance().format(Math.round((val2 / intVal[index]) * 100.0) / 100.0));
                }
            }

            double val3 = val1 + val2;
            TV4.setText(NumberFormat.getNumberInstance().format(Math.round((val3 / intVal[index]) * 100.0) / 100.0));

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

            TR.addView(TV1);            TR.addView(TV2);            TR.addView(TV3);            TR.addView(TV4);
            TL.addView(TR);

            TTDescribe.add(TV1.getText().toString());
            Amt1.add(Float.parseFloat(String.valueOf(val1 / intVal[index])));
            Amt2.add(Float.parseFloat(String.valueOf(val2 / intVal[index])));

            int ctr = 0;

            for (int k = 0 ; k < CardTenderCode1.size() ; k++)
            {
                if (Integer.parseInt(TenTyp.get(i)) == Integer.parseInt(CardTenderCode1.get(k)))
                {
                    if (ctr == 0)
                    {
                        TTDescribe.remove(TTDescribe.size() - 1);
                        Amt1.remove(Amt1.size() - 1);
                        Amt2.remove(Amt2.size() - 1);
                        ctr++;
                    }

                    TR = new TableRow(this);
                    TV1 = new TextView(this);                    TV2 = new TextView(this);                    TV3 = new TextView(this);                    TV4 = new TextView(this);

                    TV1.setText("0");                    TV2.setText("0");                    TV3.setText("0");                    TV4.setText("0");

                    TV1.setText("•" + String.valueOf(CardDescriptn1.get(k)));

                    double val4 = CardAmount12341.get(k);
                    TV2.setText(NumberFormat.getNumberInstance().format(Math.round((val4 / intVal[index]) * 100.0) / 100.0));

                    double val5 = 0;

                    for (int l = 0 ; l < CardTenderCode2.size() ; l++)
                    {
                        if (String.valueOf(CardTenderCode1.get(k)) == String.valueOf(CardTenderCode2.get(l)))
                        {
                            val5 = CardAmount12342.get(l);
                            TV3.setText(NumberFormat.getNumberInstance().format(Math.round((val5 / intVal[index]) * 100.0) / 100.0));
                        }
                    }

                    double val6 = val4 + val5;
                    TV4.setText(NumberFormat.getNumberInstance().format(Math.round((val6 / intVal[index]) * 100.0) / 100.0));

                    TV1.setTextSize(15);
                    TV1.setPadding(60, 0, 10, 0);
                    TV1.setGravity(Gravity.LEFT);
                    TV1.setTextColor(Color.WHITE);
                    TV1.setBackground(getDrawable(R.drawable.rect_border));

                    TV2.setTextSize(15);
                    TV2.setPadding(60, 0, 10, 0);
                    TV2.setGravity(Gravity.RIGHT);
                    TV2.setTextColor(Color.WHITE);
                    TV2.setBackground(getDrawable(R.drawable.rect_border));

                    TV3.setTextSize(15);
                    TV3.setPadding(60, 0, 10, 0);
                    TV3.setGravity(Gravity.RIGHT);
                    TV3.setTextColor(Color.WHITE);
                    TV3.setBackground(getDrawable(R.drawable.rect_border));

                    TV4.setTextSize(15);
                    TV4.setPadding(60, 0, 10, 0);
                    TV4.setGravity(Gravity.RIGHT);
                    TV4.setTextColor(Color.WHITE);
                    TV4.setBackground(getDrawable(R.drawable.rect_border));

                    TR.addView(TV1);                    TR.addView(TV2);                    TR.addView(TV3);                    TR.addView(TV4);
                    TL.addView(TR);

                    TTDescribe.add(TV1.getText().toString());
                    Amt1.add(Float.parseFloat(String.valueOf(val4 / intVal[index])));
                    Amt2.add(Float.parseFloat(String.valueOf(val5 / intVal[index])));
                }
            }
        }
    }
}
