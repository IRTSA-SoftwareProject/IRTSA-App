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

public class StartScanFragment extends Fragment {
  public StartScanFragment() {
    setHasOptionsMenu(true);
  }

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

  private void initialiseUi(View rootView) {
    Button startScanButton = rootView.findViewById(R.id.startScanButton);
    startScanButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        beginScan();
      }
    });
  }

  private void beginScan() {
    ViewScanFragment viewScanFragment = new ViewScanFragment();
    FragmentTransaction transaction = getFragmentManager().beginTransaction();
    // Store the Fragment in stack
    transaction.addToBackStack(null);
    transaction.replace(R.id.scanContainer, viewScanFragment, "ViewScanFragment").commit();
  }
}