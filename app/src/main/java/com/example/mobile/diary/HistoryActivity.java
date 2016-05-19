package com.example.mobile.diary;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HistoryActivity extends AppCompatActivity implements onDownload {
    private ListView listView1;
    public HashMap<String, jsonObj> jMap = new HashMap<>();
    ArrayList<String> order = new ArrayList<>();
    JSONObject jData = null;
    String userId,titleMsg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Intent intent = getIntent();
        userId = intent.getStringExtra(MainActivity.USER_ID);
        titleMsg = intent.getStringExtra(MainActivity.TITLE);
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

        Log.d("JSON RECEIVED",jData.toString());
        try {

            int len = 0;
            JSONArray arra = null;
            arra = (JSONArray) jData.get("diaries");
            len = arra.length();

            if (len == 0) {
                TextView txtvw = (TextView) findViewById(R.id.nohistory);
                txtvw.setVisibility(View.VISIBLE);

            } else {



                final ArrayList<String> disp_data = new ArrayList<String>();
                //final display disp_data[] = new display[len];



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


                    Log.d("title", titleMsg);
                    disp_data.add(titleMsg);
                   //disp_data[i] = new display(titleMsg);
                }

                final ArrayAdapter<String> itemsAdapter =
                        new ArrayAdapter<String>(this,R.layout.history_list,R.id.txtTitle, disp_data);

                listView1 = (ListView) findViewById(R.id.history);
                listView1 .setAdapter(itemsAdapter);



                //final displayAdapter adapter = new displayAdapter(this, R.layout.history_content, disp_data);

                //listView1 = (ListView) findViewById(R.id.history);
                // View header = (View) getLayoutInflater().inflate(R.layout.history_header, null);
                //listView1.addHeaderView(header);
                //listView1.setAdapter(adapter);


             listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(HistoryActivity.this, display_data.class);
                        intent.putParcelableArrayListExtra("diaryMap", jMap.get(order.get(position)));
                        intent.putExtra("titl",titleMsg);
                        startActivity(intent);
                    }
                });




               listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                   @Override
                   public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                       //final int positionToRemove = position;
                      // Toast.makeText(getApplicationContext(), "long click", Toast.LENGTH_LONG).show();
                       AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
                       builder.setMessage("Do you want to delete?")
                               .setIcon(R.drawable.ic_delete)
                               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int id) {
                                       deletefromdatabase(disp_data.get(position));
                                       disp_data.remove(position);
                                       itemsAdapter.notifyDataSetChanged();
                                       Toast.makeText(getApplicationContext(), "item deleted", Toast.LENGTH_LONG).show();

                                   }
                               })
                               .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int id) {
                                       Toast.makeText(getApplicationContext(), "item not deleted", Toast.LENGTH_LONG).show();
                                   }
                               });
                       // Create the AlertDialog object and return it
                       AlertDialog dialog = builder.create();
                       dialog.show();
                       return true;
                   }
               });

            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }




    }


   public void deletefromdatabase(String item){

        //Log.d("item to be removed",item);
        StringBuilder Urldelete = new StringBuilder();
        // encodedParam = encodeURIComponent('www.foobar.com/?first=1&second=12&third=5');

       //URL url = new URL("https://digitaldiary-76dfb.appspot.com/deleteData?param1="+userId+"&param2="+item);

        //URL Urldelete = new URL("https://digitaldiary-76dfb.appspot.com/deleteData?param1="+userId+"_"+item);

        RequestTask request = new RequestTask(this);
        //request.execute(url.toString());


    }

}

