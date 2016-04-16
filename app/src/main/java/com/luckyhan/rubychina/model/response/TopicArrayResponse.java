package com.luckyhan.rubychina.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.luckyhan.rubychina.model.Topic;

import java.util.List;

public class TopicArrayResponse implements Parcelable {

    public List<Topic> topics;

    @Override
    public String toString() {
        return "TopicArrayResponse{" +
                "topics=" + topics +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(topics);
    }

    public TopicArrayResponse() {
    }

    protected TopicArrayResponse(Parcel in) {
        this.topics = in.createTypedArrayList(Topic.CREATOR);
    }

    public static final Parcelable.Creator<TopicArrayResponse> CREATOR = new Parcelable.Creator<TopicArrayResponse>() {
        public TopicArrayResponse createFromParcel(Parcel source) {
            return new TopicArrayResponse(source);
        }

        public TopicArrayResponse[] newArray(int size) {
            return new TopicArrayResponse[size];
        }
    };
}
