package com.swinburne.irtsa.irtsa.scan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.swinburne.irtsa.irtsa.MainActivity;
import com.swinburne.irtsa.irtsa.R;
import com.swinburne.irtsa.irtsa.server.Message;
import com.swinburne.irtsa.irtsa.server.Server;
import com.swinburne.irtsa.irtsa.server.Status;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Fragment where a scan can be started.
 * This fragment provides the user with inputs that allow them to select a processing technique,
 * png directory on the micro-controller and frame range.
 * The list of directories on the micro-controller is populated once a connection to the
 * micro-controller has been established.
 */
public class StartScanFragment extends Fragment {
  Button startScanButton;
  CheckBox allCheckbox;
  EditText beginFrameRangeEditText;
  EditText endFrameRangeEditText;
  Spinner pngPathSpinner;
  Spinner processingTechniqueSpinner;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View rootView = inflater.inflate(R.layout.fragment_start_scan, container, false);

    initialiseUi(rootView);

    if (savedInstanceState != null) {
      String previousFragment = ((MainActivity) getActivity()).getPreviouslyFocusedFragment();
      setHasOptionsMenu(previousFragment.equals(getClass().getCanonicalName()));
    } else {
      setHasOptionsMenu(true);
    }

    return rootView;
  }

  /**
   * Initialise the Button and set an OnClickListener.
   *
   * @param rootView The StartScanFragment's top level View
   */
  private void initialiseUi(View rootView) {
    startScanButton = rootView.findViewById(R.id.startScanButton);
    allCheckbox = rootView.findViewById(R.id.allCheckBox);
    beginFrameRangeEditText = rootView.findViewById(R.id.beginFrameRangeEditText);
    endFrameRangeEditText = rootView.findViewById(R.id.endFrameRangeEditText);
    pngPathSpinner = rootView.findViewById(R.id.pngPathSpinner);
    processingTechniqueSpinner = rootView.findViewById(R.id.processingTechniqueSpinner);

    // Set the path spinner to disabled and to have the text 'Retrieving Directories'
    pngPathSpinner.setAdapter(new ArrayAdapter<>(getActivity(),
            android.R.layout.simple_spinner_item, new String[]{"Retrieving directories"}));
    pngPathSpinner.setAlpha((float) 0.7);
    pngPathSpinner.setEnabled(false);
    startScanButton.setEnabled(false);

    // Register beginScan as the method to execute when the start scan button is pressed.
    startScanButton.setOnClickListener(view -> beginScan());

    // Set the text for the technique spinner from strings.xml
    ArrayAdapter<CharSequence> processingTechniqueSpinnerAdapter = ArrayAdapter.createFromResource(
            getContext(),
            R.array.processing_techniques,
            android.R.layout.simple_spinner_dropdown_item
    );
    processingTechniqueSpinner.setAdapter(processingTechniqueSpinnerAdapter);

    // Check to see if the start scan button should be enabled/disabled if the checkbox is checked.
    allCheckbox.setOnCheckedChangeListener((button, isChecked) -> {
      if (Server.getStatus() == Status.CONNECTED && isChecked) {
        startScanButton.setEnabled(true);
      } else {
        startScanButton.setEnabled(false);
      }
      beginFrameRangeEditText.setEnabled(!isChecked);
      endFrameRangeEditText.setEnabled(!isChecked);
      beginFrameRangeEditText.setText("");
      endFrameRangeEditText.setText("");
    });

    // Set listeners to control if the Start Scan button should be enabled.
    beginFrameRangeEditText.setOnKeyListener(new InputRangeEnteredListener());
    endFrameRangeEditText.setOnKeyListener(new InputRangeEnteredListener());

    // Observe the status of the server connection
    Server.status.observeOn(AndroidSchedulers.mainThread())
            .takeWhile(event -> getActivity() != null)
            .subscribe(connectionStatus -> {
              boolean isConnected = connectionStatus.compareTo(Status.CONNECTED) == 0;

              // If connected to the server, get the list of available directories.
              if (isConnected) {
                Server.send(new GetDirectoriesMessage());
              }

              // Determine if the start scan button should be enabled.
              if (isConnected && allCheckbox.isChecked()) {
                startScanButton.setEnabled(true);
              } else {
                startScanButton.setEnabled(!beginFrameRangeEditText.getText().toString().equals("")
                        && !endFrameRangeEditText.getText().toString().equals(""));
              }

            });

    // Listen for a message from the server containing the list of available directories..
    Server.messages.castToType("simulationList", PngDirectoriesMessage.class)
            .takeWhile(event -> getActivity() != null)
            .observeOn(AndroidSchedulers.mainThread()).subscribe(message -> {
              // Set the spinner to use this list of directories.
              ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(),
                  android.R.layout.simple_spinner_item, message.body.directories);
              spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
              pngPathSpinner.setAdapter(spinnerAdapter);
              pngPathSpinner.setEnabled(true);
              pngPathSpinner.setAlpha(1); // Style the spinner so it looks enabled.
            });
  }

  /**
   * Replace the Fragment in the scanContainer with a new ViewScanFragment.
   * Add the StartScanFragment to the back-stack so it can be restored if
   * the user presses the back button.
   * Gather all the parameters that are to be used for the scan and pass these in a bundle to
   * the ScanProgressFragment so they may be sent to the server.
   */
  private void beginScan() {
    // Fill a bundle with the necessary scan parameters.
    Bundle parametersToPass = new Bundle();
    String processingTechnique = processingTechniqueSpinner.getSelectedItem().toString();
    parametersToPass.putString("processingTechnique", processingTechnique);
    parametersToPass.putString("pngPath", pngPathSpinner.getSelectedItem().toString());

    // Send -1 as the frame start and end range if the user wants to process all frames.
    if (allCheckbox.isChecked()) {
      parametersToPass.putInt("framesToProcess", -1);
      parametersToPass.putInt("frameStart", -1);
    } else {
      parametersToPass.putInt("framesToProcess",
              Integer.parseInt(beginFrameRangeEditText.getText().toString()));

      parametersToPass.putInt("frameStart",
              Integer.parseInt(endFrameRangeEditText.getText().toString()));
    }

    ScanProgressFragment scanProgressFragment = new ScanProgressFragment();
    FragmentTransaction transaction = getParentFragment()
            .getChildFragmentManager().beginTransaction();
    // Store the Fragment in the Fragment back-stack
    transaction.addToBackStack("StartScanFragment");
    scanProgressFragment.setArguments(parametersToPass);
    transaction.replace(R.id.scanContainer, scanProgressFragment, "ScanProgressFragment").commit();
  }

  /**
   * The message sent to the server which requests a listing of its available png directories.
   */
  private class GetDirectoriesMessage extends Message {
    GetDirectoriesMessage() {
      type = "getPngDir";
    }
  }

  /**
   * Represents a message received from the server containing a list of available directories.
   */
  private class PngDirectoriesMessage extends Message {

    class Body {
      String[] directories;
    }

    Body body;
  }

  /**
   * A listener used to control the enabled status of the start scan button when text is input
   * into the range EditTexts.
   */
  private class InputRangeEnteredListener implements View.OnKeyListener {
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
      if (!beginFrameRangeEditText.getText().toString().equals("")
              && !endFrameRangeEditText.getText().toString().equals("")
              && Server.getStatus() == Status.CONNECTED) {
        startScanButton.setEnabled(true);
      } else {
        startScanButton.setEnabled(false);
      }
      return false;
    }
  }
}