package com.luckyhan.rubychina.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Node implements Parcelable {

    public String id;
    public String name;
    public String topics_count;
    public String summary;
    public String section_id;
    public String sort;
    public String section_name;
    public String updated_at;

    @Override
    public String toString() {
        return "Node{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", topics_count='" + topics_count + '\'' +
                ", summary='" + summary + '\'' +
                ", section_id='" + section_id + '\'' +
                ", sort='" + sort + '\'' +
                ", section_name='" + section_name + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.topics_count);
        dest.writeString(this.summary);
        dest.writeString(this.section_id);
        dest.writeString(this.sort);
        dest.writeString(this.section_name);
        dest.writeString(this.updated_at);
    }

    public Node() {
    }

    protected Node(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.topics_count = in.readString();
        this.summary = in.readString();
        this.section_id = in.readString();
        this.sort = in.readString();
        this.section_name = in.readString();
        this.updated_at = in.readString();
    }

    public static final Parcelable.Creator<Node> CREATOR = new Parcelable.Creator<Node>() {
        public Node createFromParcel(Parcel source) {
            return new Node(source);
        }

        public Node[] newArray(int size) {
            return new Node[size];
        }
    };
}
