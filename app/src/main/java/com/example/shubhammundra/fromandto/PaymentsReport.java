package com.example.shubhammundra.fromandto;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
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

public class PaymentsReport extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TableLayout TL,TL2;
    TableRow TR,TR2;
    TextView TV1,TV2,TV3;
    TextView TV12,TV22,TV32;
    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    TextView storeStart;
    TextView storeEnd;

    ImageView refresh;

    String storeStartDate = "2001-01-01";

    String storeEndDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

    String selectStoreNo = "S0001";

    String dropDownStoreNo;

    /*//String[] Months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    String[] Months = {" ","1","2","3","4","5","6","7","8","9","10","11","12"};*/

    String[] Months = {"January","February","March","April","May","June","July","August","September","October","November","December"};
    String[] Months1234 = {"1","2","3","4","5","6","7","8","9","10","11","12"};

    String[] MonthsNew = {"","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

    ArrayList<String> Netpaymonth = new ArrayList<>();
    ArrayList<Double> NetpayAmt = new ArrayList<>();
    ArrayList<Double> AvgpayAmt = new ArrayList<>();
    ArrayList<String> Netpaytype = new ArrayList<>();
    ArrayList<Double> NetpaytypeAmt = new ArrayList<>();
    ArrayList<Double> AvgNetpaytypeAmt = new ArrayList<>();

    ArrayList<Double> NetcardAmt = new ArrayList<>();
    ArrayList<String> Months1 = new ArrayList<>();
    ArrayList<Double> NetcashAmt = new ArrayList<>();
    ArrayList<String> Months2 = new ArrayList<>();
    ArrayList<Double> NetchequeAmt = new ArrayList<>();
    ArrayList<String> Months3 = new ArrayList<>();
    ArrayList<Double> NetGiftcardAmt = new ArrayList<>();
    ArrayList<String> Months4 = new ArrayList<>();

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal= {1,1000,100000,1000000,10000000};

    String dropDownValue;

    ArrayList<Float> AmountToGraph11 = new ArrayList<>();
    ArrayList<Float> AmountToGraph12 = new ArrayList<>();
    ArrayList<Float> AmountToGraph21 = new ArrayList<>();
    ArrayList<Float> AmountToGraph22 = new ArrayList<>();
    ArrayList<Float> AmountToGraph3 = new ArrayList<>();
    ArrayList<Float> AmountToGraph4 = new ArrayList<>();
    ArrayList<Float> AmountToGraph5 = new ArrayList<>();
    ArrayList<Float> AmountToGraph6 = new ArrayList<>();

    GradientDrawable gd;

    int index;

    @SuppressLint("StaticFieldLeak")
    public static Spinner spinnerFilter;

    @SuppressLint("StaticFieldLeak")
    public static SearchableSpinner spinner;

    Button btnchrt;

    Calendar startCalendar = Calendar.getInstance();
    Calendar endCalendar = Calendar.getInstance();

    String CName;

    ImageView back;

    int count1 = 1;

    ArrayList<String> Year, Str;

    String[] listItems;
    boolean[] checkedItems1;

    ArrayList<Integer> UserItems1 = new ArrayList<>();

    Button SelectMonth, SelectPayType;

    ArrayList<String> getPayTypeDescrip = new ArrayList<>();

    ArrayList<String> PayTypeDescrip = new ArrayList<>();
    ArrayList<String> PayTypeCode = new ArrayList<>();

    String MonthVal = "''", PayTypeVal = "''";
    
    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payments_report_table);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TL = findViewById(R.id.tl_Paytab1);
        TL2 = findViewById(R.id.tl_Pay2);
        SelectPayType = findViewById(R.id.btn_selectPayType);
        SelectMonth = findViewById(R.id.btn_selectMonths);

        Intent intent = getIntent();
        CName = intent.getStringExtra("Company Name");
        Year = intent.getStringArrayListExtra("Unique Year");
        Str = intent.getStringArrayListExtra("Store Name");

        diffPayTypes();

        spinnerFilter = findViewById(R.id.spin_filter);
        spinnerFilter.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, amtFilter);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapter1);

        spinner = findViewById(R.id.spin_storeselect);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Str);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setTitle("Select Store");

        checkedItems1 = new boolean[Months.length];

        SelectMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(PaymentsReport.this);
                builder1.setTitle("Select Months");

                builder1.setMultiChoiceItems(Months, checkedItems1, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                        if(isChecked)
                        {
                            if (! UserItems1.contains(position)){
                                UserItems1.add(position);
                            }
                        }
                        else
                        if (UserItems1.contains(position))
                        {
                            UserItems1.remove(UserItems1.indexOf(position));
                        }

                    }
                });

                builder1.setCancelable(false);

                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String Month = "";
                        for(int i = 0 ; i < UserItems1.size() ; i++)
                        {
                            Month = Month + Months[UserItems1.get(i)];
                            if(i != UserItems1.size() -1)
                            {
                                Month = Month + "\n";
                            }
                        }
                        SelectMonth.setText(Month);
                    }
                });

                builder1.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder1.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for(int i = 0 ; i < checkedItems1.length ; i++)
                        {
                            checkedItems1[i] = false;
                            UserItems1.clear();
                            SelectMonth.setText("Select");
                        }
                    }
                });

                AlertDialog dialog1 = builder1.create();
                dialog1.show();
            }
        });

        final ArrayList<Integer> alreadySelectedItem = new ArrayList<>();

        ArrayList<MultiSelectModel> listOfItems = new ArrayList<>();

        for (int i = 0 ; i < PayTypeDescrip.size() ; i++)
        {
            listOfItems.add(new MultiSelectModel(i+1,PayTypeDescrip.get(i)));
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
                getPayTypeDescrip.addAll(commonSeperatedData);

                String txt = "";
                for (int z = 0 ; z < getPayTypeDescrip.size() ; z++)
                {
                    if (z == 0)
                    {
                        txt = getPayTypeDescrip.get(z);
                    }
                    else
                        txt = txt + "\n" + getPayTypeDescrip.get(z);
                }

                SelectPayType.setText(txt);
            }

            @Override
            public void onCancel() {}

            @Override
            public void onClear() {}

        });

        SelectPayType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPayTypeDescrip.clear();
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

        btnchrt = findViewById(R.id.btn_paymentchart);
        btnchrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getpaymentIntent = new Intent(PaymentsReport.this,PaymentBarchart.class);

                getpaymentIntent.putExtra("NPM",Netpaymonth);
                getpaymentIntent.putExtra("NPA",AmountToGraph11);
                getpaymentIntent.putExtra("APA",AmountToGraph12);

                getpaymentIntent.putExtra("NPT",Netpaytype);
                getpaymentIntent.putExtra("NPTA",AmountToGraph21);
                getpaymentIntent.putExtra("ANPTA",AmountToGraph22);

                getpaymentIntent.putExtra("NCA1",AmountToGraph3);
                getpaymentIntent.putExtra("M1",Months1);

                getpaymentIntent.putExtra("NCA2",AmountToGraph4);
                getpaymentIntent.putExtra("M2",Months2);

                getpaymentIntent.putExtra("NCA3",AmountToGraph5);
                getpaymentIntent.putExtra("M3",Months3);

                getpaymentIntent.putExtra("NGCA",AmountToGraph6);
                getpaymentIntent.putExtra("M4",Months4);
                startActivity(getpaymentIntent);
            }
        });

        new AsyncTask<Void, Void, Void>(){

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
                doInBackgroundPay2();
                doInBackgroundPay3();
                doInBackgroundPay4();
                doInBackgroundPay5();
                doInBackgroundPay6();
                return null;
            }
        }.execute();

        storeStart = findViewById(R.id.tv_startdatePay);
        storeEnd = findViewById(R.id.tv_enddatePay);
        storeStart.setText(storeStartDate);
        storeEnd.setText(storeEndDate);
    }

    @SuppressLint("RtlHardcoded")
    private void doInBackground1() {
        selectStoreNo = dropDownStoreNo;

        final DatePickerDialog.OnDateSetListener DPstartDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                startCalendar.set(Calendar.YEAR, year);
                startCalendar.set(Calendar.MONTH, month);
                startCalendar.set(Calendar.DAY_OF_MONTH, day);
                StartUpdateLabel();

                storeStartDate = storeStart.getText().toString();
            }
        };

        final DatePickerDialog.OnDateSetListener DPendDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                endCalendar.set(Calendar.YEAR, year);
                endCalendar.set(Calendar.MONTH, month);
                endCalendar.set(Calendar.DAY_OF_MONTH, day);
                EndUpdateLabel();

                storeEndDate = storeEnd.getText().toString();
            }
        };

        refresh = findViewById(R.id.iv_PayRefresh);
        storeStart = findViewById(R.id.tv_startdatePay);
        storeEnd = findViewById(R.id.tv_enddatePay);

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

                PayTypeVal = "''";
                for (int i = 0; i < getPayTypeDescrip.size() ; i++)
                {
                    for (int j = 0 ; j < PayTypeDescrip.size() ; j++)
                    {
                        if (getPayTypeDescrip.get(i).equals(PayTypeDescrip.get(j)))
                        {
                            PayTypeVal = PayTypeVal + ", '" + PayTypeCode.get(j) + "'";
                        }
                    }
                }

                MonthVal = "''";
                for (int i = 0; i < UserItems1.size() ; i++)
                {
                    MonthVal = MonthVal + ", '" + Months1234[UserItems1.get(i)] + "'";
                }

                Netpaymonth.clear();
                NetpayAmt.clear();
                AvgpayAmt.clear();
                Netpaytype.clear();
                NetpaytypeAmt.clear();
                AvgNetpaytypeAmt.clear();
                NetcardAmt.clear();
                Months1.clear();
                NetcashAmt.clear();
                Months2.clear();
                NetchequeAmt.clear();
                Months3.clear();
                NetGiftcardAmt.clear();
                Months4.clear();

                AmountToGraph11.clear();
                AmountToGraph12.clear();
                AmountToGraph21.clear();
                AmountToGraph22.clear();
                AmountToGraph3.clear();
                AmountToGraph4.clear();
                AmountToGraph5.clear();
                AmountToGraph6.clear();

                final ProgressDialog progressDialog = new ProgressDialog(PaymentsReport.this);

                /*final AlertDialog.Builder builder = new AlertDialog.Builder(PaymentsReport.this);
                builder.setMessage("Collecting Data please wait...");
                builder.setCancelable(true);

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                final AlertDialog alertDialog = builder.create();*/

                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog.setMessage("Collecting Data...");
                        progressDialog.show();
