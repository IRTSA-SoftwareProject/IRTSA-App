package com.swinburne.irtsa.irtsa.gallery;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swinburne.irtsa.irtsa.R;
import com.swinburne.irtsa.irtsa.ToolbarSetter;
import com.swinburne.irtsa.irtsa.ViewPagerAdapter;
import com.swinburne.irtsa.irtsa.model.Scan;

import java.util.List;
import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryDetailFragment extends Fragment implements ToolbarSetter {
    private Scan scan;
    private ImageView mImage;
    private TextView mName;
    private TextView mDescription;


    public GalleryDetailFragment() { setHasOptionsMenu(true); }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            menu.findItem(R.id.settings).setVisible(false);
            menu.findItem(R.id.save).setVisible(false);
            menu.findItem(R.id.select).setVisible(false);
            menu.findItem(R.id.share).setVisible(true);
            menu.findItem(R.id.delete).setVisible(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            scan = bundle.getParcelable("Scan");
        }
        View v = inflater.inflate(R.layout.fragment_gallery_detail, container, false);
        initialiseUi(v, scan);
        return v;
    }

    private void initialiseUi(View v, Scan scan) {
        mImage = v.findViewById(R.id.galleryDetailImage);
        mImage.setImageBitmap(scan.image);
        mName = v.findViewById(R.id.galleryDetailName);
        mName.setText("Name: " + scan.name);
        mDescription = v.findViewById(R.id.galleryDetailDescription);
        mDescription.setText("Description: " + scan.description);

    }

    @Override
    public void setToolbar(Menu menu) {
        menu.findItem(R.id.settings).setVisible(false);
        menu.findItem(R.id.save).setVisible(false);
        menu.findItem(R.id.select).setVisible(false);
        menu.findItem(R.id.share).setVisible(true);
        menu.findItem(R.id.delete).setVisible(true);
    }
}
