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
  private Integer id;
  private Bitmap image;
  private String description;
  private String name;
  private Date createdAt;

  public Scan(){}

  private Scan(Parcel in) {
    this.id = in.readInt();
    this.setImage(in.createByteArray());
    this.description = in.readString();
    this.name = in.readString();
    this.createdAt = new Date(in.readLong());
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Bitmap getImage() {
    return image;
  }

  public void setImage(Bitmap image) {
    this.image = image;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  private byte[] getImageAsByteArray() {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    image.compress(Bitmap.CompressFormat.PNG, 100, stream);
    return stream.toByteArray();
  }

  private void setImage(byte[] image) {
    this.image = BitmapFactory.decodeByteArray(image, 0, image.length);
  }

  static DateFormat getDateFormat() {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
    parcel.writeByteArray(this.getImageAsByteArray());
    parcel.writeString(this.description);
    parcel.writeString(this.name);
    parcel.writeLong(this.createdAt.getTime());
  }

}