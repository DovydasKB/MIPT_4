package com.example.mipt4;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private static final String TEST_NOTE_TITLE = "...............................................................";
    private static final String TEST_NOTE_CONTENT = "...............................................................................................................";

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void addAndThenDeleteNoteTest() {

        onView(withId(R.id.button_add_note)).perform(click());
        onView(withId(R.id.editTitle)).perform(typeText(TEST_NOTE_TITLE), closeSoftKeyboard());
        onView(withId(R.id.editContent)).perform(typeText(TEST_NOTE_CONTENT), closeSoftKeyboard());
        onView(withId(R.id.json_radio_button)).perform(click());
        onView(withId(R.id.saveButton)).perform(click());


        onView(withText(TEST_NOTE_TITLE)).check(matches(isDisplayed()));


        onView(withId(R.id.button_delete_note)).perform(click());


        onView(withId(R.id.spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(TEST_NOTE_TITLE))).perform(click());
        onView(withId(R.id.deleteButton)).perform(click());


        onView(withText(TEST_NOTE_TITLE)).check(doesNotExist());
    }
}
