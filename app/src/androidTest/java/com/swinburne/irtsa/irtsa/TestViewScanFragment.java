package com.swinburne.irtsa.irtsa;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class TestViewScanFragment {
  @Rule
  public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule(MainActivity.class);

  @Before
  public void setup() {
    onView(withId(R.id.startScanButton)).perform(click());
  }

  @Test
  public void image_IsVisible() {
    onView(withId(R.id.scanImage)).check(matches(isDisplayed()));
  }

  @Test
  public void backPress_NavigatesToStartScanFragment() {
    pressBack();
    onView(withId(R.id.scanImage)).check(doesNotExist());
    onView(withId(R.id.startScanButton)).check(matches(isDisplayed()));
  }
}
