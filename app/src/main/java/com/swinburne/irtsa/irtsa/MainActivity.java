package com.swinburne.irtsa.irtsa;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.swinburne.irtsa.irtsa.gallery.GalleryFragment;

import java.util.List;

/**
 * The entry point of the application. This activity is always present when
 * the app is running. It contains components that hold the different fragments.
 */
public class MainActivity extends AppCompatActivity {
    private ViewPager mPager;
    private ViewPagerAdapter mPagerAdapter;
    private TabLayout mTabLayout;

    /**
     * When the activity is created initialise the tabs and view pager.
     *
     * @param savedInstanceState Saved representation of the activity state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTabLayout = findViewById(R.id.tabLayout);
        mPager = findViewById(R.id.viewPager);

        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mPager);

        // Listen for a tap on the tab bar or swipe on pager.
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // If the gallery fragment is navigated to, refresh the gallery.
                if (position == 1) {
                   List<Fragment> childFragments = mPagerAdapter.getNestedFragments(1);
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
      
        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_scan);
        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_gallery);
    }

    /**
     * Check if there are nested fragments on screen.
     * If there are nested fragments, display the previous fragment (pop back stack).
     * If there are no nested fragments, close the app.
     */
    @Override
    public void onBackPressed() {
        List<Fragment> currentChildFragments = mPagerAdapter.getNestedFragments(mPager.getCurrentItem());

        if (currentChildFragments.size() == 1) {
            finish();
        } else {
            mPagerAdapter.getItem(mPager.getCurrentItem()).getChildFragmentManager().popBackStackImmediate();
        }
    }
}
