package com.swinburne.irtsa.irtsa.scan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
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
    String pngPath;
    String processingTechnique;
    int framesToProcess;
    int frameStart;

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
    scanProgressText.setText("Scan Progress is: 0%");
    Bundle userSelectedParameters = this.getArguments();

    StartScanMessage startScanMessage = new StartScanMessage();
    startScanMessage.pngPath = userSelectedParameters.getString("pngPath");
    startScanMessage.processingTechnique = userSelectedParameters.getString("processingTechnique");
    startScanMessage.framesToProcess = userSelectedParameters.getInt("framesToProcess");
    startScanMessage.frameStart = userSelectedParameters.getInt("frameStart");
    Server.send(startScanMessage);

    Server.messages.castToType("scan_progress", ScanProgressMessage.class)
        .takeUntil(Server.messages.ofType("scan_complete"))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(message -> {
          Log.i("MESSAGE", "Message received");
          Log.i("MESSAGE_TYPE", message.type);
          Log.i("MESSAGE_PERCENT", Integer.toString(message.body.percent));
          scanProgressBar.setProgress(message.body.percent);
          scanProgressText.setText("Scan Progress is: " + message.body.percent + "%");
        });

    Server.messages.castToType("scan_complete", ScanCompleteMessage.class)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(message -> {
          String imageEncodedToBase64 = message.body.base64EncodedString;
          byte[] decodedImage = Base64.decode(imageEncodedToBase64, Base64.DEFAULT);
          Bundle bundle = new Bundle();
          bundle.putByteArray("scanByteArray", decodedImage);

          ViewScanFragment viewScanFragment = new ViewScanFragment();
          viewScanFragment.setArguments(bundle);
          FragmentTransaction transaction = getParentFragment()

                  .getChildFragmentManager().beginTransaction();
          transaction.replace(R.id.scanContainer, viewScanFragment, "ViewScanFragment").commit();
        });


    return v;

  }

}
