package com.assignment.xiaoduo.week6lab;

import android.net.Uri;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by xiaoduo on 4/19/15.
 */
public class RequestHelper {

    public static JSONObject get(String URL)
    {
        JSONObject jsonobject = new JSONObject();
//        URL = URL.replaceAll(" ", "%20");
        Log.i("RequestHelper", URL);
        DefaultHttpClient httpClient;
        StringBuilder result = new StringBuilder();
        HttpParams paramsw = createHttpParams();
        httpClient = new DefaultHttpClient(paramsw);
        HttpGet get = new HttpGet(URL);
        get.setHeader("Accept", "application/json");
        get.setHeader("Content-type", "application/json");
        try {
            HttpResponse httpResponse = httpClient.execute(get);
            int httpCode = httpResponse.getStatusLine().getStatusCode();
            if (httpCode == HttpURLConnection.HTTP_OK && httpResponse != null) {
                Header[] headers = httpResponse.getAllHeaders();
                HttpEntity entity = httpResponse.getEntity();
                Header header = httpResponse.getFirstHeader("content-type");
                InputStream inputStream = entity.getContent();
                InputStreamReader inputStreamReader = new InputStreamReader(
                        inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String s;
                while (((s = reader.readLine()) != null)) {
                    result.append(s);
                }
                reader.close();
                jsonobject =  new JSONObject(result.toString());
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("",e.getMessage());


        } finally {
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
                httpClient = null;
            }
        }
        return jsonobject;
    }

    public static final HttpParams createHttpParams() {
        int TIMEOUT = 60;
        final HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setStaleCheckingEnabled(params, false);
        HttpConnectionParams.setConnectionTimeout(params, TIMEOUT * 50);
        HttpConnectionParams.setSoTimeout(params, TIMEOUT * 1000);
        HttpConnectionParams.setSocketBufferSize(params, 8192 * 5);
        return params;
    }

}
