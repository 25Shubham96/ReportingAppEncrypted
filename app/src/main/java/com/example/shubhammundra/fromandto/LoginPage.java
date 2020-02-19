package com.example.shubhammundra.fromandto;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shubhammundra.fromandto.SQLite.SelectingDatabase;
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.network.connectionclass.DeviceBandwidthSampler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class LoginPage extends AppCompatActivity{

    TextView button1;

    private static final String TAG = "ConnectionClass-Sample";

    public ConnectionClassManager mConnectionClassManager;
    public DeviceBandwidthSampler mDeviceBandwidthSampler;
    public ConnectionChangedListener mListener;

    public String mURL = "https://unsplash.com/photos/rdoRdjOk-OY/download?force=true";

    public int mTries = 0;

    public ConnectionQuality mConnectionClass = ConnectionQuality.UNKNOWN;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_login);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mConnectionClassManager = ConnectionClassManager.getInstance();
        mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance();

        mListener = new ConnectionChangedListener();

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        if (connected == false )
        {
            Toast.makeText(LoginPage.this, "Please connect to Wifi", Toast.LENGTH_SHORT).show();
        }

        button1 = findViewById(R.id.btn_login);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(LoginPage.this, SelectingDatabase.class);
                startActivity(loginIntent);
            }
        });

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new DownloadImage().execute(mURL);
                handler.postDelayed(this,1000);
            }
        },1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mConnectionClassManager.remove(mListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mConnectionClassManager.register(mListener);
    }

    private class ConnectionChangedListener implements ConnectionClassManager.ConnectionClassStateChangeListener {

        @Override
        public void onBandwidthStateChange(ConnectionQuality bandwidthState) {
            mConnectionClass = bandwidthState;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mConnectionClass.toString() == "POOR" || mConnectionClass.toString() == "MODERATE")
                    {
                        Toast.makeText(LoginPage.this, "very low internet speed data loading may take time", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class DownloadImage extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            mDeviceBandwidthSampler.startSampling();
        }
        @Override
        protected Void doInBackground(String... url) {
            String imageURL = url[0];
            try {
                // Open a stream to download the image from our URL.
                URLConnection connection = new URL(imageURL).openConnection();
                connection.setUseCaches(false);
                connection.connect();
                InputStream input = connection.getInputStream();
                try {
                    byte[] buffer = new byte[1024];

                    // Do some busy waiting while the stream is open.
                    while (input.read(buffer) != -1) {
                    }
                } finally {
                    input.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error while downloading image.");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void v) {
            mDeviceBandwidthSampler.stopSampling();
            // Retry for up to 10 times until we find a ConnectionClass.
            if (mConnectionClass == ConnectionQuality.UNKNOWN && mTries < 10) {
                mTries++;
                new DownloadImage().execute(mURL);
            }
            if (!mDeviceBandwidthSampler.isSampling()) {
                //Toast.makeText(LoginPage.this, "network check complete", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
//As in the reporting application I was just fetching the data directly from the database, but here something different task have to be performed.