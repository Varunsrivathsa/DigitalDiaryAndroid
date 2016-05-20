package com.example.mobile.diary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class display_data extends AppCompatActivity {

    TextView textView;
    jsonObj dobj = new jsonObj();
    String titleOfTheDay;
    MediaPlayer mediaPlayer;

    public class ImageAdapter extends BaseAdapter {

        private Context mContext;
        ArrayList<String> itemList = new ArrayList<String>();


        public ArrayList<Integer> mThumbIds = new ArrayList<Integer>();


        private Animator mCurrentAnimator;

        private int mShortAnimationDuration;


        LayoutInflater inflater;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        void add(String path){
            itemList.add(path);
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(220, 220));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position), 220, 220);

            imageView.setImageBitmap(bm);

            imageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    int id = (Integer) arg0.getTag();
                   // zoomImageFromThumb(arg0, id);
                }
            });

            return imageView;
        }

        public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {

            Bitmap bm = null;
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(path, options);

            return bm;
        }

        public int calculateInSampleSize(

                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                if (width > height) {
                    inSampleSize = Math.round((float)height / (float)reqHeight);
                } else {
                    inSampleSize = Math.round((float)width / (float)reqWidth);
                }
            }

            return inSampleSize;
        }



    }

    ImageAdapter myImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);
        textView = (TextView) findViewById(R.id.textViewOftheDay);
        //headView = (TextView) findViewById(R.id.titleHeader);

        Intent intent = getIntent();
        dobj = intent.getParcelableExtra("diaryMap");
        titleOfTheDay = intent.getStringExtra("titl");

        Log.i("joson", dobj.toString());
        Log.i("TitleBaGuru",titleOfTheDay);

        String text = dobj.text;
        String dateoftheday = dobj.date;
        String titleoftheday = dobj.titleOftheday;

        TextView title = (TextView) findViewById(R.id.title);
        TextView date = (TextView) findViewById(R.id.date);

        title.setText(titleoftheday);
        date.setText(dateoftheday);

        textView.setText(text);
        textView.setTextSize(18);
        //headView.setText(dobj.titleOftheday);



//        GridView gridview = (GridView) findViewById(R.id.gridview);
//        myImageAdapter = new ImageAdapter(this);
//        gridview.setAdapter(myImageAdapter);
//        gridview.setNumColumns(4);

        ImageView historyImg = (ImageView) findViewById(R.id.History_image);

        String ExternalStorageDirectoryPath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();

        String targetPath = ExternalStorageDirectoryPath + "/DigitalDiary/";

        StringBuilder targetFile = new StringBuilder();
        targetFile.append(targetPath);
        targetFile.append(titleOfTheDay);
        targetFile.append(".jpg");

        File targetDirector = new File(targetPath.toString());

        File[] files = targetDirector.listFiles();
        for (File file : files){
            Log.d("Files", file.toString());
            if (file.toString().equals(targetFile.toString())){
                File imageFile = new File(targetFile.toString());
                Log.i("Nodkolappa123",imageFile.getAbsolutePath().toString());
                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                historyImg.setImageBitmap(bitmap);
            }
        }

        StringBuilder targetSong = new StringBuilder();
        targetSong.append(targetPath);
        targetSong.append(titleOfTheDay);
        targetSong.append(".mp3");

        TextView songText = (TextView) findViewById(R.id.songHistory);
        Uri songUri = Uri.fromFile(new File(targetSong.toString()));
        mediaPlayer = MediaPlayer.create(getApplicationContext(), songUri);
        mediaPlayer.start();
        mediaPlayer.pause();

        songText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    Log.i("Play", "AAgtide");
                    mediaPlayer.pause();
                } else {
                    Log.i("PlayNo", "AAgtilla");
                    mediaPlayer.start();
                }
            }
        });


    }


}
