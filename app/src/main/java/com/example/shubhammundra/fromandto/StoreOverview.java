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
import java.util.Collections;

public class StoreOverview extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TableLayout TL;
    TableRow TR;
    TextView TV0,TV1,TV2,TV3,TV4,TV5,TV6,TV7,TV8,TV9,TV10,TV11,TV12,TV13,TV14,TV15,TV16,TV17,TV18,TV19,TV20,TV21;

    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    String CName;

    ArrayList<String> Store ;
    ArrayList<String> Year ;
    ArrayList<Integer> UserItems = new ArrayList<>();

    String[] listItems;
    boolean[] checkedItems;

    Button selectYear;

    ImageView back,refresh;

    String YearsSelected = "''";

    String[] Quater = {"dummy", "Q1", "Q2", "Q3", "Q4"};
    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal = {1,1000,100000,1000000,10000000};

    String dropDownValue;

    GradientDrawable gd;

    int index;

    public static int count = 0;

    @SuppressLint("StaticFieldLeak")
    public static Spinner spinnerFilter;

    ArrayList<TextView> myTV = new ArrayList<>();
    ArrayList<TextView> myTV1 = new ArrayList<>();

    ArrayList<String> StoreNum123 = new ArrayList<>();
    ArrayList<Double> NetAmt123 = new ArrayList<>();
    ArrayList<String> Yr123 = new ArrayList<>();
    ArrayList<Integer> QuatYr123 = new ArrayList<>();

    String NewStrNo = "''";

    ArrayList<String> SelectedYear = new ArrayList<>();

    Button StoreOverviewBtn;

    ArrayList<ArrayList<Float>> NetAmtToGraph = new ArrayList<>();
    ArrayList<String> StoreNameToGraph = new ArrayList<>();

    Button selectStore;

    ArrayList<String> StoreDescrip;

    ArrayList<String> getStoreDescrip = new ArrayList<>();

    String Stores123 = "''";

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_overview);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TL = findViewById(R.id.tl_myTable1);
        TV0 = findViewById(R.id.tv0);        TV1 = findViewById(R.id.tv1);        TV2 = findViewById(R.id.tv2);        TV3 = findViewById(R.id.tv3);        TV4 = findViewById(R.id.tv4);        TV5 = findViewById(R.id.tv5);        TV6 = findViewById(R.id.tv6);        TV7 = findViewById(R.id.tv7);        TV8 = findViewById(R.id.tv8);        TV9 = findViewById(R.id.tv9);        TV10 = findViewById(R.id.tv10);        TV11 = findViewById(R.id.tv11);        TV12 = findViewById(R.id.tv12);        TV13 = findViewById(R.id.tv13);        TV14 = findViewById(R.id.tv14);        TV15 = findViewById(R.id.tv15);        TV16 = findViewById(R.id.tv16);        TV17 = findViewById(R.id.tv17);        TV18 = findViewById(R.id.tv18);        TV19 = findViewById(R.id.tv19);        TV20 = findViewById(R.id.tv20);        TV21 = findViewById(R.id.tv21);
        myTV.add(TV0);        myTV.add(TV1);        myTV.add(TV2);        myTV.add(TV3);        myTV.add(TV4);        myTV.add(TV5);        myTV.add(TV6);        myTV.add(TV7);        myTV.add(TV8);        myTV.add(TV9);        myTV.add(TV10);        myTV.add(TV11);        myTV.add(TV12);        myTV.add(TV13);        myTV.add(TV14);        myTV.add(TV15);        myTV.add(TV16);        myTV.add(TV17);        myTV.add(TV18);        myTV.add(TV19);        myTV.add(TV20);        myTV.add(TV21);

        Intent getintent = getIntent();
        CName = getintent.getStringExtra("Company Name");
        Store = getintent.getStringArrayListExtra("Store Name");
        StoreDescrip = getintent.getStringArrayListExtra("Store Descrip");
        Year = getintent.getStringArrayListExtra("Unique Year");

        listItems = Year.toArray(new String[Year.size()]);
        checkedItems = new boolean[listItems.length];

        selectYear = findViewById(R.id.btn_selectYear);
        selectYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SelectedYear.clear();

                AlertDialog.Builder builder = new AlertDialog.Builder(StoreOverview.this);
                builder.setTitle("Select Year");

                builder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                        count += isChecked ? 1 : -1;

                        if(isChecked)
                        {
                            if (count < 3)
                            {
                                if (! UserItems.contains(position)){
                                    UserItems.add(position);
                                }
                                checkedItems[position] = isChecked;
                            }
                            else
                            {
                                Toast.makeText(StoreOverview.this, "You selected too many.", Toast.LENGTH_SHORT).show();
                                checkedItems[position] = false;
                                count--;
                                ((AlertDialog) dialogInterface).getListView().setItemChecked(position, false);
                            }
                        }
                        else
                        {
                            if (UserItems.contains(position))
                            {
                                UserItems.remove(UserItems.indexOf(position));
                            }
                        }
                    }
                });

                builder.setCancelable(false);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String MultiYear = "";
                        for(int i = 0 ; i < UserItems.size() ; i++)
                        {
                            MultiYear = MultiYear + listItems[UserItems.get(i)];
                            if(i != UserItems.size() -1)
                            {
                                MultiYear = MultiYear + "\n";
                            }
                        }
                        selectYear.setText(MultiYear);
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

        spinnerFilter = findViewById(R.id.spin_filter);
        spinnerFilter.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, amtFilter);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapter1);

        StoreOverviewBtn = findViewById(R.id.btn_storeOverviewChart);
        StoreOverviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chartIntent = new Intent(StoreOverview.this,StoreOverviewChart.class);
                chartIntent.putExtra("Net Amount",NetAmtToGraph);
                chartIntent.putExtra("Store Name",StoreNameToGraph);
                chartIntent.putExtra("Selected Years",SelectedYear);
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

    @SuppressLint("RtlHardcoded")
    public  void doInBackground1() {
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

                SelectedYear.clear();

                YearsSelected = "''";
                for (int i = 0; i < UserItems.size() ; i++)
                {
                    SelectedYear.add(listItems[UserItems.get(i)] + " - " + Quater[1]);
                    SelectedYear.add(listItems[UserItems.get(i)] + " - " + Quater[2]);
                    SelectedYear.add(listItems[UserItems.get(i)] + " - " + Quater[3]);
                    SelectedYear.add(listItems[UserItems.get(i)] + " - " + Quater[4]);

                    YearsSelected = YearsSelected + ", '" + listItems[UserItems.get(i)] + "'";
                }

                Stores123 = "''";
                for (int i = 0; i < getStoreDescrip.size() ; i++)
                {
                    for (int j = 0 ; j < StoreDescrip.size() ; j++)
                    {
                        if (getStoreDescrip.get(i).equals(StoreDescrip.get(j)))
                        {
                            Stores123 = Stores123 + ", '" + Store.get(j) + "'";
                        }
                    }
                }

                StoreNum123.clear();
                NetAmt123.clear();
                Yr123.clear();
                QuatYr123.clear();

                NetAmtToGraph.clear();
                StoreNameToGraph.clear();

                final ProgressDialog progressDialog = new ProgressDialog(StoreOverview.this);

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

            String query = "SELECT [Store No_], sum([Net Amount]) as netamt, DATEPART(YYYY,Date) as year, DATEPART(QUARTER,Date) as month FROM [dbo].["+CName+"$Transaction Header] where [Store No_] in ("+Stores123+") and [Net Amount] != 0 and DATEPART(YYYY,Date) in ("+YearsSelected+") group by [Store No_], DATEPART(YYYY,Date), DATEPART(QUARTER,Date)  order by [Store No_], DATEPART(YYYY,Date), DATEPART(QUARTER,Date)";

            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                TL = findViewById(R.id.tl_myTable1);
                TR = findViewById(R.id.tr_tr);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    String strNo = rs.getString("Store No_");
                    double netAmt = rs.getDouble("netamt");
                    netAmt = Math.abs(netAmt);
                    String yr = rs.getString("year");
                    int QuatYr = rs.getInt("month");

                    StoreNum123.add(strNo);
                    NetAmt123.add(netAmt);
                    Yr123.add(yr);
                    QuatYr123.add(QuatYr);
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint({"RtlHardcoded", "SetTextI18n"})
    public void PrintTable() {

        Collections.sort(SelectedYear);

        TL.setColumnStretchable(0,true);        TL.setColumnStretchable(1,true);        TL.setColumnStretchable(2,true);        TL.setColumnStretchable(3,true);        TL.setColumnStretchable(4,true);        TL.setColumnStretchable(5,true);        TL.setColumnStretchable(6,true);        TL.setColumnStretchable(7,true);        TL.setColumnStretchable(8,true);        TL.setColumnStretchable(9,true);        TL.setColumnStretchable(10,true);        TL.setColumnStretchable(11,true);        TL.setColumnStretchable(12,true);        TL.setColumnStretchable(13,true);        TL.setColumnStretchable(14,true);        TL.setColumnStretchable(15,true);        TL.setColumnStretchable(16,true);        TL.setColumnStretchable(17,true);        TL.setColumnStretchable(18,true);        TL.setColumnStretchable(19,true);        TL.setColumnStretchable(20,true);        TL.setColumnStretchable(21,true);        TL.setColumnStretchable(22,true);

        TV0.setText("Quater Year →\nStore No ↓");
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

        for (int j = 0 ; j < SelectedYear.size() ; j++)
        {
            myTV.get(j+1).setText(SelectedYear.get(j) + "\n");
            myTV.get(j+1).setTextSize(15);
            myTV.get(j+1).setPadding(10, 0, 10, 10);
            myTV.get(j+1).setGravity(Gravity.LEFT);
            myTV.get(j+1).setTextColor(Color.WHITE);
            myTV.get(j+1).setBackground(getDrawable(R.drawable.rect_border));
        }

        for (int p = SelectedYear.size()+1 ; p < SelectedYear.size()+2 ; p++)
        {
            myTV.get(p).setText("Grand\nTotal");
            myTV.get(p).setTextSize(15);
            myTV.get(p).setPadding(10, 0, 10, 10);
            myTV.get(p).setGravity(Gravity.LEFT);
            myTV.get(p).setTextColor(Color.WHITE);
            myTV.get(p).setBackground(getDrawable(R.drawable.rect_border));
        }

        for (int i = 0 ; i < StoreNum123.size() ; i++)
        {
            if (NewStrNo.equals(StoreNum123.get(i)))
            {
                GrandTotalValue = GrandTotalValue + NetAmt123.get(i);

                TV0.setText(StoreNum123.get(i));

                for (int k = 0 ; k < SelectedYear.size() ; k++)
                {
                    if (SelectedYear.get(k).equals(Yr123.get(i) + " - " + Quater[QuatYr123.get(i)]))
                    {
                        myTV1.get(k+1).setText(NumberFormat.getNumberInstance().format(Math.round((NetAmt123.get(i) / intVal[index]) * 100.0) / 100.0));
                    }
                }
                for (int p = SelectedYear.size()+1 ; p < SelectedYear.size()+2 ; p++)
                {
                    myTV1.get(p).setText(NumberFormat.getNumberInstance().format(Math.round((GrandTotalValue / intVal[index]) * 100.0) / 100.0));
                }
            }
            else
            {
                if (i != 0)
                {
                    for (int z = 0 ; z < SelectedYear.size() ; z++)
                    {
                        NetAmtToGraph.get(NetAmtToGraph.size() - 1).add(Float.parseFloat(String.valueOf(myTV1.get(z+1).getText()).replaceAll(",","")));
                    }
                }

                NetAmtToGraph.add(new ArrayList<Float>());
                StoreNameToGraph.add(StoreNum123.get(i));

                TR = new TableRow(this);
                TV0 = new TextView(this);                TV1 = new TextView(this);                TV2 = new TextView(this);                TV3 = new TextView(this);                TV4 = new TextView(this);                TV5 = new TextView(this);                TV6 = new TextView(this);                TV7 = new TextView(this);                TV8 = new TextView(this);                TV9 = new TextView(this);                TV10 = new TextView(this);                TV11 = new TextView(this);                TV12 = new TextView(this);                TV13 = new TextView(this);                TV14 = new TextView(this);                TV15 = new TextView(this);                TV16 = new TextView(this);                TV17 = new TextView(this);                TV18 = new TextView(this);                TV19 = new TextView(this);                TV20 = new TextView(this);                TV21 = new TextView(this);
                TR.addView(TV0);                TR.addView(TV1);                TR.addView(TV2);                TR.addView(TV3);                TR.addView(TV4);                TR.addView(TV5);                TR.addView(TV6);                TR.addView(TV7);                TR.addView(TV8);                TR.addView(TV9);                TR.addView(TV10);                TR.addView(TV11);                TR.addView(TV12);                TR.addView(TV13);                TR.addView(TV14);                TR.addView(TV15);                TR.addView(TV16);                TR.addView(TV17);                TR.addView(TV18);                TR.addView(TV19);                TR.addView(TV20);                TR.addView(TV21);
                TL.addView(TR);

                myTV1.clear();
                myTV1.add(TV0);        myTV1.add(TV1);        myTV1.add(TV2);        myTV1.add(TV3);        myTV1.add(TV4);        myTV1.add(TV5);        myTV1.add(TV6);        myTV1.add(TV7);        myTV1.add(TV8);        myTV1.add(TV9);        myTV1.add(TV10);        myTV1.add(TV11);        myTV1.add(TV12);        myTV1.add(TV13);        myTV1.add(TV14);        myTV1.add(TV15);        myTV1.add(TV16);        myTV1.add(TV17);        myTV1.add(TV18);        myTV1.add(TV19);        myTV1.add(TV20);        myTV1.add(TV21);

                TV0.setText(StoreNum123.get(i));
                TV0.setTextSize(15);
                TV0.setPadding(10, 0, 10, 0);
                TV0.setGravity(Gravity.LEFT);
                TV0.setTextColor(Color.WHITE);
                TV0.setBackground(getDrawable(R.drawable.rect_border));

                for (int z = 0 ; z < SelectedYear.size() + 1 ; z++)
                {
                    myTV1.get(z + 1).setText("0");

                    myTV1.get(z + 1).setTextSize(15);
                    myTV1.get(z + 1).setPadding(10, 0, 10, 0);
                    myTV1.get(z + 1).setGravity(Gravity.RIGHT);
                    myTV1.get(z + 1).setTextColor(Color.WHITE);
                    myTV1.get(z + 1).setBackground(getDrawable(R.drawable.rect_border));
                }

                for (int k = 0 ; k < SelectedYear.size() ; k++)
                {
                    if (SelectedYear.get(k).equals(Yr123.get(i) + " - " + Quater[QuatYr123.get(i)]))
                    {
                        myTV1.get(k+1).setText(NumberFormat.getNumberInstance().format(Math.round((NetAmt123.get(i) / intVal[index]) * 100.0) / 100.0));
                    }
                }

                GrandTotalValue = 0;
                GrandTotalValue = GrandTotalValue + NetAmt123.get(i);
                for (int p = SelectedYear.size()+1 ; p < SelectedYear.size()+2 ; p++)
                {
                    myTV1.get(p).setText(NumberFormat.getNumberInstance().format(Math.round((GrandTotalValue / intVal[index]) * 100.0) / 100.0));
                }
            }
            NewStrNo = StoreNum123.get(i);

            if (i == StoreNum123.size())
            {
                for (int z = 0 ; z < SelectedYear.size() ; z++)
                {
                    NetAmtToGraph.get(NetAmtToGraph.size() - 1).add(Float.parseFloat(String.valueOf(myTV1.get(z+1).getText()).replaceAll(",","")));
                }
            }
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

        NetAmtToGraph.clear();
        StoreNameToGraph.clear();

        PrintTable();
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}
}
