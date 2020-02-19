package com.example.shubhammundra.fromandto;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class InventoryOptions extends AppCompatActivity {

    Button InvenOverview,InvenValByLoc,InvenValByVen,InvenTop30ItmTab,InvenTopItmCate,InvenByEntryTyp;

    public static  String CName;

    public static ArrayList<String> Year = new ArrayList<>();

    ImageView imageView;

    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    public static ArrayList<String> ItemCategoryGroup = new ArrayList<>();
    public static ArrayList<String> ItemCategoryDescrip = new ArrayList<>();
    public static ArrayList<String> PostingGrp = new ArrayList<>();

    public static String CompStart;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_options);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        imageView = findViewById(R.id.img10);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        CName = intent.getStringExtra("Company Name");
        Year = intent.getStringArrayListExtra("Unique Year");

        ItemCategoryGroup.clear();
        ItemCategoryDescrip.clear();
        PostingGrp.clear();

        final ProgressDialog progressDialog = new ProgressDialog(InventoryOptions.this);

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
                progressDialog.dismiss();
                Toast.makeText(InventoryOptions.this, "data loading complete", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                if (Looper.myLooper() == null)
                {
                    Looper.prepare();
                }
                CompStartDate();
                ItemCategory();
                PostingGroup();
                return null;
            }
        }.execute();

        InvenOverview = findViewById(R.id.btn_InvenOverVw);
        InvenOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InventoryOptions.this,InventryOverview.class);
                intent.putExtra("Company Name",CName);
                intent.putExtra("Unique Year",Year);
                intent.putExtra("Item Category Group",ItemCategoryGroup);
                intent.putExtra("Item Category Description",ItemCategoryDescrip);
                startActivity(intent);
            }
        });

        InvenValByLoc = findViewById(R.id.btn_InvenByLoc);
        InvenValByLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InventoryOptions.this,InventryValueByLocation.class);
                intent.putExtra("Company Name",CName);
                intent.putExtra("Unique Year",Year);
                intent.putExtra("Item Category Group",ItemCategoryGroup);
                intent.putExtra("Item Category Description",ItemCategoryDescrip);
                startActivity(intent);
            }
        });

        InvenValByVen = findViewById(R.id.btn_InvenByVendr);
        InvenValByVen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InventoryOptions.this,InventryValueByVendor.class);
                intent.putExtra("Company Name",CName);
                intent.putExtra("Unique Year",Year);
                intent.putExtra("Item Category Group",ItemCategoryGroup);
                intent.putExtra("Item Category Description",ItemCategoryDescrip);
                startActivity(intent);
            }
        });

        InvenTop30ItmTab = findViewById(R.id.btn_InvenByTop30Itm);
        InvenTop30ItmTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InventoryOptions.this,InventryByTop30Itm.class);
                intent.putExtra("Company Name",CName);
                intent.putExtra("Unique Year",Year);
                intent.putExtra("Item Category Group",ItemCategoryGroup);
                intent.putExtra("Item Category Description",ItemCategoryDescrip);
                intent.putExtra("Company Start",CompStart);
                startActivity(intent);
            }
        });

        InvenTopItmCate = findViewById(R.id.btn_InvenItmCate);
        InvenTopItmCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InventoryOptions.this,InventryByTopItemCate.class);
                intent.putExtra("Company Name",CName);
                intent.putExtra("Posting Group",PostingGrp);
                intent.putExtra("Company Start",CompStart);
                startActivity(intent);
            }
        });

        InvenByEntryTyp = findViewById(R.id.btn_InvenByEntryTyp);
        InvenByEntryTyp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InventoryOptions.this,InventryByEntryType.class);
                intent.putExtra("Company Name",CName);
                intent.putExtra("Unique Year",Year);
                intent.putExtra("Posting Group",PostingGrp);
                intent.putExtra("Item Category Group",ItemCategoryGroup);
                intent.putExtra("Item Category Description",ItemCategoryDescrip);
                startActivity(intent);
            }
        });
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

                ItemCategoryGroup.add("");
                ItemCategoryDescrip.add("Blank");
                while (rs.next()) {
                    ItemCategoryGroup.add(rs.getString("Code"));
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
    public void PostingGroup() {
        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query = "select Code FROM [dbo].["+CName+"$Inventory Posting Group]";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    PostingGrp.add(rs.getString("Code"));
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
            String query = "select (Format(Convert(date, MIN([Posting Date])), 'yyyy-MM-dd')) as startDate FROM [dbo].["+CName+"$Inventory Staging]";
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
