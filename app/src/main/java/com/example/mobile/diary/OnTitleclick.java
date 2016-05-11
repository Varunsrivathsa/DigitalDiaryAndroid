package com.example.mobile.diary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class OnTitleclick extends Activity {

    TextView textView;
    jsonObj dobj = new jsonObj();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_textoftheday);

        textView = (TextView) findViewById(R.id.textViewOftheDay);
        //headView = (TextView) findViewById(R.id.titleHeader);

        Intent intent = getIntent();
        dobj = intent.getParcelableExtra("diaryMap");

        Log.i("joson", dobj.toString());

        String text = dobj.text;

        textView.setText(text);
        //headView.setText(dobj.titleOftheday);

    }

}
