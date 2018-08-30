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

public class SaveDialog extends AppCompatDialogFragment {
    private EditText mFname;
    private EditText mDescription;
    public Bitmap mBitmap;

    //Function that creates a dialog
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //Define where to get the layout from for the dialogs view
        View view = inflater.inflate(R.layout.dialog_save, null);

        mFname = view.findViewById(R.id.fName);
        mDescription = view.findViewById(R.id.fDescription);

        //set the characteristics of the dialog view
        builder.setView(view)
                .setTitle("Save Scan")
                //Create the cancel button
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                //Create the save button
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ScanInterface scanAccessObject = new ScanAccessObject(getContext());

                        Scan testScan = new Scan();
                        testScan.name = mFname.getText().toString();
                        testScan.description = mDescription.getText().toString();
                        //testScan.image = BitmapFactory.decodeByteArray(R.drawable.phase,0, R.drawable.phase.length);
                        testScan.image = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.phase);

                        scanAccessObject.insertScan(testScan);
                    }
                });
        //Build the dialog in order for it to popup
        return builder.create();
    }
}