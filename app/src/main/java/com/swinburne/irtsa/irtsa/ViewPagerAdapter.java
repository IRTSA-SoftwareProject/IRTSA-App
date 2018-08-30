package com.swinburne.irtsa.irtsa;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.swinburne.irtsa.irtsa.containers.GalleryContainerFragment;
import com.swinburne.irtsa.irtsa.containers.ScanContainerFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

  public ViewPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int position) {
    final Fragment result;
    switch (position) {
      case 0:
        // First fragment of the Scan tab
        result = new ScanContainerFragment();
        break;
      case 1:
        // Second fragment of the Gallery tab
        result = new GalleryContainerFragment();
        break;
      default:
        result = null;
        break;
    }
    return result;
  }

  @Override
  public int getCount() {
    return 2;
  }

  @Override
  public CharSequence getPageTitle(final int position) {
    switch (position) {
      case 0:
        return "Scan";
      case 1:
        return "Gallery";
      default:
        return null;
    }
  }

}