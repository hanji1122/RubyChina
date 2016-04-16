package com.luckyhan.rubychina.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Post implements Parcelable {

    public int postId;
    public Node node;
    public String title;
    public String content;
    public List<Image> localImageList;
    public List<String> remoteImageList;

    public int getUploadImageCount() {
        return localImageList == null ? 0 : localImageList.size();
    }

    public void addRemoteImage(String remoteUrl) {
        if (remoteImageList == null) {
            remoteImageList = new ArrayList<>();
        }
        remoteImageList.add(remoteUrl);
    }

    @Override
    public String toString() {
        return "Post{" +
                "node=" + node +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", localImageList=" + localImageList +
                ", remoteImageList=" + remoteImageList +
                '}';
    }

    public Post() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.node, 0);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeTypedList(localImageList);
        dest.writeStringList(this.remoteImageList);
    }

    protected Post(Parcel in) {
        this.node = in.readParcelable(Node.class.getClassLoader());
        this.title = in.readString();
        this.content = in.readString();
        this.localImageList = in.createTypedArrayList(Image.CREATOR);
        this.remoteImageList = in.createStringArrayList();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

}
