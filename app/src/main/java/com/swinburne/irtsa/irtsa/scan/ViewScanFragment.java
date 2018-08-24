package com.swinburne.irtsa.irtsa.scan;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.swinburne.irtsa.irtsa.R;

public class ViewScanFragment extends Fragment {
    private ImageView mScanImage;
    private MenuItem mSave;

    public ViewScanFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_save, menu);
        menu.findItem(R.id.save).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //Call the openSaveDialog when the save icon is selected
        if (id == R.id.save) {
            openSaveDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    public void openSaveDialog() {
        SaveDialog saveDialog = new SaveDialog();

        saveDialog.show(getFragmentManager(), "Save Dialog");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_scan, container, false);

        initialiseUI(v);

        return v;
    }

    private void initialiseUI(View v) {
        mScanImage = v.findViewById(R.id.scanImage);
        mScanImage.setImageResource(R.drawable.phase);
    }

}