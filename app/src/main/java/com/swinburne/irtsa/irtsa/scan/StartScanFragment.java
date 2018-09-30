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

import com.swinburne.irtsa.irtsa.R;
import com.swinburne.irtsa.irtsa.server.Message;
import com.swinburne.irtsa.irtsa.server.Server;
import com.swinburne.irtsa.irtsa.server.Status;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Fragment with a button that begins a scan.
 */
public class StartScanFragment extends Fragment {
  public StartScanFragment() {
    setHasOptionsMenu(true);
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View rootView = inflater.inflate(R.layout.fragment_start_scan, container, false);

    initialiseUi(rootView);

    return rootView;
  }

  /**   * Initialise the Button and set an OnClickListener.
   *
   * @param rootView The StartScanFragment's top level View
   */
  private void initialiseUi(View rootView) {
    Button startScanButton = rootView.findViewById(R.id.startScanButton);
    startScanButton.setOnClickListener(a -> beginScan());

    Server.status.observeOn(AndroidSchedulers.mainThread()).subscribe(connectionStatus ->
      startScanButton.setEnabled(connectionStatus.compareTo(Status.CONNECTED) == 0));

  }

  /**
   * Replace the Fragment in the scanContainer with a new ViewScanFragment.
   * Add the StartScanFragment to the back-stack so it can be restored if
   * the user presses the back button.
   */
  private void beginScan() {

//    Server.messages.ofType("scan_progress").subscribe(message -> {
//      Log.i("MESSAGE", "Message received");
//      Log.i("MESSAGE_TYPE", message.type);
//      Log.i("MESSAGE_BODY", message.body.toString());
//      Log.i("MESSAGE_PERCENT", message.getBodyHash().get("percent").toString());
//    });

    ScanProgressFragment scanProgressFragment = new ScanProgressFragment();
    FragmentTransaction transaction = getParentFragment()
            .getChildFragmentManager().beginTransaction();
    transaction.addToBackStack("StartScanFragment");
    transaction.replace(R.id.scanContainer, scanProgressFragment, "ScanProgressFragment").commit();


  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);

    inflater.inflate(R.menu.start_scan_toolbar, menu);
  }
}