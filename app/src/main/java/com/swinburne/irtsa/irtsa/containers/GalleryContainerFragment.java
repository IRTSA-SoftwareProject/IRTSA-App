package com.swinburne.irtsa.irtsa.containers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swinburne.irtsa.irtsa.R;
import com.swinburne.irtsa.irtsa.gallery.GalleryFragment;

public class GalleryContainerFragment extends Fragment {


  public GalleryContainerFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_gallery_container, container, false);
  }

  @Override
  public void onStart() {
    super.onStart();
    GalleryFragment galleryFragment = new GalleryFragment();
    FragmentTransaction transaction = getFragmentManager().beginTransaction();

    transaction.replace(R.id.galleryContainer, galleryFragment, "GalleryFragment").commit();

  }

}
