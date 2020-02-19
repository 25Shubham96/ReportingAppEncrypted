package com.example.shubhammundra.fromandto;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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

public class InventryByTop30Itm extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TableLayout TL1;
    TableRow TR1;
    TextView TV1,TV2,TV3,TV4,TV5,TV6,TV7,TV8,TV9,TV10;

    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    ImageView refresh;

    ImageView back;

    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> UserItems = new ArrayList<>();

    String CName;

    ArrayList<String> ItemCategory = new ArrayList<>();
    ArrayList<String> ItemCategoryDescrip = new ArrayList<>();

    String Cities = "''";

    Button selectCategory;

    GradientDrawable gd;

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal = {1,1000,100000,1000000,10000000};

    String dropDownValue;

    int index;

    @SuppressLint("StaticFieldLeak")
    public static Spinner spinnerFilter;

    ArrayList<String> ItemNo = new ArrayList<>();
    ArrayList<String> ItemDescrip = new ArrayList<>();

    ArrayList<String> ItemNo1 = new ArrayList<>();
    ArrayList<String> ItemDescrip1 = new ArrayList<>();

    ArrayList<Double> OpenStockVal = new ArrayList<>();
    ArrayList<Double> OpenStockQuantity = new ArrayList<>();

    ArrayList<String> ItemNo2 = new ArrayList<>();
    ArrayList<String> ItemDescrip2 = new ArrayList<>();

    ArrayList<Double> ValIncrease = new ArrayList<>();
    ArrayList<Double> QuanIncrease = new ArrayList<>();

    ArrayList<String> ItemNo3 = new ArrayList<>();
    ArrayList<String> ItemDescrip3 = new ArrayList<>();

    ArrayList<Double> ValDecrease = new ArrayList<>();
    ArrayList<Double> QuanDecrease = new ArrayList<>();

    TextView storeStart;
    TextView storeEnd;

    String storeStartDate = "2001-01-01";

    String storeEndDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

    Calendar startCalendar = Calendar.getInstance();
    Calendar endCalendar = Calendar.getInstance();

    String CompStart;

    public static String NoOfItem = "";

    EditText et1;

    ArrayList<String> getItemCategory = new ArrayList<>();

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventry_by_top30_itm);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TL1 = findViewById(R.id.tl_myTable);

        Intent intent = getIntent();
        CName = intent.getStringExtra("Company Name");
        ItemCategory = intent.getStringArrayListExtra("Item Category Group");
        ItemCategoryDescrip = intent.getStringArrayListExtra("Item Category Description");
        CompStart = intent.getStringExtra("Company Start");

        et1 = findViewById(R.id.et_NoOfItems);
        storeStart = findViewById(R.id.tv_startdate);
        storeEnd = findViewById(R.id.tv_enddate);

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

        final ProgressDialog progressDialog = new ProgressDialog(InventryByTop30Itm.this);

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
                PrintTable();
                progressDialog.dismiss();
                Toast.makeText(InventryByTop30Itm.this, "data loading complete", Toast.LENGTH_SHORT).show();
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

                final Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);

                while(TL1.getChildCount() > 1)
                {
                    TL1.removeView(TL1.getChildAt(TL1.getChildCount() - 1));
                }

                NoOfItem = et1.getText().toString();

                Cities = "";
                for (int i = 0; i < getItemCategory.size() ; i++)
                {
                    for (int j = 0 ; j < ItemCategory.size() ; j++)
                    {
                        if (ItemCategoryDescrip.get(j).equals(getItemCategory.get(i)))
                        {
                            if (i == 0)
                            {
                                Cities = " '" + ItemCategory.get(j) + "'";
                            }
                            else
                                Cities = Cities + ", '" + ItemCategory.get(j) + "'";
                            break;
                        }
                    }
                }

                ItemNo.clear();
                ItemDescrip.clear();

                ItemNo1.clear();
                ItemDescrip1.clear();

                OpenStockVal.clear();
                OpenStockQuantity.clear();

                ItemNo2.clear();
                ItemDescrip2.clear();

                ValIncrease.clear();
                QuanIncrease.clear();

                ItemNo3.clear();
                ItemDescrip3.clear();

                ValDecrease.clear();
                QuanDecrease.clear();

                final ProgressDialog progressDialog = new ProgressDialog(InventryByTop30Itm.this);

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
                        storeStart.setText(storeStartDate);
                        storeEnd.setText(storeEndDate);
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

                        return null;
                    }
                }.execute();

                refresh.startAnimation(rotation);
            }
        });

        storeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(InventryByTop30Itm.this, DPstartDate, startCalendar.get(Calendar.YEAR),startCalendar.get(Calendar.MONTH),startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        storeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(InventryByTop30Itm.this, DPendDate, endCalendar.get(Calendar.YEAR),endCalendar.get(Calendar.MONTH),endCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
            String query = "select InvStg.[Item No_], itm.Description, SUM(InvStg.[Cost Amount (Actual)]) as OpenStockVal, SUM(InvStg.Quantity) as OpenStockQuantity FROM [dbo].["+CName+"$Inventory Staging] as InvStg, [dbo].["+CName+"$Item] as itm where InvStg.[Item No_] = itm.No_ and [Posting Date] between '"+CompStart+"' and '"+storeStartDate+"' and InvStg.[Item Category Code] in ("+Cities+") group by InvStg.[Item No_], itm.Description";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {
                    if (!CompStart.equals(storeStartDate))
                    {
                        String a = rs.getString("Item No_");
                        ItemNo1.add(String.valueOf(a));

                        String b = rs.getString("Description");
                        ItemDescrip1.add(b);

                        double c = rs.getDouble("OpenStockVal");
                        OpenStockVal.add(c);

                        double d = rs.getDouble("OpenStockQuantity");
                        OpenStockQuantity.add(d);
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
            String query = "select Top "+NoOfItem+" [Item No_], [Item Description], SUM([Sales Amount]) as SalesValue, SUM([Value Increase]) as ValInc, SUM([Value Decrease]) as ValDec, SUM([Quantity Increase]) as QuanInc, SUM([Quantity Decrease]) as QuanDec From [dbo].["+CName+"$Top30Item] where Date between '"+storeStartDate+"' and '"+storeEndDate+"' and [Item Category Code] in ("+Cities+") group by [Item No_], [Item Description] order by SUM([Sales Amount]) DESC";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {

                    String a = rs.getString("Item No_");
                    ItemNo.add(String.valueOf(a));

                    String b = rs.getString("Item Description");
                    ItemDescrip.add(b);

                    double c = rs.getDouble("ValInc");
                    ValIncrease.add(c);

                    double d = rs.getDouble("ValDec");
                    ValDecrease.add(d);

                    double e = rs.getDouble("QuanInc");
                    QuanIncrease.add(e);

                    double f = rs.getDouble("QuanDec");
                    QuanDecrease.add(f);
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

    @SuppressLint("RtlHardcoded")
    public void PrintTable() {

        double x=0,y=0,z=0,x1=0,y1=0,z1=0,p=0,q=0,r=0;

        for (int i = 0 ; i < ItemNo.size() ; i ++)
        {
            TL1.setStretchAllColumns(true);

            TR1 = new TableRow(this);

            TV1 = new TextView(this);            TV2 = new TextView(this);            TV3 = new TextView(this);            TV4 = new TextView(this);            TV5 = new TextView(this);            TV6 = new TextView(this);            TV7 = new TextView(this);            TV8 = new TextView(this);            TV9 = new TextView(this);            TV10 = new TextView(this);

            TV1.setText(String.valueOf(ItemNo.get(i) +" - "+ ItemDescrip.get(i)));

            TV2.setText("0");
            TV6.setText("0");
            for (int j = 0 ; j < ItemNo1.size() ; j++)
            {
                if (ItemNo.get(i).equals(ItemNo1.get(j)))
                {
                    x = OpenStockVal.get(j);
                    TV2.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((OpenStockVal.get(j) / intVal[index]) * 100.0) / 100.0));
                    x1 = OpenStockQuantity.get(j);
                    TV6.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((OpenStockQuantity.get(j) / intVal[index]) * 100.0) / 100.0));
                    break;
                }
            }

            y = ValIncrease.get(i);
            TV3.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((ValIncrease.get(i) / intVal[index]) * 100.0) / 100.0));

            y1 = QuanIncrease.get(i);
            TV7.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((QuanIncrease.get(i) / intVal[index]) * 100.0) / 100.0));

            z = ValDecrease.get(i);
            TV4.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((ValDecrease.get(i) / intVal[index]) * 100.0) / 100.0));

            z1 = QuanDecrease.get(i);
            TV8.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((QuanDecrease.get(i) / intVal[index]) * 100.0) / 100.0));

            TV5.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round(((x + y + z) / intVal[index]) * 100.0) / 100.0));

            TV9.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round(((x1 + y1 + z1) / intVal[index]) * 100.0) / 100.0));

            p = x + y + z;
            q = x1 + y1 + z1;

            if(q != 0)
            {
                r = p / q ;
            }
            else
                r = p ;

            TV10.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((r / intVal[index]) * 100.0) / 100.0));

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

            TV8.setTextSize(15);
            TV8.setPadding(10, 0, 10, 0);
            TV8.setGravity(Gravity.RIGHT);
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

            TR1.addView(TV1);            TR1.addView(TV2);            TR1.addView(TV3);            TR1.addView(TV4);            TR1.addView(TV5);            TR1.addView(TV6);            TR1.addView(TV7);            TR1.addView(TV8);            TR1.addView(TV9);            TR1.addView(TV10);
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
}
