package com.swinburne.irtsa.irtsa.scan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
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

import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Modal Dialog Fragment that displays when a user taps the save icon on the toolbar.
 */
public class SaveDialog extends AppCompatDialogFragment {
    private EditText mFname;
    private EditText mDescription;

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
        // Inflate the Dialog's resource file.
        View view = inflater.inflate(R.layout.dialog_save, null);

        mFname = view.findViewById(R.id.fName);
        mDescription = view.findViewById(R.id.fDescription);

        // Set the properties of the dialog view
        builder.setView(view)
                .setTitle("Save Scan")
                // Create the Cancel button
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                // Create the Save button and register an onClickListener that saves the new scan.
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ScanInterface scanAccessObject = new ScanAccessObject(getContext());

                        Scan testScan = new Scan();
                        testScan.name = mFname.getText().toString();
                        testScan.description = mDescription.getText().toString();
                        testScan.image = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.phase);

                        scanAccessObject.insertScan(testScan);
                    }
                });
        //Return the built Dialog
        return builder.create();
    }
}