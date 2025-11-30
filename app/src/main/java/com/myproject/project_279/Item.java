package com.myproject.project_279;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {

    // Backend fields (must match PHP JSON)
    private int id;                  // NEW
    private String name;
    private String price;
    private String image_url;        // matches "image_url" from PHP
    private String category_name;    // optional — if returned by PHP
    private int quantity;

    // ---------- Constructors ----------
    public Item(int id, String name, String price, String image_url) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image_url = image_url;
        this.quantity = 1;
    }

    public Item(int id, String name, String price, String image_url, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image_url = image_url;
        this.quantity = quantity;
    }

    // Parcelable constructor
    protected Item(Parcel in) {
        id = in.readInt();
        name = in.readString();
        price = in.readString();
        image_url = in.readString();
        category_name = in.readString();
        quantity = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    // ---------- Getters & Setters ----------
    public int getId() { return id; }                   // NEW
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getImageUrl() { return image_url; }   // updated getter name
    public void setImageUrl(String imageUrl) { this.image_url = imageUrl; }

    public String getCategoryName() { return category_name; }
    public void setCategoryName(String category_name) { this.category_name = category_name; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    // ---------- Parcelable ----------
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(price);
        parcel.writeString(image_url);
        parcel.writeString(category_name);
        parcel.writeInt(quantity);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
