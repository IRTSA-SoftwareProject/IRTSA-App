package com.swinburne.irtsa.irtsa;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.assertion.ViewAssertions.selectedDescendantsMatch;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
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

    @Before
    public void setup() {
        onView(withId(R.id.viewPager)).perform(swipeLeft());
        onView(withId(R.id.recyclerView)).perform(click());
    }
    @Test
    public void Gallery_Detail_is_visible()
    {
        onView(withId(R.id.galleryDetailImage)).check(matches(isDisplayed()));
        onView(withId(R.id.galleryDetailName)).check(matches(isDisplayed()));
        onView(withId(R.id.galleryDetailDescription)).check(matches(isDisplayed()));
        onView(withId(R.id.galleryDetailDate)).check(matches(isDisplayed()));

    }
    @Test
    public void Gallery_Detail_Toolbar_has_correct_option()
    {
        onView(withId(R.id.delete)).check(matches(isDisplayed()));
        onView(withId(R.id.share)).check(matches(isDisplayed()));
    }

    @Test
    public void backPress_navigatesToGalleryFragment() {
        onView(withId(R.id.recyclerView)).check(doesNotExist());
        onView(withId(R.id.galleryDetailImage)).check(matches(isDisplayed()));
        onView(withId(R.id.galleryDetailName)).check(matches(isDisplayed()));
        onView(withId(R.id.galleryDetailDescription)).check(matches(isDisplayed()));
        onView(withId(R.id.guideline5)).check(matches(isDisplayed()));

        pressBack();

        onView(withId(R.id.galleryDetailImage)).check(doesNotExist());
        onView(withId(R.id.galleryDetailName)).check(doesNotExist());
        onView(withId(R.id.galleryDetailDescription)).check(doesNotExist());
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()));
    }
    @Test
    public void edit_image(){
        onView(withId(R.id.galleryDetailImage)).check(matches(isDisplayed()));
        onView(withId(R.id.edit)).perform(click());
        onView(withId(R.id.editName)).check(matches(isDisplayed()));
        onView(withId(R.id.editDescription)).check(matches(isDisplayed()));
    }
}