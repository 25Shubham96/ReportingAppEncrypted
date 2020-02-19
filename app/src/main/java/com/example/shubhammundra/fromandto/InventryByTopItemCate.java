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
import android.graphics.Typeface;
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
import android.widget.EditText;
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

public class InventryByTopItemCate extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TableLayout TL1;
    TableRow TR1;
    TextView TV1,TV2,TV3,TV4,TV5,TV6,TV7,TV8;

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

    ArrayList<String> InventoryPostingGroup = new ArrayList<>();

    String Cities = "''",Cities2 = "''";

    Button selectInvenPostGrp, selectLocation;

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal = {1,1000,100000,1000000,10000000};

    String dropDownValue;

    int index;

    @SuppressLint("StaticFieldLeak")
    public static Spinner spinnerFilter;

    ArrayList<String> ItemCateg = new ArrayList<>();
    ArrayList<Double> OpenStockVal = new ArrayList<>();
    ArrayList<Double> OpenStockQuantity = new ArrayList<>();

    ArrayList<String> ItemCateg2 = new ArrayList<>();
    ArrayList<Double> ValIncrease = new ArrayList<>();
    ArrayList<Double> QuanIncrease = new ArrayList<>();
    ArrayList<Double> ValDecrease = new ArrayList<>();
    ArrayList<Double> QuanDecrease = new ArrayList<>();

    ArrayList<String> ItemCateg3 = new ArrayList<>();
    ArrayList<String> ItemProductGrp = new ArrayList<>();
    ArrayList<Double> OpenStockVal2 = new ArrayList<>();
    ArrayList<Double> OpenStockQuantity2 = new ArrayList<>();

    ArrayList<String> ItemCateg4 = new ArrayList<>();
    ArrayList<String> ItemProductGrp2 = new ArrayList<>();
    ArrayList<Double> ValIncrease2 = new ArrayList<>();
    ArrayList<Double> QuanIncrease2 = new ArrayList<>();
    ArrayList<Double> ValDecrease2 = new ArrayList<>();
    ArrayList<Double> QuanDecrease2 = new ArrayList<>();

    ArrayList<String> LocationCode = new ArrayList<>();
    ArrayList<String> LocationDescrip = new ArrayList<>();

    TextView storeStart;
    TextView storeEnd;

    String storeStartDate = "2001-01-01";

    String storeEndDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

    Calendar startCalendar = Calendar.getInstance();
    Calendar endCalendar = Calendar.getInstance();

    String CompStart;

    public static String NoOfCateg = "";

    EditText et1;

    double OpenStockValue = 0, OpenStockQuan = 0, OpenStockValue2 = 0;
    double ValueIncrease = 0, ValueDecrease = 0, QuantityIncrease = 0, QuantityDecrease = 0;

    ArrayList<String> getLocation = new ArrayList<>();

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventry_by_top_item_cate);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TL1 = findViewById(R.id.tl_myTable);

        Intent intent = getIntent();
        CName = intent.getStringExtra("Company Name");
        InventoryPostingGroup = intent.getStringArrayListExtra("Posting Group");
        CompStart = intent.getStringExtra("Company Start");

        et1 = findViewById(R.id.et_NoOfCate);

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

        listItems = InventoryPostingGroup.toArray(new String[InventoryPostingGroup.size()]);
        checkedItems = new boolean[listItems.length];

        selectInvenPostGrp = findViewById(R.id.btn_selectInvenPostingGroup);
        selectInvenPostGrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(InventryByTopItemCate.this);
                builder.setTitle("Select Posting Group");

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
                                Cities = " '" + listItems[UserItems.get(i)] + "'";
                            }
                            else
                                Cities = Cities + ", '" + listItems[UserItems.get(i)] + "'";
                        }
                        selectInvenPostGrp.setText(Store);
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
                            selectInvenPostGrp.setText("Select");
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        final ProgressDialog progressDialog = new ProgressDialog(InventryByTopItemCate.this);

        new AsyncTask<Void, Void ,Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setMessage("data loading please wait...");
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                final ArrayList<Integer> alreadySelectedItem2 = new ArrayList<>();

                ArrayList<MultiSelectModel> listOfItems2 = new ArrayList<>();

                for (int i = 0 ; i < LocationDescrip.size() ; i++)
                {
                    listOfItems2.add(new MultiSelectModel(i+1,LocationDescrip.get(i)));
                }

                final MultiSelectDialog multiSelectDialog2 = new MultiSelectDialog();

                multiSelectDialog2.title("Select Location");
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

                        getLocation.clear();
                        multiSelectDialog2.show(getSupportFragmentManager(), "multiSelectDialog2");

                    }
                });
                PrintTable();
                progressDialog.dismiss();
                Toast.makeText(InventryByTopItemCate.this, "data loading complete", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                if (Looper.myLooper() == null)
                {
                    Looper.prepare();
                }
                Location();
                doInBackground1();
                return null;
            }
        }.execute();

        storeStart = findViewById(R.id.tv_startdate);
        storeEnd = findViewById(R.id.tv_enddate);

        storeStartDate = CompStart;
        storeStart.setText(storeStartDate);
        storeEnd.setText(storeEndDate);
    }

    public void doInBackground1() {

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

                while(TL1.getChildCount() > 1)
                {
                    TL1.removeView(TL1.getChildAt(TL1.getChildCount() - 1));
                }

                NoOfCateg = et1.getText().toString();

                Cities = "''";
                for (int i = 0; i < UserItems.size() ; i++)
                {
                    Cities = Cities + ", '" + listItems[UserItems.get(i)] + "'";
                }

                Cities2 = "''";
                for (int i = 0; i < getLocation.size() ; i++)
                {
                    for (int j = 0 ; j < LocationCode.size() ; j++)
                    {
                        if (LocationDescrip.get(j).equals(getLocation.get(i)))
                        {
                            Cities2 = Cities2 + ", '" + LocationCode.get(j) + "'";
                            break;
                        }
                    }
                }

                ItemCateg.clear();
                OpenStockVal.clear();
                OpenStockQuantity.clear();

                ItemCateg2.clear();
                ValIncrease.clear();
                QuanIncrease.clear();
                ValDecrease.clear();
                QuanDecrease.clear();

                ItemCateg3.clear();
                ItemProductGrp.clear();
                OpenStockVal2.clear();
                OpenStockQuantity2.clear();

                ItemCateg4.clear();
                ItemProductGrp2.clear();
                ValIncrease2.clear();
                QuanIncrease2.clear();
                ValDecrease2.clear();
                QuanDecrease2.clear();


                final ProgressDialog progressDialog = new ProgressDialog(InventryByTopItemCate.this);

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
                        doInBackground2();
                        doInBackground3();
                        doInBackground4();

                        return null;
                    }
                }.execute();

                refresh.startAnimation(rotation);
            }
        });

        storeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(InventryByTopItemCate.this, DPstartDate, startCalendar.get(Calendar.YEAR),startCalendar.get(Calendar.MONTH),startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        storeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(InventryByTopItemCate.this, DPendDate, endCalendar.get(Calendar.YEAR),endCalendar.get(Calendar.MONTH),endCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
            String query = "select itmCate.Description, SUM(ROUND([Cost Amount (Actual)],0,1)) as OpenStockValue, SUM(ROUND(Quantity,0,1)) as OpenStockQuantity FROM [dbo].["+CName+"$Inventory Staging] as InvenStg, [dbo].["+CName+"$Item Category] as itmCate, [dbo].["+CName+"$Item] as itm where itmCate.Code = InvenStg.[Item Category Code] and itm.No_ = InvenStg.[Item No_] and [Posting Date] between '"+CompStart+"' and '"+storeStartDate+"' and [Location Code] in ("+Cities2+") and [Inventory Posting Group] in ("+Cities+") group by itmCate.Description";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {

                    if (!CompStart.equals(storeStartDate))
                    {
                        String a = rs.getString("Description");
                        ItemCateg.add(String.valueOf(a));

                        double b = rs.getDouble("OpenStockValue");
                        OpenStockVal.add(b);

                        double c = rs.getDouble("OpenStockQuantity");
                        OpenStockQuantity.add(c);

                        OpenStockValue = OpenStockValue + b ;
                        OpenStockQuan = OpenStockQuan + c ;
                    }
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
            String query = "select Top "+NoOfCateg+" itmCate.Description, SUM(ROUND([Sales Amount (Actual)],0,1)) as SalesAmt, SUM(ROUND([Value Increase],0,1)) as ValInc, SUM(ROUND([Value Decrease],0,1)) as ValDec, SUM(ROUND([Quantity Increase],0,1)) as QuanInc, SUM(ROUND([Quantity Decrease],0,1)) as QuanDec FROM [dbo].["+CName+"$Inventory Staging] as InvenStg, [dbo].["+CName+"$Item Category] as itmCate, [dbo].["+CName+"$Item] as itm where itmCate.Code = InvenStg.[Item Category Code] and itm.No_ = InvenStg.[Item No_] and [Posting Date] between '"+storeStartDate+"' and '"+storeEndDate+"' and [Location Code] in ("+Cities2+") and [Inventory Posting Group] in ("+Cities+") group by itmCate.Description order by SUM([Sales Amount (Actual)]) DESC";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {
                        String a = rs.getString("Description");
                        ItemCateg2.add(String.valueOf(a));

                        double b = rs.getDouble("ValInc");
                        ValIncrease.add(b);

                        double c = rs.getDouble("ValDec");
                        ValDecrease.add(c);

                        double d = rs.getDouble("QuanInc");
                        QuanIncrease.add(d);

                        double e = rs.getDouble("QuanDec");
                        QuanDecrease.add(e);

                        ValueIncrease = ValueIncrease + b ;
                        ValueDecrease = ValueDecrease + c ;
                        QuantityIncrease = QuantityIncrease + d ;
                        QuantityDecrease = QuantityDecrease + e ;
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
            String query = "select InvenStg.[Product Group Code], itmCate.Description, SUM(ROUND([Cost Amount (Actual)],0,1)) as OpenStockValue, SUM(ROUND(Quantity,0,1)) as OpenStockQuantity FROM [dbo].["+CName+"$Inventory Staging] as InvenStg, [dbo].["+CName+"$Item Category] as itmCate, [dbo].["+CName+"$Item] as itm where itmCate.Code = InvenStg.[Item Category Code] and itm.No_ = InvenStg.[Item No_] and [Posting Date] between '"+CompStart+"' and '"+storeStartDate+"' and [Location Code] in ("+Cities2+") and [Inventory Posting Group] in ("+Cities+") group by InvenStg.[Product Group Code],itmCate.Description";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {
                    if (!CompStart.equals(storeStartDate))
                    {
                        String a = rs.getString("Description");
                        ItemCateg3.add(a);

                        String b = rs.getString("Product Group Code");
                        ItemProductGrp.add(String.valueOf(b));

                        double c = rs.getDouble("OpenStockValue");
                        OpenStockVal2.add(c);

                        double d = rs.getDouble("OpenStockQuantity");
                        OpenStockQuantity2.add(d);
                    }
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

    public void doInBackground4() {
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
            String query = "select InvenStg.[Product Group Code], itmCate.Description, SUM(ROUND([Value Increase],0,1)) as ValInc, SUM(ROUND([Value Decrease],0,1)) as ValDec, SUM(ROUND([Quantity Increase],0,1)) as QuanInc, SUM(ROUND([Quantity Decrease],0,1)) as QuanDec FROM [dbo].["+CName+"$Inventory Staging] as InvenStg, [dbo].["+CName+"$Item Category] as itmCate, [dbo].["+CName+"$Item] as itm where itmCate.Code = InvenStg.[Item Category Code] and itm.No_ = InvenStg.[Item No_] and [Posting Date] between '"+storeStartDate+"' and '"+storeEndDate+"' and [Location Code] in ("+Cities2+") and [Inventory Posting Group] in ("+Cities+") group by InvenStg.[Product Group Code], itmCate.Description";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {

                    String a = rs.getString("Description");
                    ItemCateg4.add(String.valueOf(a));

                    String b = rs.getString("Product Group Code");
                    ItemProductGrp2.add(String.valueOf(b));

                    double c = rs.getDouble("ValInc");
                    ValIncrease2.add(c);

                    double d = rs.getDouble("ValDec");
                    ValDecrease2.add(d);

                    double e = rs.getDouble("QuanInc");
                    QuanIncrease2.add(e);

                    double f = rs.getDouble("QuanDec");
                    QuanDecrease2.add(f);
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

        double x=0,y=0,z=0,x1=0,y1=0,z1=0,p=0,q=0,r=0;

        for (int i = 0 ; i < ItemCateg2.size() ; i ++)
        {
            TL1.setStretchAllColumns(true);
            TV1 = new TextView(this);            TV2 = new TextView(this);            TV3 = new TextView(this);            TV4 = new TextView(this);            TV5 = new TextView(this);            TV6 = new TextView(this);            TV7 = new TextView(this);            TV8 = new TextView(this);
            TR1 = new TableRow(this);

            TV1.setText(String.valueOf(ItemCateg2.get(i)));

            TV2.setText("0");
            for (int j = 0 ; j < ItemCateg.size() ; j++)
            {
                if (ItemCateg2.get(i).equals(ItemCateg.get(j)))
                {
                    x = OpenStockVal.get(j);
                    OpenStockValue2 = x;
                    x1 = OpenStockQuantity.get(j);
                    TV2.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((OpenStockQuantity.get(j) / intVal[index]) * 100.0) / 100.0));
                    break;
                }
            }

            y = ValIncrease.get(i);

            y1 = QuanIncrease.get(i);
            TV3.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((QuanIncrease.get(i) / intVal[index]) * 100.0) / 100.0));

            z = ValDecrease.get(i);

            z1 = QuanDecrease.get(i);
            TV4.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((QuanDecrease.get(i) / intVal[index]) * 100.0) / 100.0));

            TV5.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round(((x + y + z) / intVal[index]) * 100.0) / 100.0));

            double qwerty = ( x + y + z );
            double qwerty2 = ( OpenStockValue + ValueIncrease + ValueDecrease );

            Double Precen = qwerty * 100 / qwerty2;

            TV6.setText(String.valueOf(Math.round((Precen) * 100.0) / 100.0) + "%");

            TV7.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round(((x1 + y1 + z1) / intVal[index]) * 100.0) / 100.0));

            p = x + y + z;
            q = x1 + y1 + z1;

            if(q != 0)
            {
                r = p / q ;
            }
            else
                r = p ;

            TV8.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((r / intVal[index]) * 100.0) / 100.0));

            TV1.setTextSize(15);
            TV1.setPadding(10, 0, 10, 0);
            TV1.setGravity(Gravity.LEFT);
            TV1.setTextColor(Color.WHITE);
            TV1.setBackground(getDrawable(R.drawable.rect_border));
            TV1.setTypeface(null, Typeface.BOLD);

            TV2.setTextSize(15);
            TV2.setPadding(10, 0, 10, 0);
            TV2.setGravity(Gravity.RIGHT);
            TV2.setTextColor(Color.WHITE);
            TV2.setBackground(getDrawable(R.drawable.rect_border));
            TV2.setTypeface(null, Typeface.BOLD);

            TV3.setTextSize(15);
            TV3.setPadding(10, 0, 10, 0);
            TV3.setGravity(Gravity.RIGHT);
            TV3.setTextColor(Color.WHITE);
            TV3.setBackground(getDrawable(R.drawable.rect_border));
            TV3.setTypeface(null, Typeface.BOLD);

            TV4.setTextSize(15);
            TV4.setPadding(10, 0, 10, 0);
            TV4.setGravity(Gravity.RIGHT);
            TV4.setTextColor(Color.WHITE);
            TV4.setBackground(getDrawable(R.drawable.rect_border));
            TV4.setTypeface(null, Typeface.BOLD);

            TV5.setTextSize(15);
            TV5.setPadding(10, 0, 10, 0);
            TV5.setGravity(Gravity.RIGHT);
            TV5.setTextColor(Color.WHITE);
            TV5.setBackground(getDrawable(R.drawable.rect_border));
            TV5.setTypeface(null, Typeface.BOLD);

            TV6.setTextSize(15);
            TV6.setPadding(10, 0, 10, 0);
            TV6.setGravity(Gravity.RIGHT);
            TV6.setTextColor(Color.WHITE);
            TV6.setBackground(getDrawable(R.drawable.rect_border));
            TV6.setTypeface(null, Typeface.BOLD);

            TV7.setTextSize(15);
            TV7.setPadding(10, 0, 10, 0);
            TV7.setGravity(Gravity.RIGHT);
            TV7.setTextColor(Color.WHITE);
            TV7.setBackground(getDrawable(R.drawable.rect_border));
            TV7.setTypeface(null, Typeface.BOLD);

            TV8.setTextSize(15);
            TV8.setPadding(10, 0, 10, 0);
            TV8.setGravity(Gravity.RIGHT);
            TV8.setTextColor(Color.WHITE);
            TV8.setBackground(getDrawable(R.drawable.rect_border));
            TV8.setTypeface(null, Typeface.BOLD);

            TR1.addView(TV1);            TR1.addView(TV2);            TR1.addView(TV3);            TR1.addView(TV4);            TR1.addView(TV5);            TR1.addView(TV6);            TR1.addView(TV7);            TR1.addView(TV8);

            TL1.addView(TR1);

            for (int k = 0 ; k < ItemCateg4.size() ; k++)
            {
                if (ItemCateg2.get(i).equals(ItemCateg4.get(k)))
                {
                    TV1 = new TextView(this);                    TV2 = new TextView(this);                    TV3 = new TextView(this);                    TV4 = new TextView(this);                    TV5 = new TextView(this);                    TV6 = new TextView(this);                    TV7 = new TextView(this);                    TV8 = new TextView(this);

                    TR1 = new TableRow(this);

                    TV1.setText("•" + String.valueOf(ItemProductGrp2.get(k)));

                    TV2.setText("0");
                    for (int m = 0 ; m < ItemCateg3.size() ; m++)
                    {
                        if (ItemProductGrp2.get(k).equals(ItemProductGrp.get(m)))
                        {
                            x = OpenStockVal2.get(m);
                            x1 = OpenStockQuantity2.get(m);
                            TV2.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((OpenStockQuantity2.get(m) / intVal[index]) * 100.0) / 100.0));
                            break;
                        }
                    }

                    y = ValIncrease2.get(k);

                    y1 = QuanIncrease2.get(k);
                    TV3.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((QuanIncrease2.get(k) / intVal[index]) * 100.0) / 100.0));

                    z = ValDecrease2.get(k);

                    z1 = QuanDecrease2.get(k);
                    TV4.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((QuanDecrease2.get(k) / intVal[index]) * 100.0) / 100.0));

                    TV5.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round(((x + y + z) / intVal[index]) * 100.0) / 100.0));

                    double qwerty123 = ( x + y + z );
                    double qwerty1232 = ( OpenStockValue2 + ValIncrease.get(i) + ValDecrease.get(i) );

                    Double Precen123 = qwerty123 * 100 / qwerty1232;

                    TV6.setText(String.valueOf(Math.round((Precen123) * 100.0) / 100.0) + "%");

                    TV7.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round(((x1 + y1 + z1) / intVal[index]) * 100.0) / 100.0));

                    p = x + y + z;
                    q = x1 + y1 + z1;

                    if(q != 0)
                    {
                        r = p / q ;
                    }
                    else
                        r = p ;

                    TV8.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((r / intVal[index]) * 100.0) / 100.0));

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

                    TV5.setTextSize(15);
                    TV5.setPadding(60, 0, 10, 0);
                    TV5.setGravity(Gravity.RIGHT);
                    TV5.setTextColor(Color.WHITE);
                    TV5.setBackground(getDrawable(R.drawable.rect_border));

                    TV6.setTextSize(15);
                    TV6.setPadding(60, 0, 10, 0);
                    TV6.setGravity(Gravity.RIGHT);
                    TV6.setTextColor(Color.WHITE);
                    TV6.setBackground(getDrawable(R.drawable.rect_border));

                    TV7.setTextSize(15);
                    TV7.setPadding(60, 0, 10, 0);
                    TV7.setGravity(Gravity.RIGHT);
                    TV7.setTextColor(Color.WHITE);
                    TV7.setBackground(getDrawable(R.drawable.rect_border));

                    TV8.setTextSize(15);
                    TV8.setPadding(60, 0, 10, 0);
                    TV8.setGravity(Gravity.RIGHT);
                    TV8.setTextColor(Color.WHITE);
                    TV8.setBackground(getDrawable(R.drawable.rect_border));

                    TR1.addView(TV1);                    TR1.addView(TV2);                    TR1.addView(TV3);                    TR1.addView(TV4);                    TR1.addView(TV5);                    TR1.addView(TV6);                    TR1.addView(TV7);                    TR1.addView(TV8);

                    TL1.addView(TR1);
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

            PrintTable();
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    public void StartUpdateLabel(){
        String myStartFormat = "yyyy-MM-dd";
        SimpleDateFormat STARTsdf = new SimpleDateFormat(myStartFormat,Locale.US);
        storeStart.setText(STARTsdf.format(startCalendar.getTime()));
    }
    public void EndUpdateLabel(){
        String myEndFormat = "yyyy-MM-dd";
        SimpleDateFormat ENDsdf = new SimpleDateFormat(myEndFormat,Locale.US);
        storeEnd.setText(ENDsdf.format(endCalendar.getTime()));
    }

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

                while (rs.next()) {

                    String a = rs.getString("Code");
                    LocationCode.add(a);

                    String b = rs.getString("Name");
                    LocationDescrip.add(b);
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
