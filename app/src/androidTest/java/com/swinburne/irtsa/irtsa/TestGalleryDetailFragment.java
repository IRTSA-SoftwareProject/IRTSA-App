package com.swinburne.irtsa.irtsa;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Class to test the UI of the Save Dialog Fragment.
 */
@RunWith(AndroidJUnit4.class)
public class TestGalleryDetailFragment {

  /**
   * Launch the MainActivity.
   */
  @Rule
  public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule(MainActivity.class);

  /**
   * Navigate to the GalleryDetailFragment
   */
  @Before
  public void Setup() {
    onView(withId(R.id.viewPager)).perform(swipeLeft());
    onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
  }

  /**
   * Check if the gallery detail fragment items are visible
   */
  @Test
  public void GalleryDetailIsVisible() {
    onView(withId(R.id.galleryDetailImage)).check(matches(isDisplayed()));
    onView(withId(R.id.galleryDetailName)).check(matches(isDisplayed()));
    onView(withId(R.id.galleryDetailDescription)).check(matches(isDisplayed()));
    onView(withId(R.id.galleryDetailDate)).check(matches(isDisplayed()));
  }

  /**
   * Check the correct toolbar icons are visible
   */
  @Test
  public void GalleryDetailToolbarHasCorrectOption() {
    onView(withId(R.id.delete)).check(matches(isDisplayed()));
    onView(withId(R.id.edit)).check(matches(isDisplayed()));
    onView(withId(R.id.share)).check(matches(isDisplayed()));
    onView(withId(R.id.save)).check(matches(isDisplayed()));

  }

  /**
   * Ensure that the GalleryFragment is visible on back press
   */
  @Test
  public void BackPressNavigatesToGalleryFragment() {
    pressBack();
    onView(withId(R.id.galleryDetailImage)).check(doesNotExist());
    onView(withId(R.id.galleryDetailName)).check(doesNotExist());
    onView(withId(R.id.galleryDetailDescription)).check(doesNotExist());
    onView(withId(R.id.recyclerView)).check(matches(isDisplayed()));
  }

  /**
   * Ensure that the edit image dialog opens when clicking
   * the edit image toolbar icon
   */
  @Test
  public void EditImage(){
    onView(withId(R.id.galleryDetailImage)).check(matches(isDisplayed()));
    onView(withId(R.id.edit)).perform(click());
    onView(withId(R.id.editName)).check(matches(isDisplayed()));
    onView(withId(R.id.editDescription)).check(matches(isDisplayed()));
  }
}