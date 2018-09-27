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
import com.swinburne.irtsa.irtsa.scan.SaveDialog;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryDetailFragment extends Fragment {
  private Scan scan;
  private ImageView image;
  private TextView name;
  private TextView description;

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
    int id = item.getItemId();
    // Call the openDeleteDialog when the delete icon is selected
    if (id == R.id.delete) {
      openDeleteDialog();
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
    name.setText("Name: " + scan.name);
    description = v.findViewById(R.id.galleryDetailDescription);
    description.setText("Description: " + scan.description);
  }

  private void openDeleteDialog() {
    GalleryDeleteDialog deleteDialog = new GalleryDeleteDialog();
    Bundle scanId = new Bundle();
    scanId.putInt("scanId", scan.id);
    deleteDialog.setArguments(scanId);
    deleteDialog.show(getFragmentManager(), "Delete Dialog");
  }
}
