package com.swinburne.irtsa.irtsa;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Class to test the UI contained within the MainActivity.
 */
@RunWith(AndroidJUnit4.class)
public class TestMainActivity {

  /**
   * Launch the MainActivity.
   */
  @Rule
  public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule(MainActivity.class);

  /**
   * Check if the View Pager is visible.
   */
  @Test
  public void viewPagerIsVisible() {
    onView(withId(R.id.viewPager)).check(matches(isDisplayed()));
  }

  /**
   * Assert the first visible fragment is the StartScanFragment.
   */
  @Test
  public void defaultVisibleFragmentIsStartScan() {
    onView(withId(R.id.startScanButton)).check(matches(isCompletelyDisplayed()));
    onView(withId(R.id.recyclerView)).check(matches(not(isCompletelyDisplayed())));
    onView(withId(R.id.textView3)).check(matches(isCompletelyDisplayed()));
    onView(withId(R.id.processingTechniqueSpinner)).check(matches(isCompletelyDisplayed()));
    onView(withId(R.id.pngPathSpinner)).check(matches(isCompletelyDisplayed()));
    onView(withId(R.id.pathTextView)).check(matches(isCompletelyDisplayed()));
    onView(withId(R.id.textView4)).check(matches(isCompletelyDisplayed()));
    onView(withId(R.id.beginFrameRangeEditText)).check(matches(isCompletelyDisplayed()));
    onView(withId(R.id.endFrameRangeEditText)).check(matches(isCompletelyDisplayed()));
    onView(withId(R.id.allCheckBox)).check(matches(isCompletelyDisplayed()));
    onView(withId(R.id.guideline9)).check(matches(isCompletelyDisplayed()));
    onView(withId(R.id.guideline3)).check(matches(isCompletelyDisplayed()));
  }

  /**
   * Assert the fragment on the right of the viewpager is visible if swiped right.
   */
  @Test
  public void defaultRightPagerFragmentIsGalleryFragment() {
    onView(withId(R.id.recyclerView)).check(matches(not(isCompletelyDisplayed())));
    onView(withId(R.id.viewPager)).perform(swipeLeft());
    onView(withId(R.id.recyclerView)).check(matches(isDisplayed()));
  }

  /**
   * Assert the bottom navigation menu is visible.
   */
  @Test
  public void bottomNavigationIsVisible() {
    onView(withId(R.id.tabLayout)).check(matches(isDisplayed()));
  }

  /**
   * Assert the bottom navigation bar has the required menu options.
   */
  @Test
  public void bottomNavigationHasRequiredOptions() {
    onView(allOf(withText("Scan"), isDescendantOfA(withId(R.id.tabLayout))))
        .check(matches(isDisplayed()));
    onView(allOf(withText("Gallery"), isDescendantOfA(withId(R.id.tabLayout))))
        .check(matches(isDisplayed()));
  }

  /**
   * Assert the ViewPager contains the ScanContainerFragment.
   */
  @Test
  public void viewPagerHasScanContainer() {
    onView(withId(R.id.scanContainer)).check(matches(isCompletelyDisplayed()));
  }

  /**
   * Assert the ViewPager contains the GalleryContainerFragment.
   */
  public void viewPagerHasGalleryContainer() {
    onView(withId(R.id.galleryContainer)).check(matches(not(isCompletelyDisplayed())));
    onView(withId(R.id.viewPager)).perform(swipeLeft());
    onView(withId(R.id.galleryContainer)).check(matches(isCompletelyDisplayed()));
  }

  /**
   * Assert the Scan button on the bottom navigation menu navigates
   * the user to the StartScanFragment.
   */
  @Test
  public void bottomNavScanButtonNavigatesToStartScanFragment() {
    onView(withId(R.id.startScanButton)).check(matches(isCompletelyDisplayed()));
    onView(withId(R.id.viewPager)).perform(swipeLeft());
    onView(withId(R.id.startScanButton)).check(matches(not(isCompletelyDisplayed())));
    onView(withId(R.id.viewPager)).perform(swipeRight());
    onView(withId(R.id.startScanButton)).check(matches(isCompletelyDisplayed()));
  }

  /**
   * Assert the Gallery button on the bottom navigation menu navigates
   * the user to the GalleryFragment.
   */
  @Test
  public void bottomNavGalleryButtonNavigatesToGalleryFragment() {
    onView(withId(R.id.recyclerView)).check(matches(not(isCompletelyDisplayed())));
    onView(withId(R.id.viewPager)).perform(swipeLeft());
    onView(withId(R.id.recyclerView)).check(matches(isDisplayed()));
  }

}
