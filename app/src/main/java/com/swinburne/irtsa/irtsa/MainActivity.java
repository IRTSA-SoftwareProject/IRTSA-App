package com.swinburne.irtsa.irtsa;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.swinburne.irtsa.irtsa.gallery.GalleryFragment;
import com.swinburne.irtsa.irtsa.scan.ViewScanFragment;
import com.swinburne.irtsa.irtsa.server.Server;
import com.swinburne.irtsa.irtsa.server.Status;

import io.reactivex.android.schedulers.AndroidSchedulers;

import java.util.List;

/**
 * The entry point of the application. This activity is always present when
 * the app is running. It contains components that hold the different fragments.
 */
public class MainActivity extends AppCompatActivity {
  private ViewPager pager;
  private ViewPagerAdapter pagerAdapter;
  private TabLayout tabLayout;
  private String previouslyFocusedFragment;

  CountingIdlingResource connectionIdleResource = new CountingIdlingResource("CONNECTING_TO_PI");
  CountingIdlingResource progressIdleResource = new CountingIdlingResource("SCAN_PROGRESS");

  public String getPreviouslyFocusedFragment() {
    return previouslyFocusedFragment;
  }

  /**
   * When the activity is created initialise the tabs and view pager.
   * Also register listeners and connect to the server if not already connected.
   * @param savedInstanceState Saved representation of the activity state.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setTheme(R.style.AppThemeLight);
    super.onCreate(savedInstanceState);
    requestLocalStoragePermission();

    if (savedInstanceState != null) {
      previouslyFocusedFragment = savedInstanceState.getString("previouslyFocusedFragment");
    }
    setContentView(R.layout.activity_main);

    tabLayout = findViewById(R.id.tabLayout);
    pager = findViewById(R.id.viewPager);

    // Setup the PagerAdapter that will contain the different Fragment's of this application.
    pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    pager.setAdapter(pagerAdapter);
    tabLayout.setupWithViewPager(pager);
    UpdateToolbarListener toolbarListener = new UpdateToolbarListener();
    pager.addOnPageChangeListener(toolbarListener);

    // Observer the server's connection status and set the title bar of the app accordingly.
    Server.status.observeOn(AndroidSchedulers.mainThread()).subscribe(connectionStatus -> {
      /**
       * If condition to allow tests to wait for the connection
       * to be established before running.
       */
      if (connectionStatus == Status.CONNECTED && !connectionIdleResource.isIdleNow()) {
        connectionIdleResource.decrement();
      } else if (connectionIdleResource.isIdleNow()) {
        connectionIdleResource.increment();
      }
      setTitle(connectionStatus.toString());
    });

    /**
     * Listen for the scan_complete message type to run
     * View Scan Fragment tests.
     */
    Server.messages.observeOn(AndroidSchedulers.mainThread()).subscribe(message -> {
      if (message.type == "scan_complete") {
        progressIdleResource.decrement();
      } else if (progressIdleResource.isIdleNow()) {
        progressIdleResource.increment();
      }
    });

    // Listen for a tap on the tab bar or swipe on pager.
    pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      // Refresh the GalleryFragment if the user swipes to the gallery.
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

    // Set the bottom navigation's tab icons.
    tabLayout.getTabAt(0).setIcon(R.drawable.ic_scan);
    tabLayout.getTabAt(1).setIcon(R.drawable.ic_gallery);

    // Start attempting to connect to the server
    if (Server.getStatus() == Status.NOT_CONNECTED) {
      Server.connect();
    }
  }

  /**
   * Iterate over each of the nested fragments in the viewpager container fragments.
   * Invalidate the options menu for each fragment, unless the fragment is the currently visible
   * fragment.
   *
   * @param position position of the currently selected view pager tab.
   */
  public void invalidateFragmentMenus(int position) {
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
      Fragment visibleFragment = pagerAdapter.getCurrentlyVisibleFragment(pager.getCurrentItem());

      // The ScanProgressFragment is not added to the backstack, therefore popping the backstack
      // when on the ViewScanFragment requires that we manually remove the ViewScanFragment from
      // the container's child fragment manager.
      if (visibleFragment.getClass() == ViewScanFragment.class) {
        FragmentTransaction transaction = pagerAdapter.getFragmentMap().get(pager.getCurrentItem())
                .getChildFragmentManager().beginTransaction();
        transaction.remove(visibleFragment).commit();
      }
      pagerAdapter.getFragmentMap().get(pager.getCurrentItem())
              .getChildFragmentManager().popBackStack();
    }

    // Update the toolbar.
    invalidateFragmentMenus(pager.getCurrentItem());
  }

  /**
   * View pager page change listener that invalidates all non visible fragment toolbar menus
   * when the view pager is swiped. Multiple Fragment's are technically all on screen at once,
   * causing multiple toolabar menus to be rendered, this prevents this.
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

  /**
   * Save the name of the currently visible fragment so it can be compared if the app must be
   * recreated.
   *
   * @param outState The bundle that will store data when the activity is destroyed.
   */
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Fragment currentFragment = pagerAdapter.getCurrentlyVisibleFragment(pager.getCurrentItem());
    String canonicalName = currentFragment.getClass().getCanonicalName();
    outState.putString("previouslyFocusedFragment", canonicalName);
  }

  /**
   * Request local storage permissions if the app does not have them.
   * This is required so images can be saved to public external storage on the users device.
   * Uses the AppCompat library for backwards compatibility.
   */
  private void requestLocalStoragePermission() {
    String locationPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    int hasPermission = ContextCompat.checkSelfPermission(getApplicationContext(),
            locationPermission);
    String[] permissions = new String[]{locationPermission};
    if (hasPermission != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, permissions, 1);
    }
  }
}
