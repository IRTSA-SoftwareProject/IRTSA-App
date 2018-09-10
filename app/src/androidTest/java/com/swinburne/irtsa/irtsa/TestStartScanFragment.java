package com.swinburne.irtsa.irtsa;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TestStartScanFragment {
  @Rule
  public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule(MainActivity.class);

  @Test
  public void startScanButton_IsVisible() {
    onView(withId(R.id.startScanButton)).check(matches(isDisplayed()));
  }

  @Test
  public void startScanButton_CorrectText() {
    onView(withId(R.id.startScanButton)).check(matches(withText("Start Scan")));
  }

  @Test
  public void toolbar_HasCorrectOptions() {
    onView(withId(R.id.save)).check(matches(isDisplayed()));
  }

  @Test
  public void startScanButton_NavigatesToViewScanFragment() {
    onView(withId(R.id.startScanButton)).perform(click());
    onView(withId(R.id.startScanButton)).check(doesNotExist());
    onView(withId(R.id.scanImage)).check(matches(isDisplayed()));
  }
}
