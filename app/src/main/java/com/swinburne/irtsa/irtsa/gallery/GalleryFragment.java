package com.swinburne.irtsa.irtsa.gallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swinburne.irtsa.irtsa.R;

/**
 * Fragment that displays the saved scan gallery.
 */
public class GalleryFragment extends Fragment {
  private RecyclerView recyclerView;
  private GalleryAdapter adapter;

  public GalleryFragment() {
    // Required empty public constructor.
  }

  public void refreshGallery() {
    adapter.refreshScans();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment

    View v = inflater.inflate(R.layout.fragment_gallery, container, false);

    initialiseUi(v);

    return v;
  }

  private void initialiseUi(View v) {
    adapter = new GalleryAdapter(getActivity().getApplicationContext());

    recyclerView = v.findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
    recyclerView.setAdapter(adapter);
  }
}
