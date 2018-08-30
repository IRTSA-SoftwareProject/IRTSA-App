package com.swinburne.irtsa.irtsa;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.swinburne.irtsa.irtsa.containers.GalleryContainerFragment;
import com.swinburne.irtsa.irtsa.containers.ScanContainerFragment;

/**
 * This adapter specifies the two container Fragments the ViewPager will display.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    /**
     * Constructor that calls the superclass.
     *
     * @param fm FragmentManager required to initialise the superclass.
     */
    public ViewPagerAdapter(FragmentManager fm){
        super(fm);
    }

    /**
     * Runs once at runtime. Specifies the Fragment to display at each ViewPager position.
     *
     * @param position The position (tab) of the ViewPager.
     * @return The fragment to be displayed at the specified position.
     */
    @Override
    public Fragment getItem(int position) {
        final Fragment result;
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

}