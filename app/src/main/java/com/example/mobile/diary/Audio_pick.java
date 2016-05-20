package com.example.mobile.diary;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mobile.diary.Player;

import java.io.File;
import java.util.ArrayList;

public class Audio_pick extends AppCompatActivity {

    ListView lv;
    String[] items;
    ArrayList<File> mySongs = new ArrayList<>();
    private static final int REQUEST_READ_STORAGE = 112;

    saveMyData saveMyData = new saveMyData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audi_content);

        Intent intent = getIntent();
        saveMyData = intent.getParcelableExtra("dataStore");

<<<<<<< HEAD
        String baddimaga= saveMyData.UserId;


        Log.i("AudioShat", baddimaga);
=======
        Bitmap baddimaga= saveMyData.bitmap;

        Log.i("AudioShat", String.valueOf(baddimaga));
>>>>>>> 618a3807de7e0d2f7176044dcc95e8e79cdf4ff7

        lv = (ListView) findViewById(R.id.lvPlaylist);

        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_STORAGE);
        }

        mySongs = findSongs(new File(Environment.getExternalStorageDirectory().toString()));


        items = new String[mySongs.size()];

        for (int i=0; i<mySongs.size(); i++){
            items[i] = mySongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");
        }

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(),R.layout.song_layout,R.id.textView,items);
        lv.setAdapter(adp);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
<<<<<<< HEAD
=======
                //.setClassName(getApplicationContext(),"com.example.mobile.diary.StartDiaryActivity")
>>>>>>> 618a3807de7e0d2f7176044dcc95e8e79cdf4ff7
                startActivity(new Intent(getApplicationContext(), StartDiaryActivity.class).putExtra("pos", position).putExtra("songlist", mySongs).putExtra("takeBack",saveMyData));
            }
        });

    }

    public ArrayList<File> findSongs(File root){
        ArrayList<File> al = new ArrayList<File>();
        File[] files = root.listFiles();

        if(files == null) return al;

        for (File singleFile : files){
            if(singleFile.isDirectory() && !singleFile.isHidden()){
                al.addAll(findSongs(singleFile));
            }
            else{
                if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")){
                    al.add(singleFile);
                }
            }
        }
        return al;
    }

//    public void toast(String text){
//        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
<<<<<<< HEAD
        //if (id == R.id.action_settings) {
          //  return true;
        //}
=======
        if (id == R.id.action_settings) {
            return true;
        }
>>>>>>> 618a3807de7e0d2f7176044dcc95e8e79cdf4ff7

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}

