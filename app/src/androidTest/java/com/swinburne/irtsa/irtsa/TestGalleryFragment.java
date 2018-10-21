package com.swinburne.irtsa.irtsa;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.support.test.rule.ActivityTestRule;

import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Class to test the UI of the Gallery Fragment.
 */
@RunWith(AndroidJUnit4.class)
public class TestGalleryFragment {

  /**
   * Launch the MainActivity.
   */
  @Rule
  public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule(MainActivity.class);

  /**
   * Navigates to the GalleryFragment.
   */
  @Before
  public void Setup() {
    onView(withId(R.id.viewPager)).perform(swipeLeft());
  }

  /**
   * Asserts the RecyclerView is visible.
   */
  @Test
  public void RecyclerViewIsVisible() {
    onView(withId(R.id.recyclerView)).check(matches(isDisplayed()));
  }

  /**
   * Assert the toolbar is displaying the correct menu options.
   */
  @Test
  public void ToolbarHasCorrectOptions() {
    onView(withId(R.id.delete)).check(doesNotExist());
    onView(withId(R.id.share)).check(doesNotExist());
    onView(withId(R.id.save)).check(doesNotExist());
  }

  /**
   * Assert that the bottom navigation bar has the correct options
   */
  @Test
  public void BottomNavigationHasRequiredOptions() {
    onView(allOf(withText("Scan"), isDescendantOfA(withId(R.id.tabLayout))))
        .check(matches(isDisplayed()));
    onView(allOf(withText("Gallery"), isDescendantOfA(withId(R.id.tabLayout))))
        .check(matches(isDisplayed()));
  }

  /**
   * Ensure that the start scan fragment is shown on bottom
   * navigation scan item press.
   */
  @Test
  public void BackToStartScanFragmentByHitScanButton(){
    onView(withId(R.id.recyclerView)).check(matches(isDisplayed()));
    onView(allOf(withText("Scan"), isDescendantOfA(withId(R.id.tabLayout))))
        .perform(click());
    onView(withId(R.id.startScanButton)).check(matches(isDisplayed()));

  }

  /**
   * Ensure that start scan is shown after swiping left
   */
  @Test
  public void BackToStartScanFragmentBySwipeRight() {
    onView(withId(R.id.recyclerView)).check(matches(isDisplayed()));
    onView(withId(R.id.recyclerView)).perform(swipeRight());
    onView(withId(R.id.startScanButton)).check(matches(isDisplayed()));
  }

}
