package com.example.shubhammundra.fromandto;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GetCredentials extends AppCompatActivity {

    EditText a,b,c,d,e,f;
    TextView tv;

    Button IP;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_server_ip);

        a = findViewById(R.id.et_serverip);
        b = findViewById(R.id.et_dbname);
        c = findViewById(R.id.et_dbUsername);
        d = findViewById(R.id.et_password);
        e = findViewById(R.id.et_compName);
        f = findViewById(R.id.et_dbinstance);

        IP = findViewById(R.id.btn_getip);

        IP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String myIP = a.getText().toString();
                String DBname = b.getText().toString();
                String DBusername = c.getText().toString();
                String DBpassword = d.getText().toString();
                String CompName = e.getText().toString();
                String DBinstance = f.getText().toString();

                tv.setText(myIP);

                Intent intentToReport = new Intent(GetCredentials.this,ReportOption.class);
                intentToReport.putExtra("Server IP",myIP);
                intentToReport.putExtra("DB Name",DBname);
                intentToReport.putExtra("DB Username",DBusername);
                intentToReport.putExtra("DB Password",DBpassword);
                intentToReport.putExtra("Company Name",CompName);
                intentToReport.putExtra("DB Instance",DBinstance);
                startActivity(intentToReport);
            }
        });
    }
}
