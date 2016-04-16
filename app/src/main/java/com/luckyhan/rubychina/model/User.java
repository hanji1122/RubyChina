package com.luckyhan.rubychina.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    public String id;
    public String login;
    public String avatar_url;
    public String name;
    public String location;
    public String company;
    public String twitter;
    public String website;
    public String bio;
    public String tagline;
    public String github;
    public String created_at;
    public String email;
    public String topics_count;
    public String replies_count;
    public String following_count;
    public String followers_count;
    public String favorites_count;
    public String level;
    public String level_name;

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", login='" + login + '\'' +
                ", avatar_url='" + avatar_url + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", company='" + company + '\'' +
                ", twitter='" + twitter + '\'' +
                ", website='" + website + '\'' +
                ", bio='" + bio + '\'' +
                ", tagline='" + tagline + '\'' +
                ", github='" + github + '\'' +
                ", created_at='" + created_at + '\'' +
                ", email='" + email + '\'' +
                ", topics_count='" + topics_count + '\'' +
                ", replies_count='" + replies_count + '\'' +
                ", following_count='" + following_count + '\'' +
                ", followers_count='" + followers_count + '\'' +
                ", favorites_count='" + favorites_count + '\'' +
                ", level='" + level + '\'' +
                ", level_name='" + level_name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.login);
        dest.writeString(this.avatar_url);
        dest.writeString(this.name);
        dest.writeString(this.location);
        dest.writeString(this.company);
        dest.writeString(this.twitter);
        dest.writeString(this.website);
        dest.writeString(this.bio);
        dest.writeString(this.tagline);
        dest.writeString(this.github);
        dest.writeString(this.created_at);
        dest.writeString(this.email);
        dest.writeString(this.topics_count);
        dest.writeString(this.replies_count);
        dest.writeString(this.following_count);
        dest.writeString(this.followers_count);
        dest.writeString(this.favorites_count);
        dest.writeString(this.level);
        dest.writeString(this.level_name);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.login = in.readString();
        this.avatar_url = in.readString();
        this.name = in.readString();
        this.location = in.readString();
        this.company = in.readString();
        this.twitter = in.readString();
        this.website = in.readString();
        this.bio = in.readString();
        this.tagline = in.readString();
        this.github = in.readString();
        this.created_at = in.readString();
        this.email = in.readString();
        this.topics_count = in.readString();
        this.replies_count = in.readString();
        this.following_count = in.readString();
        this.followers_count = in.readString();
        this.favorites_count = in.readString();
        this.level = in.readString();
        this.level_name = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
