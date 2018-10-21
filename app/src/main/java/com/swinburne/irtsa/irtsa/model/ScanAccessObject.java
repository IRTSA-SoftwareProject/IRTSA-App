package com.swinburne.irtsa.irtsa.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Logic to retrieve/store Scan objects in the Scan table of the SQLite database.
 */
public class ScanAccessObject extends SQLiteOpenHelper implements ScanInterface {

  public ScanAccessObject(Context context) {
    // Set the file path of the DB and creates if it does not exist.
    super(context, context.getExternalFilesDir(null) + "ScanDB.db", null, 1);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_SCAN_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int x, int y) {

  }

  @Override
  public Boolean deleteScan(Integer id) {
    SQLiteDatabase db = this.getWritableDatabase();

    boolean result = db.delete(TABLE_SCAN, COLUMN_ID + "=" + id, null) > 0;

    db.close();

    return result;
  }

  @Override
  public Boolean editScan(Integer id, String name, String description) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues data = new ContentValues();

    if (name.length() > 0) {
      data.put(COLUMN_NAME, name);
    }

    if (description.length() > 0) {
      data.put(COLUMN_DESCRIPTION, description);
    }

    boolean result = data.size() > 0
            && db.update(TABLE_SCAN, data, COLUMN_ID + "=" + id, null) > 0;

    db.close();

    return result;
  }


  @Override
  public List<Scan> getAllScans() {
    SQLiteDatabase db = this.getReadableDatabase();
    List<Scan> result = new ArrayList<Scan>();

    String sql = "SELECT * FROM " + TABLE_SCAN;

    Cursor resultCursor = db.rawQuery(sql, null);

    while (resultCursor.moveToNext()) {
      Scan scan = new Scan();
      scan.setId(resultCursor.getInt(resultCursor.getColumnIndex(COLUMN_ID)));
      scan.setName(resultCursor.getString(resultCursor.getColumnIndex(COLUMN_NAME)));
      scan.setDescription(resultCursor.getString(resultCursor.getColumnIndex(COLUMN_DESCRIPTION)));

      try {
        scan.setCreatedAt(Scan.getDateFormat().parse(
                resultCursor.getString(resultCursor.getColumnIndex(COLUMN_CREATED_AT))));
      } catch (ParseException e) {
        Log.e("ScanAccessObject",
                "(" + scan.getId() + ", " + scan.getName() + ") Unable to parse created at date: "
                        + e.getMessage());
      }


      byte[] image = resultCursor.getBlob(resultCursor.getColumnIndex(COLUMN_IMAGE));
      scan.setImage(BitmapFactory.decodeByteArray(image, 0, image.length));

      result.add(scan);
    }

    db.close();

    return result;
  }

  @Override
  public Boolean insertScan(Scan scan) {
    //Convert the Bitmap into a byte array
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    scan.getImage().compress(Bitmap.CompressFormat.PNG, 100, outputStream);
    byte[] imageBlob = outputStream.toByteArray();

    // Build the SQL query
    ContentValues data = new ContentValues();
    data.put(COLUMN_NAME, scan.getName());
    data.put(COLUMN_DESCRIPTION, scan.getDescription());
    data.put(COLUMN_IMAGE, imageBlob);
    data.put(COLUMN_CREATED_AT, Scan.getDateFormat().format(new Date()));

    SQLiteDatabase db = this.getWritableDatabase();
    long rowInserted = db.insert(TABLE_SCAN, null, data);

    db.close();

    return rowInserted != -1;
  }
}
