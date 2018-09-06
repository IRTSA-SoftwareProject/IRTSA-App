package com.swinburne.irtsa.irtsa;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.swinburne.irtsa.irtsa.gallery.GalleryFragment;

import java.util.List;

/**
 * The entry point of the application. This activity is always present when
 * the app is running. It contains components that hold the different fragments.
 */
public class MainActivity extends AppCompatActivity {
  private ViewPager pager;
  private ViewPagerAdapter pagerAdapter;
  private TabLayout tabLayout;
  private Menu menu;

  /**
   * When the activity is created initialise the tabs and view pager.
   *
   * @param savedInstanceState Saved representation of the activity state.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    tabLayout = findViewById(R.id.tabLayout);
    pager = findViewById(R.id.viewPager);

    pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    pager.setAdapter(pagerAdapter);
    tabLayout.setupWithViewPager(pager);

    // Listen for a tap on the tab bar or swipe on pager.
    pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        Fragment selectedFragment = pagerAdapter.getCurrentlyVisibleFragment(position);
          ((ToolbarSetter)selectedFragment).setToolbar(menu);

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
  }

  /**
   * Called when the toolbar (menu) is created and set visible icons
   * for the default screen that the app launches on.
   *
   * @param menu Menu View to contain the inflated menu.
   * @return true that the menu is created
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.toolbar, menu);
    menu.findItem(R.id.settings).setVisible(true);
    menu.findItem(R.id.save).setVisible(false);
    menu.findItem(R.id.select).setVisible(false);
    menu.findItem(R.id.share).setVisible(false);
    menu.findItem(R.id.delete).setVisible(false);
    this.menu = menu;
    return true;
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
  }
}
