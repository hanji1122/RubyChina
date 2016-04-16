package com.luckyhan.rubychina.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Token implements Parcelable {

    public String access_token;
    public String token_type;
    public int expires_in;
    public String refresh_token;
    public int created_at;

    @Override
    public String toString() {
        return "Token{" +
                "access_token='" + access_token + '\'' +
                ", token_type='" + token_type + '\'' +
                ", expires_in=" + expires_in +
                ", refresh_token='" + refresh_token + '\'' +
                ", created_at=" + created_at +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.access_token);
        dest.writeString(this.token_type);
        dest.writeInt(this.expires_in);
        dest.writeString(this.refresh_token);
        dest.writeInt(this.created_at);
    }

    public Token() {
    }

    protected Token(Parcel in) {
        this.access_token = in.readString();
        this.token_type = in.readString();
        this.expires_in = in.readInt();
        this.refresh_token = in.readString();
        this.created_at = in.readInt();
    }

    public static final Parcelable.Creator<Token> CREATOR = new Parcelable.Creator<Token>() {
        public Token createFromParcel(Parcel source) {
            return new Token(source);
        }

        public Token[] newArray(int size) {
            return new Token[size];
        }
    };

}
