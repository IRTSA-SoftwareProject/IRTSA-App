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
 * A Fragment that displays a progress bar and text as a scan is happening.
 * This Fragment begins a scan using Scan parameters passed from the StartScanFragment.
 */
public class ScanProgressFragment extends Fragment {
  private ProgressBar scanProgressBar;
  private TextView scanProgressText;

  /**
   * The message sent to the micro-controller that is used to begin a scan.
   * Its variables are parameters that will be passed to the processScan event on the server side.
   */
  private class StartScanMessage extends Message {
    String pngPath; // The folder of pngs to process on the Pi
    String processingTechnique; // The processing technique to apply to the images.
    int framesToProcess; // The end range of frames to process. -1 to process all.
    int frameStart; // The beginning range of frames to process. -1 to process all.

    StartScanMessage() {
      type = "processScan";
    }
  }

  /**
   * A class to represent a progress event received from the server whilst a scan is in progress.
   */
  private class ScanProgressMessage extends Message {
    class Body {
      int percent; // Percentage representing scan completion.
    }

    Body body;
  }

  /**
   * A class to represent the data received from the server upon completion of a scan.
   * The body contains a Base 64 encoded string representing the processed image.
   */
  private class ScanCompleteMessage extends Message {
    class Body {
      String base64EncodedString;
    }

    Body body;
  }

  /**
   * Initialise the view for this Fragment containing a progress bar and text.
   * Also sends the start scan command and registers as an observer of progress and completion
   * messages received from the server.
   * @param inflater Layout inflater to inflate fragment layout.
   * @param container Container layout for this fragment..
   * @param savedInstanceState The saved instance state of this fragment.
   * @return The view for this fragment.
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_scan_progress, container, false);
    initView(v);
    Bundle userSelectedParameters = this.getArguments();

    StartScanMessage startScanMessage = new StartScanMessage();
    startScanMessage.pngPath = userSelectedParameters.getString("pngPath");
    startScanMessage.processingTechnique = userSelectedParameters.getString("processingTechnique");
    startScanMessage.framesToProcess = userSelectedParameters.getInt("framesToProcess");
    startScanMessage.frameStart = userSelectedParameters.getInt("frameStart");
    Server.send(startScanMessage);

    // Listen for scan progress messages and set the scan progress bar and text accordingly.
    Server.messages.castToType("scan_progress", ScanProgressMessage.class)
        .takeUntil(Server.messages.ofType("scan_complete"))
        .takeWhile(event -> getActivity() != null)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(message -> {
          Log.i("MESSAGE", "Message received");
          Log.i("MESSAGE_TYPE", message.type);
          Log.i("MESSAGE_PERCENT", Integer.toString(message.body.percent));
          scanProgressBar.setProgress(message.body.percent);
          scanProgressText.setText("Scan Progress is: " + message.body.percent + "%");
        });

    // Listen for a scan complete message so we can extract its
    // image and pass it to the ViewScanFragment
    Server.messages.castToType("scan_complete", ScanCompleteMessage.class)
        .observeOn(AndroidSchedulers.mainThread())
        .takeWhile(event -> getActivity() != null)
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

  /**
   * Bind our UI Components to objects in the controller and initialise them.
   * @param v The root view for this Fragment.
   */
  private void initView(View v) {
    scanProgressBar = v.findViewById(R.id.scanProgressBar);
    scanProgressText = v.findViewById(R.id.scanProgressText);
    scanProgressText.setText("Scan Progress is: 0%");
  }

}
