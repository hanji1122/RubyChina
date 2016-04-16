package com.luckyhan.rubychina.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class HtmlData implements Parcelable {

    public String html;
    public List<Image> imgList;

    public void addImage(Image img) {
        if (imgList == null) {
            imgList = new ArrayList<>();
        }
        imgList.add(img);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.html);
        dest.writeTypedList(imgList);
    }

    public HtmlData() {
    }

    protected HtmlData(Parcel in) {
        this.html = in.readString();
        this.imgList = in.createTypedArrayList(Image.CREATOR);
    }

    public static final Parcelable.Creator<HtmlData> CREATOR = new Parcelable.Creator<HtmlData>() {
        public HtmlData createFromParcel(Parcel source) {
            return new HtmlData(source);
        }

        public HtmlData[] newArray(int size) {
            return new HtmlData[size];
        }
    };

}