//                        alertDialog.show();
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        storeStart.setText(storeStartDate);
                        storeEnd.setText(storeEndDate);
                        PrintTable();
                        progressDialog.dismiss();
//                        alertDialog.dismiss();
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        if (Looper.myLooper() == null)
                        {
                            Looper.prepare();
                        }
                        doInBackground1();
                        doInBackgroundPay2();
                        doInBackgroundPay3();
                        doInBackgroundPay4();
                        doInBackgroundPay5();
                        doInBackgroundPay6();
                        return null;
                    }
                }.execute();
            }
        });

        storeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(PaymentsReport.this, DPstartDate, startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        storeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(PaymentsReport.this, DPendDate, endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
            String query = "Select datepart(MM,[Date]) as Month, sum([Amount Tendered]) as NetPayment, count([Receipt No_]) as  NoOfReceipt from ["+CName+"$Trans_ Payment Entry] where [Tender Type] in ("+PayTypeVal+") and datepart(MM,[Date]) in ("+MonthVal+") and [Store No_] = '"+selectStoreNo+"' and [Date] between '" + storeStartDate + "' and '" + storeEndDate + "' group by datepart(MM,[Date])";

            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    Netpaymonth.add(rs.getString("Month"));
                    NetpayAmt.add(rs.getDouble("NetPayment"));
                    AvgpayAmt.add(rs.getDouble("NetPayment") / rs.getDouble("NoOfReceipt"));
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
    private void doInBackgroundPay2() {
        count1 = 1;
        
        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query2 = "Select sum(PE.[Amount Tendered]) as netamt, tt.[Description], count([Receipt No_]) as receNo from [dbo].["+CName+"$Tender Type] as tt, [dbo].["+CName+"$Trans_ Payment Entry] as PE where PE.[Tender Type] in ("+PayTypeVal+") and datepart(MM,[Date]) in ("+MonthVal+") and tt.[Code] = PE.[Tender Type] and tt.[Store No_] = PE.[Store No_] and PE.[Date] between '"+storeStartDate+"' and '"+storeEndDate+"' and PE.[Store No_] = '"+selectStoreNo+"' group by tt.[Description]";

            try {
                Statement stmt = connect.createStatement();
                ResultSet rs2 = stmt.executeQuery(query2);

                Log.w("Android", "database Connected");

                while(rs2.next())
                {
                    Netpaytype.add(rs2.getString("Description"));
                    NetpaytypeAmt.add(rs2.getDouble("netamt"));
                    AvgNetpaytypeAmt.add(rs2.getDouble("netamt") / rs2.getDouble("receNo"));
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private void doInBackgroundPay3() {

        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query3 = "Select sum([Amount Tendered]) as netamt, DATEPART(MM,[Date]) as Month from [dbo].["+CName+"$Tender Type] as tt, [dbo].["+CName+"$Trans_ Payment Entry] as PE where tt.[Code] = PE.[Tender Type] and PE.[Date] between '"+storeStartDate+"' and '"+storeEndDate+"' and tt.[Store No_] = '"+selectStoreNo+"' and tt.[Description] in ('Cash') group by DATEPART(MM,[Date])";

            try {
                Statement stmt = connect.createStatement();
                ResultSet rs3 = stmt.executeQuery(query3);

                Log.w("Android", "database Connected");

                while(rs3.next())
                {
                    NetcardAmt.add(Math.abs(rs3.getDouble("netamt")));
                    Months1.add(MonthsNew[Integer.parseInt(rs3.getString("Month"))]);
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private void doInBackgroundPay4() {
        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query4 = "Select sum([Amount Tendered]) as netamt, DATEPART(MM,[Date]) as Month from [dbo].["+CName+"$Tender Type] as tt, [dbo].["+CName+"$Trans_ Payment Entry] as PE where tt.[Code] = PE.[Tender Type] and PE.[Date] between '"+storeStartDate+"' and '"+storeEndDate+"' and tt.[Store No_] = '"+selectStoreNo+"' and tt.[Description] in ('Cards') group by DATEPART(MM,[Date])";

            try {
                Statement stmt = connect.createStatement();
                ResultSet rs3 = stmt.executeQuery(query4);

                Log.w("Android", "database Connected");

                while(rs3.next())
                {
                    NetcashAmt.add(Math.abs(rs3.getDouble("netamt")));
                    Months2.add(MonthsNew[Integer.parseInt(rs3.getString("Month"))]);
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private void doInBackgroundPay5() {
        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");

            String query5 = "Select sum([Amount Tendered]) as netamt, DATEPART(MM,[Date]) as Month from [dbo].["+CName+"$Tender Type] as tt, [dbo].["+CName+"$Trans_ Payment Entry] as PE where tt.[Code] = PE.[Tender Type] and PE.[Date] between '"+storeStartDate+"' and '"+storeEndDate+"' and tt.[Store No_] = '"+selectStoreNo+"' and tt.[Description] in ('Gift Card') group by DATEPART(MM,[Date])";

            try {
                Statement stmt = connect.createStatement();
                ResultSet rs3 = stmt.executeQuery(query5);

                Log.w("Android", "database Connected");

                while(rs3.next())
                {
                    NetchequeAmt.add(Math.abs(rs3.getDouble("netAmt")));
                    Months3.add(MonthsNew[Integer.parseInt(rs3.getString("Month"))]);
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private void doInBackgroundPay6() {
        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query6 = "Select sum([Amount Tendered]) as netamt, DATEPART(MM,[Date]) as Month from [dbo].["+CName+"$Tender Type] as tt, [dbo].["+CName+"$Trans_ Payment Entry] as PE where tt.[Code] = PE.[Tender Type] and PE.[Date] between '"+storeStartDate+"' and '"+storeEndDate+"' and tt.[Store No_] = '"+selectStoreNo+"' and tt.[Description] in ('Check') group by DATEPART(MM,[Date])";

            try {
                Statement stmt = connect.createStatement();
                ResultSet rs3 = stmt.executeQuery(query6);

                Log.w("Android", "database Connected");

                while(rs3.next())
                {
                    NetGiftcardAmt.add(Math.abs(rs3.getDouble("netamt")));
                    Months4.add(MonthsNew[Integer.parseInt(rs3.getString("Month"))]);
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void diffPayTypes() {
        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query6 = "select Distinct Code, Description FROM [dbo].["+CName+"$Tender Type] order by Description";

            try {
                Statement stmt = connect.createStatement();
                ResultSet rs4 = stmt.executeQuery(query6);

                Log.w("Android", "database Connected");

                while(rs4.next())
                {
                    PayTypeCode.add(rs4.getString("Code"));
                    PayTypeDescrip.add(rs4.getString("Description"));
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
        for (int i = 0 ; i < Netpaymonth.size() ; i++)
        {
            TL.setStretchAllColumns(true);

            TR = new TableRow(this);

            TV1 = new TextView(this);
            TV2 = new TextView(this);
            TV3 = new TextView(this);

            TV1.setText(MonthsNew[Integer.parseInt(Netpaymonth.get(i))]);

            double val11 = Math.abs(NetpayAmt.get(i)) / Double.parseDouble(String.valueOf(intVal[index]));
            val11 = Math.round(val11 * 100.0) / 100.0;

            TV2.setText(NumberFormat.getNumberInstance(Locale.US).format(val11));

            double val12 = Math.abs(AvgpayAmt.get(i)) / Double.parseDouble(String.valueOf(intVal[index]));
            val12 = Math.round(val12 * 100.0) / 100.0;

            TV3.setText(NumberFormat.getNumberInstance(Locale.US).format(val12));

            AmountToGraph11.add(Float.parseFloat(String.valueOf(val11)));
            AmountToGraph12.add(Float.parseFloat(String.valueOf(val12)));

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
        for (int i = 0 ; i < Netpaytype.size() ; i++)
        {
            TL2.setStretchAllColumns(true);

            TR2 = new TableRow(this);

            TV12 = new TextView(this);
            TV22 = new TextView(this);
            TV32 = new TextView(this);

            TV12.setText(Netpaytype.get(i));

            double val21 = Math.abs(NetpaytypeAmt.get(i)) / Double.parseDouble(String.valueOf(intVal[index]));
            val21 = Math.round(val21 * 100.0) / 100.0;

            TV22.setText(NumberFormat.getNumberInstance(Locale.US).format(val21));

            double val22 = Math.abs(AvgNetpaytypeAmt.get(i)) / Double.parseDouble(String.valueOf(intVal[index]));
            val22 = Math.round(val22 * 100.0) / 100.0;

            TV32.setText(NumberFormat.getNumberInstance(Locale.US).format(val22));

            AmountToGraph21.add(Float.parseFloat(String.valueOf(val21)));
            AmountToGraph22.add(Float.parseFloat(String.valueOf(val22)));

            TV12.setTextSize(15);
            TV12.setPadding(10, 0, 10, 0);
            TV12.setGravity(Gravity.LEFT);
            TV12.setTextColor(Color.WHITE);
            TV12.setBackground(getDrawable(R.drawable.rect_border));

            TV22.setTextSize(15);
            TV22.setPadding(10, 0, 10, 0);
            TV22.setGravity(Gravity.RIGHT);
            TV22.setTextColor(Color.WHITE);
            TV22.setBackground(getDrawable(R.drawable.rect_border));

            TV32.setTextSize(15);
            TV32.setPadding(10, 0, 10, 0);
            TV32.setGravity(Gravity.RIGHT);
            TV32.setTextColor(Color.WHITE);
            TV32.setBackground(getDrawable(R.drawable.rect_border));

            TR2.addView(TV12);
            TR2.addView(TV22);
            TR2.addView(TV32);

            TL2.addView(TR2);
        }
        for (int i = 0 ; i < Months1.size() ; i++)
        {
            double val31 = Math.abs(NetcardAmt.get(i)) / Double.parseDouble(String.valueOf(intVal[index]));
            val31 = Math.round(val31 * 100.0) / 100.0;
            AmountToGraph3.add(Float.parseFloat(String.valueOf(val31)));
        }
        for (int i = 0 ; i < Months2.size() ; i++)
        {
            double val31 = Math.abs(NetcashAmt.get(i)) / Double.parseDouble(String.valueOf(intVal[index]));
            val31 = Math.round(val31 * 100.0) / 100.0;
            AmountToGraph4.add(Float.parseFloat(String.valueOf(val31)));
        }
        for (int i = 0 ; i < Months3.size() ; i++)
        {
            double val31 = Math.abs(NetchequeAmt.get(i)) / Double.parseDouble(String.valueOf(intVal[index]));
            val31 = Math.round(val31 * 100.0) / 100.0;
            AmountToGraph5.add(Float.parseFloat(String.valueOf(val31)));
        }
        for (int i = 0 ; i < Months4.size() ; i++)
        {
            double val31 = Math.abs(NetGiftcardAmt.get(i)) / Double.parseDouble(String.valueOf(intVal[index]));
            val31 = Math.round(val31 * 100.0) / 100.0;
            AmountToGraph6.add(Float.parseFloat(String.valueOf(val31)));
        }
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner) adapterView;
        if (spinner.getId() == R.id.spin_filter)
        {
            index = i;

            dropDownValue = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(adapterView.getContext(), "Sorted by: " + dropDownValue, Toast.LENGTH_SHORT).show();

            while(TL.getChildCount() > 1)
            {
                TL.removeView(TL.getChildAt(TL.getChildCount() - 1));
            }

            while(TL2.getChildCount() > 1)
            {
                TL2.removeView(TL2.getChildAt(TL2.getChildCount() - 1));
            }

            AmountToGraph11.clear();
            AmountToGraph12.clear();
            AmountToGraph21.clear();
            AmountToGraph22.clear();
            AmountToGraph3.clear();
            AmountToGraph4.clear();
            AmountToGraph5.clear();
            AmountToGraph6.clear();

            PrintTable();
        }
        else
        if (spinner.getId() == R.id.spin_storeselect)
        {
            dropDownStoreNo = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(adapterView.getContext(), "Selected Store : " + dropDownStoreNo, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    public void StartUpdateLabel() {
        String myStartFormat = "yyyy-MM-dd";
        SimpleDateFormat STARTsdf = new SimpleDateFormat(myStartFormat,Locale.US);
        storeStart.setText(STARTsdf.format(startCalendar.getTime()));
    }
    public void EndUpdateLabel() {
        String myEndFormat = "yyyy-MM-dd";
        SimpleDateFormat ENDsdf = new SimpleDateFormat(myEndFormat,Locale.US);
        storeEnd.setText(ENDsdf.format(endCalendar.getTime()));
    }
}
