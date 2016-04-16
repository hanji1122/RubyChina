package com.luckyhan.rubychina.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable {

    public String url;
    public String realPath;
    public String description;
    public String mimeType;

    public Image(String url, String description) {
        this.url = url;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Image{" +
                "url='" + url + '\'' +
                ", realPath='" + realPath + '\'' +
                ", description='" + description + '\'' +
                ", mimeType='" + mimeType + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.realPath);
        dest.writeString(this.description);
        dest.writeString(this.mimeType);
    }

    protected Image(Parcel in) {
        this.url = in.readString();
        this.realPath = in.readString();
        this.description = in.readString();
        this.mimeType = in.readString();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

}
