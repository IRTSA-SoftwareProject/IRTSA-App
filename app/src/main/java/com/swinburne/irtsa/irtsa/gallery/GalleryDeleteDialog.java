package com.swinburne.irtsa.irtsa.gallery;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swinburne.irtsa.irtsa.R;
import com.swinburne.irtsa.irtsa.model.Scan;
import com.swinburne.irtsa.irtsa.model.ScanAccessObject;
import com.swinburne.irtsa.irtsa.model.ScanInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryDeleteDialog extends AppCompatDialogFragment {



  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    Integer scanId = getArguments().getInt("scanId");
    //Define where to get the layout from for the dialogs view
    View view = inflater.inflate(R.layout.dialog_gallery_delete, null);
    //set the characteristics of the dialog view
    builder.setView(view)
        .setTitle("Confirm Delete of Current Scan?")
        //Create the cancel button
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialogInterface, int i) {

          }
        })
        //Create the save button
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialogInterface, int i) {
            ScanInterface scanAccessObject = new ScanAccessObject(getContext());
            scanAccessObject.deleteScan(scanId);
            getActivity().onBackPressed();
          }
        });
    //Build the dialog in order for it to popup
    return builder.create();
  }
}