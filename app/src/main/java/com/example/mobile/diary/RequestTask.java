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
<<<<<<< HEAD
import java.net.URLEncoder;

public class RequestTask extends AsyncTask<String, String, String> {

    //public JSONObject jsono;
    //public JSONObject json;

=======

public class RequestTask extends AsyncTask<String, String, String> {

    public JSONObject jsono;
    public JSONObject json;
>>>>>>> 618a3807de7e0d2f7176044dcc95e8e79cdf4ff7
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
<<<<<<< HEAD
            Log.d("url",urls[0]);
=======
>>>>>>> 618a3807de7e0d2f7176044dcc95e8e79cdf4ff7

            HttpGet httppost = new HttpGet(urls[0]);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httppost);

            // StatusLine stat = response.getStatusLine();
            int status = response.getStatusLine().getStatusCode();

            if (status == 200) {
                HttpEntity entity = response.getEntity();
                data = EntityUtils.toString(entity);

<<<<<<< HEAD
                Log.i("TAG","JSON Data:"+data);
=======
                Log.i("TAG","JSON Data           :"+data);
>>>>>>> 618a3807de7e0d2f7176044dcc95e8e79cdf4ff7

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
