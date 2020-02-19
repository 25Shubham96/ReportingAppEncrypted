package com.example.shubhammundra.fromandto;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

public class ReportOption extends AppCompatActivity {

    Button StoreBTN,CategoryBTN,TimeBTN,PaymentsBTN,StaffBTN,FrequencyBTN,ReceiptBTN,DiscTypeBTN,SpecDiscBTN,ExpenseBTN,IncomeBTN,StoreOvrYrBTN,HourlySalesBTN,SalesBTN,KPIAnalysisBTN,POSYearBTN,YTDBTN,StOvrvwBTN,MTDBTN,ReceivableBTN,POSPaymentBTN,WTDBTN,ValueEntryBTN,ReceivablesReportingBTN,InventoryReportingBTN,SalesHistoryBTN;
    ImageView iv;

    public static String CName;

    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    ArrayList<String> stores = new ArrayList<>();
    ArrayList<String> storesDescrip = new ArrayList<>();

    ArrayList<String> cities = new ArrayList<>();
    ArrayList<String> year = new ArrayList<>();
    ArrayList<String> item = new ArrayList<>();

    ArrayList<String> Week123 = new ArrayList<>();
    ArrayList<String> Quater123 = new ArrayList<>();
    ArrayList<String> Month123 = new ArrayList<>();

    @SuppressLint({"CutPasteId", "StaticFieldLeak", "SetTextI18n"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_option);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        CName = intent.getStringExtra("Company Name");

        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();
        if (connect == null) {
            Toast.makeText(ReportOption.this, " Network error : Connection refused", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(ReportOption.this, "Connection Successful", Toast.LENGTH_SHORT).show();
        }

        StoreBTN = findViewById(R.id.btn_storeReport);
        CategoryBTN = findViewById(R.id.btn_categoryReport);
        TimeBTN = findViewById(R.id.btn_timeReport);
        PaymentsBTN = findViewById(R.id.btn_paymentsReport);
        StaffBTN = findViewById(R.id.btn_staffReport);
        FrequencyBTN = findViewById(R.id.btn_frequencyReport);
        ReceiptBTN  = findViewById(R.id.btn_ReceiptReport);
        SpecDiscBTN = findViewById(R.id.btn_DiscountSpeReport);
        DiscTypeBTN = findViewById(R.id.btn_DiscountReport);
        ExpenseBTN = findViewById(R.id.btn_ExpenseReport);
        IncomeBTN  = findViewById(R.id.btn_IncomeReport);
        StoreOvrYrBTN = findViewById(R.id.btn_StrOverYrReport);
        HourlySalesBTN = findViewById(R.id.btn_HourlySalesReport);
        SalesBTN  = findViewById(R.id.btn_SalesReport);
        KPIAnalysisBTN = findViewById(R.id.btn_KPIAnalysis);
        POSYearBTN = findViewById(R.id.btn_POSYear);
        YTDBTN = findViewById(R.id.btn_YTDReport);
        StOvrvwBTN = findViewById(R.id.btn_StrOverviewReport);
        MTDBTN = findViewById(R.id.btn_MTDReport);
//        ReceivableBTN = findViewById(R.id.btn_ReceivableReport);
        POSPaymentBTN = findViewById(R.id.btn_posPaymentReport);
        WTDBTN = findViewById(R.id.btn_WTDReport);
        ValueEntryBTN = findViewById(R.id.btn_valueEntryReport);
        ReceivablesReportingBTN = findViewById(R.id.btn_receivablesReporting);
        InventoryReportingBTN = findViewById(R.id.btn_inventoryReporting);
        SalesHistoryBTN = findViewById(R.id.btn_salesHistory);

        iv = findViewById(R.id.img10);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ReportOption.this);
                alertDialog.setTitle("Exit?");
                alertDialog.setMessage("Are you sure?");

                alertDialog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(1);
                        /*finishAffinity();
                        System.exit(0);
                        finishAndRemoveTask();*/
                    }
                });

                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });

                alertDialog.show();
            }
        });

//        final ProgressDialog progressDialog = new ProgressDialog(ReportOption.this);

