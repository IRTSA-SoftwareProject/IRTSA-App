package com.swinburne.irtsa.irtsa;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
  private ViewPager pager;
  private PagerAdapter pagerAdapter;
  private TabLayout tabLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    tabLayout = findViewById(R.id.tabLayout);
    pager = findViewById(R.id.viewPager);

    pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    pager.setAdapter(pagerAdapter);
    tabLayout.setupWithViewPager(pager);
  }

  @Override
  public void onBackPressed() {
    FragmentManager fm = getSupportFragmentManager();

    boolean backStackPopped = false;

    if (pager.getCurrentItem() == 0) {
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
