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
 * The ScanProgressFragment passes the scan image it has received
 * from the server in an argument to this Fragment, this Fragment then displays that image.
 */
public class ViewScanFragment extends Fragment {
  private ZoomableImageView scanImage;
  private Bitmap scanBitmap;
  private byte[] scanByteArray;

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

    // Determines if this Fragment's toolbar should be shown in the case the app is rotated.
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
   * Bind the different layout components in the view to objects here in the controller..
   * @param v The root view
   * @param scanBitmap The
   */
  private void initialiseUi(View v, Bitmap scanBitmap) {
    scanImage = v.findViewById(R.id.scanImage);
    scanImage.setImageBitmap(scanBitmap);
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

  /**
   * When the view scan fragment is opened inflate the appropriate toolbar layout.
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
}