package com.swinburne.irtsa.irtsa;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

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
  public void setup() {
    onView(withId(R.id.viewPager)).perform(swipeLeft());
  }

  /**
   * Asserts the RecyclerView is visible.
   */
  @Test
  public void recyclerView_IsVisible() {
    onView(withId(R.id.recyclerView)).check(matches(isDisplayed()));
  }

  /**
   * Assert the toolbar is displaying the correct menu options.
   */
  @Test
  public void toolbar_HasCorrectOptions() {
    onView(withId(R.id.delete)).check(matches(isDisplayed()));
    onView(withId(R.id.share)).check(matches(isDisplayed()));
    onView(withId(R.id.save)).check(doesNotExist());
    onView(withId(R.id.settings)).check(doesNotExist());
  }
}
