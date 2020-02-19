package com.example.shubhammundra.fromandto;

import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper {

    public static String server_ip, db_name, db_instance, db_username, db_password;

    EditText ServerIP, DBname, DBinstance, DBusername, DBpassword, CompName;

    public static String myIP = "", Name = "", Instance = "", Username = "", Password = "", company = "";

    Button IP;

    /*@Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_server_ip);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ServerIP = findViewById(R.id.et_serverip);
        DBname = findViewById(R.id.et_dbname);
        DBinstance = findViewById(R.id.et_dbinstance);
        DBusername = findViewById(R.id.et_dbUsername);
        DBpassword = findViewById(R.id.et_password);
        CompName = findViewById(R.id.et_compName);

        IP = findViewById(R.id.btn_getip);

        IP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myIP = ServerIP.getText().toString();
                Name = DBname.getText().toString();
                Instance = DBinstance.getText().toString();
                Username = DBusername.getText().toString();
                Password = DBpassword.getText().toString();
                company = CompName.getText().toString();

                Intent intentToReport = new Intent(ConnectionHelper.this,ReportOption.class);
                intentToReport.putExtra("Company Name",company);
                startActivity(intentToReport);
            }
        });
    }*/

    public static Connection connectionClass() {

        server_ip = myIP;
        db_name = Name;
        db_instance = Instance;
        db_username = Username;
        db_password = Password;

        /*Log.d("Server IP",server_ip);
        Log.d("Database name",db_name);
        Log.d("username",db_username);
        Log.d("password",db_password);*/

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection connection = null;
        String ConnectionURL = null;

        try {
            Log.d("Connection Status", "pass");
            Class.forName("net.sourceforge.jtds.jdbc.Driver");

            //this is connection string
            ConnectionURL = "jdbc:jtds:sqlserver://" + server_ip + ";databaseName=" + db_name + ";instance=" + db_instance + ";user=" + db_username + ";password=" + db_password + ";";

            connection = DriverManager.getConnection(ConnectionURL);
            Log.d("Connection Status", "Successfull");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

}