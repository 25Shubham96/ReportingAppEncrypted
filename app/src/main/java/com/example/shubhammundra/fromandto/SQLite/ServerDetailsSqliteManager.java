package com.example.shubhammundra.fromandto.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

public class ServerDetailsSqliteManager {

    private SQLiteDatabase sqLiteDatabase;
    private static final String DATABASE_NAME = "ServerDetails";
    private static final String TABLE_NAME = "ServerData";
    private static final String COL1_ID = "ID";
    private static final String COL2_ServerIP_Port = "serverIP";
    private static final String COL3_InstanceName = "instanseName";
    private static final String COL4_DatabaseName = "databaseName";
    private static final String COL5_Username = "username";
    private static final String COL6_Password = "password";
    private static final String COL7_CompanyName = "companyName";
    private static final int Database_Version = 1;

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COL1_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                                                                                        COL2_ServerIP_Port+" TEXT, "+
                                                                                        COL3_InstanceName+" TEXT, "+
                                                                                        COL4_DatabaseName+" TEXT, "+
                                                                                        COL5_Username+" TEXT, "+
                                                                                        COL6_Password+" TEXT, "+
                                                                                        COL7_CompanyName+" TEXT);";

    static class DbHelper extends SQLiteOpenHelper{

        Context context;

        public DbHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null, Database_Version);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE);
            Log.w("SQLITE",TABLE_NAME + ": table created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }

    public ServerDetailsSqliteManager(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public long InsertData(String ServerIP, String InstanceName, String DatabaseName, String Username, String Password, String CompanyName){

        ContentValues values = new ContentValues();
        values.put(COL2_ServerIP_Port,ServerIP);
        values.put(COL3_InstanceName,InstanceName);
        values.put(COL4_DatabaseName,DatabaseName);
        values.put(COL5_Username,Username);
        values.put(COL6_Password,Password);
        values.put(COL7_CompanyName,CompanyName);

        return sqLiteDatabase.insert(TABLE_NAME, null, values);
    }

    public Cursor ViewData(){
        return sqLiteDatabase.rawQuery("Select * from " + TABLE_NAME, null);
    }

    public boolean UpdateData(String ID, String ServerIP, String InstanceName, String DatabaseName, String Username, String Password, String CompanyName){
        ContentValues values = new ContentValues();
        values.put(COL2_ServerIP_Port,ServerIP);
        values.put(COL3_InstanceName,InstanceName);
        values.put(COL4_DatabaseName,DatabaseName);
        values.put(COL5_Username,Username);
        values.put(COL6_Password,Password);
        values.put(COL7_CompanyName,CompanyName);
        sqLiteDatabase.update(TABLE_NAME, values, "ID = ?", new String[] {ID});
        return true;
    }

    public int DeleteData(String ID){
        return sqLiteDatabase.delete(TABLE_NAME,"ID = ?", new String[] {ID});
    }
}
