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
import com.swinburne.irtsa.irtsa.model.ScanAccessObject;
import com.swinburne.irtsa.irtsa.model.ScanInterface;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Modal Dialog Fragment that displays when a user taps the edit icon on the toolbar.
 * Used to edit the detail's of the currently selected Scan saved in the SQLite database.
 */
public class GalleryEditDialog extends AppCompatDialogFragment {
  private EditText name;
  private EditText description;
  private String scanName;
  private String scanDescription;

  private final PublishSubject<Bundle> onClickSave = PublishSubject.create();

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

    // Set the view and behaviour of this dialog.
    builder.setView(view)
        .setTitle("Rename Scan")
        //Create the cancel button
        .setNegativeButton("Cancel", ((dialogInterface, i) -> { } ))
        //Create the save button
        .setPositiveButton("Save", ((dialogInterface, i) -> {
          scanName = name.getText().toString();
          scanDescription = description.getText().toString();

          // Edit the scan's details in the SQLite database.
          ScanInterface scanAccessObject = new ScanAccessObject(getContext());
          scanAccessObject.editScan(scanId, scanName, scanDescription);

          Bundle newScanData = new Bundle();
          if (scanName.length() > 0) {
            newScanData.putString("newName", scanName);
          }

          if (scanDescription.length() > 0) {
            newScanData.putString("newDescription", scanDescription);
          }

          // This notifies the GalleryDetailFragment that the scan has had its details edited.
          onClickSave.onNext(newScanData);
        }));
    //Build the dialog in order for it to popup
    return builder.create();
  }

  public Observable<Bundle> getSavedScanInformation() {
    return onClickSave;
  }
}