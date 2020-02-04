package com.example.moviesearcher.model.data;


import android.os.Parcel;
import android.os.Parcelable;

public class Subcategory implements Parcelable {

    private int id;
    private String name;

    public Subcategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    private Subcategory(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<Subcategory> CREATOR = new Creator<Subcategory>() {
        @Override public Subcategory createFromParcel(Parcel in) {
            return new Subcategory(in);
        }
        @Override public Subcategory[] newArray(int size) {
            return new Subcategory[size];
        }
    };

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }
}
