package com.swinburne.irtsa.irtsa.containers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swinburne.irtsa.irtsa.MainActivity;
import com.swinburne.irtsa.irtsa.R;
import com.swinburne.irtsa.irtsa.gallery.GalleryFragment;

/**
 * This Fragment's layout consists purely of an empty FrameLayout.
 * This Fragment serves as a container for the Gallery fragments.
 */
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

  /**
   * Immediately load the GalleryFragment once this container Fragment becomes visible.
   */
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) {
      GalleryFragment galleryFragment = new GalleryFragment();
      FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

      transaction.replace(R.id.galleryContainer, galleryFragment, "GalleryFragment").commit();
    }
  }
}
