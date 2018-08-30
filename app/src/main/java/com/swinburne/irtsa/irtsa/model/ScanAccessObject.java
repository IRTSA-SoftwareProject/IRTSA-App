package com.swinburne.irtsa.irtsa.model;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Logic to retrieve/store Scan objects in the Scan table of the SQLite database.
 */
public class ScanAccessObject extends SQLiteOpenHelper implements ScanInterface {

    public ScanAccessObject(Context context) {
        // Set the file path of the DB and creates if it does not exist.
        super(context, context.getExternalFilesDir(null) + "/ScanDB.db", null, 1);
    }

    /**
     * Creates the scan table.
     *
     * @param db Instance of the SQLite database.
     */
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_SCAN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int x, int y) {

    }

    /**
     * Deletes a scan from the SQLite database.
     *
     * @param id ID of the scan we want to delete
     * @return Represents whether the delete operation was successful.
     */
    @Override
    public Boolean deleteScan(Integer  id) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_SCAN, COLUMN_ID + "=" + id, null) > 0;
    }

    /**
     * Get all scans from the SQLite database.
     *
     * @return List containing all scans retrieved from database.
     */
    @Override
    public List<Scan> getAllScans() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Scan> result = new ArrayList<Scan>();

        String sql = "SELECT * FROM " + TABLE_SCAN;

        Cursor resultCursor = db.rawQuery(sql, null);

        while (resultCursor.moveToNext()) {
            Scan scan = new Scan();

            byte[] image = resultCursor.getBlob(resultCursor.getColumnIndex(COLUMN_IMAGE));

            scan.id = resultCursor.getInt(resultCursor.getColumnIndex(COLUMN_ID));
            scan.name = resultCursor.getString(resultCursor.getColumnIndex(COLUMN_NAME));
            scan.description = resultCursor.getString(resultCursor.getColumnIndex(COLUMN_DESCRIPTION));
            scan.image = BitmapFactory.decodeByteArray(image,0, image.length);

            result.add(scan);
        }

        return result;
    }

    /**
     * Inserts a Scan into the database.
     *
     * @param scan The Scan to insert into the database.
     * @return Represents whether the insert operation was successful.
     */
    @Override
    public Boolean insertScan(Scan scan) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues data = new ContentValues();

        //Convert the Bitmap into a byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        scan.image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] imageBlob = outputStream.toByteArray();

        // Build the SQL query
        data.put(COLUMN_NAME, scan.name);
        data.put(COLUMN_DESCRIPTION, scan.description);
        data.put(COLUMN_IMAGE, imageBlob);

        long rowInserted = db.insert(TABLE_SCAN, null, data);

        if (rowInserted != -1) {
            return true;
        } else {
            return false;
        }
    }
}
