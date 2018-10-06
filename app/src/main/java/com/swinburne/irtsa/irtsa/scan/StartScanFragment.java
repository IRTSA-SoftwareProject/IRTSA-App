package com.swinburne.irtsa.irtsa.scan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.swinburne.irtsa.irtsa.MainActivity;
import com.swinburne.irtsa.irtsa.R;
import com.swinburne.irtsa.irtsa.server.Message;
import com.swinburne.irtsa.irtsa.server.Server;

/**
 * Fragment with a button that begins a scan.
 */
public class StartScanFragment extends Fragment {
  private class StartScanMessage extends Message {
    class Body {
      String fileName;

      Body(String fileName) {
        this.fileName = fileName;
      }
    }

    Body body;

    StartScanMessage(String fileName) {
      type = "scan";
      body = new Body(fileName);
    }
  }

  private class ScanProgressMessage extends Message {
    class Body {
      int percent;
    }

    Body body;
  }

//  public StartScanFragment() {
//    //setHasOptionsMenu(true);
//  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View rootView = inflater.inflate(R.layout.fragment_start_scan, container, false);

    initialiseUi(rootView);

    if (savedInstanceState != null) {
      setHasOptionsMenu(((MainActivity)getActivity()).getPreviouslyFocusedFragment().equals(getClass().getCanonicalName()));
    }

    return rootView;
  }

  /**   * Initialise the Button and set an OnClickListener.
   *
   * @param rootView The StartScanFragment's top level View
   */
  private void initialiseUi(View rootView) {
    Button startScanButton = rootView.findViewById(R.id.startScanButton);
    startScanButton.setOnClickListener(view -> beginScan());
  }

  @Override
  public void onResume() {
    super.onResume();
    if (isVisible()) setHasOptionsMenu(true);
  }

  /**
   * Replace the Fragment in the scanContainer with a new ViewScanFragment.
   * Add the StartScanFragment to the back-stack so it can be restored if
   * the user presses the back button.
   */
  private void beginScan() {
    // Send a message to start the scan
    Server.send(new StartScanMessage("scan_001.png"));
    Server.messages.castToType("scan_progress", ScanProgressMessage.class)
        .takeUntil(Server.messages.ofType("scan_complete"))
        .subscribe(message -> {
          Log.i("MESSAGE", "Message received");
          Log.i("MESSAGE_TYPE", message.type);
          Log.i("MESSAGE_PERCENT", Integer.toString(message.body.percent));
        });

    ViewScanFragment viewScanFragment = new ViewScanFragment();
    FragmentTransaction transaction = getParentFragment()
            .getChildFragmentManager().beginTransaction();
    // Store the Fragment in the Fragment back-stack
    transaction.addToBackStack("StartScanFragment");
    transaction.replace(R.id.scanContainer, viewScanFragment, "ViewScanFragment").commit();
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);

    inflater.inflate(R.menu.start_scan_toolbar, menu);
  }
}