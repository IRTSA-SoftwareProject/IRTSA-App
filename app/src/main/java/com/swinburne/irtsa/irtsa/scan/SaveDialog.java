package com.swinburne.irtsa.irtsa.scan;

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
public class SaveDialog extends AppCompatDialogFragment {
  private EditText name;
  private EditText description;
  private byte[] imageByteArray;

  /**
   * Initialises the EditText's on the dialog and registers an onClickListener on the Save button.
   *
   * @param savedInstanceState Saved representation of the dialog's state.
   * @return The dialog to be displayed.
   */
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    LayoutInflater inflater = getActivity().getLayoutInflater();
    //Define where to get the layout from for the dialogs view
    View view = inflater.inflate(R.layout.dialog_save, null);

    imageByteArray = getArguments().getByteArray("scanImage");
    name = view.findViewById(R.id.fName);
    description = view.findViewById(R.id.fDescription);


    //set the characteristics of the dialog view
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setView(view)
            .setTitle(R.string.save_dialog_title)
            //Create the cancel button
            .setNegativeButton(R.string.save_dialog_button_cancel, (dialogInterface, i) -> {
            })
        //Create the save button
        .setPositiveButton(R.string.save_dialog_button_save,
          new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
              Scan testScan = new Scan();
              testScan.name = name.getText().toString();
              testScan.description = description.getText().toString();
              testScan.image
                      = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);

              ScanInterface scanAccessObject = new ScanAccessObject(getContext());
              scanAccessObject.insertScan(testScan);
            }
          });
    //Build the dialog in order for it to popup
    return builder.create();
  }
}