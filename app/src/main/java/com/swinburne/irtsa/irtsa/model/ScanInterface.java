package com.swinburne.irtsa.irtsa.model;

import java.util.List;

/**
 * Defines constants and methods to access the Scan table in the SQLite database.
 */
public interface ScanInterface {
    String TABLE_SCAN = "SCAN";
    String COLUMN_NAME = "Name";
    String COLUMN_DESCRIPTION = "Description";
    String COLUMN_IMAGE = "Image";
    String COLUMN_ID = "Id"; // Unique Identifier

    String CREATE_SCAN_TABLE = "CREATE TABLE " + TABLE_SCAN +
            "(" +
                "Id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "Name VARCHAR(255) NOT NULL," +
                "Description VARCHAR(255) NOT NULL," +
                "Image varbinary(1000) NOT NULL" +
            ")";

    List<Scan> getAllScans();
    Boolean insertScan(Scan scan);
    Boolean deleteScan(Integer scanId);
}