//        final Dialog dialog = new Dialog(ReportOption.this);
//        dialog.setContentView(R.layout.custom_dialog);
//
//        TextView tv = dialog.findViewById(R.id.tv_comment);
//        tv.setText("loading content please wait...");

        new AsyncTask<Void, Void, Void>() {

            ProgressDialog progressDialog = new ProgressDialog(ReportOption.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setMessage("Loading content please wait...");
                progressDialog.show();
//                dialog.show();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                if (Looper.myLooper() == null)
                {
                    Looper.prepare();
                }

                Stores();
                Cities();
                Year();
                Week123();
                Quater123();
                Month123();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();
//                dialog.dismiss();
            }

        }.execute();
        SelectReports();
    }

    public void SelectReports() {

        StoreBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent storeintent = new Intent(ReportOption.this,DisplayDate.class);
                storeintent.putExtra("Company Name",CName);
                storeintent.putExtra("Unique Year",year);
                storeintent.putExtra("Store Name",stores);
                startActivity(storeintent);
            }
        });

        CategoryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent categoryintent = new Intent(ReportOption.this,ItemCategoryReport.class);
                categoryintent.putExtra("Company Name",CName);
                categoryintent.putExtra("Unique Year",year);
                categoryintent.putExtra("Store Name",stores);
                startActivity(categoryintent);
            }
        });

        TimeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent timeintent = new Intent(ReportOption.this,TimeReport.class);
                timeintent.putExtra("Company Name",CName);
                timeintent.putExtra("Unique Year",year);
                timeintent.putExtra("Store Name",stores);
                startActivity(timeintent);
            }
        });

        PaymentsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent paymentsintent = new Intent(ReportOption.this,PaymentsReport.class);
                paymentsintent.putExtra("Company Name",CName);
                paymentsintent.putExtra("Unique Year",year);
                paymentsintent.putExtra("Store Name",stores);
                startActivity(paymentsintent);
            }
        });

        StaffBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent staffintent = new Intent(ReportOption.this,StaffReport.class);
                staffintent.putExtra("Company Name",CName);
                staffintent.putExtra("Unique Year",year);
                staffintent.putExtra("Store Name",stores);
                startActivity(staffintent);
            }
        });

        FrequencyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent frequencyintent = new Intent(ReportOption.this,FrequencyReport.class);
                frequencyintent.putExtra("Company Name",CName);
                frequencyintent.putExtra("Unique Year",year);
                frequencyintent.putExtra("Store Name",stores);
                startActivity(frequencyintent);
            }
        });

        ReceiptBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent receiptintent = new Intent(ReportOption.this,ReceiptReport.class);
                receiptintent.putExtra("Company Name",CName);
                receiptintent.putExtra("Unique Year",year);
                receiptintent.putExtra("Store Name",stores);
                startActivity(receiptintent);
            }
        });

        SpecDiscBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SpecDiscintent = new Intent(ReportOption.this,DiscountSpecials.class);
                SpecDiscintent.putExtra("Company Name",CName);
                SpecDiscintent.putExtra("Unique Year",year);
                SpecDiscintent.putExtra("Store Name",stores);
                startActivity(SpecDiscintent);
            }
        });

        DiscTypeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Disctypintent = new Intent(ReportOption.this,DiscountType.class);
                Disctypintent.putExtra("Company Name",CName);
                Disctypintent.putExtra("Unique Year",year);
                Disctypintent.putExtra("Store Name",stores);
                startActivity(Disctypintent);
            }
        });

        ExpenseBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Expenseintent = new Intent(ReportOption.this,ExpenseReport.class);
                Expenseintent.putExtra("Company Name",CName);
                Expenseintent.putExtra("Unique Year",year);
                Expenseintent.putExtra("Store Name",stores);
                startActivity(Expenseintent);
            }
        });

        IncomeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Incomeintent = new Intent(ReportOption.this,IncomeReport.class);
                Incomeintent.putExtra("Company Name",CName);
                Incomeintent.putExtra("Unique Year",year);
                Incomeintent.putExtra("Store Name",stores);
                startActivity(Incomeintent);
            }
        });

        StoreOvrYrBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent StrOvrYrintent = new Intent(ReportOption.this,StrOvrYrReport.class);
                StrOvrYrintent.putExtra("Company Name",CName);
                StrOvrYrintent.putExtra("Store Name",stores);
                StrOvrYrintent.putExtra("Unique Year",year);
                startActivity(StrOvrYrintent);
            }
        });

        HourlySalesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent HrlySaleintent = new Intent(ReportOption.this,HourlySales.class);
                HrlySaleintent.putExtra("Company Name",CName);
                HrlySaleintent.putExtra("Unique Year",year);
                startActivity(HrlySaleintent);
            }
        });

        KPIAnalysisBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent KPIintent = new Intent(ReportOption.this,KPIAnalysis.class);
                KPIintent.putExtra("Company Name",CName);
                KPIintent.putExtra("Unique Year",year);
                KPIintent.putExtra("Store Name",stores);
                startActivity(KPIintent);
            }
        });

        POSYearBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent POSyrintent = new Intent(ReportOption.this,POSYear.class);
                POSyrintent.putExtra("Company Name",CName);
                POSyrintent.putExtra("Unique Year",year);
                POSyrintent.putExtra("Store Name",stores);
                startActivity(POSyrintent);
            }
        });

        SalesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Saleintent = new Intent(ReportOption.this,Sales.class);
                Saleintent.putExtra("Company Name",CName);
                Saleintent.putExtra("Unique Year",year);
                Saleintent.putExtra("Store Name",stores);
                Saleintent.putExtra("Store Descrip",storesDescrip);
                startActivity(Saleintent);
            }
        });

        YTDBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent YTDintent = new Intent(ReportOption.this,YtdAnalysis.class);
                YTDintent.putExtra("Company Name",CName);
                YTDintent.putExtra("Unique Year",year);
                YTDintent.putExtra("Store Name",stores);
                startActivity(YTDintent);
            }
        });

        StOvrvwBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent StrOvrvwintent = new Intent(ReportOption.this,StoreOverview.class);
                StrOvrvwintent.putExtra("Company Name",CName);
                StrOvrvwintent.putExtra("Unique Year",year);
                StrOvrvwintent.putExtra("Store Name",stores);
                StrOvrvwintent.putExtra("Store Descrip",storesDescrip);
                startActivity(StrOvrvwintent);
            }
        });

        MTDBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MTDintent = new Intent(ReportOption.this,MtdAnalysis.class);
                MTDintent.putExtra("Company Name",CName);
                MTDintent.putExtra("Unique Year",year);
                MTDintent.putExtra("Store Name",stores);
                startActivity(MTDintent);
            }
        });

        WTDBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent WTDintent = new Intent(ReportOption.this,WtdAnalysis.class);
                WTDintent.putExtra("Company Name",CName);
                WTDintent.putExtra("Unique Year",year);
                WTDintent.putExtra("Store Name",stores);
                startActivity(WTDintent);
            }
        });

        /*ReceivableBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Receivableintent = new Intent(ReportOption.this,Receiveable.class);
                Receivableintent.putExtra("Company Name",CName);
                startActivity(Receivableintent);
            }
        });*/

        POSPaymentBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent POSPayintent = new Intent(ReportOption.this,PosPayment.class);
                POSPayintent.putExtra("Company Name",CName);
                POSPayintent.putExtra("Unique Year",year);
                POSPayintent.putExtra("Store Name",stores);
                POSPayintent.putExtra("Store Descrip",storesDescrip);
                startActivity(POSPayintent);
            }
        });

        ValueEntryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ValueEntryintent = new Intent(ReportOption.this,ValueEntryReport.class);
                ValueEntryintent.putExtra("Company Name",CName);
                ValueEntryintent.putExtra("Store Name",stores);
                ValueEntryintent.putExtra("Store Descrip",storesDescrip);
                startActivity(ValueEntryintent);
            }
        });

        ReceivablesReportingBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ReceivableReporting = new Intent(ReportOption.this,ReceivablesOptions.class);
                ReceivableReporting.putExtra("Company Name",CName);
                ReceivableReporting.putExtra("Unique Year",year);
                startActivity(ReceivableReporting);
            }
        });

        InventoryReportingBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent InventoryReporting = new Intent(ReportOption.this,InventoryOptions.class);
                InventoryReporting.putExtra("Company Name",CName);
                InventoryReporting.putExtra("Unique Year",year);
                startActivity(InventoryReporting);
            }
        });

        SalesHistoryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportOption.this,SalesHistory.class);
                intent.putExtra("Company Name",CName);
                intent.putExtra("Week",Week123);
                intent.putExtra("Quater",Quater123);
                intent.putExtra("Month",Month123);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Exit?");
        alertDialog.setMessage("Are you sure?");

        alertDialog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.exit(1);
                /*finishAffinity();
                System.exit(0);
                finishAndRemoveTask();*/
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertDialog.show();
    }

    public void Stores() {
        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query = "Select No_, Name from ["+CName+"$Store] where No_ != 'HO' order by No_";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    stores.add(rs.getString("No_"));
                    storesDescrip.add(rs.getString("No_") + " - " + rs.getString("Name"));
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void Cities() {
        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query = "Select distinct City from [" + CName + "$Store]";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    cities.add(rs.getString("City"));
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void Year() {
        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query = "select distinct DATEPART(YYYY,Date) as Year from [dbo].["+CName+"$Transaction Header] order by DATEPART(YYYY,Date)";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    year.add(rs.getString("Year"));
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void Week123() {
        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query = "Select distinct DATEPART(WEEK,[Posting Date]) as WEEK FROM [dbo].["+CName+"$Value Entry] order by DATEPART(WEEK,[Posting Date])";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    Week123.add(rs.getString("WEEK"));
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void Quater123() {
        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query = "Select distinct DATEPART(QUARTER,[Posting Date]) as QUARTER FROM [dbo].["+CName+"$Value Entry] order by DATEPART(QUARTER,[Posting Date])";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    Quater123.add(rs.getString("QUARTER"));
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void Month123() {
        ConnectionHelper connectString = new ConnectionHelper();
        connect = connectString.connectionClass();

        Log.w("Android", "Connecting....");

        if (connect == null) {
            Log.w("Android", "Connection Failed");
            ConnectionResult = "Check Your Internet Connection";
        } else {
            Log.w("Android", "Connected");
            String query = "Select distinct DATEPART(MONTH,[Posting Date]) as MONTH FROM [dbo].["+CName+"$Value Entry] order by DATEPART(MONTH,[Posting Date])";
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                Log.w("Android", "database Connected");

                while (rs.next()) {
                    Month123.add(rs.getString("MONTH"));
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
