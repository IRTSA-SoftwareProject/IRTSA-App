package com.swinburne.irtsa.irtsa;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

public class MainActivity extends AppCompatActivity {
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private TabLayout mTabLayout;

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
