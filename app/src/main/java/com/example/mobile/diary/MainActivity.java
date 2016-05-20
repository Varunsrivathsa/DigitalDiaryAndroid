package com.example.mobile.diary;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final static String TITLE = "Diary Title";
    public final static String DATE = "Diary Date";
    public final static String USER_ID = "User Id";
    private static final String TAG = "Sending Data to Server";
    String selectedDate;

    String personName;
<<<<<<< HEAD
    String message;
=======
>>>>>>> 618a3807de7e0d2f7176044dcc95e8e79cdf4ff7
    String photoUri;
    String userId;
    Uri profileImg;

    TextView name;
    ImageView imgView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
<<<<<<< HEAD
                  b      .setAction("Action", null).show();
=======
                        .setAction("Action", null).show();
>>>>>>> 618a3807de7e0d2f7176044dcc95e8e79cdf4ff7
            }
        });*/



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
<<<<<<< HEAD
        userId = intent.getStringExtra(LoginActivity.USER_ID);
        personName = intent.getStringExtra(LoginActivity.NAME);
        photoUri = intent.getStringExtra(LoginActivity.PHOTO_URL);

=======

        //if(intent.getStringExtra(LoginActivity.USER_ID) != null) {
            userId = intent.getStringExtra(LoginActivity.USER_ID);
            personName = intent.getStringExtra(LoginActivity.NAME);
            photoUri = intent.getStringExtra(LoginActivity.PHOTO_URL);
        //}
//        else if(intent.getBooleanExtra("Done", true)){
//            Intent intent1 = new Intent(MainActivity.this,MainActivity.class);
//            startActivity(intent1);
//        }

        profileImg = Uri.parse(photoUri);
>>>>>>> 618a3807de7e0d2f7176044dcc95e8e79cdf4ff7

        Log.d("USER NAME",personName);

        View header=navigationView.getHeaderView(0);

        name = (TextView)header.findViewById(R.id.textView_name);

        name.setText(personName);

        //imgView=(ImageView)findViewById(R.id.profile_image);
        //Uri imgUri = profileImg;

        //imgView.setImageBitmap(getBitmapFromURL(imgUri));
        //imgView.setImageBitmap();
        //imgView.setImageURI(null);
        // imgView.setImageURI(imgUri);



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_newdiary) {
            getDrawerToggleDelegate(); //close drawer
            //open dialog box and ask for title and date
            showDialog();
        } else if (id == R.id.nav_history) {
            onHistoryClick();
<<<<<<< HEAD
=======
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

>>>>>>> 618a3807de7e0d2f7176044dcc95e8e79cdf4ff7
        } else if (id == R.id.nav_logout) {


        }

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    public void showDialog(){

        final EditText title_message;
        final CalendarView date;
        Button okbutton;


        final Dialog titledatedialog = new Dialog(this);

        titledatedialog.setContentView(R.layout.newdiary);



        TextView title = new TextView(this);
        title.setText("Enter Title and Date");
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);


        titledatedialog.setTitle(title.toString());

        title_message = (EditText) titledatedialog.findViewById(R.id.title);
<<<<<<< HEAD

=======
>>>>>>> 618a3807de7e0d2f7176044dcc95e8e79cdf4ff7
        date = (CalendarView) titledatedialog.findViewById(R.id.date);
        okbutton = (Button) titledatedialog.findViewById(R.id.ok);


        date.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //Toast.makeText(getApplicationContext(), dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
                StringBuilder sb = new StringBuilder();
                sb.append(dayOfMonth).append("/").append(month).append("/").append(year);
                selectedDate = sb.toString();
            }
        });

<<<<<<< HEAD
        titledatedialog.show();

=======
>>>>>>> 618a3807de7e0d2f7176044dcc95e8e79cdf4ff7
        okbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Intent intent = new Intent(getBaseContext(), StartDiaryActivity.class);
                if (title_message.getText().toString().length() == 0)
                    title_message.setError("Title is required!");
                else {
<<<<<<< HEAD
                    message = title_message.getText().toString();
=======
                    String message = title_message.getText().toString();
>>>>>>> 618a3807de7e0d2f7176044dcc95e8e79cdf4ff7
                    intent.putExtra(TITLE, message);
                    intent.putExtra(DATE, selectedDate);
                    intent.putExtra(USER_ID,userId);
                    startActivity(intent);
                }

<<<<<<< HEAD
                titledatedialog.dismiss();

=======
>>>>>>> 618a3807de7e0d2f7176044dcc95e8e79cdf4ff7
                //if( selectedDate.length() == 0 )
                //selectedDate.setError( "Date is required!" );


            }
        });


        //AlertDialog dialog = builder.create();
<<<<<<< HEAD


        return;
=======
        titledatedialog.show();
>>>>>>> 618a3807de7e0d2f7176044dcc95e8e79cdf4ff7
    }

    public void onHistoryClick(){
        Intent iHistory = new Intent(MainActivity.this,HistoryActivity.class);
<<<<<<< HEAD
        iHistory.putExtra(TITLE,message);
=======
>>>>>>> 618a3807de7e0d2f7176044dcc95e8e79cdf4ff7
        iHistory.putExtra(USER_ID,userId);
        startActivity(iHistory);

    }
}
