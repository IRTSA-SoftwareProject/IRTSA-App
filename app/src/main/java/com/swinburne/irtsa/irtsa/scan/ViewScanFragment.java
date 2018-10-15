package com.swinburne.irtsa.irtsa.scan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.swinburne.irtsa.irtsa.MainActivity;
import com.swinburne.irtsa.irtsa.R;
import com.swinburne.irtsa.irtsa.utility.ZoomableImageView;

/**
 * Fragment that displays the details of a completed scan.
 */
public class ViewScanFragment extends Fragment {
  private ZoomableImageView scanImage;
  private Bitmap scanBitmap;
  private byte[] scanByteArray;

  /**
   * When the view scan fragment is opened the icons are changed in the
   * top menu toolbar.
   *
   * @param menu Menu View to contain the inflated menu.
   * @param inflater Inflates the menu resource into the Menu View
   */
  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.view_scan_toolbar, menu);
  }

  /**
   * Opens the SaveDialog Fragment if the save menu icon is selected.
   *
   * @param item Selected menu item.
   * @return
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    // Call the openSaveDialog when the save icon is selected
    if (id == R.id.save) {
      openSaveDialog();
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * Display the modal SaveDialog Fragment.
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    Bundle bundle = this.getArguments();
    if (bundle != null) {
      scanByteArray = bundle.getByteArray("scanByteArray");
      scanBitmap = BitmapFactory.decodeByteArray(scanByteArray, 0, scanByteArray.length);
    }
    View v = inflater.inflate(R.layout.fragment_view_scan, container, false);

    if (savedInstanceState != null) {
      String previousFragment = ((MainActivity) getActivity()).getPreviouslyFocusedFragment();
      setHasOptionsMenu(previousFragment.equals(getClass().getCanonicalName()));
    } else {
      setHasOptionsMenu(true);
    }

    initialiseUi(v, scanBitmap);

    return v;
  }

  /**
   * Initialise the ImageView and have it display an image.
   */
  private void openSaveDialog() {
    SaveDialog saveDialog = new SaveDialog();
    Bundle bundle = new Bundle();
    bundle.putByteArray("scanImage", scanByteArray);
    saveDialog.setArguments(bundle);
    saveDialog.show(getFragmentManager(), "Save Dialog");
  }

  private void initialiseUi(View v, Bitmap scanBitmap) {
    scanImage = v.findViewById(R.id.scanImage);
    scanImage.setImageBitmap(scanBitmap);
  }
}