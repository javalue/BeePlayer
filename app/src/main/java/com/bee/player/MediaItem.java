package com.bee.player;

import android.os.Parcel;
import android.os.Parcelable;

public class MediaItem implements Parcelable {

    public int id;
    public String path;
    public String displayName;
    public String album;
    public String artist;
    public String title;
    public String mimeType;
    public long duration;
    public long size;
    public String resolution;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(path);
        dest.writeString(displayName);
        dest.writeString(album);
        dest.writeString(artist);
        dest.writeString(title);
        dest.writeString(mimeType);
        dest.writeLong(duration);
        dest.writeLong(size);
        dest.writeString(resolution);
    }

    public static final Parcelable.Creator<MediaItem> CREATOR =
            new Creator<MediaItem>() {
                @Override
                public MediaItem[] newArray(int size) {
                    return new MediaItem[size];
                }

                @Override
                public MediaItem createFromParcel(Parcel in) {
                    return new MediaItem(in);
                }
            };


    public MediaItem() {
    }

    private MediaItem(Parcel in) {
        id = in.readInt();
        path = in.readString();
        displayName = in.readString();
        album = in.readString();
        artist = in.readString();
        title = in.readString();
        mimeType = in.readString();
        duration = in.readLong();
        size = in.readLong();
        resolution = in.readString();

    }
}
