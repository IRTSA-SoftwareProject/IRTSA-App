package com.swinburne.irtsa.irtsa.gallery;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.swinburne.irtsa.irtsa.R;
import com.swinburne.irtsa.irtsa.model.Scan;
import com.swinburne.irtsa.irtsa.model.ScanAccessObject;
import com.swinburne.irtsa.irtsa.model.ScanInterface;

/**
 * Modal Dialog Fragment that displays when a user taps the save icon on the toolbar.
 */
public class GalleryEditDialog extends AppCompatDialogFragment {
  private EditText name;
  private EditText description;
  private String scanName;
  private String scanDescription;

  /**
   * Initialises the EditText's on the dialog and registers an onClickListener on the Save button.
   *
   * @param savedInstanceState Saved representation of the dialog's state.
   * @return The dialog to be displayed.
   */
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    Integer scanId = getArguments().getInt("scanId");
    //Define where to get the layout from for the dialogs view
    View view = inflater.inflate(R.layout.dialog_gallery_edit, null);

    name = view.findViewById(R.id.editName);
    description = view.findViewById(R.id.editDescription);

    //set the characteristics of the dialog view
    builder.setView(view)
        .setTitle("Rename Scan")
        //Create the cancel button
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialogInterface, int i) {

          }
        })
        //Create the save button
        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialogInterface, int i) {
            scanName = name.getText().toString();
            scanDescription = description.getText().toString();
            ScanInterface scanAccessObject = new ScanAccessObject(getContext());
            scanAccessObject.editScan(scanId, scanName, scanDescription);
            getActivity().onBackPressed();
          }
        });
    //Build the dialog in order for it to popup
    return builder.create();
  }
}