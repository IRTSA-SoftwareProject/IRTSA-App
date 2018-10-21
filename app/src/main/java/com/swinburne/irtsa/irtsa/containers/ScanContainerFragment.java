package com.swinburne.irtsa.irtsa.containers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swinburne.irtsa.irtsa.R;
import com.swinburne.irtsa.irtsa.scan.StartScanFragment;

/**
 * This Fragment's layout consists purely of an empty FrameLayout.
 * This Fragment serves as a container for the Scan fragments and is added to the first tab
 * of the ViewPager when the app is started.
 */
public class ScanContainerFragment extends Fragment {

  /**
   * Inflate the layout for this view.
   * As this is a container, the layout consists of a single FrameLayout that is used to contain
   * different fragments.
   * @param inflater Instance of the layout inflater.
   * @param container The root view
   * @param savedInstanceState Saved state of the fragment
   * @return The view for this fragment.
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_scan_container, container, false);
  }

  /**
   * Load the start scan fragment once this fragment has been created.
   */
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Don't replace the container's contents if this fragment is being recreated.
    if (savedInstanceState == null) {
      StartScanFragment startScanFragment = new StartScanFragment();
      FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
      transaction.replace(R.id.scanContainer, startScanFragment, "StartScanFragment").commit();
    }
  }
}
