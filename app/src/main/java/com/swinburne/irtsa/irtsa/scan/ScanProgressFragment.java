package com.swinburne.irtsa.irtsa.scan;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.swinburne.irtsa.irtsa.R;
import com.swinburne.irtsa.irtsa.server.Message;
import com.swinburne.irtsa.irtsa.server.Server;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScanProgressFragment extends Fragment {
  private ProgressBar scanProgressBar;
  private TextView scanProgressText;


  public ScanProgressFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_scan_progress, container, false);
    scanProgressBar = v.findViewById(R.id.scanProgressBar);
    scanProgressText = v.findViewById(R.id.scanProgressText);

    Server.messages.ofType("scan_progress").observeOn(AndroidSchedulers.mainThread()).subscribe(message -> {
      String progress = message.getBodyHash().get("percent").toString();
      int progressInt = (int) Double.parseDouble(progress);
      scanProgressBar.setProgress(progressInt);
      scanProgressText.setText("Scan Progress is: " + progressInt);
    });

    Server.messages.ofType("scan_complete").observeOn(AndroidSchedulers.mainThread()).subscribe(message -> {
      String filename = message.getBodyHash().get("filename").toString();

      scanProgressText.setText("Scan Completed: " + filename);
    });
    //    ViewScanFragment viewScanFragment = new ViewScanFragment();
//    FragmentTransaction transaction = getParentFragment()
//            .getChildFragmentManager().beginTransaction();
//    // Store the Fragment in the Fragment back-stack
//    transaction.addToBackStack("StartScanFragment");
//    transaction.replace(R.id.scanContainer, viewScanFragment, "ViewScanFragment").commit();

    Server.send(new Message("scan", new Object() {
      public String scanName = "scan_001.png";
    }));

    return v;

  }

}
