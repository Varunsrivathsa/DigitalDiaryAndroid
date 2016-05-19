package com.example.mobile.diary;


import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class saveMyData implements Parcelable {

    String titleMsg;
    String date;
    String UserId;
    Bitmap bitmap;
    Uri imgUri;
    String data;

    public saveMyData(){}

    public saveMyData(String titleMsg){
        this.titleMsg = titleMsg;
    }

    public saveMyData(String titleMsg, String date){
        this.titleMsg = titleMsg;
        this.date = date;
    }


    protected saveMyData(Parcel in) {
        titleMsg = in.readString();
        date = in.readString();
        UserId = in.readString();
        bitmap = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        imgUri = (Uri) in.readValue(Uri.class.getClassLoader());
        data = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titleMsg);
        dest.writeString(date);
        dest.writeString(UserId);
        dest.writeValue(bitmap);
        dest.writeValue(imgUri);
        dest.writeString(data);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<saveMyData> CREATOR = new Parcelable.Creator<saveMyData>() {
        @Override
        public saveMyData createFromParcel(Parcel in) {
            return new saveMyData(in);
        }

        @Override
        public saveMyData[] newArray(int size) {
            return new saveMyData[size];
        }
    };
}