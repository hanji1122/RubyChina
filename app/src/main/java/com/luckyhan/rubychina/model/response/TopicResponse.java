package com.luckyhan.rubychina.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.luckyhan.rubychina.model.Meta;
import com.luckyhan.rubychina.model.Topic;

public class TopicResponse implements Parcelable {

    public Topic topic;
    public Meta meta;

    public TopicResponse(Topic topic, Meta meta) {
        this.topic = topic;
        this.meta = meta;
    }

    @Override
    public String toString() {
        return "TopicItem{" +
                "topic=" + topic +
                ", meta=" + meta +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.topic, 0);
        dest.writeParcelable(this.meta, 0);
    }

    public TopicResponse() {
    }

    protected TopicResponse(Parcel in) {
        this.topic = in.readParcelable(Topic.class.getClassLoader());
        this.meta = in.readParcelable(Meta.class.getClassLoader());
    }

    public static final Parcelable.Creator<TopicResponse> CREATOR = new Parcelable.Creator<TopicResponse>() {
        public TopicResponse createFromParcel(Parcel source) {
            return new TopicResponse(source);
        }

        public TopicResponse[] newArray(int size) {
            return new TopicResponse[size];
        }
    };
}
