package com.swinburne.irtsa.irtsa;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.swinburne.irtsa.irtsa.containers.GalleryContainerFragment;
import com.swinburne.irtsa.irtsa.containers.ScanContainerFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This adapter specifies the two container Fragments the ViewPager will display.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
  private Map<Integer, Fragment> fragmentMap;

  /**
  * Constructor that calls the superclass.
  *
  * @param fm FragmentManager required to initialise the superclass.
  */
  public ViewPagerAdapter(FragmentManager fm) {
    super(fm);
    fragmentMap = new HashMap<>();
  }

  /**
   * Runs once at runtime to initialise the ViewPager.
   * Specifies the Fragment to display at each ViewPager position.
   *
   * @param position The position (tab) of the ViewPager.
   * @return The fragment to be displayed at the specified position.
   */
  @Override
  public Fragment getItem(int position) {
    final Fragment result;

    if (fragmentMap.containsKey(position)) {
      return fragmentMap.get(position);
    } else {
      switch (position) {
        case 0:
          result = new ScanContainerFragment();
          break;
        case 1:
          result = new GalleryContainerFragment();
          break;
        default:
          result = null;
          break;
      }
    }
    fragmentMap.put(position, result);
    return result;
  }

  /**
   * Returns the amount of ViewPager tabs.
   *
   * @return The amount of ViewPager tabs.
   */
  @Override
  public int getCount() {
    return 2;
  }

  /**
   * Specifies tab titles displayed in the TabLayout.
   *
   * @param position The position (tab) of the ViewPager.
   * @return The name of the tab.
   */
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

  /**
   * Returns a List of all fragments in the container.
   * This includes the currently visible fragment in the container and any fragments in backstack.
   * The container Fragment is not returned in this list.
   *
   * @param position ViewPager position (to reference container Fragment displayed by ViewPager).
   * @return A List containing the currently visible fragment, and any fragments on the backstack.
   */
  public List<Fragment> getNestedFragments(int position) {
    Fragment fragmentContainer = fragmentMap.get(position);
    FragmentManager containerChildFm = fragmentContainer.getChildFragmentManager();
    List<Fragment> nestedFragments = new ArrayList<>();

    // Add any fragments not added to the backstack (currently visible fragment in container)
    for (Fragment fragment : containerChildFm.getFragments()) {
      nestedFragments.add(fragment);
    }

    // Add any fragments present in the container's child fragment manager backstack.
    for (int i = 0; i < containerChildFm.getBackStackEntryCount(); i++) {
      String name = containerChildFm.getBackStackEntryAt(i).getName();
      nestedFragments.add(containerChildFm.findFragmentByTag(name));
      System.out.println("test");
    }

    return nestedFragments;
  }

  /**
   * Returns the current Fragment being displayed at the supplied ViewPager position
   * Use getTag() on the returned fragment to determine its type and cast if required.
   *
   * @param position ViewPager position (to reference container Fragment displayed by ViewPager).
   * @return The currently displayed fragment
   */
  public Fragment getCurrentlyVisibleFragment(int position) {
    Fragment fragmentContainer = fragmentMap.get(position);
    FragmentManager containerChildFm = fragmentContainer.getChildFragmentManager();

    // The container should only contain one fragment (the rest will be on the backstack).
    for (Fragment fragment : containerChildFm.getFragments()) {
      System.out.println(fragment.getTag());
      return fragment;
    }

    return null;
  }
}