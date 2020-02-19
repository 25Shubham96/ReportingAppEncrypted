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
import java.util.Locale;

public class InventryByEntryType extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TableLayout TL1;
    TableRow TR1;
    TextView TV1,TV2,TV3,TV4,TV5,TV6,TV7,TV8,TV9,TV10,TV11;

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
    ArrayList<String> ItemCategory = new ArrayList<>();
    ArrayList<String> ItemCategoryDescrip = new ArrayList<>();

    String Cities = "''", Cities2 = "''", Cities3 = "''";

    Button selectYear, selectPostingGrp, selectCategory;

    String[] amtFilter = {"Ones","Thousands","Lacs","Millions","Crores"};

    int[] intVal = {1,1000,100000,1000000,10000000};

    String dropDownValue;

    int index;

    @SuppressLint("StaticFieldLeak")
    public static Spinner spinnerFilter;

    ArrayList<String> Year1 = new ArrayList<>();
    ArrayList<Integer> EntryType = new ArrayList<>();
    ArrayList<Double> CostAmt = new ArrayList<>();

    String EntryTyp;

    double CostAmount;

    ArrayList<String> getItemCategory = new ArrayList<>();

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventry_by_entry_type);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TL1 = findViewById(R.id.tl_myTable);

        Intent intent = getIntent();
        Year = intent.getStringArrayListExtra("Unique Year");
        CName = intent.getStringExtra("Company Name");
        PostingGrp = intent.getStringArrayListExtra("Posting Group");
        ItemCategory = intent.getStringArrayListExtra("Item Category Group");
        ItemCategoryDescrip = intent.getStringArrayListExtra("Item Category Description");

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

        listItems = Year.toArray(new String[Year.size()]);
        checkedItems = new boolean[listItems.length];

        selectYear = findViewById(R.id.btn_selectYear);
        selectYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(InventryByEntryType.this);
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
                                Cities = " '" + listItems[UserItems.get(i)] + "'";
                            }
                            else
                                Cities = Cities + ", '" + listItems[UserItems.get(i)] + "'";
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

            }
        });

        listItems2 = PostingGrp.toArray(new String[PostingGrp.size()]);
        checkedItems2 = new boolean[listItems2.length];

        selectPostingGrp = findViewById(R.id.btn_selectPostingGrp);
        selectPostingGrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(InventryByEntryType.this);
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
                            Store = Store + ",'" + listItems2[UserItems2.get(i)] + "'";
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

                Cities3 = "";
                for (int i = 0; i < getItemCategory.size() ; i++)
                {
                    for (int j = 0 ; j < ItemCategory.size() ; j++)
                    {
                        if (ItemCategoryDescrip.get(j).equals(getItemCategory.get(i)))
                        {
                            if (i == 0)
                            {
                                Cities3 = " '" + ItemCategory.get(j) + "'";
                            }
                            else
                                Cities3 = Cities3 + ", '" + ItemCategory.get(j) + "'";
                            break;
                        }
                    }
                }

                Year1.clear();
                CostAmt.clear();
                EntryType.clear();

                final ProgressDialog progressDialog = new ProgressDialog(InventryByEntryType.this);

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
            String query = "select DATEPART(YYYY, [Posting Date]) as Yr, [Entry Type], SUM([Cost Amount (Actual)]) as costAmt FROM [dbo].["+CName+"$Inventory Staging] as InvenStag, [dbo].["+CName+"$Item] as item where InvenStag.[Item No_] = item.No_ and DATEPART(YYYY, [Posting Date]) in ("+Cities+") and item.[Inventory Posting Group] in ("+Cities2+") and InvenStag.[Item Category Code] in ("+Cities3+") group by DATEPART(YYYY, [Posting Date]), [Entry Type] order by DATEPART(YYYY, [Posting Date]), [Entry Type]";
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android","database Connected");

                while (rs.next()) {

                    String StoreNo = rs.getString("Yr");
                    Year1.add(String.valueOf(StoreNo));

                    int a = rs.getInt("Entry Type");
                    EntryType.add(a);

                    double ReceBalAmt = rs.getDouble("costAmt");
                    ReceBalAmt = Math.abs(ReceBalAmt);
                    CostAmt.add(ReceBalAmt);
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
    public  void PrintTable() {
        String NewYr = "";

        for (int i = 0 ; i < Year1.size() ; i++)
        {
            EntryTyp = String.valueOf(EntryType.get(i));
            CostAmount = CostAmt.get(i);

            if (NewYr.equals(Year1.get(i)))
            {
                TableEntry();
            }
            else
            {
                TL1.setStretchAllColumns(true);

                TR1 = new TableRow(this);                TV1 = new TextView(this);                TV2 = new TextView(this);                TV3 = new TextView(this);                TV4 = new TextView(this);                TV5 = new TextView(this);                TV6 = new TextView(this);                TV7 = new TextView(this);

                TV1.setText("0");                TV2.setText("0");                TV3.setText("0");                TV4.setText("0");                TV5.setText("0");                TV6.setText("0");                TV7.setText("0");
                TR1.addView(TV1);                TR1.addView(TV2);                TR1.addView(TV3);                TR1.addView(TV4);                TR1.addView(TV5);                TR1.addView(TV6);                TR1.addView(TV7);
                TL1.addView(TR1);

                TV1.setText(Year1.get(i));
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

                TableEntry();
            }
            NewYr = Year1.get(i);
        }

    }
    @SuppressLint("RtlHardcoded")
    public void TableEntry() {
        switch (EntryTyp) {
            case "0":
                TV2.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((CostAmount / intVal[index]) * 100.0) / 100.0));
                break;

            case "1":
                TV3.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((CostAmount / intVal[index]) * 100.0) / 100.0));
                break;

            case "2":
                TV4.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((CostAmount / intVal[index]) * 100.0) / 100.0));
                break;

            case "3":
                TV5.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((CostAmount / intVal[index]) * 100.0) / 100.0));
                break;

            case "4":
                TV6.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((CostAmount / intVal[index]) * 100.0) / 100.0));
                break;

            case "5":
                TV7.setText(NumberFormat.getNumberInstance(Locale.US).format(Math.round((CostAmount / intVal[index]) * 100.0) / 100.0));
                break;
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
}
