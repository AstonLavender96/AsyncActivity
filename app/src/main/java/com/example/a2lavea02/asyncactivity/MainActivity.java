package com.example.a2lavea02.asyncactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.AsyncTask;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    class MyTask extends AsyncTask<String,Void,String>
    {
        public String doInBackground(String... artist)
        {
            HttpURLConnection conn = null;
            try
            {
                URL url = new URL("http://www.free-map.org.uk/course/mad/ws/hits.php?artist=" + URLEncoder.encode(artist[0], "UTF-8"));
                conn = (HttpURLConnection) url.openConnection();
                InputStream in = conn.getInputStream();
                EditText et = (EditText)findViewById(R.id.et1);
                if(conn.getResponseCode() == 200)
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String result = "", line;
                    while((line = br.readLine()) !=null)
                    {
                        result += line + "\n";
                    }
                    return result;
                }
                else
                {
                    return "HTTP ERROR:" + conn.getResponseCode();
                }
            }
            catch(IOException e)
            {
                return e.toString();
            }
            finally
            {
                if(conn!=null)
                {
                    conn.disconnect();
                }
            }
        }
        public void onPostExecute(String result)
        {
            TextView artistlist = (TextView)findViewById(R.id.artistlist);
            artistlist.setText(result);
        }
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()== R.id.postArtist)
        {
            Intent intent = new Intent(this, PostActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button Download = (Button)findViewById(R.id.btn1);
        Download.setOnClickListener(this);
    }
    public void onClick(View v)
    {
        String artist=null;
        EditText et = (EditText)findViewById(R.id.et1);
        artist=et.getText().toString();
        MyTask t = new MyTask();
        t.execute(artist);
    }
}
