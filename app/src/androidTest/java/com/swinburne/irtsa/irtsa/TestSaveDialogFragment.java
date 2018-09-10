package com.swinburne.irtsa.irtsa;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isFocusable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Class to test the UI of the Save Dialog Fragment.
 */
@RunWith(AndroidJUnit4.class)
public class TestSaveDialogFragment {

  /**
   * Launch the MainActivity.
   */
  @Rule
  public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule(MainActivity.class);

  /**
   * Navigate to the Save Dialog Fragment.
   */
  @Before
  public void setup() {
    onView(withId(R.id.startScanButton)).perform(click());
    onView(withId(R.id.save)).perform(click());
  }

  /**
   * Assert that the name input is visible.
   */
  @Test
  public void nameInput_IsVisible() {
    onView(withId(R.id.fName)).check(matches(isDisplayed()));
  }

  /**
   * Assert that the description input is visible.
   */
  @Test
  public void descriptionInput_IsVisible() {
    onView(withId(R.id.fDescription)).check(matches(isDisplayed()));
  }

  /**
   * Assert the name input can be selected and manipulated.
   */
  @Test
  public void nameInput_AcceptsInput() {
    onView(withId(R.id.fName)).check(matches(isFocusable()));
    onView(withId(R.id.fName)).perform(replaceText("Test String"));
    onView(withId(R.id.fName)).check(matches(withText("Test String")));
  }

  /**
   * Assert the description input can be selected and manipulated.
   */
  @Test
  public void descriptionInput_AcceptsInput() {
    onView(withId(R.id.fDescription)).check(matches(isFocusable()));
    onView(withId(R.id.fDescription)).perform(replaceText("Test String"));
    onView(withId(R.id.fDescription)).check(matches(withText("Test String")));
  }

  /**
   * Assert that the save button closes the dialog and display's the ViewScan Fragment.
   */
  @Test
  public void saveButton_ClosesDialog_ShowsViewScanFragment() {
    onView(withId(R.id.scanImage)).check(doesNotExist());
    onView(withText("SAVE")).check(matches(isDisplayed()));

    onView(withText("SAVE")).perform(click());

    onView(withText("SAVE")).check(doesNotExist());
    onView(withId(R.id.scanImage)).check(matches(isDisplayed()));
  }

  /**
   * Assert that the cancel button closes the dialog and display's the ViewScan Fragment.
   */
  @Test
  public void cancelButton_ClosesDialog_ShowsViewScanFragment() {
    onView(withId(R.id.scanImage)).check(doesNotExist());
    onView(withText("CANCEL")).check(matches(isDisplayed()));

    onView(withText("CANCEL")).perform(click());

    onView(withText("CANCEL")).check(doesNotExist());
    onView(withId(R.id.scanImage)).check(matches(isDisplayed()));
  }

  /**
   * Assert that the back-press button closes the dialog and display's the ViewScan Fragment.
   */
  @Test
  public void backPress_ClosesDialog_ShowsViewScanFragment() {
    onView(withId(R.id.scanImage)).check(doesNotExist());
    onView(withText("CANCEL")).check(matches(isDisplayed()));

    pressBack();

    onView(withText("CANCEL")).check(doesNotExist());
    onView(withId(R.id.scanImage)).check(matches(isDisplayed()));
  }
}
