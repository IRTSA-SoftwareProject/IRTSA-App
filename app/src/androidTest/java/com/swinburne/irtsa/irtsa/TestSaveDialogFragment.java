package com.swinburne.irtsa.irtsa;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isFocusable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class TestSaveDialogFragment {
  @Rule
  public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule(MainActivity.class);

  @Before
  public void setup() {
    onView(withId(R.id.startScanButton)).perform(click());
    onView(withId(R.id.save)).perform(click());
  }

  @Test
  public void nameInput_IsVisible() {
    onView(withId(R.id.fName)).check(matches(isDisplayed()));
  }

  @Test
  public void descriptionInput_IsVisible() {
    onView(withId(R.id.fDescription)).check(matches(isDisplayed()));
  }

  @Test
  public void nameInput_AcceptsInput() {
    onView(withId(R.id.fName)).check(matches(isFocusable()));
    onView(withId(R.id.fName)).perform(replaceText("Test String"));
    onView(withId(R.id.fName)).check(matches(withText("Test String")));
  }

  @Test
  public void descriptionInput_AcceptsInput() {
    onView(withId(R.id.fDescription)).check(matches(isFocusable()));
    onView(withId(R.id.fDescription)).perform(replaceText("Test String"));
    onView(withId(R.id.fDescription)).check(matches(withText("Test String")));
  }
}
