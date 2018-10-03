package com.swinburne.irtsa.irtsa.scan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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

  private class StartScanMessage extends Message {
    StartScanMessage() {
      type = "processScan";
    }
  }

  private class ScanProgressMessage extends Message {
    class Body {
      int percent;
    }

    Body body;
  }

  private class ScanCompleteMessage extends Message {
    class Body {
      String base64EncodedString;
    }

    Body body;
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_scan_progress, container, false);
    scanProgressBar = v.findViewById(R.id.scanProgressBar);
    scanProgressText = v.findViewById(R.id.scanProgressText);

    Server.send(new StartScanMessage());
    Server.messages.castToType("scan_progress", ScanProgressMessage.class)
            .takeUntil(Server.messages.ofType("scan_complete"))
            .subscribe(message -> {
              Log.i("MESSAGE", "Message received");
              Log.i("MESSAGE_TYPE", message.type);
              Log.i("MESSAGE_PERCENT", Integer.toString(message.body.percent));
            });

    Server.messages.castToType("scan_complete", ScanCompleteMessage.class).observeOn(AndroidSchedulers.mainThread()).subscribe(message -> {
      //String imageEncodedToBase64 = message.body.base64EncodedString;
      // Image needs to be loaded into a Bitmap from the base64 encoded string here
      // The image should then be added to a Bundle and passed to ViewScanFragment

      ViewScanFragment viewScanFragment = new ViewScanFragment();
      FragmentTransaction transaction = getParentFragment()
              .getChildFragmentManager().beginTransaction();
      // Store the Fragment in the Fragment back-stack
      transaction.addToBackStack("ScanProgressFragment");
      transaction.replace(R.id.scanContainer, viewScanFragment, "ViewScanFragment").commit();
    });


    return v;

  }

}
