package com.example.mobile.diary;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity implements onDownload {
    private ListView listView1;
    public HashMap<String, jsonObj> jMap = new HashMap<>();
    ArrayList<String> order = new ArrayList<>();
    JSONObject jData = null;
    String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_content);

        Intent intent = getIntent();
        userId = intent.getStringExtra(MainActivity.USER_ID);
        StringBuilder UrlGetJson = new StringBuilder();
        UrlGetJson.append("https://digitaldiary-76dfb.appspot.com/retrieveData/");
        UrlGetJson.append(userId);

        RequestTask request = new RequestTask(this);
        request.execute(UrlGetJson.toString());

    }


    @Override
    public void onDownload(String values) {
        try {
            jData = new JSONObject(values);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int len = 0;
        JSONArray arra = null;


        try {
            arra = (JSONArray) jData.get("diaries");
            len = arra.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        display disp_data[] = new display[len];

        for (int i = 0; i < len; i++) {
            jsonObj jsonObj = new jsonObj();
            String titleMsg = "";
            try {
                JSONObject json = (JSONObject) arra.get(i);
                titleMsg = json.get("title").toString();

                order.add(titleMsg);


                jsonObj.titleOftheday = json.get("title").toString();
                jsonObj.date = json.get("date").toString();
                jsonObj.text = json.get("text").toString();

                jMap.put(titleMsg, jsonObj);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            disp_data[i] = new display(titleMsg);
        }

        displayAdapter adapter = new displayAdapter(this, R.layout.history_text_list, disp_data);

        listView1 = (ListView) findViewById(R.id.history);
        View header = (View) getLayoutInflater().inflate(R.layout.history_header, null);
        listView1.addHeaderView(header);
        listView1.setAdapter(adapter);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HistoryActivity.this, OnTitleclick.class);
                intent.putParcelableArrayListExtra("diaryMap",jMap.get(order.get(position-1)));
                startActivity(intent);
            }
        });


    }

}
