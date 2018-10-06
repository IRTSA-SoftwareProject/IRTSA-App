package com.swinburne.irtsa.irtsa.gallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swinburne.irtsa.irtsa.MainActivity;
import com.swinburne.irtsa.irtsa.R;
import com.swinburne.irtsa.irtsa.model.Scan;

import io.reactivex.functions.Consumer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryDetailFragment extends Fragment {
  private Scan scan;
  private ImageView image;
  private TextView name;
  private TextView description;
  private TextView date;

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.gallery_detail_toolbar, menu);
  }

  /**
   * Opens the DeleteDialog Fragment if the save menu icon is selected.
   *
   * @param item Selected menu item.
   * @return
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Call the openDeleteDialog when the delete icon is selected
    switch (item.getItemId()) {
      case R.id.delete:
        openDeleteDialog();
        break;
      case R.id.edit:
        openEditDialog();
        break;
      case R.id.share:
        shareImage();
        break;
      case R.id.save:
        openSaveToGalleryDialog();
        break;
      default:
        System.out.println("Unregistered toolbar button selected");
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    Bundle bundle = this.getArguments();
    if (bundle != null) {
      scan = bundle.getParcelable("Scan");
    }
    View v = inflater.inflate(R.layout.fragment_gallery_detail, container, false);
    if (savedInstanceState != null) {
      setHasOptionsMenu(((MainActivity)getActivity()).getPreviouslyFocusedFragment().equals(getClass().getCanonicalName()));
    } else {
      setHasOptionsMenu(true);
    }
    initialiseUi(v, scan);
    return v;
  }

  private void initialiseUi(View v, Scan scan) {
    image = v.findViewById(R.id.galleryDetailImage);
    image.setImageBitmap(scan.image);
    name = v.findViewById(R.id.galleryDetailName);
    name.setText(getString(R.string.gallery_detail_label_name) + scan.name);
    description = v.findViewById(R.id.galleryDetailDescription);
    description.setText(getString(R.string.gallery_detail_label_description) + scan.description);
    date = v.findViewById(R.id.galleryDetailDate);
    date.setText(getString(R.string.gallery_detail_label_date) + scan.createdAt);
  }

  private void openDeleteDialog() {
    GalleryDeleteDialog deleteDialog = new GalleryDeleteDialog();
    Bundle scanId = new Bundle();
    scanId.putInt("scanId", scan.id);
    deleteDialog.setArguments(scanId);
    deleteDialog.show(getFragmentManager(), "Delete Dialog");
  }

  private void openEditDialog() {
    GalleryEditDialog editDialog = new GalleryEditDialog();
    editDialog.getSavedScanInformation().subscribe(scanDetailsSavedConsumer);
    Bundle scanId = new Bundle();
    scanId.putInt("scanId", scan.id);
    editDialog.setArguments(scanId);
    editDialog.show(getFragmentManager(), "Edit Dialog");
  }

  private void openSaveToGalleryDialog() {
    GallerySaveDialog saveDialog = new GallerySaveDialog();
    Bundle scanBundle = new Bundle();
    scanBundle.putParcelable("scan", scan);
    saveDialog.setArguments(scanBundle);
    saveDialog.show(getFragmentManager(), "Edit Dialog");
  }

  /**
   * To share an image, it must first be saved on the device.
   * Save the bitmap to the internal cache dir and provide the file URI to the sharing intent.
   * The image in the cache is overwritten each time an image is shared.
   * Contents of the cache are lost on uninstall and are inaccessible to other apps without a URI.
   */
  private void shareImage() {
    try {
      // Save the file to the cache
      File cachePath = new File(getContext().getCacheDir(), "images");
      cachePath.mkdirs();
      FileOutputStream stream = new FileOutputStream(cachePath + "/imageToShare.png");
      scan.image.compress(Bitmap.CompressFormat.PNG, 100, stream);
      stream.close();

      // Get the saved files URI
      File imagePath = new File(getContext().getCacheDir(), "images");
      File newFile = new File(imagePath, "/imageToShare.png");
      Uri contentUri = FileProvider.getUriForFile(getContext(),
              "com.swinburne.irtsa.irtsa.fileprovider", newFile);

      // Share the file via an intent
      if (contentUri != null) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setDataAndType(contentUri,
                getActivity().getContentResolver().getType(contentUri));
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        startActivity(Intent.createChooser(shareIntent, "Share Thermogram"));
      }
    } catch (Exception e) {
      System.out.println("Unable to share thermogram: " + e.toString());
    }
  }

  Consumer<Bundle> scanDetailsSavedConsumer = (savedScanDetails) -> {
    if (savedScanDetails.containsKey("newName")) {
      name.setText("Description: " + savedScanDetails.getString("newName"));
    }

    if (savedScanDetails.containsKey("newDescription")) {
      description.setText("Name: " + savedScanDetails.getString("newDescription"));
    }
  };
}
