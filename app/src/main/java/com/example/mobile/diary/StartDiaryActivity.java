package com.example.mobile.diary;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class StartDiaryActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "Sending Data to Server";

    static MediaPlayer mp;
    private static int RESULT_LOAD_IMG = 1;
    ArrayList<File> mySongs;
    String title_message,date_message;
    String userId,songName,picName;
    EditText editText;
    File songPath;
    TextView Tv,title,date;
    int position;
    Uri u;
    Thread updateSeekBar;
    Button btPlayDairy;
    private static final int PICK_FROM_CAM = 1;
    private static final int PICK_FROM_FILE = 2;
    private static final int REQUEST_CODE_CREATOR = 2;
    private static final int REQUEST_CODE_RESOLUTION = 3;
    private static final int PICK_FROM_GALLERY = 4;
    private static final int REQUEST_CAMERA = 112;
    private String imgFileLoc = "";
    private ImageView picCap;
    private Uri imageUri;
    private GoogleApiClient mGoogleApiClient;
    private Bitmap mBitmapToSave;
    saveMyData smd = new saveMyData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_diary);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        title = (TextView) findViewById(R.id.title);
        date = (TextView) findViewById(R.id.date);
        picCap = (ImageView) findViewById(R.id.capturePic);
        editText = (EditText) findViewById(R.id.diary);

        Intent intent = getIntent();

        if(intent.getStringExtra(MainActivity.TITLE) != null) {
            title_message = intent.getStringExtra(MainActivity.TITLE);
            title.setText(title_message);
            title.setTextSize(25);

            smd.titleMsg = title_message;


            date_message = intent.getStringExtra(MainActivity.DATE);

            date.setText(date_message);
            date.setTextSize(15);
            smd.date = date_message;

            userId = intent.getStringExtra(MainActivity.USER_ID);
            smd.UserId = userId;
        }
        else {
            if(intent.getExtras() != null) {

                Bundle b = intent.getExtras();
                mySongs = (ArrayList) b.getParcelableArrayList("songlist");
                position = b.getInt("pos", 0);
                smd = b.getParcelable("takeBack");

                if(smd.titleMsg != null)
                    Log.i("titnew", smd.titleMsg);
                else
                    Log.i("titNew", "dabba");

                title_message = smd.titleMsg;
                title.setText(smd.titleMsg);
                title.setTextSize(25);

                date_message = smd.date;
                date.setText(smd.date);
                date.setTextSize(15);

                userId = smd.UserId;

                editText.setText(smd.data);

                String ExternalStorageDirectoryPath = Environment
                        .getExternalStorageDirectory()
                        .getAbsolutePath();

                String targetPath = ExternalStorageDirectoryPath + "/DigitalDiary/";

                StringBuilder targetFile = new StringBuilder();
                targetFile.append(targetPath);
                //targetFile.append(File.separator);
                targetFile.append(smd.titleMsg);
                targetFile.append(".jpg");

                //Log.i("Nodkolappa",targetFile.toString());

                        //Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_LONG).show();
                File targetDirector = new File(targetPath.toString());

                File[] files = targetDirector.listFiles();
                for (File file : files){
                    Log.d("Files", file.toString());
                    if (file.toString().equals(targetFile.toString())){
                        File imageFile = new File(targetFile.toString());
                        //Log.i("Nodkolappa123",imageFile.getAbsolutePath().toString());
                        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                        picCap.setImageBitmap(bitmap);
                    }
                }

                u = Uri.parse(mySongs.get(position).toString());
                songPath = new File(u.getPath());
                songName = songPath.getAbsolutePath();


                try {
                    addSongtoDirectory(songName);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                mp = MediaPlayer.create(getApplicationContext(), u);
                mp.start();
                mp.pause();

            }
        }

        ImageView fab = (ImageView) findViewById(R.id.fab);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                sendJson();
                Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_LONG).show();
            }
        });

        ImageView attachfile = (ImageView) findViewById(R.id.attachfile);
        attachfile.setOnClickListener(new OnClickListener() {
            int button01pos = 0;
            TableLayout attach_fileTable;

            @Override
            public void onClick(View v) {
                if (button01pos == 0) {
                    attach_fileTable = (TableLayout) findViewById(R.id.attach_file);
                    attach_fileTable.setVisibility(View.VISIBLE);
                    button01pos = 1;
                } else if (button01pos == 1) {
                    attach_fileTable.setVisibility(View.INVISIBLE);
                    button01pos = 0;
                }
            }

        });
        Tv = (TextView) findViewById(R.id.songName);
        Tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    Log.i("Play", "AAgtide");
                    mp.pause();
                } else {
                    Log.i("PlayNo", "AAgtilla");
                    mp.start();
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "API client connected.");
//        new createFolder().execute();
        if(mBitmapToSave != null) {
            //Toast.makeText(this,"Save to drive called",Toast.LENGTH_LONG).show();
            saveFileToDrive();
        }
        else
            Log.i("Just a statement","yes");
            //Toast.makeText(this,"Bit map is null",Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this,"onresume called",Toast.LENGTH_LONG).show();
        if (mGoogleApiClient == null) {
            // Create the API client and bind it to an instance variable.
            // We use this instance as the callback for connection and connection
            // failures.
            // Since no account name is passed, the user is prompted to choose.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        // Connect the client. Once connected, the camera is launched.
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Called whenever the API client fails to connect.
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }
        // The failure has a resolution. Resolve it.
        // Called typically when the app is not yet authorized, and an
        // authorization
        // dialog is displayed to the user.
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }

    public void goToCamera(View v){
        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }

        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        startActivityForResult(intent, PICK_FROM_CAM);
    }

    public void goToGallery(View v){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Complete Action Using"), PICK_FROM_GALLERY);
        //saveFileToDrive();
        return;
    }

    public void goToAudio(View v){
        Intent intent = new Intent(StartDiaryActivity.this,Audio_pick.class);
        editText = (EditText) findViewById(R.id.diary);
        String text = editText.getText().toString();
        smd.data = text;
        intent.putExtra("dataStore", smd);
        startActivity(intent);
    }

    public void goToLocation(View v){

    }


    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }

    @Override
    protected void onPause() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }


    public void sendJson(){

        final EditText textForTheDay;
        textForTheDay = (EditText) findViewById(R.id.diary);

        try {
            JSONObject send = new JSONObject();

            JSONObject dairyObj = new JSONObject();

            dairyObj.put("title",title_message);
            dairyObj.put("date", date_message);
            dairyObj.put("text",textForTheDay.getText().toString());
            send.put("user_id",userId);
            //send.put("email_id",emailId);
            send.put("diary",dairyObj);
            Log.i("json",send.toString());

            //ServerTask SendDiaryDetailsToServerTask = new ServerTask();
            //String statusCode = SendDiaryDetailsToServerTask.doInBackground(send.toString(),"https://digitaldiary-4c8d6.appspot.com/saveData");
            new SendDiaryDetailsToServerTask().execute(send.toString());




        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = null;
        String path = "";

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_FROM_CAM) {
                boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                if (!hasPermission) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CAMERA);
                }

                String RootDir = Environment.getExternalStorageDirectory() + File.separator + "DigitalDiary"+File.separator;
                ///File RootFile = new File(RootDir);

                //RootFile.mkdir();
                StringBuilder sb = new StringBuilder();
                sb.append(RootDir);
                sb.append(title_message);
                sb.append(".jpg");
                Log.i("Path of imag : ", sb.toString());
                File imageFile = new File(sb.toString());
                mBitmapToSave = BitmapFactory.decodeFile(imageFile.getAbsolutePath());


                int nh = (int) ( mBitmapToSave.getHeight() * (1024.0 / mBitmapToSave.getWidth()) );
                Bitmap scaled = Bitmap.createScaledBitmap(mBitmapToSave, 1024, nh, true);
                picCap.setImageBitmap(scaled);



                //picCap.setImageBitmap(mBitmapToSave);

                //File imgFile = new File(sb.toString());

                /*if(imgFile.exists()){

                    mBitmapToSave = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    //ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);

                    picCap.setImageBitmap(mBitmapToSave);

                }*/
                //Toast.makeText(this, "Picture captured Successfully", Toast.LENGTH_LONG).show();

            }
            else if(requestCode == REQUEST_CODE_CREATOR){
                // Called after a file is saved to Drive.
                //if (resultCode == RESULT_OK) {
                Log.i(TAG, "Image successfully saved.");
                mBitmapToSave = null;
                // Just start the camera again for another photo.
                //startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),REQUEST_CODE_CAPTURE_IMAGE);
                //}
            }
            else if(requestCode == PICK_FROM_GALLERY){
                Bitmap bitmap1 = null;
                imageUri = data.getData();
                path = getPath(imageUri);
                picCap.setImageURI(imageUri);
                try {
                    mBitmapToSave = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    createImageFileGallery(mBitmapToSave);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (path == null) {
                    path = imageUri.getPath();
                    if (path != null)
                        Log.i("path of file..:", path);
                }

                /*imageUri = data.getData();
                try {
                    mBitmapToSave = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                picCap.setImageURI(imageUri);*/
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    File createImageFile() throws IOException {

        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CAMERA);
        }

        String RootDir = Environment.getExternalStorageDirectory() + File.separator + "DigitalDiary";
        File RootFile = new File(RootDir);

        RootFile.mkdir();
        StringBuilder sb = new StringBuilder();
        sb.append(title_message);
        sb.append(".jpg");
        File image = new File(RootFile, sb.toString());
        imgFileLoc = image.getAbsolutePath();

        return image;
    }


    File createImageFileGallery(Bitmap imageData) throws IOException {

        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CAMERA);
        }

        OutputStream out;
        String RootDir = Environment.getExternalStorageDirectory() + File.separator + "DigitalDiary";
        File RootFile = new File(RootDir);

        StringBuilder sb = new StringBuilder();
        sb.append(title_message);
        sb.append(".jpg");

        RootFile.mkdir();
        File image = new File(RootFile, sb.toString());
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imageData.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

        FileOutputStream fo = new FileOutputStream(image);
        fo.write(bytes.toByteArray());
        fo.close();

        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA: {
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

    private void saveFileToDrive() {
        // Start by creating a new contents, and setting a callback.
        Log.i(TAG, "Creating new contents.");
        final Bitmap image = mBitmapToSave;
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {

                    @Override
                    public void onResult(DriveApi.DriveContentsResult result) {
                        // If the operation was not successful, we cannot do anything
                        // and must
                        // fail.
                        if (!result.getStatus().isSuccess()) {
                            Log.i(TAG, "Failed to create new contents.");
                            return;
                        }
                        // Otherwise, we can write our data to the new contents.
                        Log.i(TAG, "New contents created.");
                        // Get an output stream for the contents.
                        OutputStream outputStream = result.getDriveContents().getOutputStream();
                        // Write the bitmap data from it.
                        ByteArrayOutputStream bitmapStream = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.PNG, 100, bitmapStream);
                        try {
                            outputStream.write(bitmapStream.toByteArray());
                        } catch (IOException e1) {
                            Log.i(TAG, "Unable to write file contents.");
                        }
                        // Create the initial metadata - MIME type and title.
                        // Note that the user will be able to change the title later.
                        String s = title_message + ".png";
                        MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
                                .setMimeType("image/jpeg").setTitle(s).build();
                        // Create an intent for the file chooser, and start it.
                        IntentSender intentSender = Drive.DriveApi
                                .newCreateFileActivityBuilder()
                                .setInitialMetadata(metadataChangeSet)
                                .setInitialDriveContents(result.getDriveContents())
                                .build(mGoogleApiClient);
                        try {
                            startIntentSenderForResult(intentSender, REQUEST_CODE_CREATOR, null, 0, 0, 0);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "Failed to launch file chooser.");
                        }
                    }
                });
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    final ResultCallback<DriveFolder.DriveFolderResult> callback = new ResultCallback<DriveFolder.DriveFolderResult>() {
        @Override
        public void onResult(DriveFolder.DriveFolderResult result) {
            if (!result.getStatus().isSuccess()) {
                showMessage("Error while trying to create the folder");
                return;
            }
            //showMessage("Created a folder: " + result.getDriveFolder().getDriveId());
        }
    };


    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void addSongtoDirectory (String sourcePath) throws FileNotFoundException {

        String RootDir = Environment.getExternalStorageDirectory() + File.separator + "DigitalDiary";
        File RootFile = new File(RootDir);

        RootFile.mkdir();

        File source = new File(sourcePath);

        String destinationPath = RootDir;

        StringBuilder songStore = new StringBuilder();
        songStore.append(title_message);
        songStore.append(".mp3");

        File destination = new File(destinationPath,songStore.toString());
        try
        {
            FileUtils.copyFile(source, destination);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }



    private class SendDiaryDetailsToServerTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            Log.d("Start Diary Activity", "Sending Data to Server");

            HttpClient httpClient = new DefaultHttpClient();
            //HttpPost httpPost = new HttpPost("https://digitaldiary-4c8d6.appspot.com/saveData");

            try {

                HttpPost request = new HttpPost("https://digitaldiary-76dfb.appspot.com/saveData");
                StringEntity parameter =new StringEntity(params[0]);
                request.addHeader("content-type", "application/json");
                request.setEntity(parameter);
                HttpResponse response = httpClient.execute(request);


               // List nameValuePairs = new ArrayList(1);
                //nameValuePairs.add(new BasicNameValuePair("diary details", params[0]));
               // httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

               // HttpResponse response = httpClient.execute(httpPost);

                int statusCode = response.getStatusLine().getStatusCode();

                //final String responseBody = EntityUtils.toString(response.getEntity());
                Log.i(TAG, "Status Code:" + statusCode);


                if (statusCode == 200) {
                    Log.d("Diary Data Send", "Server Working");
                } else {
                    Log.d("Not Able To Send Data", "Server Not Working");
                }

            } catch (ClientProtocolException e) {
                Log.e(TAG, "Error sending ID token to backend.", e);
            } catch (IOException e) {
                Log.e(TAG, "Error sending ID token to backend.", e);
            }


            return null;
        }


    }


    }
