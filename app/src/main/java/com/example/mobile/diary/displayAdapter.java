package com.example.mobile.diary;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;

=======
>>>>>>> 618a3807de7e0d2f7176044dcc95e8e79cdf4ff7

/**
 * Created by Varun on 5/4/16.
 */
public class displayAdapter extends ArrayAdapter<display> {
    Context context;
    int layoutResourceId;
<<<<<<< HEAD
    ArrayList<display> data = null;

    public displayAdapter(Context context, int layoutResourceId, ArrayList<display> data) {
=======
    display data[] = null;

    public displayAdapter(Context context, int layoutResourceId, display[] data) {
>>>>>>> 618a3807de7e0d2f7176044dcc95e8e79cdf4ff7
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        displayHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new displayHolder();
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);

            row.setTag(holder);
        }
        else
        {
            holder = (displayHolder)row.getTag();
        }

<<<<<<< HEAD
        display disp = data.get(position);
=======
        display disp = data[position];
>>>>>>> 618a3807de7e0d2f7176044dcc95e8e79cdf4ff7
        holder.txtTitle.setText(disp.title);

        return row;
    }

    static class displayHolder
    {
        TextView txtTitle;
    }
}