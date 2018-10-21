package com.swinburne.irtsa.irtsa;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Class to test the UI of the View Scan Fragment.
 */
@RunWith(AndroidJUnit4.class)
public class TestViewScanFragment {

  /**
   * Launch the MainActivity.
   */
  @Rule
  public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule(MainActivity.class);

  /**
   * Navigate to the View Scan Fragment.
   */
  @Before
  public void setup() {
    IdlingRegistry.getInstance().register(activityRule.getActivity().connectionIdleResource);
    onView(withId(R.id.allCheckBox)).perform(click());
    onView(withId(R.id.startScanButton)).perform(click());
  }

  /**
   * Assert the scan image is visible.
   */
  @Test
  public void imageIsVisible() {
    IdlingRegistry.getInstance().register(activityRule.getActivity().progressIdleResource);
    onView(withId(R.id.scanImage)).check(matches(isDisplayed()));
  }

  /**
   * Assert the back button hides the View Scan fragment and
   * displays the Start Scan Fragment.
   */
  @Test
  public void backPressNavigatesToStartScanFragment() {
    IdlingRegistry.getInstance().register(activityRule.getActivity().progressIdleResource);
    onView(withId(R.id.scanImage)).check(matches(isDisplayed()));
    onView(withId(R.id.startScanButton)).check(doesNotExist());

    pressBack();

    onView(withId(R.id.scanImage)).check(doesNotExist());
    onView(withId(R.id.startScanButton)).check(matches(isDisplayed()));
  }

  /**
   * Assert the toolbar is displaying the correct options.
   */
  @Test
  public void toolbarHasCorrectOptions() {
    IdlingRegistry.getInstance().register(activityRule.getActivity().progressIdleResource);
    onView(withId(R.id.save)).check(matches(isDisplayed()));
    onView(withId(R.id.delete)).check(doesNotExist());
    onView(withId(R.id.share)).check(doesNotExist());
    //onView(withId(R.id.settings)).check(doesNotExist());
  }
}
