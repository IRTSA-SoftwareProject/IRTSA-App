package com.swinburne.irtsa.irtsa.gallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swinburne.irtsa.irtsa.R;
import com.swinburne.irtsa.irtsa.model.Scan;

import java.util.ArrayList;

/**
 * Fragment that displays the saved scan gallery.
 */
public class GalleryFragment extends Fragment {
    private RecyclerView recyclerView;
    private GalleryAdapter mAdapter;

    public GalleryFragment() {
        // Required empty public constructor.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_gallery, container, false);

        initialiseUI(v);

        return v;
    }

    /**
     * Initialise GalleryAdapter and RecyclerView
     *
     * @param v The inflated root View of this Fragment.
     */
    private void initialiseUI(View v) {
        mAdapter = new GalleryAdapter(getActivity().getApplicationContext());

        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(mAdapter);
    }


}
