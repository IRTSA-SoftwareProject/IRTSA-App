package com.swinburne.irtsa.irtsa.gallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swinburne.irtsa.irtsa.R;
import com.swinburne.irtsa.irtsa.model.Scan;

import io.reactivex.functions.Consumer;

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

  public GalleryDetailFragment() {
    setHasOptionsMenu(true);
  }

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

  Consumer<Bundle> scanDetailsSavedConsumer = (savedScanDetails) -> {
    if (savedScanDetails.containsKey("newName"))
      name.setText("Description: " + savedScanDetails.getString("newName"));

    if (savedScanDetails.containsKey("newDescription"))
      description.setText("Name: " + savedScanDetails.getString("newDescription"));
  };
}
