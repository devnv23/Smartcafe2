package com.cyient.smartcoffee;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class activity_switchon extends AppCompatActivity {

    Vibrator vibe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switchon);
        vibe = (Vibrator)getSystemService(MainActivity.VIBRATOR_SERVICE);

    }
    public void ledOn (View view){
        vibe.vibrate(80);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo!= null && networkInfo.isConnected())
            // new DownloadWebpageTask().execute("http://"+ArduinoIP+"/arduino/digital/13/1");
            new DownloadWebpageTask().execute("http://192.168.15.145/arduino/cup/4");
//        else
//            textView.setText("No Network connection available");
//        urlsentText.setText("http://"+ArduinoIP+"/arduino/digital/13/1");
        Intent i = new Intent(activity_switchon.this, MainActivity.class);
        startActivity(i);

    }
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls){
            //params comes from the execute()
            try{
                return downloadUrl(urls[0]);
            }catch (IOException e){
                return "Unable to retrieve web page. URL may be invalid";
            }
        }
        //onPostExecute display the result of AsyncTask
        @Override
        protected void onPostExecute(String result){
          //  textView.setText(result);

        }
    }
    private String downloadUrl (String myurl) throws IOException {
        InputStream is = null;
        int len = 500; //length of content
        try{
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            //start the query
            conn.connect();
            int response = conn.getResponseCode();
            //Log.d(DEBUG_TAG, "The response is:" + response);
            is = conn.getInputStream();

            //convert to string
            String contentAsString = readIt (is,len);
            return contentAsString;

            //make sure the inputstream is closed after used by app
        }finally{
            if(is!=null)
                is.close();
        }
    }

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }


}
