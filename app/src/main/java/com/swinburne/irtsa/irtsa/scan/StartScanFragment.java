package com.swinburne.irtsa.irtsa.scan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.swinburne.irtsa.irtsa.server.Server;
import com.swinburne.irtsa.irtsa.server.Status;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Fragment with a button that begins a scan.
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

    ArrayAdapter<CharSequence> pngPathSpinnerAdapter = ArrayAdapter.createFromResource(
        getContext(),
        R.array.image_png_paths,
        android.R.layout.simple_spinner_dropdown_item
    );
    pngPathSpinner.setAdapter(pngPathSpinnerAdapter);
    pngPathSpinner.setEnabled(false);

    ArrayAdapter<CharSequence> processingTechniqueSpinnerAdapter = ArrayAdapter.createFromResource(
        getContext(),
        R.array.processing_techniques,
        android.R.layout.simple_spinner_dropdown_item
    );
    processingTechniqueSpinner.setAdapter(processingTechniqueSpinnerAdapter);

    allCheckbox.setOnCheckedChangeListener((button, isChecked) -> {
      if (Server.getStatus() == Status.CONNECTED && isChecked) {
        startScanButton.setEnabled(true);
      } else {
        startScanButton.setEnabled(false);
      }
      beginFrameRangeEditText.setEnabled(!isChecked);
      beginFrameRangeEditText.setText("");
    });

    beginFrameRangeEditText.setOnKeyListener((view, keyEvent, eventId) -> {
      if (!beginFrameRangeEditText.getText().toString().equals("")
          && Server.getStatus() == Status.CONNECTED) {
        startScanButton.setEnabled(true);
      } else {
        startScanButton.setEnabled(false);
      }
      return false;
    });

    startScanButton.setOnClickListener(view -> beginScan());
    Server.status.observeOn(AndroidSchedulers.mainThread())
        .subscribe(connectionStatus -> {
          boolean isConnected = connectionStatus.compareTo(Status.CONNECTED) == 0;
          startScanButton.setEnabled(isConnected
              && beginFrameRangeEditText.getText().toString() != "" || allCheckbox.isChecked());
        });
  }

  /**
   * Replace the Fragment in the scanContainer with a new ViewScanFragment.
   * Add the StartScanFragment to the back-stack so it can be restored if
   * the user presses the back button.
   */
  private void beginScan() {
    Bundle parametersToPass = new Bundle();
    String processingTechnique = processingTechniqueSpinner.getSelectedItem().toString();
    parametersToPass.putString("processingTechnique", processingTechnique);
    parametersToPass.putString("pngPath", pngPathSpinner.getSelectedItem().toString());

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
}