package com.swinburne.irtsa.irtsa.scan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.swinburne.irtsa.irtsa.R;
import com.swinburne.irtsa.irtsa.ToolbarSetter;

/**
 * Fragment that displays the details of a completed scan.
 */
public class ViewScanFragment extends Fragment implements ToolbarSetter {
  private ImageView scanImage;

  public ViewScanFragment() {
    setHasOptionsMenu(true);
  }

  /**
   * When the view scan fragment is opened the icons are changed in the
   * top menu toolbar.
   *
   * @param menu Menu View to contain the inflated menu.
   * @param inflater Inflates the menu resource into the Menu View
   */
  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    menu.findItem(R.id.save).setVisible(true);
    menu.findItem(R.id.settings).setVisible(false);
    super.onCreateOptionsMenu(menu, inflater);
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
    //Call the openSaveDialog when the save icon is selected
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
    View v = inflater.inflate(R.layout.fragment_view_scan, container, false);

    initialiseUi(v);

    return v;
  }

  /**
   * Initialise the ImageView and have it display an image.
   */
  private void openSaveDialog() {
    SaveDialog saveDialog = new SaveDialog();
    saveDialog.show(getFragmentManager(), "Save Dialog");
  }

  private void initialiseUi(View v) {
    scanImage = v.findViewById(R.id.scanImage);
    scanImage.setImageResource(R.drawable.phase);
  }

  /**
   * Change the icons that are viewable on the top menu toolbar.
   *
   * @param menu the menu at the top of the application
   */
  @Override
  public void setToolbar(Menu menu) {
    menu.findItem(R.id.settings).setVisible(false);
    menu.findItem(R.id.save).setVisible(true);
    menu.findItem(R.id.select).setVisible(false);
    menu.findItem(R.id.share).setVisible(false);
    menu.findItem(R.id.delete).setVisible(false);
  }
}