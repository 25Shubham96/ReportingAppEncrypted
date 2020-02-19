package com.example.shubhammundra.fromandto;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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

public class ValueEntryReport extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TableLayout TL,TL2,TL3,TL4;
    TableRow TR,TR2,TR3,TR4;
    TextView TV1,TV2,TV3,TV4,TV5,TV6,TV7,TV21,TV22,TV23,TV24,TV25,TV26,TV27,TV31,TV32,TV33,TV34,TV35,TV36,TV37,TV41,TV42,TV43,TV44,TV45,TV46,TV47;

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

    ArrayList<String> Store;
    ArrayList<String> StoreDescrip;
    ArrayList<String> Item = new ArrayList<>();
    ArrayList<String> ItemCategory = new ArrayList<>();
    ArrayList<String> ItemCategoryDescrip = new ArrayList<>();
    ArrayList<String> ItemDivision = new ArrayList<>();
    ArrayList<String> ItemDivisionDescrip = new ArrayList<>();

    String Cities = "''", Cities2 = "''", Cities3 = "''", Cities4 = "''";

    Button selectStore, selectCategory, selectDivision;

    TextView storeStart;
    TextView storeEnd;

    String storeStartDate = "2001-01-01";

    String storeEndDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

    Calendar startCalendar = Calendar.getInstance();
    Calendar endCalendar = Calendar.getInstance();

    String ItemSelect = "", dropDownItem/*, ItemCateSelect = "", ItemDivSelect = "", dropDownItemCate, dropDownItemDiv*/;

    ArrayList<String> StoreNumber = new ArrayList<>();
    ArrayList<String> ItemDescription = new ArrayList<>();
    ArrayList<Double> SalesAmount = new ArrayList<>();
    ArrayList<Double> CostAmount = new ArrayList<>();
    ArrayList<Double> Quantity = new ArrayList<>();

    ArrayList<String> StoreNumber11 = new ArrayList<>();
    ArrayList<String> ItemDescription11 = new ArrayList<>();
    ArrayList<String> ItemVariant11 = new ArrayList<>();
    ArrayList<Double> SalesAmount11 = new ArrayList<>();
    ArrayList<Double> CostAmount11 = new ArrayList<>();
    ArrayList<Double> Quantity11 = new ArrayList<>();

    ArrayList<String> StoreNumber2 = new ArrayList<>();
    ArrayList<String> ItemDescription2 = new ArrayList<>();
    ArrayList<Double> SalesAmount2 = new ArrayList<>();
    ArrayList<Double> CostAmount2 = new ArrayList<>();
    ArrayList<Double> Quantity2 = new ArrayList<>();

    ArrayList<String> StoreNumber3 = new ArrayList<>();
    ArrayList<String> ItemDescription3 = new ArrayList<>();
    ArrayList<Double> SalesAmount3 = new ArrayList<>();
    ArrayList<Double> CostAmount3 = new ArrayList<>();
    ArrayList<Double> Quantity3 = new ArrayList<>();

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal = {1,1000,100000,1000000,10000000};

    String dropDownValue;

    int index;

    @SuppressLint("StaticFieldLeak")
    public static Spinner spinnerFilter;

    Button dd;

    ArrayList<String> getItemsName = new ArrayList<>();
    ArrayList<String> getCategorysName = new ArrayList<>();
    ArrayList<String> getDivisionsName = new ArrayList<>();
    ArrayList<String> getStoresName = new ArrayList<>();

    ArrayList<String> getStoreDescrip = new ArrayList<>();

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value_entry_report);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TL = findViewById(R.id.tl_myTable);
        TL4 = findViewById(R.id.tl_myTable11);
        TL2 = findViewById(R.id.tl_myTable2);
        TL3 = findViewById(R.id.tl_myTable3);

        Intent intent = getIntent();
        Store = intent.getStringArrayListExtra("Store Name");
        StoreDescrip = intent.getStringArrayListExtra("Store Descrip");
        CName = intent.getStringExtra("Company Name");

        Items();
        ItemCategory();
        ItemDivision();

        back = findViewById(R.id.img10);

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

        final ArrayList<Integer> alreadySelectedItem = new ArrayList<>();

        ArrayList<MultiSelectModel> listOfItems = new ArrayList<>();

        for (int i = 0 ; i < Item.size() ; i++)
        {
            listOfItems.add(new MultiSelectModel(i+1,Item.get(i)));
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
                getItemsName.addAll(commonSeperatedData);

                String txt = "";
                for (int z = 0 ; z < getItemsName.size() ; z++)
                {
                    if (z == 0)
                    {
                        txt = getItemsName.get(z);
                    }
                    else
                        txt = txt + "\n" + getItemsName.get(z);
                }

                dd.setText(txt);
            }

            @Override
            public void onCancel() {}

            @Override
            public void onClear() {}

        });

        final ArrayList<Integer> alreadySelectedItem2 = new ArrayList<>();

        ArrayList<MultiSelectModel> listOfItems2 = new ArrayList<>();

        for (int i = 0 ; i < ItemCategoryDescrip.size() ; i++)
        {
            listOfItems2.add(new MultiSelectModel(i+1,ItemCategoryDescrip.get(i)));
        }

        final MultiSelectDialog multiSelectDialog2 = new MultiSelectDialog();

        multiSelectDialog2.title("Select Item Category");
        multiSelectDialog2.titleSize(24);
        multiSelectDialog2.positiveText("Apply");
        multiSelectDialog2.negativeText("Cancel");
        multiSelectDialog2.clearText("Clear All");
        multiSelectDialog2.preSelectIDsList(alreadySelectedItem2);
        multiSelectDialog2.multiSelectList(listOfItems2);

        multiSelectDialog2.onSubmit(new MultiSelectDialog.SubmitCallbackListener() {

            @Override
            public void onDismiss(ArrayList<Integer> ids, ArrayList<String> commonSeperatedData) throws PackageManager.NameNotFoundException {
                getCategorysName.addAll(commonSeperatedData);

                String txt = "";
                for (int z = 0 ; z < getCategorysName.size() ; z++)
                {
                    if (z == 0)
                    {
                        txt = getCategorysName.get(z);
                    }
                    else
                        txt = txt + "\n" + getCategorysName.get(z);
                }

                selectCategory.setText(txt);
            }

            @Override
            public void onCancel() {}

            @Override
            public void onClear() {}

        });

        final ArrayList<Integer> alreadySelectedItem3 = new ArrayList<>();

        ArrayList<MultiSelectModel> listOfItems3 = new ArrayList<>();

        for (int i = 0 ; i < ItemDivisionDescrip.size() ; i++)
        {
            listOfItems3.add(new MultiSelectModel(i+1,ItemDivisionDescrip.get(i)));
        }

        final MultiSelectDialog multiSelectDialog3 = new MultiSelectDialog();

        multiSelectDialog3.title("Select Item Division");
        multiSelectDialog3.titleSize(24);
        multiSelectDialog3.positiveText("Apply");
        multiSelectDialog3.negativeText("Cancel");
        multiSelectDialog3.clearText("Clear All");
        multiSelectDialog3.preSelectIDsList(alreadySelectedItem3);
        multiSelectDialog3.multiSelectList(listOfItems3);

        multiSelectDialog3.onSubmit(new MultiSelectDialog.SubmitCallbackListener() {

            @Override
            public void onDismiss(ArrayList<Integer> ids, ArrayList<String> commonSeperatedData) throws PackageManager.NameNotFoundException {
                getDivisionsName.addAll(commonSeperatedData);

                String txt = "";
                for (int z = 0 ; z < getDivisionsName.size() ; z++)
                {
                    if (z == 0)
                    {
                        txt = getDivisionsName.get(z);
                    }
                    else
                        txt = txt + "\n" + getDivisionsName.get(z);
                }

                selectDivision.setText(txt);
            }

            @Override
            public void onCancel() {}

            @Override
            public void onClear() {}

        });

        final ArrayList<Integer> alreadySelectedItem4 = new ArrayList<>();

        ArrayList<MultiSelectModel> listOfItems4 = new ArrayList<>();

        for (int i = 0 ; i < StoreDescrip.size() ; i++)
        {
            listOfItems4.add(new MultiSelectModel(i+1,StoreDescrip.get(i)));
        }

        final MultiSelectDialog multiSelectDialog4 = new MultiSelectDialog();

        multiSelectDialog4.title("Select Stores");
        multiSelectDialog4.titleSize(24);
        multiSelectDialog4.positiveText("Apply");
        multiSelectDialog4.negativeText("Cancel");
        multiSelectDialog4.clearText("Clear All");
        multiSelectDialog4.preSelectIDsList(alreadySelectedItem4);
        multiSelectDialog4.multiSelectList(listOfItems4);

        multiSelectDialog4.onSubmit(new MultiSelectDialog.SubmitCallbackListener() {

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

        dd = findViewById(R.id.dropDown);
        dd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");
            }
        });


        selectCategory = findViewById(R.id.btn_selectCategory);
        selectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                multiSelectDialog2.show(getSupportFragmentManager(), "multiSelectDialog2");

            }
        });

        selectDivision = findViewById(R.id.btn_selectDivision);
        selectDivision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                multiSelectDialog3.show(getSupportFragmentManager(), "multiSelectDialog3");
            }
        });

        selectStore = findViewById(R.id.btn_selectStore);
        selectStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                multiSelectDialog4.show(getSupportFragmentManager(), "multiSelectDialog4");

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

        storeStart = findViewById(R.id.tv_startdate);
        storeEnd = findViewById(R.id.tv_enddate);
        storeStart.setText(storeStartDate);
        storeEnd.setText(storeEndDate);
    }

    public void doInBackground1() {

        ItemSelect = dropDownItem;

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

        refresh = findViewById(R.id.iv_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {

                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);

                while(TL.getChildCount() > 1)
                {
                    TL.removeView(TL.getChildAt(TL.getChildCount() - 1));
                }

                while(TL2.getChildCount() > 1)
                {
                    TL2.removeView(TL2.getChildAt(TL2.getChildCount() - 1));
                }

                while(TL3.getChildCount() > 1)
                {
                    TL3.removeView(TL3.getChildAt(TL3.getChildCount() - 1));
                }

                while(TL4.getChildCount() > 1)
                {
                    TL4.removeView(TL4.getChildAt(TL4.getChildCount() - 1));
                }

                Cities = "''";
                for (int i = 0; i < UserItems.size() ; i++)
                {
                    Cities = Cities + ", '" + listItems[UserItems.get(i)] + "'";
                }

                Cities2 = "''";
                for (int i = 0; i < getItemsName.size() ; i++)
                {
                    Cities2 = Cities2 + ", '" + getItemsName.get(i) + "'";
                }

                Cities3 = "''";
                for (int i = 0; i < getCategorysName.size() ; i++)
                {
                    for (int j = 0 ; j < ItemCategory.size() ; j++)
                    {
                        if (ItemCategoryDescrip.get(j).equals(getCategorysName.get(i)))
                        {
                            Cities3 = Cities3 + ", '" + ItemCategory.get(j) + "'";
                            break;
                        }
                    }
                }

                Cities4 = "''";
                for (int i = 0; i < getDivisionsName.size() ; i++)
                {
                    for (int j = 0 ; j < ItemDivision.size() ; j++)
                    {
                        if (ItemDivisionDescrip.get(j).equals(getDivisionsName.get(i)))
                        {
                            Cities4 = Cities4 + ", '" + ItemDivision.get(j) + "'";
                            break;
                        }
                    }
                }

                StoreNumber.clear();
                ItemDescription.clear();
                SalesAmount.clear();
                CostAmount.clear();
                Quantity.clear();

                StoreNumber11.clear();
                ItemDescription11.clear();
                SalesAmount11.clear();
                CostAmount11.clear();
                Quantity11.clear();

                StoreNumber2.clear();
                ItemDescription2.clear();
                SalesAmount2.clear();
                CostAmount2.clear();
                Quantity2.clear();

                StoreNumber3.clear();
                ItemDescription3.clear();
                SalesAmount3.clear();
                CostAmount3.clear();
                Quantity3.clear();

                getItemsName.clear();
                getCategorysName.clear();
                getDivisionsName.clear();
                getStoresName.clear();

                final ProgressDialog progressDialog = new ProgressDialog(ValueEntryReport.this);

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
                        storeStart.setText(storeStartDate);
                        storeEnd.setText(storeEndDate);
                        progressDialog.dismiss();
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        if (Looper.myLooper() == null)
                        {
                            Looper.prepare();
                        }
                        doInBackground1();
                        doInBackground11();
                        doInBackground2();
                        doInBackground3();

                        return null;
                    }
                }.execute();

                refresh.startAnimation(rotation);
            }
        });

        storeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ValueEntryReport.this, DPstartDate, startCalendar.get(Calendar.YEAR),startCalendar.get(Calendar.MONTH),startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        storeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ValueEntryReport.this, DPendDate, endCalendar.get(Calendar.YEAR),endCalendar.get(Calendar.MONTH),endCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
            String query = "select ve.[Location Code], itm.[Search Description], sum(abs(round([Sales Amount (Actual)],0,1))) as SalesAmt, sum(abs(round([Cost Amount (Actual)],0,1))) as CostAmt, sum(abs([Item Ledger Entry Quantity])) as TotQuan FROM [dbo].["+CName+"$Value Entry] as ve, [dbo].["+CName+"$Item] as itm, [dbo].["+CName+"$Store] as str where ve.[Item No_] = itm.No_ and [Posting Date] between '"+storeStartDate+"' and '"+storeEndDate+"' and [Item Ledger Entry Type] = 1 and ve.[Location Code] = str.No_ and ve.[Location Code] in ("+Cities+") and [Search Description] in ("+Cities2+") group by itm.[Search Description], ve.[Location Code] order by itm.[Search Description], ve.[Location Code]";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {

                    String StoreNo = rs.getString("Location Code");
                    StoreNumber.add(String.valueOf(StoreNo));

                    String ItemDes = rs.getString("Search Description");
                    ItemDescription.add(String.valueOf(ItemDes));

                    double SalesAmt = rs.getLong("SalesAmt");
                    SalesAmt = Math.abs(SalesAmt);
                    SalesAmount.add(SalesAmt);

                    double CostAmt = rs.getLong("CostAmt");
                    CostAmt = Math.abs(CostAmt);
                    CostAmount.add(CostAmt);

                    double Quant = rs.getLong("TotQuan");
                    Quant = Math.abs(Quant);
                    Quantity.add(Quant);

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

    public void doInBackground11() {

        ItemSelect = dropDownItem;

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
            String query = "select ve.[Location Code], itm.[Search Description], varin.[Description 2], sum(abs(round([Sales Amount (Actual)],0,1))) as SalesAmt, sum(abs(round([Cost Amount (Actual)],0,1))) as CostAmt, sum(abs([Item Ledger Entry Quantity])) as TotQuan FROM [dbo].["+CName+"$Value Entry] as ve, [dbo].["+CName+"$Item] as itm, ["+CName+"$Store] as str, [dbo].["+CName+"$Item Variant] as varin where ve.[Item No_] = varin.[Item No_] and ve.[Variant Code] = varin.Code and ve.[Item No_] = itm.No_ and [Posting Date] between '"+storeStartDate+"' and '"+storeEndDate+"' and [Item Ledger Entry Type] = 1 and [Item Ledger Entry Type] = 1 and ve.[Location Code] = str.No_ and ve.[Location Code] in ("+Cities+") and [Search Description] in ("+Cities2+") group by itm.[Search Description], ve.[Location Code], varin.[Description 2] order by itm.[Search Description], ve.[Location Code], varin.[Description 2]";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {

                    String StoreNo = rs.getString("Location Code");
                    StoreNumber11.add(String.valueOf(StoreNo));

                    String ItemDes = rs.getString("Search Description");
                    ItemDescription11.add(String.valueOf(ItemDes));

                    String ItemVarin = rs.getString("Description 2");
                    ItemVariant11.add(String.valueOf(ItemVarin));

                    double SalesAmt = rs.getLong("SalesAmt");
                    SalesAmt = Math.abs(SalesAmt);
                    SalesAmount11.add(SalesAmt);

                    double CostAmt = rs.getLong("CostAmt");
                    CostAmt = Math.abs(CostAmt);
                    CostAmount11.add(CostAmt);

                    double Quant = rs.getLong("TotQuan");
                    Quant = Math.abs(Quant);
                    Quantity11.add(Quant);

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

    public void doInBackground2(){

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
            String query = "select ve.[Location Code], itmCat.[Description], sum(abs(round([Sales Amount (Actual)],0,1))) as SalesAmt, sum(abs(round([Cost Amount (Actual)],0,1))) as CostAmt, sum(abs([Item Ledger Entry Quantity])) as TotQuan FROM [dbo].["+CName+"$Value Entry] as ve, [dbo].["+CName+"$Item] as itm, [dbo].["+CName+"$Store] as str, [dbo].["+CName+"$Item Category] as itmCat where ve.[Item No_] = itm.No_ and itm.[Item Category Code] = itmCat.Code and [Posting Date] between '"+storeStartDate+"' and '"+storeEndDate+"' and [Item Ledger Entry Type] = 1 and ve.[Location Code] = str.No_ and ve.[Location Code] in ("+Cities+") and itm.[Item Category Code] in ("+Cities3+") group by itmCat.[Description], ve.[Location Code] order by itmCat.[Description], ve.[Location Code]";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {
                    String StoreNo = rs.getString("Location Code");
                    StoreNumber2.add(String.valueOf(StoreNo));

                    String ItemDes = rs.getString("Description");
                    ItemDescription2.add(String.valueOf(ItemDes));

                    double SalesAmt = rs.getLong("SalesAmt");
                    SalesAmt = Math.abs(SalesAmt);
                    SalesAmount2.add(SalesAmt);

                    double CostAmt = rs.getLong("CostAmt");
                    CostAmt = Math.abs(CostAmt);
                    CostAmount2.add(CostAmt);

                    double Quant = rs.getLong("TotQuan");
                    Quant = Math.abs(Quant);
                    Quantity2.add(Quant);
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

    public void doInBackground3(){

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
            String query = "select ve.[Location Code], itmDiv.[Description], sum(abs(round([Sales Amount (Actual)],0,1))) as SalesAmt, sum(abs(round([Cost Amount (Actual)],0,1))) as CostAmt, sum(abs([Item Ledger Entry Quantity])) as TotQuan FROM [dbo].["+CName+"$Value Entry] as ve, [dbo].["+CName+"$Store] as str, [dbo].["+CName+"$Division] as itmDiv where ve.[Location Code] = str.No_ and itmDiv.Code = ve.Division and [Posting Date] between '"+storeStartDate+"' and '"+storeEndDate+"' and [Item Ledger Entry Type] = 1 and ve.[Location Code] in ("+Cities+") and ve.Division in ("+Cities4+") group by itmDiv.[Description], ve.[Location Code] order by itmDiv.[Description], ve.[Location Code]";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {
                    String StoreNo = rs.getString("Location Code");
                    StoreNumber3.add(String.valueOf(StoreNo));

                    String ItemDes = rs.getString("Description");
                    ItemDescription3.add(String.valueOf(ItemDes));

                    double SalesAmt = rs.getLong("SalesAmt");
                    SalesAmt = Math.abs(SalesAmt);
                    SalesAmount3.add(SalesAmt);

                    double CostAmt = rs.getLong("CostAmt");
                    CostAmt = Math.abs(CostAmt);
                    CostAmount3.add(CostAmt);

                    double Quant = rs.getLong("TotQuan");
                    Quant = Math.abs(Quant);
                    Quantity3.add(Quant);
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
    public void PrintTable(){
        for (int i = 0 ; i < SalesAmount.size() ; i++)
        {
            TL.setStretchAllColumns(true);
            TR = new TableRow(this);
            TV1 = new TextView(this);            TV2 = new TextView(this);            TV3 = new TextView(this);            TV4 = new TextView(this);            TV5 = new TextView(this);            TV6 = new TextView(this);            TV7 = new TextView(this);

            TV1.setText(StoreNumber.get(i));
            TV2.setText(ItemDescription.get(i));
            TV3.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((SalesAmount.get(i) / intVal[index]) * 100.0) / 100.0));
            TV4.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((CostAmount.get(i) / intVal[index]) * 100.0) / 100.0));
            TV5.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((Quantity.get(i) / intVal[index]) * 100.0) / 100.0));

            double profit = SalesAmount.get(i) - CostAmount.get(i);
            TV6.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((profit / intVal[index]) * 100.0) / 100.0));

            if (SalesAmount.get(i) != 0)
            {
                double profitPer = (profit * 100) / SalesAmount.get(i);
                TV7.setText(String.valueOf(Math.round((profitPer) * 100.0) / 100.0) + "%");
            }
            else
                TV7.setText("0");

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
            TV3.setGravity(Gravity.RIGHT);
            TV3.setTextColor(Color.WHITE);
            TV3.setBackground(getDrawable(R.drawable.rect_border));

            TV4.setTextSize(15);
            TV4.setPadding(10, 0, 10, 0);
            TV4.setGravity(Gravity.RIGHT);
            TV4.setTextColor(Color.WHITE);
            TV4.setBackground(getDrawable(R.drawable.rect_border));

            TV5.setTextSize(15);
            TV5.setPadding(10, 0, 10, 0);
            TV5.setGravity(Gravity.RIGHT);
            TV5.setTextColor(Color.WHITE);
            TV5.setBackground(getDrawable(R.drawable.rect_border));

            TV6.setTextSize(15);
            TV6.setPadding(10, 0, 10, 0);
            TV6.setGravity(Gravity.RIGHT);
            TV6.setTextColor(Color.WHITE);
            TV6.setBackground(getDrawable(R.drawable.rect_border));

            TV7.setTextSize(15);
            TV7.setPadding(10, 0, 10, 0);
            TV7.setGravity(Gravity.RIGHT);
            TV7.setTextColor(Color.WHITE);
            TV7.setBackground(getDrawable(R.drawable.rect_border));

            TR.addView(TV1);            TR.addView(TV2);            TR.addView(TV3);            TR.addView(TV4);            TR.addView(TV5);            TR.addView(TV6);            TR.addView(TV7);
            TL.addView(TR);
        }

        for (int i = 0 ; i < SalesAmount.size() ; i++)
        {
            TL4.setStretchAllColumns(true);
            TR4 = new TableRow(this);
            TV41 = new TextView(this);            TV42 = new TextView(this);            TV43 = new TextView(this);            TV44 = new TextView(this);            TV45 = new TextView(this);            TV46 = new TextView(this);            TV47 = new TextView(this);

            TV41.setText(StoreNumber.get(i));
            TV42.setText(ItemDescription.get(i));
            TV43.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((SalesAmount.get(i) / intVal[index]) * 100.0) / 100.0));
            TV44.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((CostAmount.get(i) / intVal[index]) * 100.0) / 100.0));
            TV45.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((Quantity.get(i) / intVal[index]) * 100.0) / 100.0));

            double profit = SalesAmount.get(i) - CostAmount.get(i);
            TV46.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((profit / intVal[index]) * 100.0) / 100.0));

            if (SalesAmount.get(i) != 0)
            {
                double profitPer = (profit * 100) / SalesAmount.get(i);
                TV47.setText(String.valueOf(Math.round((profitPer) * 100.0) / 100.0) + "%");
            }
            else
                TV47.setText("0");

            TV41.setTextSize(15);
            TV41.setPadding(10, 0, 10, 0);
            TV41.setGravity(Gravity.LEFT);
            TV41.setTextColor(Color.WHITE);
            TV41.setBackground(getDrawable(R.drawable.rect_border));

            TV42.setTextSize(15);
            TV42.setPadding(10, 0, 10, 0);
            TV42.setGravity(Gravity.LEFT);
            TV42.setTextColor(Color.WHITE);
            TV42.setBackground(getDrawable(R.drawable.rect_border));

            TV43.setTextSize(15);
            TV43.setPadding(10, 0, 10, 0);
            TV43.setGravity(Gravity.RIGHT);
            TV43.setTextColor(Color.WHITE);
            TV43.setBackground(getDrawable(R.drawable.rect_border));

            TV44.setTextSize(15);
            TV44.setPadding(10, 0, 10, 0);
            TV44.setGravity(Gravity.RIGHT);
            TV44.setTextColor(Color.WHITE);
            TV44.setBackground(getDrawable(R.drawable.rect_border));

            TV45.setTextSize(15);
            TV45.setPadding(10, 0, 10, 0);
            TV45.setGravity(Gravity.RIGHT);
            TV45.setTextColor(Color.WHITE);
            TV45.setBackground(getDrawable(R.drawable.rect_border));

            TV46.setTextSize(15);
            TV46.setPadding(10, 0, 10, 0);
            TV46.setGravity(Gravity.RIGHT);
            TV46.setTextColor(Color.WHITE);
            TV46.setBackground(getDrawable(R.drawable.rect_border));

            TV47.setTextSize(15);
            TV47.setPadding(10, 0, 10, 0);
            TV47.setGravity(Gravity.RIGHT);
            TV47.setTextColor(Color.WHITE);
            TV47.setBackground(getDrawable(R.drawable.rect_border));

            TR4.addView(TV41);            TR4.addView(TV42);            TR4.addView(TV43);            TR4.addView(TV44);            TR4.addView(TV45);            TR4.addView(TV46);            TR4.addView(TV47);

            TL4.addView(TR4);

            for (int z = 0 ; z < SalesAmount11.size() ; z++)
            {
                if (StoreNumber11.get(z).equals(StoreNumber.get(i)) && ItemDescription11.get(z).equals(ItemDescription.get(i)))
                {
                    TL4.setStretchAllColumns(true);
                    TR4 = new TableRow(this);
                    TV41 = new TextView(this);                    TV42 = new TextView(this);                    TV43 = new TextView(this);                    TV44 = new TextView(this);                    TV45 = new TextView(this);                    TV46 = new TextView(this);                    TV47 = new TextView(this);

                    TV41.setText("");
                    TV42.setText(ItemVariant11.get(z));
                    TV43.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((SalesAmount11.get(z) / intVal[index]) * 100.0) / 100.0));
                    TV44.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((CostAmount11.get(z) / intVal[index]) * 100.0) / 100.0));
                    TV45.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((Quantity11.get(z) / intVal[index]) * 100.0) / 100.0));

                    double profit123 = SalesAmount11.get(z) - CostAmount11.get(z);
                    TV46.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((profit123 / intVal[index]) * 100.0) / 100.0));

                    if (SalesAmount11.get(z) != 0)
                    {
                        double profitPer123 = (profit123 * 100) / SalesAmount11.get(z);
                        TV47.setText(String.valueOf(Math.round((profitPer123) * 100.0) / 100.0) + "%");
                    }
                    else
                        TV47.setText("0");

                    TV41.setTextSize(15);
                    TV41.setPadding(10, 0, 10, 0);
                    TV41.setGravity(Gravity.LEFT);
                    TV41.setTextColor(Color.WHITE);
                    TV41.setBackground(getDrawable(R.drawable.rect_border));

                    TV42.setTextSize(15);
                    TV42.setPadding(60, 0, 10, 0);
                    TV42.setGravity(Gravity.LEFT);
                    TV42.setTextColor(Color.WHITE);
                    TV42.setBackground(getDrawable(R.drawable.rect_border));

                    TV43.setTextSize(15);
                    TV43.setPadding(10, 0, 10, 0);
                    TV43.setGravity(Gravity.RIGHT);
                    TV43.setTextColor(Color.WHITE);
                    TV43.setBackground(getDrawable(R.drawable.rect_border));

                    TV44.setTextSize(15);
                    TV44.setPadding(10, 0, 10, 0);
                    TV44.setGravity(Gravity.RIGHT);
                    TV44.setTextColor(Color.WHITE);
                    TV44.setBackground(getDrawable(R.drawable.rect_border));

                    TV45.setTextSize(15);
                    TV45.setPadding(10, 0, 10, 0);
                    TV45.setGravity(Gravity.RIGHT);
                    TV45.setTextColor(Color.WHITE);
                    TV45.setBackground(getDrawable(R.drawable.rect_border));

                    TV46.setTextSize(15);
                    TV46.setPadding(10, 0, 10, 0);
                    TV46.setGravity(Gravity.RIGHT);
                    TV46.setTextColor(Color.WHITE);
                    TV46.setBackground(getDrawable(R.drawable.rect_border));

                    TV47.setTextSize(15);
                    TV47.setPadding(10, 0, 10, 0);
                    TV47.setGravity(Gravity.RIGHT);
                    TV47.setTextColor(Color.WHITE);
                    TV47.setBackground(getDrawable(R.drawable.rect_border));

                    TR4.addView(TV41);                    TR4.addView(TV42);                    TR4.addView(TV43);                    TR4.addView(TV44);                    TR4.addView(TV45);                    TR4.addView(TV46);                    TR4.addView(TV47);
                    TL4.addView(TR4);
                }
            }
        }

        for (int i = 0 ; i < SalesAmount2.size() ; i++)
        {

            TL2.setStretchAllColumns(true);
            TR2 = new TableRow(this);            TV21 = new TextView(this);            TV22 = new TextView(this);            TV23 = new TextView(this);            TV24 = new TextView(this);            TV25 = new TextView(this);            TV26 = new TextView(this);            TV27 = new TextView(this);

            TV21.setText(StoreNumber2.get(i));
            TV22.setText(ItemDescription2.get(i));
            TV23.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((SalesAmount2.get(i) / intVal[index]) * 100.0) / 100.0));
            TV24.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((CostAmount2.get(i) / intVal[index]) * 100.0) / 100.0));
            TV25.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((Quantity2.get(i) / intVal[index]) * 100.0) / 100.0));

            double profit = SalesAmount2.get(i) - CostAmount2.get(i);
            TV26.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((profit / intVal[index]) * 100.0) / 100.0));

            if (SalesAmount2.get(i) != 0)
            {
                double profitPer = (profit * 100) / SalesAmount2.get(i);
                TV27.setText(String.valueOf(Math.round((profitPer) * 100.0) / 100.0) + "%");
            }
            else
                TV27.setText("0");

            TV21.setTextSize(15);
            TV21.setPadding(10, 0, 10, 0);
            TV21.setGravity(Gravity.LEFT);
            TV21.setTextColor(Color.WHITE);
            TV21.setBackground(getDrawable(R.drawable.rect_border));

            TV22.setTextSize(15);
            TV22.setPadding(10, 0, 10, 0);
            TV22.setGravity(Gravity.LEFT);
            TV22.setTextColor(Color.WHITE);
            TV22.setBackground(getDrawable(R.drawable.rect_border));

            TV23.setTextSize(15);
            TV23.setPadding(10, 0, 10, 0);
            TV23.setGravity(Gravity.RIGHT);
            TV23.setTextColor(Color.WHITE);
            TV23.setBackground(getDrawable(R.drawable.rect_border));

            TV24.setTextSize(15);
            TV24.setPadding(10, 0, 10, 0);
            TV24.setGravity(Gravity.RIGHT);
            TV24.setTextColor(Color.WHITE);
            TV24.setBackground(getDrawable(R.drawable.rect_border));

            TV25.setTextSize(15);
            TV25.setPadding(10, 0, 10, 0);
            TV25.setGravity(Gravity.RIGHT);
            TV25.setTextColor(Color.WHITE);
            TV25.setBackground(getDrawable(R.drawable.rect_border));

            TV26.setTextSize(15);
            TV26.setPadding(10, 0, 10, 0);
            TV26.setGravity(Gravity.RIGHT);
            TV26.setTextColor(Color.WHITE);
            TV26.setBackground(getDrawable(R.drawable.rect_border));

            TV27.setTextSize(15);
            TV27.setPadding(10, 0, 10, 0);
            TV27.setGravity(Gravity.RIGHT);
            TV27.setTextColor(Color.WHITE);
            TV27.setBackground(getDrawable(R.drawable.rect_border));

            TR2.addView(TV21);            TR2.addView(TV22);            TR2.addView(TV23);            TR2.addView(TV24);            TR2.addView(TV25);            TR2.addView(TV26);            TR2.addView(TV27);
            TL2.addView(TR2);
        }

        for (int i = 0 ; i < SalesAmount3.size() ; i++)
        {
            TL3.setStretchAllColumns(true);
            TR3 = new TableRow(this);
            TV31 = new TextView(this);            TV32 = new TextView(this);            TV33 = new TextView(this);            TV34 = new TextView(this);            TV35 = new TextView(this);            TV36 = new TextView(this);            TV37 = new TextView(this);

            TV31.setText(StoreNumber3.get(i));
            TV32.setText(ItemDescription3.get(i));
            TV33.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((SalesAmount3.get(i) / intVal[index]) * 100.0) / 100.0));
            TV34.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((CostAmount3.get(i) / intVal[index]) * 100.0) / 100.0));
            TV35.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((Quantity3.get(i) / intVal[index]) * 100.0) / 100.0));

            double profit = SalesAmount3.get(i) - CostAmount3.get(i);
            TV36.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((profit / intVal[index]) * 100.0) / 100.0));

            if (SalesAmount3.get(i) != 0)
            {
                double profitPer = (profit * 100) / SalesAmount3.get(i);
                TV37.setText(String.valueOf(Math.round((profitPer) * 100.0) / 100.0) + "%");
            }
            else
                TV37.setText("0");

            TV31.setTextSize(15);
            TV31.setPadding(10, 0, 10, 0);
            TV31.setGravity(Gravity.LEFT);
            TV31.setTextColor(Color.WHITE);
            TV31.setBackground(getDrawable(R.drawable.rect_border));

            TV32.setTextSize(15);
            TV32.setPadding(10, 0, 10, 0);
            TV32.setGravity(Gravity.LEFT);
            TV32.setTextColor(Color.WHITE);
            TV32.setBackground(getDrawable(R.drawable.rect_border));

            TV33.setTextSize(15);
            TV33.setPadding(10, 0, 10, 0);
            TV33.setGravity(Gravity.RIGHT);
            TV33.setTextColor(Color.WHITE);
            TV33.setBackground(getDrawable(R.drawable.rect_border));

            TV34.setTextSize(15);
            TV34.setPadding(10, 0, 10, 0);
            TV34.setGravity(Gravity.RIGHT);
            TV34.setTextColor(Color.WHITE);
            TV34.setBackground(getDrawable(R.drawable.rect_border));

            TV35.setTextSize(15);
            TV35.setPadding(10, 0, 10, 0);
            TV35.setGravity(Gravity.RIGHT);
            TV35.setTextColor(Color.WHITE);
            TV35.setBackground(getDrawable(R.drawable.rect_border));

            TV36.setTextSize(15);
            TV36.setPadding(10, 0, 10, 0);
            TV36.setGravity(Gravity.RIGHT);
            TV36.setTextColor(Color.WHITE);
            TV36.setBackground(getDrawable(R.drawable.rect_border));

            TV37.setTextSize(15);
            TV37.setPadding(10, 0, 10, 0);
            TV37.setGravity(Gravity.RIGHT);
            TV37.setTextColor(Color.WHITE);
            TV37.setBackground(getDrawable(R.drawable.rect_border));

            TR3.addView(TV31);            TR3.addView(TV32);            TR3.addView(TV33);            TR3.addView(TV34);            TR3.addView(TV35);            TR3.addView(TV36);            TR3.addView(TV37);
            TL3.addView(TR3);
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

    public void Items() {
        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query = "select [Search Description] FROM [dbo].["+CName+"$Item] where [Search Description] != ''";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    Item.add(rs.getString("Search Description"));
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void ItemCategory() {
        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query = "Select Code, Description FROM [dbo].["+CName+"$Item Category]";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    ItemCategory.add(rs.getString("Code"));
                    ItemCategoryDescrip.add(rs.getString("Description"));
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void ItemDivision() {
        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query = "select Code, Description FROM [dbo].["+CName+"$Division]";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    ItemDivision.add(rs.getString("Code"));
                    ItemDivisionDescrip.add(rs.getString("Description"));
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
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

            while(TL.getChildCount() > 1)
            {
                TL.removeView(TL.getChildAt(TL.getChildCount() - 1));
            }

            while(TL2.getChildCount() > 1)
            {
                TL2.removeView(TL2.getChildAt(TL2.getChildCount() - 1));
            }

            while(TL3.getChildCount() > 1)
            {
                TL3.removeView(TL3.getChildAt(TL3.getChildCount() - 1));
            }

            while(TL4.getChildCount() > 1)
            {
                TL4.removeView(TL4.getChildAt(TL4.getChildCount() - 1));
            }

            PrintTable();
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}
}
