package com.example.binanceapitest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        Thread thread = new Thread() {

            @Override
            public void run() {

                try {
                    while (!currentThread().isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String price;
                                DownloadJson dl = new DownloadJson();
                                try{
                                    price = dl.execute("https://api.binance.com/api/v3/ticker/price?symbol=BTCUSDT").get();
                                    textView.setText("BTCUSD "+price);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public class DownloadJson extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String p;
            try{
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data;
                String json = "";
                do{
                    data = reader.read();
                    char c = (char) data;
                    json += c;
                }while(data!=-1);
                JSONObject jsonObject = new JSONObject(json);
                p = jsonObject.getString("price");
                p = String.format("%.2f",Double.parseDouble(p));
                Log.i("BTC Price", p);
                return p;
            }catch(Exception e){
                e.printStackTrace();
                return "failed";
            }
        }
    }

}