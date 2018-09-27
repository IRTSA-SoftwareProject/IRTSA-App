package com.swinburne.irtsa.irtsa;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

import com.swinburne.irtsa.irtsa.gallery.GalleryFragment;
import com.swinburne.irtsa.irtsa.server.Server;

import java.util.List;

/**
 * The entry point of the application. This activity is always present when
 * the app is running. It contains components that hold the different fragments.
 */
public class MainActivity extends AppCompatActivity {
  private ViewPager pager;
  private ViewPagerAdapter pagerAdapter;
  private TabLayout tabLayout;

  /**
   * When the activity is created initialise the tabs and view pager.
   *
   * @param savedInstanceState Saved representation of the activity state.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setTheme(R.style.AppTheme);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    tabLayout = findViewById(R.id.tabLayout);
    pager = findViewById(R.id.viewPager);

    pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    pager.setAdapter(pagerAdapter);
    tabLayout.setupWithViewPager(pager);
    UpdateToolbarListener toolbarListener = new UpdateToolbarListener();
    pager.addOnPageChangeListener(toolbarListener);

    // Listen for a tap on the tab bar or swipe on pager.
    pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        // If the gallery fragment is navigated to, refresh the gallery.
        if (position == 1) {
          List<Fragment> childFragments = pagerAdapter.getNestedFragments(1);
          for (Fragment fragment : childFragments) {
            if (fragment.getTag() == "GalleryFragment") {
              ((GalleryFragment)fragment).refreshGallery();
            }
          }
        }
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });

    tabLayout.getTabAt(0).setIcon(R.drawable.ic_scan);
    tabLayout.getTabAt(1).setIcon(R.drawable.ic_gallery);

    // Start attempting to connect to the server
    Server.connect();
  }

  /**
   * Iterate over each of the nested fragments in the viewpager container fragments.
   * Invalidate the options menu for each fragment, unless the fragment is the currently visible
   * fragment.
   *
   * @param position position of the currently selected view pager tab.
   */
  private void invalidateFragmentMenus(int position) {
    Fragment visibleFragment = pagerAdapter.getCurrentlyVisibleFragment(position);
    for (int i = 0; i < pagerAdapter.getCount(); i++) {
      for (Fragment fragment : pagerAdapter.getNestedFragments(i)) {
        fragment.setHasOptionsMenu(fragment == visibleFragment);
      }
    }
    invalidateOptionsMenu();
  }

  /**
   * Check if there are nested fragments on screen.
   * If there are nested fragments, display the previous fragment (pop back stack).
   * If there are no nested fragments, close the app.
   */
  @Override
  public void onBackPressed() {
    List<Fragment> currentChildFragments = pagerAdapter.getNestedFragments(pager.getCurrentItem());

    if (currentChildFragments.size() == 1) {
      finish();
    } else {
      pagerAdapter.getItem(pager.getCurrentItem())
              .getChildFragmentManager().popBackStackImmediate();
    }

    // Update the toolbar.
    invalidateFragmentMenus(pager.getCurrentItem());
  }

  /**
   * View pager page change listener that invalidates all non visible fragment toolbar menus
   * when the view pager is swiped.
   */
  private class UpdateToolbarListener implements ViewPager.OnPageChangeListener {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
      invalidateFragmentMenus(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
  }
}
