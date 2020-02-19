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
import android.widget.DatePicker;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SalesHistory extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TableLayout TL;
    TableRow TR;
    TextView TV1, TV2, TV3, TV4, TV5, TV6, TV7, TV8, TV9, TV10, TV11, TV12, TV13;
    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    TextView storeStart;
    TextView storeEnd;

    ImageView refresh;

    ImageView back;

    String storeStartDate = "2001-01-01";

    String storeEndDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

    Calendar startCalendar = Calendar.getInstance();
    Calendar endCalendar = Calendar.getInstance();

    String CName;

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal= {1,1000,100000,1000000,10000000};

    String dropDownYear;

    int index;

    Button selectWeek, selectQuater, selectMonth;

    Button /*selectItmDivision, selectItmCategory, selectItmProductGrp, SelectItm,*/selectBranchName;

    String[] filterOption = {"Select to apply","Item Division","Item Category","Product Group","Item Description"};

    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> UserItems = new ArrayList<>();

    String[] listItems2;
    boolean[] checkedItems2;
    ArrayList<Integer> UserItems2 = new ArrayList<>();

    String[] listItems3;
    boolean[] checkedItems3;
    ArrayList<Integer> UserItems3 = new ArrayList<>();

    ArrayList<String> Week123 = new ArrayList<>();
    ArrayList<String> Quater123 = new ArrayList<>();
    ArrayList<String> Month123 = new ArrayList<>();

    ArrayList<String> dummyWeek123 = new ArrayList<>();
    ArrayList<String> dummyQuater123 = new ArrayList<>();
    ArrayList<String> dummyMonth123 = new ArrayList<>();

    public static String WeekStr = "''", QuaterStr = "''", MonthStr = "''";
    public static String ItmDivStr = "", ItmCategStr = "", ItmProGrpStr = "", ItmStr = "", Str = "";

    TextView reset;

    Intent intent;

    ArrayList<String> ItemDivisionCode = new ArrayList<>();    ArrayList<String> ItemDivisionDescription = new ArrayList<>();    ArrayList<String> ItemCategoryCode = new ArrayList<>();    ArrayList<String> ItemCategoryDescription = new ArrayList<>();    ArrayList<String> ItemProductGrpCode = new ArrayList<>();    ArrayList<String> ItemProductGrpDescription = new ArrayList<>();    ArrayList<String> ItemCode = new ArrayList<>();    ArrayList<String> ItemDescription = new ArrayList<>();
    ArrayList<String> dummyItemDivisionCode = new ArrayList<>();    ArrayList<String> dummyItemDivisionDescription = new ArrayList<>();    ArrayList<String> dummyItemCategoryCode = new ArrayList<>();    ArrayList<String> dummyItemCategoryDescription = new ArrayList<>();    ArrayList<String> dummyItemProductGrpCode = new ArrayList<>();    ArrayList<String> dummyItemProductGrpDescription = new ArrayList<>();    ArrayList<String> dummyItemCode = new ArrayList<>();    ArrayList<String> dummyItemDescription = new ArrayList<>();
    ArrayList<String> StoreNo = new ArrayList<>();
    ArrayList<String> StoreName = new ArrayList<>();

    ArrayList<String> TabItemNo = new ArrayList<>();
    ArrayList<String> TabVarCode = new ArrayList<>();
    ArrayList<String> TabItemDes = new ArrayList<>();
    ArrayList<String> TabLocCode = new ArrayList<>();
    ArrayList<String> TabLocName = new ArrayList<>();
    ArrayList<String> TabItmCateg = new ArrayList<>();
    ArrayList<String> TabProGrp = new ArrayList<>();
    ArrayList<String> TabDiv = new ArrayList<>();
    ArrayList<Double> TabSalesQuant = new ArrayList<>();
    ArrayList<Double> TabSaleAmt = new ArrayList<>();
    ArrayList<Double> TabCostAmt = new ArrayList<>();

    ArrayList<String> IvenTabItemNo = new ArrayList<>();
    ArrayList<String> IvenTabVarCod = new ArrayList<>();
    ArrayList<String> IvenTabLocCode = new ArrayList<>();
    ArrayList<Double> IvenTabSaleQuant = new ArrayList<>();

    ArrayList<String> VariantTabItemNo = new ArrayList<>();
    ArrayList<String> VariantTabItemVarCode = new ArrayList<>();
    ArrayList<String> VariantTabItemVarDes = new ArrayList<>();

    String CompStart;

    ArrayList<String> getDivisionDescrip = new ArrayList<>();
    ArrayList<String> getItemCategoryDescription = new ArrayList<>();
    ArrayList<String> getItemProductGrpDescription = new ArrayList<>();
    ArrayList<String> getItemDescription = new ArrayList<>();
    ArrayList<String> getBranchName = new ArrayList<>();

    MultiSelectDialog multiSelectDialog1,multiSelectDialog2,multiSelectDialog3,multiSelectDialog4;

    String txt = "";

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_history);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TL = findViewById(R.id.TL);

        Spinner spinner = findViewById(R.id.spin_filter);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, amtFilter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Spinner optionSpinner = findViewById(R.id.filterOption);
        optionSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, filterOption);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionSpinner.setAdapter(adapter1);

        back = findViewById(R.id.img10);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        intent = getIntent();
        CName = intent.getStringExtra("Company Name");        Week123 = intent.getStringArrayListExtra("Week");        Quater123 = intent.getStringArrayListExtra("Quater");        Month123 = intent.getStringArrayListExtra("Month");

        dummyWeek123.addAll(Week123);        dummyQuater123.addAll(Quater123);        dummyMonth123.addAll(Month123);

        reset = findViewById(R.id.tv_resetFilter);
        reset.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                storeStartDate = "2001-01-01";
                storeEndDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                storeStart.setText(storeStartDate);
                storeEnd.setText(storeEndDate);

                Str = "";

                Week123.clear();                Quater123.clear();                Month123.clear();
                Week123.addAll(dummyWeek123);                Quater123.addAll(dummyQuater123);                Month123.addAll(dummyMonth123);
                filterWeekValues();                filterQuaterValues();                filterMonthValues();

                ItemDivisionCode.addAll(dummyItemDivisionCode);                ItemDivisionDescription.addAll(dummyItemDivisionDescription);                ItemCategoryCode.addAll(dummyItemCategoryCode);                ItemCategoryDescription.addAll(dummyItemCategoryDescription);                ItemProductGrpCode.addAll(dummyItemProductGrpCode);                ItemProductGrpDescription.addAll(dummyItemProductGrpDescription);                ItemCode.addAll(dummyItemCode);                ItemDescription.addAll(dummyItemDescription);
                filterItemDiv();                filterItemCate();                filterItmProGrp();                filterItm();
                Toast.makeText(SalesHistory.this, "Reset Successful", Toast.LENGTH_SHORT).show();

                selectWeek.setText("Week");
                selectQuater.setText("Quarter");
                selectMonth.setText("Month");
                selectBranchName.setText("Branch Name");
            }
        });

        final ProgressDialog progressDialog = new ProgressDialog(SalesHistory.this);

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
                dummyItemDivisionCode.addAll(ItemDivisionCode);        dummyItemDivisionDescription.addAll(ItemDivisionDescription);        dummyItemCategoryCode.addAll(ItemCategoryCode);        dummyItemCategoryDescription.addAll(ItemCategoryDescription);        dummyItemProductGrpCode.addAll(ItemProductGrpCode);        dummyItemProductGrpDescription.addAll(ItemProductGrpDescription);        dummyItemCode.addAll(ItemCode);        dummyItemDescription.addAll(ItemDescription);
                filterWeekValues();        filterQuaterValues();        filterMonthValues();        filterItemDiv();        filterItemCate();        filterItmProGrp();        filterItm();        filterBranchName();
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
                BranchName();
                ItemDetails();
                ItmVariant();
                return null;
            }
        }.execute();

        storeStart = findViewById(R.id.tv_startdate);
        storeEnd = findViewById(R.id.tv_enddate);
        storeStart.setText(storeStartDate);
        storeEnd.setText(storeEndDate);
    }

    public void doInBackground1() {

        final DatePickerDialog.OnDateSetListener DPstartDate = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                startCalendar.set(Calendar.YEAR, year);
                startCalendar.set(Calendar.MONTH, month);
                startCalendar.set(Calendar.DAY_OF_MONTH, day);
                StartUpdateLabel();

                storeStartDate = storeStart.getText().toString();

                final ProgressDialog progressDialog = new ProgressDialog(SalesHistory.this);

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
                        progressDialog.dismiss();
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        if (Looper.myLooper() == null)
                        {
                            Looper.prepare();
                        }
                        someRepeatition();
                        return null;
                    }
                }.execute();

            }
        };

        final DatePickerDialog.OnDateSetListener DPendDate = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                endCalendar.set(Calendar.YEAR, year);
                endCalendar.set(Calendar.MONTH, month);
                endCalendar.set(Calendar.DAY_OF_MONTH, day);
                EndUpdateLabel();

                storeEndDate = storeEnd.getText().toString();

                final ProgressDialog progressDialog = new ProgressDialog(SalesHistory.this);

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
                        progressDialog.dismiss();
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        if (Looper.myLooper() == null)
                        {
                            Looper.prepare();
                        }
                        someRepeatition();
                        return null;
                    }
                }.execute();
            }
        };

        refresh = findViewById(R.id.iv_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {

                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);

                while (TL.getChildCount() > 1)
                {
                    TL.removeView(TL.getChildAt(TL.getChildCount() - 1));
                }

                TabItemNo.clear();                TabVarCode.clear();                TabItemDes.clear();                TabLocCode.clear();                TabLocName.clear();                TabItmCateg.clear();                TabProGrp.clear();                TabDiv.clear();                TabSalesQuant.clear();                TabSaleAmt.clear();                TabCostAmt.clear();                IvenTabItemNo.clear();                IvenTabVarCod.clear();                IvenTabLocCode.clear();                IvenTabSaleQuant.clear();

                final ProgressDialog progressDialog = new ProgressDialog(SalesHistory.this);

                new AsyncTask<Void, Void, Void>(){

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
                        storeStart.setText(storeStartDate);
                        storeEnd.setText(storeEndDate);
                        Str = "";

                        progressDialog.dismiss();
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        if (Looper.myLooper() == null)
                        {
                            Looper.prepare();
                        }
                        doInBackground1();
                        CompStartDate();
                        doInBackground2();

                        return null;
                    }
                }.execute();

                refresh.startAnimation(rotation);
            }
        });

        storeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Week123.clear();                Quater123.clear();                Month123.clear();
                Week123.addAll(dummyWeek123);                Quater123.addAll(dummyQuater123);                Month123.addAll(dummyMonth123);
                filterWeekValues();                filterQuaterValues();                filterMonthValues();

                final DatePickerDialog datePickerDialog = new DatePickerDialog(SalesHistory.this, DPstartDate, startCalendar.get(Calendar.YEAR),startCalendar.get(Calendar.MONTH),startCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (i == DialogInterface.BUTTON_POSITIVE) {
                            DatePicker datePicker = datePickerDialog.getDatePicker();
                            DPstartDate.onDateSet(datePicker, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                        }
                    }
                });

                datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == DialogInterface.BUTTON_NEGATIVE) {
                            final ProgressDialog progressDialog = new ProgressDialog(SalesHistory.this);

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
                                    progressDialog.dismiss();
                                }

                                @Override
                                protected Void doInBackground(Void... voids) {
                                    if (Looper.myLooper() == null)
                                    {
                                        Looper.prepare();
                                    }
                                    someRepeatition();
                                    return null;
                                }
                            }.execute();
                            dialogInterface.cancel();
                        }
                    }
                });

                datePickerDialog.setCancelable(false);
                datePickerDialog.show();
            }
        });


        storeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Week123.clear();                Quater123.clear();                Month123.clear();
                Week123.addAll(dummyWeek123);                Quater123.addAll(dummyQuater123);                Month123.addAll(dummyMonth123);
                filterWeekValues();                filterQuaterValues();                filterMonthValues();

                final DatePickerDialog datePickerDialog = new DatePickerDialog(SalesHistory.this, DPendDate, endCalendar.get(Calendar.YEAR),endCalendar.get(Calendar.MONTH),endCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == DialogInterface.BUTTON_POSITIVE) {
                            DatePicker datePicker = datePickerDialog.getDatePicker();
                            DPendDate.onDateSet(datePicker, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                        }
                    }
                });

                datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == DialogInterface.BUTTON_NEGATIVE) {
                            final ProgressDialog progressDialog = new ProgressDialog(SalesHistory.this);

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
                                    progressDialog.dismiss();
                                }

                                @Override
                                protected Void doInBackground(Void... voids) {
                                    if (Looper.myLooper() == null)
                                    {
                                        Looper.prepare();
                                    }
                                    someRepeatition();
                                    return null;
                                }
                            }.execute();
                            dialogInterface.cancel();
                        }
                    }
                });

                datePickerDialog.setCancelable(false);
                datePickerDialog.show();
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
            String query = "select VLE.[Item No_] as TabItmNo, ItmMas.Description as TabItmDes, VLE.[Variant Code] as TabVarCod, VLE.[Location Code] as TabLocCod, StrMas.Name as TabLocDes, VLE.[Item Category] as TabItmCateg, ItmCateg.Description as TabItmCategDes, VLE.[Product Group] as TabProGrp, ItmProGrp.Description as TabProGrpDes, VLE.Division as TabDivCod, ItmDiv.Description as TabDivDes, sum([Item Ledger Entry Quantity]) * (-1) as SaleQuant, sum([Sales Amount (Actual)]) as SaleAmt, sum([Cost Amount (Actual)]) * (-1) as CostAmt FROM [dbo].["+CName+"$Value Entry] as VLE, ["+CName+"$Item] as ItmMas, ["+CName+"$Store] as StrMas, ["+CName+"$Item Category] as ItmCateg, ["+CName+"$Product Group] as ItmProGrp, ["+CName+"$Division] as ItmDiv Where VLE.[Item No_] = ItmMas.No_ and VLE.[Location Code] = StrMas.No_ and VLE.[Item Category] = ItmCateg.Code and VLE.[Product Group] = ItmProGrp.Code and VLE.Division = ItmDiv.Code and VLE.[Item Ledger Entry Type] = '1' and VLE.[Posting Date] between '"+storeStartDate+"' and '"+storeEndDate+"' and DATEPART(WEEK,[Posting Date]) in ("+WeekStr+") and DATEPART(QUARTER,[Posting Date]) in ("+QuaterStr+") and DATEPART(MONTH, [Posting Date]) in ("+MonthStr+") and VLE.Division in ("+ItmDivStr+") and VLE.[Item Category] in ("+ItmCategStr+") and VLE.[Product Group] in ("+ItmProGrpStr+") and VLE.[Item No_] in ("+ItmStr+") and VLE.[Location Code] in ("+Str+") group by VLE.[Item No_], ItmMas.Description, VLE.[Variant Code], VLE.[Location Code], StrMas.Name, VLE.[Item Category], ItmCateg.Description, VLE.[Product Group], ItmProGrp.Description, VLE.Division, ItmDiv.Description order by VLE.[Item No_], ItmMas.Description, VLE.[Variant Code], VLE.[Location Code], StrMas.Name, VLE.[Item Category], ItmCateg.Description, VLE.[Product Group], ItmProGrp.Description, VLE.Division, ItmDiv.Description";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {
                    TabItemNo.add(rs.getString("TabItmNo"));
                    TabVarCode.add(rs.getString("TabVarCod"));
                    TabItemDes.add(rs.getString("TabItmDes"));
                    TabLocCode.add(rs.getString("TabLocCod"));
                    TabLocName.add(rs.getString("TabLocDes"));
                    TabItmCateg.add(rs.getString("TabItmCateg") + " - " + rs.getString("TabItmCategDes"));
                    TabProGrp.add(rs.getString("TabProGrp") + " - " + rs.getString("TabProGrpDes"));
                    TabDiv.add(rs.getString("TabDivCod") + " - " + rs.getString("TabDivDes"));

                    TabSalesQuant.add(rs.getDouble("SaleQuant"));
                    TabSaleAmt.add(rs.getDouble("SaleAmt"));
                    TabCostAmt.add(rs.getDouble("CostAmt"));
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
            String query = "select VLE.[Item No_] as TabItmNo, VLE.[Variant Code] as TabVarCod, VLE.[Location Code] as TabLocCod, sum([Item Ledger Entry Quantity]) as SaleQuant FROM [dbo].["+CName+"$Value Entry] as VLE, ["+CName+"$Item] as ItmMas, ["+CName+"$Item Variant] as ItmVar, ["+CName+"$Store] as StrMas Where VLE.[Item No_] = ItmMas.No_ and VLE.[Variant Code] = ItmVar.Code and VLE.[Item No_] = ItmVar.[Item No_] and VLE.[Location Code] = StrMas.No_ and VLE.[Posting Date] between '"+CompStart+"' and '"+storeStartDate+"' and VLE.[Location Code] in ("+Str+") group by VLE.[Item No_], VLE.[Variant Code], VLE.[Location Code] order by VLE.[Item No_], VLE.[Variant Code], VLE.[Location Code]";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {
                    IvenTabItemNo.add(rs.getString("TabItmNo"));
                    IvenTabVarCod.add(rs.getString("TabVarCod"));
                    IvenTabLocCode.add(rs.getString("TabLocCod"));

                    IvenTabSaleQuant.add(rs.getDouble("SaleQuant"));
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

        for (int i = 0 ; i < TabItemNo.size() ; i++)
        {
            TR = new TableRow(this);
            TV1 = new TextView(this);            TV2 = new TextView(this);            TV3 = new TextView(this);            TV4 = new TextView(this);            TV5 = new TextView(this);            TV6 = new TextView(this);            TV7 = new TextView(this);            TV8 = new TextView(this);            TV9 = new TextView(this);            TV10 = new TextView(this);            TV11 = new TextView(this);            TV12 = new TextView(this);            TV13 = new TextView(this);

            TV1.setTextSize(15);
            TV1.setPadding(10, 0, 10, 0);
            TV1.setGravity(Gravity.LEFT);
            TV1.setTextColor(Color.WHITE);
            TV1.setBackground(getDrawable(R.drawable.rect_border));

            TV2.setTextSize(15);
            TV2.setPadding(10, 0, 10, 0);
            TV2.setGravity(Gravity.LEFT);
            TV2.setTextColor(Color.WHITE);
            TV2.setBackground(getDrawable(R.drawable.rect_border));

            TV3.setTextSize(15);
            TV3.setPadding(10, 0, 10, 0);
            TV3.setGravity(Gravity.LEFT);
            TV3.setTextColor(Color.WHITE);
            TV3.setBackground(getDrawable(R.drawable.rect_border));

            TV4.setTextSize(15);
            TV4.setPadding(10, 0, 10, 0);
            TV4.setGravity(Gravity.LEFT);
            TV4.setTextColor(Color.WHITE);
            TV4.setBackground(getDrawable(R.drawable.rect_border));

            TV5.setTextSize(15);
            TV5.setPadding(10, 0, 10, 0);
            TV5.setGravity(Gravity.LEFT);
            TV5.setTextColor(Color.WHITE);
            TV5.setBackground(getDrawable(R.drawable.rect_border));

            TV6.setTextSize(15);
            TV6.setPadding(10, 0, 10, 0);
            TV6.setGravity(Gravity.LEFT);
            TV6.setTextColor(Color.WHITE);
            TV6.setBackground(getDrawable(R.drawable.rect_border));

            TV7.setTextSize(15);
            TV7.setPadding(10, 0, 10, 0);
            TV7.setGravity(Gravity.LEFT);
            TV7.setTextColor(Color.WHITE);
            TV7.setBackground(getDrawable(R.drawable.rect_border));

            TV8.setTextSize(15);
            TV8.setPadding(10, 0, 10, 0);
            TV8.setGravity(Gravity.LEFT);
            TV8.setTextColor(Color.WHITE);
            TV8.setBackground(getDrawable(R.drawable.rect_border));

            TV9.setTextSize(15);
            TV9.setPadding(10, 0, 10, 0);
            TV9.setGravity(Gravity.RIGHT);
            TV9.setTextColor(Color.WHITE);
            TV9.setBackground(getDrawable(R.drawable.rect_border));

            TV10.setTextSize(15);
            TV10.setPadding(10, 0, 10, 0);
            TV10.setGravity(Gravity.RIGHT);
            TV10.setTextColor(Color.WHITE);
            TV10.setBackground(getDrawable(R.drawable.rect_border));

            TV11.setTextSize(15);
            TV11.setPadding(10, 0, 10, 0);
            TV11.setGravity(Gravity.RIGHT);
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

            TR.addView(TV1);            TR.addView(TV2);            TR.addView(TV3);            TR.addView(TV4);            TR.addView(TV5);            TR.addView(TV6);            TR.addView(TV7);            TR.addView(TV8);            TR.addView(TV9);            TR.addView(TV10);            TR.addView(TV11);            TR.addView(TV12);            TR.addView(TV13);
            TL.addView(TR);

            TV1.setText(TabItemNo.get(i));

            for (int j = 0 ; j < VariantTabItemVarCode.size() ; j++)
            {
                if (TabVarCode.get(i).equals(VariantTabItemVarCode.get(j)) && TabItemNo.get(i).equals(VariantTabItemNo.get(j)))
                {
                    TV2.setText(VariantTabItemVarDes.get(i));
                    break;
                }
                else
                {
                    TV2.setText("Main Item");
                }
            }

            TV3.setText(TabItemDes.get(i));
            TV4.setText(TabLocCode.get(i));
            TV5.setText(TabLocName.get(i));
            TV6.setText(TabItmCateg.get(i));
            TV7.setText(TabProGrp.get(i));
            TV8.setText(TabDiv.get(i));

            TV9.setText("0");
            for (int j = 0 ; j < IvenTabItemNo.size() ; j++)
            {
                if (IvenTabItemNo.get(j).equals(TabItemNo.get(i)) && IvenTabVarCod.get(j).equals(TabVarCode.get(i)) && IvenTabLocCode.get(j).equals(TabLocCode.get(i)))
                {
                    TV9.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((IvenTabSaleQuant.get(j) / intVal[index]) * 100.0) / 100.0));
                    break;
                }
            }

            TV10.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((TabSalesQuant.get(i) / intVal[index]) * 100.0) / 100.0));
            TV11.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((TabSaleAmt.get(i) / intVal[index]) * 100.0) / 100.0));
            TV12.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round(((TabSaleAmt.get(i) - TabCostAmt.get(i)) / intVal[index]) * 100.0) / 100.0));
            TV13.setText("0");
            if (TabSaleAmt.get(i) != 0)
            {
                TV13.setText(String.valueOf(Math.round(((TabSaleAmt.get(i) - TabCostAmt.get(i)) * 100 / TabSaleAmt.get(i)) * 100.0) / 100.0));
            }
        }
    }

    public void Week1234() {
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
            String query = "Select distinct DATEPART(WEEK,[Posting Date]) as WEEK FROM [dbo].["+CName+"$Value Entry] where [Posting Date] between '"+storeStartDate+"' and '"+storeEndDate+"' and DATEPART(QUARTER,[Posting Date]) in ("+QuaterStr+") and DATEPART(MONTH,[Posting Date]) in ("+MonthStr+") order by DATEPART(WEEK,[Posting Date])";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                Week123.clear();

                while (rs.next()) {
                    Week123.add(rs.getString("WEEK"));
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
    public void Quater1234() {
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
            String query = "Select distinct DATEPART(QUARTER,[Posting Date]) as QUARTER FROM [dbo].["+CName+"$Value Entry] where [Posting Date] between '"+storeStartDate+"' and '"+storeEndDate+"' and DATEPART(WEEK,[Posting Date]) in ("+WeekStr+") and DATEPART(MONTH,[Posting Date]) in ("+MonthStr+") order by DATEPART(QUARTER,[Posting Date])";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                Quater123.clear();

                while (rs.next()) {
                    Quater123.add(rs.getString("QUARTER"));
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
    public void Month1234() {
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
            String query = "Select distinct DATEPART(MONTH,[Posting Date]) as MONTH FROM [dbo].["+CName+"$Value Entry] where [Posting Date] between '"+storeStartDate+"' and '"+storeEndDate+"' and DATEPART(WEEK,[Posting Date]) in ("+WeekStr+") and DATEPART(QUARTER,[Posting Date]) in ("+QuaterStr+") order by DATEPART(MONTH,[Posting Date])";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                Month123.clear();

                while (rs.next()) {
                    Month123.add(rs.getString("MONTH"));
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner) adapterView;
        ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter();
        if (spinner.getId() == R.id.spin_filter)
        {
            index = i;

            dropDownYear = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(adapterView.getContext(), "Sorted by: " + dropDownYear, Toast.LENGTH_SHORT).show();

            PrintTable();
        }
        else
            if (spinner.getId() == R.id.filterOption)
            {
                String myString = "Select to apply";

                int spinnerPosition = myAdap.getPosition(myString);

                String value = adapterView.getItemAtPosition(i).toString();

                switch(value)
                {
                    case "Item Division":
                    {
                        getDivisionDescrip.clear();
                        multiSelectDialog1.show(getSupportFragmentManager(), "multiSelectDialog");
                        spinner.setSelection(spinnerPosition);
                        break;
                    }
                    case "Item Category":
                    {
                        getItemCategoryDescription.clear();
                        multiSelectDialog2.show(getSupportFragmentManager(), "multiSelectDialog");
                        spinner.setSelection(spinnerPosition);
                        break;
                    }
                    case "Product Group":
                    {
                        getItemProductGrpDescription.clear();
                        multiSelectDialog3.show(getSupportFragmentManager(), "multiSelectDialog");
                        spinner.setSelection(spinnerPosition);
                        break;
                    }
                    case "Item Description":
                    {
                        getItemProductGrpDescription.clear();
                        multiSelectDialog4.show(getSupportFragmentManager(), "multiSelectDialog");
                        spinner.setSelection(spinnerPosition);
                        break;
                    }
                }
            }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    public void filterWeekValues() {

        listItems = Week123.toArray(new String[Week123.size()]);
        checkedItems = new boolean[listItems.length];

        selectWeek = findViewById(R.id.WeekSelect);

        selectWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SalesHistory.this);
                builder.setTitle("Select Week No.");

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
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        final ProgressDialog progressDialog = new ProgressDialog(SalesHistory.this);

                        new AsyncTask<Void, Void, Void>() {

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                progressDialog.setMessage("Loading Please Wait...");
                                progressDialog.show();
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);

                                String Store = "";
                                for(int i = 0 ; i < UserItems.size() ; i++)
                                {
                                    if (i == 0)
                                    {
                                        Store = selectWeek.getText().toString() + "\n" + listItems[UserItems.get(i)];
                                    }
                                    else
                                        Store = Store + ", " + listItems[UserItems.get(i)];
                                }
                                selectWeek.setText(Store);

                                progressDialog.dismiss();
                            }

                            @Override
                            protected Void doInBackground(Void... voids) {
                                if (Looper.myLooper() == null)
                                {
                                    Looper.prepare();
                                }
                                WeekStr = "''";
                                for (int i = 0; i < UserItems.size() ; i++)
                                {
                                    WeekStr = WeekStr + ", '" + listItems[UserItems.get(i)] + "'";
                                }

                                QuaterStr = "''";
                                for (int i = 0; i < Quater123.size() ; i++)
                                {
                                    QuaterStr = QuaterStr + ", '" + Quater123.get(i) + "'";
                                }

                                MonthStr = "''";
                                for (int i = 0; i < Month123.size() ; i++)
                                {
                                    MonthStr = MonthStr + ", '" + Month123.get(i) + "'";
                                }

                                Quater1234();
                                filterQuaterValues();

                                Month1234();
                                filterMonthValues();

                                QuaterStr = "''";
                                for (int i = 0; i < Quater123.size() ; i++)
                                {
                                    QuaterStr = QuaterStr + ", '" + Quater123.get(i) + "'";
                                }

                                MonthStr = "''";
                                for (int i = 0; i < Month123.size() ; i++)
                                {
                                    MonthStr = MonthStr + ", '" + Month123.get(i) + "'";
                                }

                                return null;
                            }
                        }.execute();

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
                            selectWeek.setText("Week");
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    public void filterQuaterValues() {

        listItems2 = Quater123.toArray(new String[Quater123.size()]);
        checkedItems2 = new boolean[listItems2.length];

        selectQuater = findViewById(R.id.QuaterSelect);

        selectQuater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SalesHistory.this);
                builder.setTitle("Select Quater Year");

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
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        final ProgressDialog progressDialog = new ProgressDialog(SalesHistory.this);

                        new AsyncTask<Void, Void, Void>(){

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                progressDialog.show();
                                progressDialog.setMessage("Loading Please Wait...");
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);

                                String Store123 = "";
                                for(int i = 0 ; i < UserItems2.size() ; i++)
                                {
                                    if (i == 0)
                                    {
                                        Store123 = selectQuater.getText().toString() + "\n" + listItems2[UserItems2.get(i)];
                                    }
                                    else
                                        Store123 = Store123 + ", " + listItems2[UserItems2.get(i)];
                                }
                                selectQuater.setText(Store123);

                                progressDialog.dismiss();
                            }

                            @Override
                            protected Void doInBackground(Void... voids) {
                                if (Looper.myLooper() == null)
                                {
                                    Looper.prepare();
                                }
                                QuaterStr = "''";
                                for (int i = 0; i < UserItems2.size() ; i++)
                                {
                                    QuaterStr = QuaterStr + ", '" + listItems2[UserItems2.get(i)] + "'";
                                }

                                WeekStr = "''";
                                for (int i = 0; i < Week123.size() ; i++)
                                {
                                    WeekStr = WeekStr + ", '" + Week123.get(i) + "'";
                                }

                                MonthStr = "''";
                                for (int i = 0; i < Month123.size() ; i++)
                                {
                                    MonthStr = MonthStr + ", '" + Month123.get(i) + "'";
                                }

                                Week1234();
                                filterWeekValues();

                                Month1234();
                                filterMonthValues();

                                WeekStr = "''";
                                for (int i = 0; i < Week123.size() ; i++)
                                {
                                    WeekStr = WeekStr + ", '" + Week123.get(i) + "'";
                                }

                                MonthStr = "''";
                                for (int i = 0; i < Month123.size() ; i++)
                                {
                                    MonthStr = MonthStr + ", '" + Month123.get(i) + "'";
                                }

                                return null;
                            }
                        }.execute();
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
                            selectQuater.setText("Quarter");
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }
    public void filterMonthValues() {

        listItems3 = Month123.toArray(new String[Month123.size()]);
        checkedItems3 = new boolean[listItems3.length];

        selectMonth = findViewById(R.id.MonthSelect);

        selectMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SalesHistory.this);
                builder.setTitle("Select Months");

                builder.setMultiChoiceItems(listItems3, checkedItems3, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                        if(isChecked)
                        {
                            if (! UserItems3.contains(position)){
                                UserItems3.add(position);
                            }
                        }
                        else
                        if (UserItems3.contains(position))
                        {
                            UserItems3.remove(UserItems3.indexOf(position));
                        }
                    }
                });

                builder.setCancelable(false);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        final ProgressDialog progressDialog = new ProgressDialog(SalesHistory.this);

                        new AsyncTask<Void, Void, Void>(){

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                progressDialog.setMessage("Loading Please Wait...");
                                progressDialog.show();
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);

                                String Store = "";
                                for(int i = 0 ; i < UserItems3.size() ; i++)
                                {
                                    if (i == 0)
                                    {
                                        Store = selectMonth.getText().toString() + "\n" + listItems3[UserItems3.get(i)];
                                    }
                                    else
                                        Store = Store + ", " + listItems3[UserItems3.get(i)];
                                }
                                selectMonth.setText(Store);

                                progressDialog.dismiss();
                            }

                            @Override
                            protected Void doInBackground(Void... voids) {
                                if (Looper.myLooper() == null)
                                {
                                    Looper.prepare();
                                }
                                MonthStr = "''";
                                for (int i = 0; i < UserItems3.size() ; i++)
                                {
                                    MonthStr = MonthStr + ", '" + listItems3[UserItems3.get(i)] + "'";
                                }

                                WeekStr = "''";
                                for (int i = 0; i < Week123.size() ; i++)
                                {
                                    WeekStr = WeekStr + ", '" + Week123.get(i) + "'";
                                }

                                QuaterStr = "''";
                                for (int i = 0; i < Quater123.size() ; i++)
                                {
                                    QuaterStr = QuaterStr + ", '" + Quater123.get(i) + "'";
                                }

                                Week1234();
                                filterWeekValues();

                                Quater1234();
                                filterQuaterValues();

                                WeekStr = "''";
                                for (int i = 0; i < Week123.size() ; i++)
                                {
                                    WeekStr = WeekStr + ", '" + Week123.get(i) + "'";
                                }

                                QuaterStr = "''";
                                for (int i = 0; i < Quater123.size() ; i++)
                                {
                                    QuaterStr = QuaterStr + ", '" + Quater123.get(i) + "'";
                                }
                                return null;
                            }
                        }.execute();
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
                        for(int i = 0 ; i < checkedItems3.length ; i++)
                        {
                            checkedItems3[i] = false;
                            UserItems3.clear();
                            selectMonth.setText("Month");
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }

    public void ItemDetails() {
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
            String query = "select ItmMas.[Division Code], ItmDivision.Description as ItmDivDes, ItmMas.[Item Category Code], ItmCateg.Description as ItmCateDes ,ItmMas.[Product Group Code], ItmProduct.Description as ItmProDes, ItmMas.No_, ItmMas.Description as ItmDes FROM [dbo].["+CName+"$Item] as ItmMas, ["+CName+"$Division] as ItmDivision, ["+CName+"$Item Category] as ItmCateg, ["+CName+"$Product Group] as ItmProduct where ItmMas.[Division Code] = ItmDivision.Code and ItmMas.[Item Category Code] = ItmCateg.Code and ItmMas.[Product Group Code] = ItmProduct.Code group by ItmMas.[Division Code], ItmDivision.Description, ItmMas.[Item Category Code], ItmCateg.Description ,ItmMas.[Product Group Code], ItmProduct.Description, ItmMas.No_, ItmMas.Description order by ItmMas.[Division Code], ItmDivision.Description, ItmMas.[Item Category Code], ItmCateg.Description ,ItmMas.[Product Group Code], ItmProduct.Description, ItmMas.No_, ItmMas.Description";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {

                    if (!ItemDivisionCode.contains(rs.getString("Division Code")))
                    {
                        ItemDivisionCode.add(rs.getString("Division Code"));
                        ItemDivisionDescription.add(rs.getString("ItmDivDes"));
                    }

                    if (!ItemCategoryCode.contains(rs.getString("Item Category Code")))
                    {
                        ItemCategoryCode.add(rs.getString("Item Category Code"));
                        ItemCategoryDescription.add(rs.getString("ItmCateDes"));
                    }

                    if (!ItemProductGrpCode.contains(rs.getString("Product Group Code")))
                    {
                        ItemProductGrpCode.add(rs.getString("Product Group Code"));
                        ItemProductGrpDescription.add(rs.getString("ItmProDes"));
                    }

                    ItemCode.add(rs.getString("No_"));
                    ItemDescription.add(rs.getString("ItmDes"));
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

    public void filterItemDiv() {

        final ArrayList<Integer> alreadySelectedItem1 = new ArrayList<>();
        ArrayList<MultiSelectModel> listOfItems1 = new ArrayList<>();

        for (int i = 0 ; i < ItemDivisionCode.size() ; i++)
        {
            listOfItems1.add(new MultiSelectModel(i+1,ItemDivisionDescription.get(i)));
        }

        multiSelectDialog1 = new MultiSelectDialog();

        multiSelectDialog1.title("Select Item Division\n\nSelected:" + txt);
        multiSelectDialog1.titleSize(24);
        multiSelectDialog1.positiveText("Apply");
        multiSelectDialog1.negativeText("Cancel");
        multiSelectDialog1.clearText("Clear All");
        multiSelectDialog1.preSelectIDsList(alreadySelectedItem1);
        multiSelectDialog1.multiSelectList(listOfItems1);

        multiSelectDialog1.onSubmit(new MultiSelectDialog.SubmitCallbackListener() {

            @SuppressLint("StaticFieldLeak")
            @Override
            public void onDismiss(ArrayList<Integer> ids, ArrayList<String> commonSeperatedData) throws PackageManager.NameNotFoundException {
                getDivisionDescrip.addAll(commonSeperatedData);
                final ProgressDialog progressDialog = new ProgressDialog(SalesHistory.this);

                new AsyncTask<Void, Void, Void>(){

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog.setMessage("Loading Please Wait...");
                        progressDialog.show();
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);

                        txt = "";
                        for (int z = 0 ; z < getDivisionDescrip.size() ; z++)
                        {
                            if (z == 0)
                            {
                                txt = /*selectItmDivision.getText().toString() + "\n" +*/ getDivisionDescrip.get(z);
                            }
                            else
                                txt = txt + "\n" + getDivisionDescrip.get(z);
                        }

                        progressDialog.dismiss();
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        if (Looper.myLooper() == null)
                        {
                            Looper.prepare();
                        }
                        ItmDivStr = "";
                        for (int i = 0; i < getDivisionDescrip.size() ; i++)
                        {
                            for (int x = 0 ; x < ItemDivisionCode.size() ; x++)
                            {
                                if (getDivisionDescrip.get(i).equals(ItemDivisionDescription.get(x)))
                                {
                                    if (i==0)
                                    {
                                        ItmDivStr = " '" + ItemDivisionCode.get(x) + "'";
                                    }
                                    else
                                    {
                                        ItmDivStr = ItmDivStr + ", '" + ItemDivisionCode.get(x) + "'";
                                    }
                                }
                            }
                        }

                        ItmCategStr = "";
                        for (int i = 0; i < ItemCategoryCode.size() ; i++)
                        {
                            if (i == 0) { ItmCategStr = "'" + ItemCategoryCode.get(i) + "'"; }
                            else { ItmCategStr = ItmCategStr + ", '" + ItemCategoryCode.get(i) + "'"; }
                        }

                        ItmProGrpStr = "";
                        for (int i = 0; i < ItemProductGrpCode.size() ; i++)
                        {
                            if (i == 0) { ItmProGrpStr = "'" + ItemProductGrpCode.get(i) + "'"; }
                            else { ItmProGrpStr = ItmProGrpStr + ", '" + ItemProductGrpCode.get(i) + "'"; }
                        }

                        ItmStr = "";
                        for (int i = 0; i < ItemCode.size() ; i++)
                        {
                            if (i == 0) { ItmStr = "'" + ItemCode.get(i) + "'"; }
                            else { ItmStr = ItmStr + ", '" + ItemCode.get(i) + "'"; }
                        }

                        ItmCateg();
                        filterItemCate();

                        ItmProGrp();
                        filterItmProGrp();

                        Itm();
                        filterItm();

                        ItmCategStr = "";
                        for (int i = 0; i < ItemCategoryCode.size() ; i++)
                        {
                            if (i == 0) { ItmCategStr = "'" + ItemCategoryCode.get(i) + "'"; }
                            else { ItmCategStr = ItmCategStr + ", '" + ItemCategoryCode.get(i) + "'"; }
                        }

                        ItmProGrpStr = "";
                        for (int i = 0; i < ItemProductGrpCode.size() ; i++)
                        {
                            if (i == 0) { ItmProGrpStr = "'" + ItemProductGrpCode.get(i) + "'"; }
                            else { ItmProGrpStr = ItmProGrpStr + ", '" + ItemProductGrpCode.get(i) + "'"; }
                        }

                        ItmStr = "";
                        for (int i = 0; i < ItemCode.size() ; i++)
                        {
                            if (i == 0) { ItmStr = "'" + ItemCode.get(i) + "'"; }
                            else { ItmStr = ItmStr + ", '" + ItemCode.get(i) + "'"; }
                        }
                        return null;
                    }
                }.execute();
//                selectItmDivision.setText(txt);
            }

            @Override
            public void onCancel() {}

            @Override
            public void onClear() {
//                selectItmDivision.setText("Item Division");
            }

        });

        /*selectItmDivision = findViewById(R.id.ItemDivisionSelect);

        selectItmDivision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getDivisionDescrip.clear();
                multiSelectDialog1.show(getSupportFragmentManager(), "multiSelectDialog");
            }
        });*/
    }
    public void filterItemCate() {
        final ArrayList<Integer> alreadySelectedItem = new ArrayList<>();
        ArrayList<MultiSelectModel> listOfItems = new ArrayList<>();

        for (int i = 0 ; i < ItemCategoryCode.size() ; i++)
        {
            listOfItems.add(new MultiSelectModel(i+1,ItemCategoryDescription.get(i)));
        }

        multiSelectDialog2 = new MultiSelectDialog();

        multiSelectDialog2.title("Select Item Category");
        multiSelectDialog2.titleSize(24);
        multiSelectDialog2.positiveText("Apply");
        multiSelectDialog2.negativeText("Cancel");
        multiSelectDialog2.clearText("Clear All");
        multiSelectDialog2.preSelectIDsList(alreadySelectedItem);
        multiSelectDialog2.multiSelectList(listOfItems);

        multiSelectDialog2.onSubmit(new MultiSelectDialog.SubmitCallbackListener() {

            @SuppressLint("StaticFieldLeak")
            @Override
            public void onDismiss(ArrayList<Integer> ids, ArrayList<String> commonSeperatedData) throws PackageManager.NameNotFoundException {
                getItemCategoryDescription.addAll(commonSeperatedData);

                final ProgressDialog progressDialog = new ProgressDialog(SalesHistory.this);

                new AsyncTask<Void, Void, Void>(){

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog.setMessage("Loading Please Wait...");
                        progressDialog.show();
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        progressDialog.dismiss();
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        if (Looper.myLooper() == null)
                        {
                            Looper.prepare();
                        }
                        ItmCategStr = "";
                        for (int i = 0; i < getItemCategoryDescription.size() ; i++)
                        {
                            for (int x = 0 ; x < ItemCategoryCode.size() ; x++)
                            {
                                if (getItemCategoryDescription.get(i).equals(ItemCategoryDescription.get(x)))
                                {
                                    if (i == 0)
                                    {
                                        ItmCategStr = " '" + ItemCategoryCode.get(x) + "'";
                                    }
                                    else
                                    {
                                        ItmCategStr = ItmCategStr + ", '" + ItemCategoryCode.get(x) + "'";
                                    }
                                }
                            }
                        }

                        ItmDivStr = "";
                        for (int i = 0; i < ItemDivisionCode.size() ; i++)
                        {
                            if (i == 0) { ItmDivStr = "'" + ItemDivisionCode.get(i) + "'"; }
                            else { ItmDivStr = ItmDivStr + ", '" + ItemDivisionCode.get(i) + "'"; }
                        }

                        ItmProGrpStr = "";
                        for (int i = 0; i < ItemProductGrpCode.size() ; i++)
                        {
                            if (i == 0) { ItmProGrpStr = "'" + ItemProductGrpCode.get(i) + "'"; }
                            else { ItmProGrpStr = ItmProGrpStr + ", '" + ItemProductGrpCode.get(i) + "'"; }
                        }

                        ItmStr = "";
                        for (int i = 0; i < ItemCode.size() ; i++)
                        {
                            if (i == 0) { ItmStr = "'" + ItemCode.get(i) + "'"; }
                            else { ItmStr = ItmStr + ", '" + ItemCode.get(i) + "'"; }
                        }

                        ItmDiv();
                        filterItemDiv();

                        ItmProGrp();
                        filterItmProGrp();

                        Itm();
                        filterItm();

                        ItmDivStr = "";
                        for (int i = 0; i < ItemDivisionCode.size() ; i++)
                        {
                            if (i == 0) { ItmDivStr = "'" + ItemDivisionCode.get(i) + "'"; }
                            else { ItmDivStr = ItmDivStr + ", '" + ItemDivisionCode.get(i) + "'"; }
                        }

                        ItmProGrpStr = "";
                        for (int i = 0; i < ItemProductGrpCode.size() ; i++)
                        {
                            if (i == 0) { ItmProGrpStr = "'" + ItemProductGrpCode.get(i) + "'"; }
                            else { ItmProGrpStr = ItmProGrpStr + ", '" + ItemProductGrpCode.get(i) + "'"; }
                        }

                        ItmStr = "";
                        for (int i = 0; i < ItemCode.size() ; i++)
                        {
                            if (i == 0) { ItmStr = "'" + ItemCode.get(i) + "'"; }
                            else { ItmStr = ItmStr + ", '" + ItemCode.get(i) + "'"; }
                        }
                        return null;
                    }
                }.execute();

                /*String txt = "";
                for (int z = 0 ; z < getItemCategoryDescription.size() ; z++)
                {
                    if (z == 0)
                    {
                        txt = selectItmCategory.getText().toString() + "\n" + getItemCategoryDescription.get(z);
                    }
                    else
                        txt = txt + "\n" + getItemCategoryDescription.get(z);
                }

                selectItmCategory.setText(txt);*/
            }

            @Override
            public void onCancel() {}

            @Override
            public void onClear() {
//                selectItmCategory.setText("Item Category");
            }

        });

        /*selectItmCategory = findViewById(R.id.ItemCateSelect);

        selectItmCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getItemCategoryDescription.clear();
                multiSelectDialog2.show(getSupportFragmentManager(), "multiSelectDialog");
            }
        });*/
    }
    public void filterItmProGrp() {

        final ArrayList<Integer> alreadySelectedItem = new ArrayList<>();
        ArrayList<MultiSelectModel> listOfItems = new ArrayList<>();

        for (int i = 0 ; i < ItemProductGrpCode.size() ; i++)
        {
            listOfItems.add(new MultiSelectModel(i+1,ItemProductGrpDescription.get(i)));
        }

        multiSelectDialog3 = new MultiSelectDialog();

        multiSelectDialog3.title("Select Item Product Group");
        multiSelectDialog3.titleSize(24);
        multiSelectDialog3.positiveText("Apply");
        multiSelectDialog3.negativeText("Cancel");
        multiSelectDialog3.clearText("Clear All");
        multiSelectDialog3.preSelectIDsList(alreadySelectedItem);
        multiSelectDialog3.multiSelectList(listOfItems);

        multiSelectDialog3.onSubmit(new MultiSelectDialog.SubmitCallbackListener() {

            @SuppressLint("StaticFieldLeak")
            @Override
            public void onDismiss(ArrayList<Integer> ids, ArrayList<String> commonSeperatedData) throws PackageManager.NameNotFoundException {
                getItemProductGrpDescription.addAll(commonSeperatedData);

                final ProgressDialog progressDialog = new ProgressDialog(SalesHistory.this);

                new AsyncTask<Void, Void, Void>(){

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog.setMessage("Loading Please Wait...");
                        progressDialog.show();
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        progressDialog.dismiss();
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        if (Looper.myLooper() == null)
                        {
                            Looper.prepare();
                        }
                        ItmProGrpStr = "";
                        for (int i = 0; i < getItemProductGrpDescription.size() ; i++)
                        {
                            for (int x = 0 ; x < ItemProductGrpCode.size() ; x++)
                            {
                                if (getItemProductGrpDescription.get(i).equals(ItemProductGrpDescription.get(x)))
                                {
                                    if (i == 0)
                                    {
                                        ItmProGrpStr = "'" + ItemProductGrpCode.get(x) + "'";
                                    }
                                    else
                                    {
                                        ItmProGrpStr = ItmProGrpStr + ", '" + ItemProductGrpCode.get(x) + "'";
                                    }
                                }
                            }
                        }

                        ItmDivStr = "";
                        for (int i = 0; i < ItemDivisionCode.size() ; i++)
                        {
                            if (i == 0) { ItmDivStr = "'" + ItemDivisionCode.get(i) + "'"; }
                            else { ItmDivStr = ItmDivStr + ", '" + ItemDivisionCode.get(i) + "'"; }
                        }

                        ItmCategStr = "";
                        for (int i = 0; i < ItemCategoryCode.size() ; i++)
                        {
                            if (i == 0) { ItmCategStr = "'" + ItemCategoryCode.get(i) + "'"; }
                            else { ItmCategStr = ItmCategStr + ", '" + ItemCategoryCode.get(i) + "'"; }
                        }

                        ItmStr = "";
                        for (int i = 0; i < ItemCode.size() ; i++)
                        {
                            if (i == 0) { ItmStr = "'" + ItemCode.get(i) + "'"; }
                            else { ItmStr = ItmStr + ", '" + ItemCode.get(i) + "'"; }
                        }

                        ItmDiv();
                        filterItemDiv();

                        ItmCateg();
                        filterItemCate();

                        Itm();
                        filterItm();

                        ItmDivStr = "";
                        for (int i = 0; i < ItemDivisionCode.size() ; i++)
                        {
                            if (i == 0) { ItmDivStr = "'" + ItemDivisionCode.get(i) + "'"; }
                            else { ItmDivStr = ItmDivStr + ", '" + ItemDivisionCode.get(i) + "'"; }
                        }

                        ItmCategStr = "";
                        for (int i = 0; i < ItemCategoryCode.size() ; i++)
                        {
                            if (i == 0) { ItmCategStr = "'" + ItemCategoryCode.get(i) + "'"; }
                            else { ItmCategStr = ItmCategStr + ", '" + ItemCategoryCode.get(i) + "'"; }
                        }

                        ItmStr = "";
                        for (int i = 0; i < ItemCode.size() ; i++)
                        {
                            if (i == 0) { ItmStr = "'" + ItemCode.get(i) + "'"; }
                            else { ItmStr = ItmStr + ", '" + ItemCode.get(i) + "'"; }
                        }

                        return null;
                    }
                }.execute();

                /*String txt = "";
                for (int z = 0 ; z < getItemProductGrpDescription.size() ; z++)
                {
                    if (z == 0)
                    {
                        txt = selectItmProductGrp.getText().toString() + "\n" + getItemProductGrpDescription.get(z);
                    }
                    else
                        txt = txt + "\n" + getItemProductGrpDescription.get(z);
                }

                selectItmProductGrp.setText(txt);*/
            }

            @Override
            public void onCancel() {}

            @Override
            public void onClear() {
//                selectItmProductGrp.setText("Product Group");
            }

        });

        /*selectItmProductGrp = findViewById(R.id.ProductGrpSelect);

        selectItmProductGrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getItemProductGrpDescription.clear();
                multiSelectDialog2.show(getSupportFragmentManager(), "multiSelectDialog");
            }
        });*/
    }
    public void filterItm() {
        final ArrayList<Integer> alreadySelectedItem = new ArrayList<>();
        ArrayList<MultiSelectModel> listOfItems = new ArrayList<>();

        for (int i = 0 ; i < ItemCode.size() ; i++)
        {
            listOfItems.add(new MultiSelectModel(i+1,ItemDescription.get(i)));
        }

        multiSelectDialog4 = new MultiSelectDialog();

        multiSelectDialog4.title("Select Item");
        multiSelectDialog4.titleSize(24);
        multiSelectDialog4.positiveText("Apply");
        multiSelectDialog4.negativeText("Cancel");
        multiSelectDialog4.clearText("Clear All");
        multiSelectDialog4.preSelectIDsList(alreadySelectedItem);
        multiSelectDialog4.multiSelectList(listOfItems);

        multiSelectDialog4.onSubmit(new MultiSelectDialog.SubmitCallbackListener() {

            @SuppressLint("StaticFieldLeak")
            @Override
            public void onDismiss(ArrayList<Integer> ids, ArrayList<String> commonSeperatedData) throws PackageManager.NameNotFoundException {
                getItemDescription.addAll(commonSeperatedData);

                final ProgressDialog progressDialog = new ProgressDialog(SalesHistory.this);

                new AsyncTask<Void, Void, Void>(){

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog.setMessage("Loading Please Wait...");
                        progressDialog.show();
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        progressDialog.dismiss();
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        if (Looper.myLooper() == null)
                        {
                            Looper.prepare();
                        }
                        ItmStr = "";
                        for (int i = 0; i < getItemDescription.size() ; i++)
                        {
                            for (int x = 0 ; x < ItemCode.size() ; x++)
                            {
                                if (getItemDescription.get(i).equals(ItemDescription.get(x)))
                                {
                                    if (i == 0)
                                    {
                                        ItmStr = "'" + ItemCode.get(x) + "'";
                                    }
                                    else
                                    {
                                        ItmStr = ItmStr + ", '" + ItemCode.get(x) + "'";
                                    }
                                }
                            }
                        }

                        ItmDivStr = "";
                        for (int i = 0; i < ItemDivisionCode.size() ; i++)
                        {
                            if (i == 0) { ItmDivStr = "'" + ItemDivisionCode.get(i) + "'"; }
                            else { ItmDivStr = ItmDivStr + ", '" + ItemDivisionCode.get(i) + "'"; }
                        }

                        ItmCategStr = "";
                        for (int i = 0; i < ItemCategoryCode.size() ; i++)
                        {
                            if (i == 0) { ItmCategStr = "'" + ItemCategoryCode.get(i) + "'"; }
                            else { ItmCategStr = ItmCategStr + ", '" + ItemCategoryCode.get(i) + "'"; }
                        }

                        ItmProGrpStr = "";
                        for (int i = 0; i < ItemProductGrpCode.size() ; i++)
                        {
                            if (i == 0) { ItmProGrpStr = "'" + ItemProductGrpCode.get(i) + "'"; }
                            else { ItmProGrpStr = ItmProGrpStr + ", '" + ItemProductGrpCode.get(i) + "'"; }
                        }

                        ItmDiv();
                        filterItemDiv();

                        ItmCateg();
                        filterItemCate();

                        ItmProGrp();
                        filterItmProGrp();

                        ItmDivStr = "";
                        for (int i = 0; i < ItemDivisionCode.size() ; i++)
                        {
                            if (i == 0) { ItmDivStr = "'" + ItemDivisionCode.get(i) + "'"; }
                            else { ItmDivStr = ItmDivStr + ", '" + ItemDivisionCode.get(i) + "'"; }
                        }

                        ItmCategStr = "";
                        for (int i = 0; i < ItemCategoryCode.size() ; i++)
                        {
                            if (i == 0) { ItmCategStr = "'" + ItemCategoryCode.get(i) + "'"; }
                            else { ItmCategStr = ItmCategStr + ", '" + ItemCategoryCode.get(i) + "'"; }
                        }

                        ItmProGrpStr = "";
                        for (int i = 0; i < ItemProductGrpCode.size() ; i++)
                        {
                            if (i == 0) { ItmProGrpStr = "'" + ItemProductGrpCode.get(i) + "'"; }
                            else { ItmProGrpStr = ItmProGrpStr + ", '" + ItemProductGrpCode.get(i) + "'"; }
                        }

                        return null;
                    }
                }.execute();

                /*String txt = "";
                for (int z = 0 ; z < getItemDescription.size() ; z++)
                {
                    if (z == 0)
                    {
                        txt = SelectItm.getText().toString() + "\n" + getItemDescription.get(z);
                    }
                    else
                        txt = txt + "\n" + getItemDescription.get(z);
                }

                SelectItm.setText(txt);*/
            }

            @Override
            public void onCancel() {}

            @Override
            public void onClear() {
//                SelectItm.setText("Item Descrip");
            }

        });

        /*SelectItm = findViewById(R.id.ItemSelect);

        SelectItm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getItemDescription.clear();
                multiSelectDialog4.show(getSupportFragmentManager(), "multiSelectDialog");
            }
        });*/
    }

    public void filterBranchName() {
        final ArrayList<Integer> alreadySelectedItem = new ArrayList<>();
        ArrayList<MultiSelectModel> listOfItems = new ArrayList<>();

        for (int i = 0 ; i < StoreNo.size() ; i++)
        {
            listOfItems.add(new MultiSelectModel(i+1,StoreName.get(i)));
        }

        final MultiSelectDialog multiSelectDialog = new MultiSelectDialog();

        multiSelectDialog.title("Select Branch");
        multiSelectDialog.titleSize(24);
        multiSelectDialog.positiveText("Apply");
        multiSelectDialog.negativeText("Cancel");
        multiSelectDialog.clearText("Clear All");
        multiSelectDialog.preSelectIDsList(alreadySelectedItem);
        multiSelectDialog.multiSelectList(listOfItems);

        multiSelectDialog.onSubmit(new MultiSelectDialog.SubmitCallbackListener() {

            @Override
            public void onDismiss(ArrayList<Integer> ids, ArrayList<String> commonSeperatedData) throws PackageManager.NameNotFoundException {
                getBranchName.addAll(commonSeperatedData);

                String txt = "";
                for (int z = 0 ; z < getBranchName.size() ; z++)
                {
                    if (z == 0)
                    {
                        txt = selectBranchName.getText().toString() + "\n" + getBranchName.get(z);
                    }
                    else
                        txt = txt + "\n" + getBranchName.get(z);
                }

                selectBranchName.setText(txt);

                Str = "";
                for (int i = 0; i < getBranchName.size() ; i++)
                {
                    for (int x = 0 ; x < StoreNo.size() ; x++)
                    {
                        if (getBranchName.get(i).equals(StoreName.get(x)))
                        {
                            if (i == 0)
                            {
                                Str = "'" + StoreNo.get(x) + "'";
                            }
                            else
                            {
                                Str = Str + ", '" + StoreNo.get(x) + "'";
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancel() {}

            @SuppressLint("SetTextI18n")
            @Override
            public void onClear() {
                selectBranchName.setText("Branch Name");
            }

        });

        selectBranchName = findViewById(R.id.BranchName);

        selectBranchName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getBranchName.clear();
                multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");
            }
        });
    }

    public void ItmDiv(){
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
            String query = "select distinct ItmMas.[Division Code], ItmDivision.Description FROM [dbo].["+CName+"$Item] as ItmMas, ["+CName+"$Division] as ItmDivision where ItmMas.[Division Code] = ItmDivision.Code and ItmMas.[Item Category Code] in ("+ItmCategStr+") and ItmMas.[Product Group Code] in ("+ItmProGrpStr+") and ItmMas.No_ in ("+ItmStr+") group by ItmMas.[Division Code], ItmDivision.Description order by ItmMas.[Division Code], ItmDivision.Description";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                ItemDivisionCode.clear();
                ItemDivisionDescription.clear();

                while (rs.next()) {
                    ItemDivisionCode.add(rs.getString("Division Code"));
                    ItemDivisionDescription.add(rs.getString("Description"));
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
    public void ItmCateg(){
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
            String query = "select distinct ItmMas.[Item Category Code], ItmCateg.Description FROM [dbo].["+CName+"$Item] as ItmMas, ["+CName+"$Item Category] as ItmCateg where ItmMas.[Item Category Code] = ItmCateg.Code and ItmMas.[Division Code] in ("+ItmDivStr+") and ItmMas.[Product Group Code] in ("+ItmProGrpStr+") and ItmMas.No_ in ("+ItmStr+") group by ItmMas.[Item Category Code], ItmCateg.Description order by ItmMas.[Item Category Code], ItmCateg.Description";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                ItemCategoryCode.clear();
                ItemCategoryDescription.clear();

                while (rs.next()) {
                    ItemCategoryCode.add(rs.getString("Item Category Code"));
                    ItemCategoryDescription.add(rs.getString("Description"));
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
    public void ItmProGrp(){
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
            String query = "select distinct ItmMas.[Product Group Code], ItmProduct.Description FROM [dbo].["+CName+"$Item] as ItmMas, ["+CName+"$Product Group] as ItmProduct where ItmMas.[Product Group Code] = ItmProduct.Code and ItmMas.[Division Code] in ("+ItmDivStr+") and ItmMas.[Item Category Code] in ("+ItmCategStr+") and ItmMas.No_ in ("+ItmStr+") group by ItmMas.[Product Group Code], ItmProduct.Description order by ItmMas.[Product Group Code], ItmProduct.Description";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                ItemProductGrpCode.clear();
                ItemProductGrpDescription.clear();

                while (rs.next()) {
                    ItemProductGrpCode.add(rs.getString("Product Group Code"));
                    ItemProductGrpDescription.add(rs.getString("Description"));
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
    public void Itm(){
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
            String query = "select distinct No_, Description FROM [dbo].["+CName+"$Item] where [Division Code] in ("+ItmDivStr+") and [Item Category Code] in ("+ItmCategStr+") and [Product Group Code] in ("+ItmProGrpStr+")  group by No_, Description order by No_, Description";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                ItemCode.clear();
                ItemDescription.clear();

                while (rs.next()) {
                    ItemCode.add(rs.getString("No_"));
                    ItemDescription.add(rs.getString("Description"));
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
    public void ItmVariant() {
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
            String query = "select distinct [Item No_], Code, Description, [Description 2] FROM [dbo].["+CName+"$Item Variant]";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {
                    VariantTabItemNo.add(rs.getString("Item No_"));
                    VariantTabItemVarCode.add(rs.getString("Code"));
                    VariantTabItemVarDes.add(rs.getString("Description") + " - " + rs.getString("Description 2"));
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

    public void BranchName() {
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
            String query = "Select No_, Name FROM [dbo].["+CName+"$Store]";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {
                    StoreNo.add(rs.getString("No_"));
                    StoreName.add(rs.getString("No_") + " - " + rs.getString("Name"));
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

    public void someRepeatition(){
        QuaterStr = "''";
        for (int i = 0; i < Quater123.size(); i++) {
            QuaterStr = QuaterStr + ", '" + Quater123.get(i) + "'";
        }
        MonthStr = "''";
        for (int i = 0; i < Month123.size(); i++) {
            MonthStr = MonthStr + ", '" + Month123.get(i) + "'";
        }
        Week1234();
        filterWeekValues();

        WeekStr = "''";
        for (int i = 0; i < Week123.size(); i++) {
            WeekStr = WeekStr + ", '" + Week123.get(i) + "'";
        }
        MonthStr = "''";
        for (int i = 0; i < Month123.size(); i++) {
            MonthStr = MonthStr + ", '" + Month123.get(i) + "'";
        }
        Quater1234();
        filterQuaterValues();

        WeekStr = "''";
        for (int i = 0; i < Week123.size(); i++) {
            WeekStr = WeekStr + ", '" + Week123.get(i) + "'";
        }
        QuaterStr = "''";
        for (int i = 0; i < Quater123.size(); i++) {
            QuaterStr = QuaterStr + ", '" + Quater123.get(i) + "'";
        }
        Month1234();
        filterMonthValues();
    }

    public void CompStartDate() {
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
            String query = "select (Format(Convert(date, MIN([Posting Date])), 'yyyy-MM-dd')) as startDate FROM [dbo].["+CName+"$Value Entry]";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {
                    CompStart = rs.getString("startDate");
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
