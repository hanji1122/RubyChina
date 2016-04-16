package com.luckyhan.rubychina.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Ability implements Parcelable {

    public boolean update;
    public boolean destroy;

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isDestroy() {
        return destroy;
    }

    public void setDestroy(boolean destroy) {
        this.destroy = destroy;
    }

    @Override
    public String toString() {
        return "Ability{" +
                "update=" + update +
                ", destroy=" + destroy +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(update ? (byte) 1 : (byte) 0);
        dest.writeByte(destroy ? (byte) 1 : (byte) 0);
    }

    public Ability() {
    }

    protected Ability(Parcel in) {
        this.update = in.readByte() != 0;
        this.destroy = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Ability> CREATOR = new Parcelable.Creator<Ability>() {
        public Ability createFromParcel(Parcel source) {
            return new Ability(source);
        }

        public Ability[] newArray(int size) {
            return new Ability[size];
        }
    };

}
