package com.swinburne.irtsa.irtsa;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Class to test the Start Scan Fragment.
 */
@RunWith(AndroidJUnit4.class)
public class TestStartScanFragment {

  /**
   * Launch the MainActivity.
   */
  @Rule
  public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule(MainActivity.class);


  /**
   * Assert the Start Scan Button is displayed.
   */
  @Test
  public void startScanButton_IsVisible() {
    onView(withId(R.id.startScanButton)).check(matches(isDisplayed()));
  }

  /**
   * Assert the toolbar is displaying the correct options.
   */
  @Test
  public void toolbar_HasCorrectOptions() {
    onView(withId(R.id.delete)).check(doesNotExist());
    onView(withId(R.id.share)).check(doesNotExist());
    onView(withId(R.id.save)).check(doesNotExist());
  }

  /**
   * Assert the start scan button removes the Start Scan Fragment
   * and displays the View Scan Fragment.
   */
  @Test
  public void startScanButton_NavigatesToViewScanFragment() {
    IdlingRegistry.getInstance().register(activityRule.getActivity().idlingResource);
    onView(withId(R.id.frameInputEditText)).perform(replaceText("1"));

    //onView(withId(R.id.share)).check(doesNotExist());
    onView(withId(R.id.startScanButton)).perform(click());
    IdlingRegistry.getInstance().register(activityRule.getActivity().idlingResource);
    //onView(withId(R.id))
    onView(withId(R.id.scanImage)).check(matches(isDisplayed()));
    //onView(withId(R.id.startScanButton)).check(matches(isDisplayed()));
  }
}
