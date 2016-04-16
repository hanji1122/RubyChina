package com.luckyhan.rubychina.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.luckyhan.rubychina.model.User;

import java.util.List;

public class FollowersArrayResponse implements Parcelable {

    public List<User> followers;

    @Override
    public String toString() {
        return "FollowersArrayResponse{" +
                "followers=" + followers +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(followers);
    }

    public FollowersArrayResponse() {
    }

    protected FollowersArrayResponse(Parcel in) {
        this.followers = in.createTypedArrayList(User.CREATOR);
    }

    public static final Parcelable.Creator<FollowersArrayResponse> CREATOR = new Parcelable.Creator<FollowersArrayResponse>() {
        public FollowersArrayResponse createFromParcel(Parcel source) {
            return new FollowersArrayResponse(source);
        }

        public FollowersArrayResponse[] newArray(int size) {
            return new FollowersArrayResponse[size];
        }
    };
}
