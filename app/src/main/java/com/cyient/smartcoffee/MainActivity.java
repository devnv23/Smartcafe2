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
import android.view.MenuItem;
        import android.view.View;
        import android.widget.EditText;
import android.widget.SeekBar;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.Reader;
        import java.io.UnsupportedEncodingException;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.concurrent.ArrayBlockingQueue;

       // import static com.cyient.cafe.R.id.smallcup;

public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "HttpExample";

    public final String ARDUINO_IP_ADDRESS = "192.168.15.145";
    public String ArduinoIP = "0.0.0.0";
    public String CustomCup;

    private EditText urlText,customcup;
    private TextView textView;
    private TextView yunIPtextView;
    private TextView urlsentText;

    private SeekBar mSeekBar;
    private ArrayBlockingQueue<Integer> mQueue = new ArrayBlockingQueue<Integer>(255);
    Vibrator vibe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smart_coffee);
        mSeekBar= (SeekBar) findViewById(R.id.seekBar);
        vibe = (Vibrator)getSystemService(MainActivity.VIBRATOR_SERVICE);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mQueue.clear();
                mQueue.offer(progress);
                String tempstring = Integer.toString(progress);
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo!= null && networkInfo.isConnected())
                    new DownloadWebpageTask().execute("http://"+ArduinoIP+"/arduino/analog/13/"+tempstring);
                else
                    textView.setText("No Network connection available");
                urlsentText.setText("http://"+ArduinoIP+"/arduino/analog/13/"+tempstring);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        urlText = (EditText) findViewById(R.id.myUrl);
        textView = (TextView) findViewById(R.id.myText);
        yunIPtextView = (TextView) findViewById(R.id.ipinformation);
        urlsentText = (TextView) findViewById(R.id.urlsent);


    }

    public void myClickHandler (View view){
        //Gets URL from UI's text field
        String stringUrl = urlText.getText().toString();
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo!= null && networkInfo.isConnected()){
            new DownloadWebpageTask().execute(stringUrl);
        }
        else
            textView.setText("No Network connection available");
    }

    public void saveClickHandler (View view){
        ArduinoIP= urlText.getText().toString();
        yunIPtextView.setText("Arduino Yun IP: "+ArduinoIP);
    }

    public void ledOn (View view){
        vibe.vibrate(80);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo!= null && networkInfo.isConnected())
            // new DownloadWebpageTask().execute("http://"+ArduinoIP+"/arduino/digital/13/1");
            new DownloadWebpageTask().execute("http://192.168.15.145/arduino/cup/4");
        else
            textView.setText("No Network connection available");
        urlsentText.setText("http://"+ArduinoIP+"/arduino/digital/13/1");

    }
    public void smallcup (View view){
        Toast.makeText(getApplicationContext(), "Small Coffee" ,Toast.LENGTH_SHORT).show();
        vibe.vibrate(80);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        Toast.makeText(getApplicationContext(), "Small Coffee" ,Toast.LENGTH_SHORT).show();
        if (networkInfo!= null && networkInfo.isConnected())
            // new DownloadWebpageTask().execute("http://"+ArduinoIP+"/arduino/digital/13/1");
            new DownloadWebpageTask().execute("http://192.168.15.145/arduino/cup/1");
        else
            textView.setText("No Network connection available");
        urlsentText.setText("http://"+ArduinoIP+"/arduino/digital/13/1");
    }
    public void mediumcup (View view){
        Toast.makeText(getApplicationContext(), "Medium Coffee" ,Toast.LENGTH_SHORT).show();
        vibe.vibrate(80);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        Toast.makeText(getApplicationContext(), "Medium Coffee" ,Toast.LENGTH_SHORT).show();
        if (networkInfo!= null && networkInfo.isConnected())
            // new DownloadWebpageTask().execute("http://"+ArduinoIP+"/arduino/digital/13/1");
            new DownloadWebpageTask().execute("http://192.168.15.145/arduino/cup/2");
        else
            textView.setText("No Network connection available");
        urlsentText.setText("http://"+ArduinoIP+"/arduino/digital/13/1");
    }
    public void largecup (View view)
    {
        Toast.makeText(getApplicationContext(), "Large Coffee" ,Toast.LENGTH_SHORT).show();
        vibe.vibrate(80);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        Toast.makeText(getApplicationContext(), "Large Coffee" ,Toast.LENGTH_SHORT).show();
        if (networkInfo!= null && networkInfo.isConnected())
            // new DownloadWebpageTask().execute("http://"+ArduinoIP+"/arduino/digital/13/1");
            new DownloadWebpageTask().execute("http://192.168.15.145/arduino/cup/3");
        else
            textView.setText("No Network connection available");
        urlsentText.setText("http://"+ArduinoIP+"/arduino/digital/13/1");
    }


    public void customcup (View view)
    {
        customcup=(EditText)findViewById(R.id.customcup) ;
        vibe.vibrate(80);
        int val = Integer.parseInt( customcup.getText().toString() );
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        Toast.makeText(getApplicationContext(), "Custom Coffee"+val, Toast.LENGTH_SHORT).show();
        if (networkInfo!= null && networkInfo.isConnected())
            // new DownloadWebpageTask().execute("http://"+ArduinoIP+"/arduino/digital/13/1");
            new DownloadWebpageTask().execute("http://192.168.15.145/arduino/cup/"+val+"");
        else
            textView.setText("No Network connection available");
        urlsentText.setText("http://"+ArduinoIP+"/arduino/digital/13/1");
        customcup.setText("");


    }



    public void ledOff (View view){
        vibe.vibrate(80);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo!= null && networkInfo.isConnected()) {
            //new DownloadWebpageTask().execute("http://"+ArduinoIP+"/arduino/digital/13/0");
            new DownloadWebpageTask().execute("http://192.168.15.145/arduino/cup/5");
        }
        else
            textView.setText("No Network connection available");
        urlsentText.setText("http://"+ArduinoIP+"/arduino/digital/13/0");
        Intent i = new Intent(MainActivity.this, activity_switchon.class);
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
            textView.setText(result);

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
            Log.d(DEBUG_TAG, "The response is:" + response);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void smallcoffee (View view){
        Toast.makeText(getApplicationContext(), "Small Coffee" ,Toast.LENGTH_SHORT).show();
    }
    public void mediumcoffee (View view){
        Toast.makeText(getApplicationContext(), "Medium Coffee" ,Toast.LENGTH_SHORT).show();
    }
    public void largecoffee (View view){
        Toast.makeText(getApplicationContext(), "Large Coffee" ,Toast.LENGTH_SHORT).show();
    }
}
