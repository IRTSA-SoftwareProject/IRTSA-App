package com.swinburne.irtsa.irtsa.containers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swinburne.irtsa.irtsa.R;
import com.swinburne.irtsa.irtsa.scan.StartScanFragment;

/**
 * This Fragment's layout consists purely of an empty FrameLayout.
 * This Fragment serves as a container for the Scan fragments.
 */
public class ScanContainerFragment extends Fragment {
  public ScanContainerFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_scan_container, container, false);
  }

  /**
   * Immediately load the StartScanFragment once this container Fragment becomes visible.
   */
  @Override
  public void onStart() {
    super.onStart();
    StartScanFragment startScanFragment = new StartScanFragment();
    FragmentTransaction transaction = getFragmentManager().beginTransaction();
    transaction.replace(R.id.scanContainer, startScanFragment, "StartScanFragment").commit();
  }
}
