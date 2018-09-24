package com.swinburne.irtsa.irtsa.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Scan model object to represent a saved scan.
 * Implements Parcelable so a Scan can be added to a bundle
 * and easily passed between Fragments/Activities.
 */
public class Scan implements Parcelable {
  public Integer id;
  public Bitmap image;
  public String description;
  public String name;
  public Date createdAt;

  public Scan(){}

  private Scan(Parcel in) {
    this.id = in.readInt();
    this.setImage(in.createByteArray());
    this.description = in.readString();
    this.name = in.readString();
  }

  private byte[] getImage() {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    image.compress(Bitmap.CompressFormat.PNG, 100, stream);
    return stream.toByteArray();
  }

  static DateFormat getDateFormat() {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  }

  private void setImage(byte[] image) {
    this.image = BitmapFactory.decodeByteArray(image, 0, image.length);
  }

  public static final Parcelable.Creator<Scan> CREATOR = new Parcelable.Creator<Scan>() {
    public Scan createFromParcel(Parcel in) {
      return new Scan(in);
    }

    public Scan[] newArray(int size) {
      return new Scan[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeInt(this.id);
    parcel.writeByteArray(this.getImage());
    parcel.writeString(this.description);
    parcel.writeString(this.name);
  }

}