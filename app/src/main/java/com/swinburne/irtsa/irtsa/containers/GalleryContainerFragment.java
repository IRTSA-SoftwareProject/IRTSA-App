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
 * This Fragment serves as a container for the Gallery fragments and is added to the second
 * tab of the ViewPager when the app is started.
 */
public class GalleryContainerFragment extends Fragment {
  /**
   * Inflate the layout for this view.
   * As this is a container, the layout consists of a single FrameLayout that is used to contain
   * different fragments.
   * @param inflater Instance of the layout inflater.
   * @param container The root view
   * @param savedInstanceState Saved state of the fragment
   * @return The view for this fragment.
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment..
    return inflater.inflate(R.layout.fragment_gallery_container, container, false);
  }

  /**
   * Load the Gallery fragment when this fragment is created.
   */
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Don't replace the container's contents if this fragment is being recreated.
    if (savedInstanceState == null) {
      GalleryFragment galleryFragment = new GalleryFragment();
      FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

      transaction.replace(R.id.galleryContainer, galleryFragment, "GalleryFragment").commit();
    }
  }
}
