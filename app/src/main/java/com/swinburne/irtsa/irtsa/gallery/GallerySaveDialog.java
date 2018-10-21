package com.swinburne.irtsa.irtsa.gallery;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.swinburne.irtsa.irtsa.R;
import com.swinburne.irtsa.irtsa.model.Scan;

public class GallerySaveDialog extends AppCompatDialogFragment {
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    View view = inflater.inflate(R.layout.dialog_gallery_save, null);

    Scan scan = getArguments().getParcelable("scan");

    // Set the view and behaviour of this dialog.
    builder.setView(view)
            .setTitle("Save thermogram to local storage?")
            .setNegativeButton("No", (dialogInterface, i) -> { })
            .setPositiveButton("Yes", (dialogInterface, i) -> {
              String url = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                      scan.getImage(), scan.getName(), scan.getDescription());
              System.out.println("Image saved to: " + url);
            });
    // Build the dialog in order for it to popup
    return builder.create();
  }
}
