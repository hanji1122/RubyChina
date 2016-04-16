package com.luckyhan.rubychina.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.luckyhan.rubychina.model.User;

import java.util.List;

public class FollowingArrayResponse implements Parcelable {
    public List<User> following;

    @Override
    public String toString() {
        return "FollowingArrayResponse{" +
                "following=" + following +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(following);
    }

    public FollowingArrayResponse() {
    }

    protected FollowingArrayResponse(Parcel in) {
        this.following = in.createTypedArrayList(User.CREATOR);
    }

    public static final Parcelable.Creator<FollowingArrayResponse> CREATOR = new Parcelable.Creator<FollowingArrayResponse>() {
        public FollowingArrayResponse createFromParcel(Parcel source) {
            return new FollowingArrayResponse(source);
        }

        public FollowingArrayResponse[] newArray(int size) {
            return new FollowingArrayResponse[size];
        }
    };
}
