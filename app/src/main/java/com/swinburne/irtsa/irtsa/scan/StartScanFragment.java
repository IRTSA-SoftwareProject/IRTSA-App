package com.swinburne.irtsa.irtsa.scan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.swinburne.irtsa.irtsa.R;

/**
 * Fragment with a button that begins a scan.
 */
public class StartScanFragment extends Fragment {
  public StartScanFragment() {
    setHasOptionsMenu(true);
  }

  /**
   * Called when the toolbar (menu) is created.
   *
   * @param menu Menu View to contain the inflated menu.
   * @param inflater Inflates the menu resource into the Menu View
   */
  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.toolbar_scan, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View rootView = inflater.inflate(R.layout.fragment_start_scan, container, false);

    initialiseUi(rootView);

    return rootView;
  }

  /**
   * Initialise the Button and set an OnClickListener
   *
   * @param rootView The StartScanFragment's top level View
   */
  private void initialiseUi(View rootView) {
    Button startScanButton = rootView.findViewById(R.id.startScanButton);
    startScanButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        beginScan();
      }
    });
  }

  /**
   * Replace the Fragment in the scanContainer with a new ViewScanFragment.
   * Add the StartScanFragment to the back-stack so it can be restored if
   * the user presses the back button.
   */
  private void beginScan() {
    ViewScanFragment viewScanFragment = new ViewScanFragment();
    FragmentTransaction transaction = getFragmentManager().beginTransaction();
    // Store the Fragment in stack
    transaction.addToBackStack(null);
    transaction.replace(R.id.scanContainer, viewScanFragment, "ViewScanFragment").commit();
  }
}