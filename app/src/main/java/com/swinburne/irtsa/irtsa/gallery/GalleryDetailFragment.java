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
import android.widget.TextView;

import com.swinburne.irtsa.irtsa.MainActivity;
import com.swinburne.irtsa.irtsa.R;
import com.swinburne.irtsa.irtsa.model.Scan;
import com.swinburne.irtsa.irtsa.utility.ZoomableImageView;

import io.reactivex.functions.Consumer;

import java.io.File;
import java.io.FileOutputStream;

/**
 * This fragment displays a scan that has been selected from the {@link GalleryDetailFragment}.
 * The user is able to view, share, edit, delete and save the image to local storage from here.
 */
public class GalleryDetailFragment extends Fragment {
  private Scan scan;
  private ZoomableImageView image;
  private TextView name;
  private TextView description;
  private TextView date;

  /**
   * Inflate the layout for this fragment.
   * Retrieves the scan to be displayed from its list of arguments.
   * @param inflater An inflater to inflate this fragment's layout.
   * @param container This view's container.
   * @param savedInstanceState Saved instance of this fragment.
   * @return This Fragment's view.
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_gallery_detail, container, false);

    // Retrieve the scan passed to this fragment that must be displayed.
    Bundle bundle = this.getArguments();
    if (bundle != null) {
      scan = bundle.getParcelable("Scan");
    }

    // Set the visibility of the options menu
    if (savedInstanceState != null) {
      String previousFragment = ((MainActivity) getActivity()).getPreviouslyFocusedFragment();
      setHasOptionsMenu(previousFragment.equals(getClass().getCanonicalName()));
    } else {
      setHasOptionsMenu(true);
    }
    initialiseUi(v, scan);
    return v;
  }

  /**
   * Initialise the different UI components to references of their view counterparts.
   * @param v This layout's root view.
   * @param scan The scan passed to this Fragment as an argument.
   */
  private void initialiseUi(View v, Scan scan) {
    image = v.findViewById(R.id.galleryDetailImage);
    image.setImageBitmap(scan.getImage());
    name = v.findViewById(R.id.galleryDetailName);
    name.setText(scan.getName());
    description = v.findViewById(R.id.galleryDetailDescription);
    description.setText(scan.getDescription());
    date = v.findViewById(R.id.galleryDetailDate);
    date.setText(scan.getCreatedAt().toString());
  }

  /**
   * Defines the layout to use for the options menu.
   * @param menu The current menu instance.
   * @param inflater An inflate to inflate our toolbar layout.
   */
  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.gallery_detail_toolbar, menu);
  }

  /**
   * Performs the appropriate action in response to a click on a toolbar icon.
   *
   * @param item Selected menu item.
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

  /**
   * Opens a dialog that allows the user to delete a scan from the SQLite database.
   */
  private void openDeleteDialog() {
    GalleryDeleteDialog deleteDialog = new GalleryDeleteDialog();
    Bundle scanId = new Bundle();
    scanId.putInt("scanId", scan.getId());
    deleteDialog.setArguments(scanId);
    deleteDialog.show(getFragmentManager(), "Delete Dialog");
  }

  /**
   * Opens a dialog that allows the user to edit the details of a scan in the SQLite database.
   */
  private void openEditDialog() {
    GalleryEditDialog editDialog = new GalleryEditDialog();
    editDialog.getSavedScanInformation().subscribe(scanDetailsSavedConsumer);
    Bundle scanId = new Bundle();
    scanId.putInt("scanId", scan.getId());
    editDialog.setArguments(scanId);
    editDialog.show(getFragmentManager(), "Edit Dialog");
  }

  /**
   * Opens a dialog that allows the user to save a scan to
   * the public gallery folder on their device.
   */
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
      scan.getImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
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

  /**
   * Be notified and update the UI if the details of the currently viewed scan change.
   */
  Consumer<Bundle> scanDetailsSavedConsumer = (savedScanDetails) -> {
    if (savedScanDetails.containsKey("newName")) {
      name.setText(savedScanDetails.getString("newName"));
    }
    if (savedScanDetails.containsKey("newDescription")) {
      description.setText(savedScanDetails.getString("newDescription"));
    }
  };
}
