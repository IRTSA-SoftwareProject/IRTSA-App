package com.swinburne.irtsa.irtsa;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isFocusable;
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
public class TestGalleryEditDialog {
  /**
   * Launch the MainActivity.
   */
  @Rule
  public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule(MainActivity.class);

  /**
   * Navigate to the GalleryEditDialog.
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

    onView(withId(R.id.edit)).perform(click());
  }

  /**
   * Assert that the dialog is present with the correct title..
   */
  @Test
  public void dialogTitleIsCorrect() {
    onView(withText("Rename Scan")).check(matches(isDisplayed()));
  }

  /**
   * Assert that the scan name input is visible.
   */
  @Test
  public void scanNameInputVisible() {
    onView(withId(R.id.editName)).check(matches(isDisplayed()));
  }

  /**
   * Assert that the scan description input is visible.
   */
  @Test
  public void scanDescriptionInputIsVisible() {
    onView(withId(R.id.editDescription)).check(matches(isDisplayed()));
  }

  /**
   * Assert the scan name input can be selected and manipulated.
   */
  @Test
  public void scanNameInputAcceptsInput() {
    onView(withId(R.id.editName)).check(matches(isFocusable()));
    onView(withId(R.id.editName)).perform(replaceText("Test String"));
    onView(withId(R.id.editName)).check(matches(withText("Test String")));
  }

  /**
   * Assert the scan description input can be selected and manipulated.
   */
  @Test
  public void scanDescriptionInputAcceptsInput() {
    onView(withId(R.id.editDescription)).check(matches(isFocusable()));
    onView(withId(R.id.editDescription)).perform(replaceText("Test String"));
    onView(withId(R.id.editDescription)).check(matches(withText("Test String")));
  }

  /**
   * Assert that the save button closes the dialog and display's the GalleryDetail Fragment.
   */
  @Test
  public void saveButtonClosesDialogShowsGalleryDetailFragment() {
    onView(withId(R.id.galleryDetailImage)).check(doesNotExist());
    onView(withText("SAVE")).check(matches(isDisplayed()));

    onView(withText("SAVE")).perform(click());

    onView(withText("SAVE")).check(doesNotExist());
    onView(withId(R.id.galleryDetailImage)).check(matches(isDisplayed()));
  }

  /**
   * Assert that the cancel button closes the dialog and display's the GalleryDetail Fragment.
   */
  @Test
  public void cancelButtonClosesDialogShowsGalleryDetailFragment() {
    onView(withId(R.id.galleryDetailImage)).check(doesNotExist());
    onView(withText("CANCEL")).check(matches(isDisplayed()));

    onView(withText("CANCEL")).perform(click());

    onView(withText("CANCEL")).check(doesNotExist());
    onView(withId(R.id.galleryDetailImage)).check(matches(isDisplayed()));
  }

  /**
   * Assert that the back-press button closes the dialog and display's the GalleryDetail Fragment.
   */
  @Test
  public void backPressClosesDialogShowsGalleryDetailFragment() {
    onView(withId(R.id.galleryDetailImage)).check(doesNotExist());
    onView(withText("CANCEL")).check(matches(isDisplayed()));

    // Twice to close the keyboard.
    pressBack();
    pressBack();

    onView(withText("CANCEL")).check(doesNotExist());
    onView(withId(R.id.galleryDetailImage)).check(matches(isDisplayed()));
  }
}
