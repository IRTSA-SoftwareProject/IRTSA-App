package com.swinburne.irtsa.irtsa;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * The entry point of the application. This activity is always present when
 * the app is running. It contains components that hold the different fragments.
 */
public class MainActivity extends AppCompatActivity {
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
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
    }

    /**
     * Check if there are nested fragments on screen.
     * If there are nested fragments, display the previous fragment.
     * If there are no nested fragments, close the app.
     */
    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();

        boolean backStackPopped = false;

        if (mPager.getCurrentItem() == 0) {
            for (Fragment fragment : fm.getFragments()) {
                if (fragment.getTag() == "ViewScanFragment") {
                    fm.popBackStackImmediate();
                    backStackPopped = true;
                }
            }
        }

        if (!backStackPopped) {
            finish();
        }
    }
}
