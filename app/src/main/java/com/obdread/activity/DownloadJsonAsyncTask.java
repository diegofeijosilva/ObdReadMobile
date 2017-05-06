package com.obdread.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.obdread.ed.Usuario;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Feijó on 05/05/2017.
 */

public class DownloadJsonAsyncTask extends AsyncTask<String, Void, List<Usuario>> {

    ProgressDialog dialog;

    private Context mContext;
    private String urlStr = "";

    @Override
    protected List<Usuario> doInBackground(String... params) {
        String urlString = params[0];

        try {
            //constants
            URL url = new URL("http://192.168.14.206:8080/ObdReadWeb/rest/ObdService");
            String message = new JSONObject().toString();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout( 15000 /*milliseconds*/ );
            conn.setConnectTimeout( 15000 /* milliseconds */ );
            conn.setRequestMethod("POST"); // or POST
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Don't use a cached copy.
            conn.setUseCaches(false);
           // conn.setFixedLengthStreamingMode(message.getBytes().length);

            conn.setRequestProperty("Connection", "Keep-Alive");
           // conn.setRequestProperty("http.keepAlive", "false");

            //make some HTTP header nicety
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            //  conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
         //   conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            //open
            conn.connect();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                InputStream in = conn.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                Log.i("JSON",sb.toString());

            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
           return new ArrayList<Usuario>();
        }

    }
}
