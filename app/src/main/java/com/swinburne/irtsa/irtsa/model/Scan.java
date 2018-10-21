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

  /**
   * Restores the properties of a scan from a Parcel object.
   * Required as this object implements Parcelable.
   * The order in which data is read from the Parcel matters here, it must match the order in which
   * the data has been read into the Parcel.
   * @param in The Parcel containing scan data to restore.
   */
  private Scan(Parcel in) {
    this.id = in.readInt();
    this.setImage(in.createByteArray());
    this.description = in.readString();
    this.name = in.readString();
    this.createdAt = new Date(in.readLong());
  }

  /**
   * Returns the Scan's unique ID in the SQLite database.
   * Only Scans that have been retrieved from the database will have this property.
   * @return The Scan's unique ID.
   */
  public Integer getId() {
    return id;
  }

  /**
   * Set the ID of a scan.
   * @param id The ID to set for this scan.
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * Get the Bitmap representation of the scan image.
   * @return Bitmap representation of the scan image..
   */
  public Bitmap getImage() {
    return image;
  }

  /**
   * Losslessly compress this image to a byte array.
   * Used so the scan image can be easily placed in a Parcel as required by its
   * Parcelable implementation.
   * @return The scan image represented as a byte array.
   */
  private byte[] getImageAsByteArray() {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    image.compress(Bitmap.CompressFormat.PNG, 100, stream);
    return stream.toByteArray();
  }

  /**
   * Set the scans image.
   * @param image Image to set in Bitmap format.
   */
  public void setImage(Bitmap image) {
    this.image = image;
  }

  /**
   * Converts a given byte array to a bitmap and sets this to be the scan image.
   * @param image The image byte array to convert and set as the scan's image.
   */
  private void setImage(byte[] image) {
    this.image = BitmapFactory.decodeByteArray(image, 0, image.length);
  }

  /**
   * Returns the description of the scan.
   * @return The description of the scan.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Set the description of the scan.
   * @param description The description to set for the scan.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Get the name of the scan.
   * @return The name of the scan.
   */
  public String getName() {
    return name;
  }

  /**
   * Set the name of the scan.
   * @param name The name to set for the scan.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the date the scan was saved.
   * @return The date the scan was saved.
   */
  public Date getCreatedAt() {
    return createdAt;
  }

  /**
   * Set the time this scan was created (saved).
   * @param createdAt The time this scan was created.
   */
  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  /**
   * A format used to represent the date in yyyy-MMM-dd HH:mm:ss.
   * @return The date format..
   */
  static DateFormat getDateFormat() {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  }

  /**
   * Field required to successfully implement Parcelable.
   * Defines how to create a scan from a parcel and a method to create an array of Scans.
   */
  public static final Parcelable.Creator<Scan> CREATOR = new Parcelable.Creator<Scan>() {
    /**
     * Returns a scan given a parcel.
     * @param in The Parcel containing Scan information.
     * @return A Scan containing information from the given Parcel.
     */
    public Scan createFromParcel(Parcel in) {
      return new Scan(in);
    }

    /**
     * Creates an array of Scans of a given size.
     * @param size Size of the array.
     * @return An array of scans of the provided size.
     */
    public Scan[] newArray(int size) {
      return new Scan[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  /**
   * Writes Scan information into a parcel.
   * The order here must match the order in which scans are unpacked from the
   * Parcel in the Scan constructor that accepts a Parcel.
   * @param parcel Parcel to write scan data to.
   */
  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeInt(this.id);
    parcel.writeByteArray(this.getImageAsByteArray());
    parcel.writeString(this.description);
    parcel.writeString(this.name);
    parcel.writeLong(this.createdAt.getTime());
  }

}