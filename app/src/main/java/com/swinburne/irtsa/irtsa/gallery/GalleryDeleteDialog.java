package com.swinburne.irtsa.irtsa.gallery;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.swinburne.irtsa.irtsa.R;
import com.swinburne.irtsa.irtsa.model.ScanAccessObject;
import com.swinburne.irtsa.irtsa.model.ScanInterface;

/**
 * This dialog is displayed when the user selects delete when viewing a saved scan.
 * It confirms the user would like to delete the scan. If confirmed, the scan is deleted from
 * the local SQLite database.
 */
public class GalleryDeleteDialog extends AppCompatDialogFragment {

  /**
   * Builds the dialog's view and specifies the behaviour of its buttons.
   * @param savedInstanceState The saved instance of the dialog.
   * @return The created dialog.
   */
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    View view = inflater.inflate(R.layout.dialog_gallery_delete, null);

    // Get the ID of the scan we wish to delete.
    Integer scanId = getArguments().getInt("scanId");

    // Set the view and behaviour of this dialog.
    builder.setView(view)
        .setTitle("Confirm Delete of Scan")
        //Create the cancel button
        .setNegativeButton("No", (dialogInterface, i) -> {
        })
        //Create the save button
        .setPositiveButton("Yes", (dialogInterface, i) -> {
          ScanInterface scanAccessObject = new ScanAccessObject(getContext());
          scanAccessObject.deleteScan(scanId);
          getActivity().onBackPressed();
        });

    // Return the created dialog.
    return builder.create();
  }
}