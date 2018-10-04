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
  String COLUMN_CREATED_AT = "Created_At";

  String CREATE_SCAN_TABLE = "CREATE TABLE " + TABLE_SCAN
      + "("
      + "Id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
      + "Name VARCHAR(255) NOT NULL,"
      + "Description VARCHAR(255) NOT NULL,"
      + "Image varbinary(1000) NOT NULL,"
      + "Created_At TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL"
      + ")";

  /**
   * Get all scans from the SQLite database.
   *
   * @return List containing all scans retrieved from database.
   */
  List<Scan> getAllScans();

  /**
   * Inserts a Scan into the database.
   *
   * @param scan The Scan to insert into the database.
   * @return Represents whether the insert operation was successful.
   */
  Boolean insertScan(Scan scan);

  /**
   * Deletes a scan from the SQLite database.
   *
   * @param scanId ID of the scan we want to delete
   * @return Represents whether the delete operation was successful.
   */
  Boolean deleteScan(Integer scanId);

  Boolean editScan(Integer scanId, String scanName, String scanDescription);
}
