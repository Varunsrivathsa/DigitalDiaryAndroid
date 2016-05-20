package com.example.mobile.diary;

<<<<<<< HEAD

=======
>>>>>>> 618a3807de7e0d2f7176044dcc95e8e79cdf4ff7
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class saveMyData implements Parcelable {

    String titleMsg;
    String date;
<<<<<<< HEAD
    String UserId;
=======
>>>>>>> 618a3807de7e0d2f7176044dcc95e8e79cdf4ff7
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
<<<<<<< HEAD
        UserId = in.readString();
        bitmap = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        imgUri = (Uri) in.readValue(Uri.class.getClassLoader());
        data = in.readString();
=======
>>>>>>> 618a3807de7e0d2f7176044dcc95e8e79cdf4ff7
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titleMsg);
        dest.writeString(date);
<<<<<<< HEAD
        dest.writeString(UserId);
        dest.writeValue(bitmap);
        dest.writeValue(imgUri);
        dest.writeString(data);
=======
>>>>>>> 618a3807de7e0d2f7176044dcc95e8e79cdf4ff7
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
<<<<<<< HEAD
}
=======
}
>>>>>>> 618a3807de7e0d2f7176044dcc95e8e79cdf4ff7
