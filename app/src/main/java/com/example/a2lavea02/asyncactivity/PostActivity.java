package com.example.a2lavea02.asyncactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.io.OutputStream;

import android.app.AlertDialog;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {
    class MyTask extends AsyncTask<String, Void, String> {
        public String doInBackground(String... params) {

            HttpURLConnection conn = null;
            try {
                URL url = new URL("http://www.free-map.org.uk/course/mad/ws/addhit.php" );
                conn = (HttpURLConnection) url.openConnection();

                String songname = params[0];
                String artist = params[1];
                String year = params[2];
                String postData = "song=" + songname +
                        "&artist=" + artist +
                        "&year=" + year;

                conn.setDoOutput(true);
                conn.setFixedLengthStreamingMode(postData.length());

                OutputStream out = null;
                out = conn.getOutputStream();
                Log.d("network", "postData="+ postData);
                out.write(postData.getBytes());
                if (conn.getResponseCode() == 200) {
                    InputStream in = conn.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String all = "", line;
                    while((line = br.readLine()) != null)
                    {
                        all += line;
                    }
                    return all;
                } else {
                    return "HTTP ERROR: " + conn.getResponseCode();
                }
            } catch (IOException e) {
                return e.toString();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }

        public void onPostExecute(String result) {
            new AlertDialog.Builder(PostActivity.this).setMessage("server sent back: " + result).setPositiveButton("OK", null).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_activity);
        Button Download = (Button) findViewById(R.id.addhitbtn);
        Download.setOnClickListener(this);
    }

    public void onClick(View v) {
        String artist = null;
        String songname = null;
        String year = null;

        EditText et = (EditText) findViewById(R.id.et2);
        EditText et2 = (EditText) findViewById(R.id.et3);
        EditText et3 = (EditText) findViewById(R.id.et4);
        artist = et.getText().toString();
        songname = et2.getText().toString();
        year = et3.getText().toString();
        PostActivity.MyTask t = new PostActivity.MyTask();
        t.execute(artist, songname, year);

    }
}
