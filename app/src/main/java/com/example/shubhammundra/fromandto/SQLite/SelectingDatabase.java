package com.example.shubhammundra.fromandto.SQLite;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shubhammundra.fromandto.AES256;
import com.example.shubhammundra.fromandto.ConnectionHelper;
import com.example.shubhammundra.fromandto.R;
import com.example.shubhammundra.fromandto.ReportOption;

import java.sql.Connection;
import java.util.ArrayList;

public class SelectingDatabase extends AppCompatActivity {

    Button btn_newDatabase,btn_exsitingDatabase;
    View view1;

    LayoutInflater layoutInflater;
    EditText ServerIP, DBname, DBinstance, DBusername, DBpassword, CompName;
    public static String ID = "", myIP = "", Name = "", Instance = "", Username = "", Password = "", company = "";
    Button LoginToDB;
    boolean checkInsertion, checkUpdation;

    Connection connect;
    ServerDetailsSqliteManager sqliteManager;

    ListView listView;

    SQLITEadapter sqlitEadapter;

    ArrayList<SQLITEpojo> details = new ArrayList<>();

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_database);

        layoutInflater = LayoutInflater.from(this);

        btn_newDatabase = findViewById(R.id.btn_newDatabase);
        btn_exsitingDatabase = findViewById(R.id.btn_exsitingDatabase);
        listView = findViewById(R.id.ServerListListView);

        sqlitEadapter = new SQLITEadapter(details, this, new SQLITEadapter.TextViewOnClickListner() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClickItem(final SQLITEpojo sqlitEpojo) {

                final ProgressDialog progressDialog = new ProgressDialog(SelectingDatabase.this);

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog.setMessage("Connecting...");
                        progressDialog.show();
                    }

                    @SuppressLint("WrongThread")
                    @Override
                    protected Void doInBackground(Void... voids) {
                        ConnectionHelper connectString = new ConnectionHelper();

                        AES256 aes256 = new AES256();

                        ConnectionHelper.myIP = aes256.decrypt(sqlitEpojo.getMyIP());
                        ConnectionHelper.Instance = aes256.decrypt(sqlitEpojo.getInstance());
                        ConnectionHelper.Name = aes256.decrypt(sqlitEpojo.getName());
                        ConnectionHelper.Username = aes256.decrypt(sqlitEpojo.getUsername());
                        ConnectionHelper.Password = aes256.decrypt(sqlitEpojo.getPassword());
                        ConnectionHelper.company = aes256.decrypt(sqlitEpojo.getCompany());

                        connect = connectString.connectionClass();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);

                        if (connect == null) {
                            Toast.makeText(SelectingDatabase.this, " Network error : Connection refused", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Intent intent = new Intent(SelectingDatabase.this, ReportOption.class);
                            intent.putExtra("Company Name",ConnectionHelper.company);
                            startActivity(intent);
                        }
                        progressDialog.dismiss();
                    }
                }.execute();

            }
        }, new SQLITEadapter.EditOnClickListner() {
            @Override
            public void onClickItemEdit(final SQLITEpojo sqlitEpojo) {
                view1 = layoutInflater.inflate(R.layout.get_server_ip,null);

                ServerIP = view1.findViewById(R.id.et_serverip);
                DBname = view1.findViewById(R.id.et_dbname);
                DBinstance = view1.findViewById(R.id.et_dbinstance);
                DBusername = view1.findViewById(R.id.et_dbUsername);
                DBpassword = view1.findViewById(R.id.et_password);
                CompName = view1.findViewById(R.id.et_compName);
                LoginToDB = view1.findViewById(R.id.btn_getip);

                final AES256 aes256 = new AES256();

                ServerIP.setText(aes256.decrypt(sqlitEpojo.getMyIP()));
                DBname.setText(aes256.decrypt(sqlitEpojo.getName()));
                DBinstance.setText(aes256.decrypt(sqlitEpojo.getInstance()));
                DBusername.setText(aes256.decrypt(sqlitEpojo.getUsername()));
                DBpassword.setText(aes256.decrypt(sqlitEpojo.getPassword()));
                CompName.setText(aes256.decrypt(sqlitEpojo.getCompany()));

                LoginToDB.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onClick(View view) {

                        if(!ServerIP.getText().toString().equals("") && !DBname.getText().toString().equals("") && !DBusername.getText().toString().equals("") && !DBpassword.getText().toString().equals("") && !CompName.getText().toString().equals("")){

                            ID = String.valueOf(sqlitEpojo.getID());
                            myIP = aes256.encrypt(ServerIP.getText().toString());
                            Name = aes256.encrypt(DBname.getText().toString());
                            Instance = aes256.encrypt(DBinstance.getText().toString());
                            Username = aes256.encrypt(DBusername.getText().toString());
                            Password = aes256.encrypt(DBpassword.getText().toString());
                            company = aes256.encrypt(CompName.getText().toString());

                            new AsyncTask<Void, Void, Void>() {

                                ProgressDialog progressDialog = new ProgressDialog(SelectingDatabase.this);
                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
                                    progressDialog.setMessage("Connecting...");
                                    progressDialog.show();
                                }

                                @SuppressLint("WrongThread")
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    if (Looper.myLooper() == null)
                                    {
                                        Looper.prepare();
                                    }
                                    ConnectionHelper connectString = new ConnectionHelper();
                                    ConnectionHelper.myIP = ServerIP.getText().toString();
                                    ConnectionHelper.Name = DBname.getText().toString();
                                    ConnectionHelper.Instance = DBinstance.getText().toString();
                                    ConnectionHelper.Username = DBusername.getText().toString();
                                    ConnectionHelper.Password = DBpassword.getText().toString();
                                    ConnectionHelper.company = CompName.getText().toString();
                                    connect = connectString.connectionClass();
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    super.onPostExecute(aVoid);
                                    if (connect == null) {
                                        Toast.makeText(SelectingDatabase.this, " Network error : Connection refused", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(SelectingDatabase.this, "Connection Successful", Toast.LENGTH_SHORT).show();
                                        checkUpdation =  updateToSqLite(ID, myIP, Instance, Name, Username, Password, company);
                                        if (checkUpdation){
                                            Intent intent = new Intent(SelectingDatabase.this, ReportOption.class);
                                            intent.putExtra("Company Name",ConnectionHelper.company);
                                            startActivity(intent);
                                        }
                                        else{
                                            Toast.makeText(SelectingDatabase.this, "Cannot perform Updation", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    progressDialog.dismiss();
                                }
                            }.execute();

                        }else {
                            Toast.makeText(SelectingDatabase.this, "Entries Cannot be Blank", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(SelectingDatabase.this);
                builder.setView(view1);
                builder.create().show();
            }
        }, new SQLITEadapter.DeleteOnClickListner() {
            @Override
            public void onClickItemDelete(final SQLITEpojo sqlitEpojo) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(SelectingDatabase.this);
                dialog.setTitle("Caution!?");
                dialog.setMessage("Are you sure you want to Delete '"+sqlitEpojo.getCompany() + ": " + sqlitEpojo.getName() + "' entry");
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int deleteRow = sqliteManager.DeleteData(String.valueOf(sqlitEpojo.getID()));
                        if (deleteRow > 0)
                        {
                            Toast.makeText(SelectingDatabase.this, sqlitEpojo.getCompany() + ": " + sqlitEpojo.getName() + " is deleted", Toast.LENGTH_SHORT).show();
                            details.clear();
                            ViewFromSqLite();
                        }
                        else
                            Toast.makeText(SelectingDatabase.this, "Deletion failed", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.create().show();
            }
        });

        btn_newDatabase.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"StaticFieldLeak", "InflateParams"})
            @Override
            public void onClick(View view) {

                view1 = layoutInflater.inflate(R.layout.get_server_ip,null);

                ServerIP = view1.findViewById(R.id.et_serverip);
                DBname = view1.findViewById(R.id.et_dbname);
                DBinstance = view1.findViewById(R.id.et_dbinstance);
                DBusername = view1.findViewById(R.id.et_dbUsername);
                DBpassword = view1.findViewById(R.id.et_password);
                CompName = view1.findViewById(R.id.et_compName);
                LoginToDB = view1.findViewById(R.id.btn_getip);

                ServerIP.setText("");
                DBname.setText("");
                DBinstance.setText("");
                DBusername.setText("");
                DBpassword.setText("");
                CompName.setText("");

                LoginToDB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(!ServerIP.getText().toString().equals("") && !DBname.getText().toString().equals("") && !DBusername.getText().toString().equals("") && !DBpassword.getText().toString().equals("") && !CompName.getText().toString().equals("")){

                            AES256 aes256 = new AES256();

                            myIP = aes256.encrypt(ServerIP.getText().toString());
                            Name = aes256.encrypt(DBname.getText().toString());
                            Instance = aes256.encrypt(DBinstance.getText().toString());
                            Username = aes256.encrypt(DBusername.getText().toString());
                            Password = aes256.encrypt(DBpassword.getText().toString());
                            company = aes256.encrypt(CompName.getText().toString());

                            Log.w("dataEncryptedmyIP", myIP);
                            Log.w("dataEncryptedName", Name);
                            Log.w("dataEncryptedInstance", Instance);
                            Log.w("dataEncryptedUsername", Username);
                            Log.w("dataEncryptedPassword", Password);
                            Log.w("dataEncryptedcompany", company);

                            final String val1 = ServerIP.getText().toString();
                            final String val2 = DBname.getText().toString();
                            final String val3 = DBinstance.getText().toString();
                            final String val4 = DBusername.getText().toString();
                            final String val5 = DBpassword.getText().toString();
                            final String val6 = CompName.getText().toString();

                            new AsyncTask<Void, Void, Void>() {

                                ProgressDialog progressDialog = new ProgressDialog(SelectingDatabase.this);
                                @Override
                                protected void onPreExecute() {
//                                    super.onPreExecute();
                                    progressDialog.setMessage("Connecting...");
                                    progressDialog.show();
                                }

                                @Override
                                protected Void doInBackground(Void... voids) {

                                    Log.w("0AdataEncryptedmyIP", val1);
                                    Log.w("0AdataEncryptedName", val2);
                                    Log.w("0AdataEncryptedInstance", val3);
                                    Log.w("0AdataEncryptedUsername", val4);
                                    Log.w("0AdataEncryptedPassword", val5);
                                    Log.w("0AdataEncryptedcompany", val6);

                                    ConnectionHelper connectString = new ConnectionHelper();
                                    ConnectionHelper.myIP = val1;
                                    ConnectionHelper.Name = val2;
                                    ConnectionHelper.Instance = val3;
                                    ConnectionHelper.Username = val4;
                                    ConnectionHelper.Password = val5;
                                    ConnectionHelper.company = val6;

                                    Log.w("AdataEncryptedmyIP", val1);
                                    Log.w("AdataEncryptedName", val2);
                                    Log.w("AdataEncryptedInstance", val3);
                                    Log.w("AdataEncryptedUsername", val4);
                                    Log.w("AdataEncryptedPassword", val5);
                                    Log.w("AdataEncryptedcompany", val6);

                                    connect = connectString.connectionClass();
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    super.onPostExecute(aVoid);
                                    if (connect == null) {
                                        Toast.makeText(SelectingDatabase.this, " Network error : Connection refused", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(SelectingDatabase.this, "Connection Successful", Toast.LENGTH_SHORT).show();
                                        checkInsertion =  addToSqLite(myIP,Instance,Name,Username,Password,company);
                                        if (checkInsertion){
                                            Intent intent = new Intent(SelectingDatabase.this, ReportOption.class);
                                            intent.putExtra("Company Name",ConnectionHelper.company);
                                            startActivity(intent);
                                        }
                                        else{
                                            Toast.makeText(SelectingDatabase.this, "Cannot perform Insertion", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    progressDialog.dismiss();
                                }
                            }.execute();

                        }else {
                            Toast.makeText(SelectingDatabase.this, "Entries Cannot be Blank", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(SelectingDatabase.this);
                builder.setView(view1);
                builder.create().show();
            }
        });

        btn_exsitingDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                details.clear();
                ViewFromSqLite();
            }
        });
    }

    private boolean addToSqLite(String myIP, String instance, String name, String username, String password, String company) {
        sqliteManager = new ServerDetailsSqliteManager(SelectingDatabase.this);
        long insert = sqliteManager.InsertData(myIP,instance,name,username,password,company);
        if(insert==-1){
            Log.d("SQLITE","Insertion Failed.");
            return false;
        }
        else {
            Log.d("SQLITE","Insertion Successful.");
            return true;
        }
    }

    private void ViewFromSqLite() {
        sqliteManager = new ServerDetailsSqliteManager(SelectingDatabase.this);
        Cursor cursor = sqliteManager.ViewData();

        while (cursor.moveToNext()){
            details.add(new SQLITEpojo(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6)));
        }

        listView.setAdapter(sqlitEadapter);
    }

    private boolean updateToSqLite(String ID, String myIP, String instance, String name, String username, String password, String company)
    {
        sqliteManager = new ServerDetailsSqliteManager(SelectingDatabase.this);
        boolean update = sqliteManager.UpdateData(ID, myIP, instance, name, username, password, company);
        if(!update) {
            Log.d("SQLITE","Updation Failed.");
            return false;
        }
        else {
            Log.d("SQLITE","Updation Successful.");
            return true;
        }
    }

}
