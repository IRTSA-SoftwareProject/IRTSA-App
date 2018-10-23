package com.swinburne.irtsa.irtsa;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Class to test the UI of the Edit Scan Dialog Fragment.
 * For these tests to pass, at least one scan must be saved to the devices database.
 */
@RunWith(AndroidJUnit4.class)
public class TestGalleryDeleteDialog {
  /**
   * Launch the MainActivity.
   */
  @Rule
  public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule(MainActivity.class);

  /**
   * Navigate to the GalleryDeleteDialog.
   */
  @Before
  public void setup() {
    onView(withId(R.id.viewPager)).perform(swipeLeft());
    try {
      Thread.sleep(1000);
    } catch (Exception e) {
      System.out.println("Error performing sleep during test");
    }

    onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0,
            click()));

    onView(withId(R.id.delete)).perform(click());
  }

  /**
   * Assert that the dialog is present with the correct title.
   */
  @Test
  public void dialogTitleIsCorrect() {
    onView(withText("Confirm Delete of Scan")).check(matches(isDisplayed()));
  }

  /**
   * Assert that the no button closes the dialog and display's the GalleryDetail Fragment.
   */
  @Test
  public void noButtonClosesDialogShowsGalleryDetailFragment() {
    onView(withId(R.id.galleryDetailImage)).check(doesNotExist());
    onView(withText("NO")).check(matches(isDisplayed()));

    onView(withText("NO")).perform(click());

    onView(withText("NO")).check(doesNotExist());
    onView(withId(R.id.galleryDetailImage)).check(matches(isDisplayed()));
  }

  /**
   * Assert that the back-press button closes the dialog and display's the GalleryDetail Fragment.
   */
  @Test
  public void backPressClosesDialogShowsGalleryDetailFragment() {
    onView(withId(R.id.galleryDetailImage)).check(doesNotExist());
    onView(withText("NO")).check(matches(isDisplayed()));

    pressBack();

    onView(withText("NO")).check(doesNotExist());
    onView(withId(R.id.galleryDetailImage)).check(matches(isDisplayed()));
  }
}
