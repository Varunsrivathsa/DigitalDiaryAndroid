package com.example.mobile.diary;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

public class RequestTask extends AsyncTask<String, String, String> {

    public JSONObject jsono;
    public JSONObject json;
    public String data="";

    private onDownload asyncTaskListener;

    public RequestTask(onDownload asyncTaskListener){
        this.asyncTaskListener = asyncTaskListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... urls) {
        try {

            HttpGet httppost = new HttpGet(urls[0]);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httppost);

            // StatusLine stat = response.getStatusLine();
            int status = response.getStatusLine().getStatusCode();

            if (status == 200) {
                HttpEntity entity = response.getEntity();
                data = EntityUtils.toString(entity);

                Log.i("TAG","JSON Data           :"+data);

                return data;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }



    public void onPostExecute(String data) {
        asyncTaskListener.onDownload(data);
    }


}
