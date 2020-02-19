package com.example.shubhammundra.fromandto;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.util.ArrayList;

public class PosPayment extends AppCompatActivity {

    Button ByMonthBTN,byTenderBTN,byCardBTN,byTTnCardBTN;
    public static  String CName;
    public static ArrayList<String> Year = new ArrayList<>();
    public static ArrayList<String> stores = new ArrayList<>();
    public static ArrayList<String> storesDescrip = new ArrayList<>();
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_payment);

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
        stores = intent.getStringArrayListExtra("Store Name");
        storesDescrip = intent.getStringArrayListExtra("Store Descrip");

        ByMonthBTN = findViewById(R.id.btn_payByMonReport);
        ByMonthBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent POSPayintentbyMon = new Intent(PosPayment.this,PayByMonth.class);
                POSPayintentbyMon.putExtra("Company Name",CName);
                POSPayintentbyMon.putExtra("Unique Year",Year);
                POSPayintentbyMon.putExtra("Store Name",stores);
                POSPayintentbyMon.putExtra("Store Descrip",storesDescrip);
                startActivity(POSPayintentbyMon);
            }
        });
        byTenderBTN = findViewById(R.id.btn_tenderReport);
        byTenderBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent POSPayintentbyTender = new Intent(PosPayment.this,Tender.class);
                POSPayintentbyTender.putExtra("Company Name",CName);
                POSPayintentbyTender.putExtra("Unique Year",Year);
                POSPayintentbyTender.putExtra("Store Name",stores);
                POSPayintentbyTender.putExtra("Store Descrip",storesDescrip);
                startActivity(POSPayintentbyTender);
            }
        });
        byTTnCardBTN = findViewById(R.id.btn_PayByTTnCardReport);
        byTTnCardBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent POSPayintentbyTTnCard = new Intent(PosPayment.this,PayByTTnCard.class);
                POSPayintentbyTTnCard.putExtra("Company Name",CName);
                POSPayintentbyTTnCard.putExtra("Unique Year",Year);
                POSPayintentbyTTnCard.putExtra("Store Name",stores);
                POSPayintentbyTTnCard.putExtra("Store Descrip",storesDescrip);
                startActivity(POSPayintentbyTTnCard);
            }
        });
        byCardBTN =findViewById(R.id.btn_cardPayReport);
        byCardBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent POSPayintentbyCard = new Intent(PosPayment.this,PayByCard.class);
                POSPayintentbyCard.putExtra("Company Name",CName);
                POSPayintentbyCard.putExtra("Unique Year",Year);
                POSPayintentbyCard.putExtra("Store Name",stores);
                POSPayintentbyCard.putExtra("Store Descrip",storesDescrip);
                startActivity(POSPayintentbyCard);
            }
        });
    }
}
