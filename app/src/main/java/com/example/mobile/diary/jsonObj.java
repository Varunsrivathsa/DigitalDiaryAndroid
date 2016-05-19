package com.example.mobile.diary;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Struct;
import java.util.ArrayList;

/**
 * Created by Varun on 5/5/16.
 */
public class jsonObj extends ArrayList<Parcelable> implements Parcelable {
    public String titleOftheday = "";
    public String date = "";
    public String text = "";

    public jsonObj(){}



    public jsonObj(String titleOftheday, String date, String text){
        this.titleOftheday = titleOftheday;
        this.date = date;
        this.text = text;
    }

    public String toString(){
        return titleOftheday;
    }
    protected jsonObj(Parcel in) {
        titleOftheday = in.readString();
        date = in.readString();
        text = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titleOftheday);
        dest.writeString(date);
        dest.writeString(text);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<jsonObj> CREATOR = new Parcelable.Creator<jsonObj>() {
        @Override
        public jsonObj createFromParcel(Parcel in) {
            return new jsonObj(in);
        }

        @Override
        public jsonObj[] newArray(int size) {
            return new jsonObj[size];
        }
    };
}