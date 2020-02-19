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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ReceivablesOptions extends AppCompatActivity {

    Button ReceBal,RecePayHabs,ReceKPIs,ReceSales;
    Button ReceBal2,RecePayHabs2,ReceKPIs2,ReceSales2;

    public static  String CName;

    public static ArrayList<String> Year = new ArrayList<>();

    ImageView imageView;

    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    public static ArrayList<String> PostingGrp = new ArrayList<>();
    public static ArrayList<String> CountryCode = new ArrayList<>();
    public static ArrayList<String> Country = new ArrayList<>();

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receivables_options);

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

        PostingGrp.clear();
        CountryCode.clear();
        Country.clear();

        final ProgressDialog progressDialog = new ProgressDialog(ReceivablesOptions.this);

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
                progressDialog.dismiss();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                if (Looper.myLooper() != null)
                {
                    Looper.prepare();
                }
                PostingGroup();
                Country();
                return null;
            }
        }.execute();

        ReceBal = findViewById(R.id.btn_ReceBal);
        ReceBal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReceivablesOptions.this,ReceivableBalance.class);
                intent.putExtra("Company Name",CName);
                intent.putExtra("Unique Year",Year);
                intent.putExtra("Posting Group",PostingGrp);
                intent.putExtra("Country Code",CountryCode);
                intent.putExtra("Country",Country);
                startActivity(intent);
            }
        });

        RecePayHabs = findViewById(R.id.btn_RecePayHab);
        RecePayHabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReceivablesOptions.this,ReceivablePaymentHabits.class);
                intent.putExtra("Company Name",CName);
                intent.putExtra("Unique Year",Year);
                intent.putExtra("Posting Group",PostingGrp);
                intent.putExtra("Country Code",CountryCode);
                intent.putExtra("Country",Country);
                startActivity(intent);
            }
        });


        ReceKPIs = findViewById(R.id.btn_ReceKPIs);
        ReceKPIs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReceivablesOptions.this,ReceivableKPIs.class);
                intent.putExtra("Company Name",CName);
                intent.putExtra("Unique Year",Year);
                intent.putExtra("Posting Group",PostingGrp);
                intent.putExtra("Country Code",CountryCode);
                intent.putExtra("Country",Country);
                startActivity(intent);
            }
        });

        ReceSales =findViewById(R.id.btn_ReceSales);
        ReceSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReceivablesOptions.this,ReceivableSales.class);
                intent.putExtra("Company Name",CName);
                intent.putExtra("Unique Year",Year);
                intent.putExtra("Posting Group",PostingGrp);
                intent.putExtra("Country Code",CountryCode);
                intent.putExtra("Country",Country);
                startActivity(intent);
            }
        });

        ReceBal2 = findViewById(R.id.btn_ReceBal2);
        ReceBal2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReceivablesOptions.this,ReceivableBalance2.class);
                intent.putExtra("Company Name",CName);
                intent.putExtra("Unique Year",Year);
                intent.putExtra("Posting Group",PostingGrp);
                intent.putExtra("Country Code",CountryCode);
                intent.putExtra("Country",Country);
                startActivity(intent);
            }
        });

        RecePayHabs2 = findViewById(R.id.btn_RecePayHab2);
        RecePayHabs2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReceivablesOptions.this,ReceivablePaymentHabits2.class);
                intent.putExtra("Company Name",CName);
                intent.putExtra("Unique Year",Year);
                intent.putExtra("Posting Group",PostingGrp);
                intent.putExtra("Country Code",CountryCode);
                intent.putExtra("Country",Country);
                startActivity(intent);
            }
        });


        ReceKPIs2 = findViewById(R.id.btn_ReceKPIs2);
        ReceKPIs2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReceivablesOptions.this,ReceivableKPIs2.class);
                intent.putExtra("Company Name",CName);
                intent.putExtra("Unique Year",Year);
                intent.putExtra("Posting Group",PostingGrp);
                intent.putExtra("Country Code",CountryCode);
                intent.putExtra("Country",Country);
                startActivity(intent);
            }
        });

        ReceSales2 =findViewById(R.id.btn_ReceSales2);
        ReceSales2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReceivablesOptions.this,ReceivableSales2.class);
                intent.putExtra("Company Name",CName);
                intent.putExtra("Unique Year",Year);
                intent.putExtra("Posting Group",PostingGrp);
                intent.putExtra("Country Code",CountryCode);
                intent.putExtra("Country",Country);
                startActivity(intent);
            }
        });
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
            String query = "select Code FROM [dbo].["+CName+"$Customer Posting Group]";
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
    public void Country() {

        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query = "select Code, Name FROM [dbo].["+CName+"$Country_Region]";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    CountryCode.add(rs.getString("Code"));
                    Country.add(rs.getString("Name"));
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
