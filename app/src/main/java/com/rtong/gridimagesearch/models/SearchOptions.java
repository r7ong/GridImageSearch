package com.rtong.gridimagesearch.models;


import android.os.Parcel;
import android.os.Parcelable;

public class SearchOptions implements Parcelable {
    public String size;
    public String color;
    public String type;
    public String site;

    public SearchOptions(String size, String color, String type, String site) {
        this.size = size;
        this.color = color;
        this.type = type;
        this.site = site;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.size);
        dest.writeString(this.color);
        dest.writeString(this.type);
        dest.writeString(this.site);
    }

    private SearchOptions(Parcel in) {
        this.size = in.readString();
        this.color = in.readString();
        this.type = in.readString();
        this.site = in.readString();
    }

    public static final Creator<SearchOptions> CREATOR = new Creator<SearchOptions>() {
        public SearchOptions createFromParcel(Parcel source) {
            return new SearchOptions(source);
        }

        public SearchOptions[] newArray(int size) {
            return new SearchOptions[size];
        }
    };
}
