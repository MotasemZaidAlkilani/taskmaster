package com.example.taskmaster;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.content.Intent;
import android.widget.Adapter;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.taskmaster.models.task;
import com.example.taskmaster.ui.viewAdapter;

import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Rule
    public ActivityScenarioRule<MainActivity> rule=new ActivityScenarioRule<>(MainActivity.class);


    public ActivityTestRule<setting> intentsTestRule =
            new ActivityTestRule <>(setting.class);

    List<task> dataList= viewAdapter.dataList;
    @Test
    public void testChangeUsername() {
        Intent intent=new Intent();
        intentsTestRule.launchActivity(intent);
        String testText="hello’s tasks";
        onView(withId(R.id.username))
                .perform(typeText("hello"), closeSoftKeyboard());
        onView(withId(R.id.submitBtnId)).perform(click());
        onView(withId(R.id.usernameTitle)).check(matches(withText(testText)));

    }
    @Test
    public void testMyTaskText() {
        // Context of the app under test.
        onView(withId(R.id.myTaskText)).check(matches(withText("My Tasks")));
    }
    @Test
    public void testLabButtonsText() {
        // Context of the app under test.

//        onView(withId(R.id.lab26)).check(matches(withText("lab26")));
//        onView(withId(R.id.lab27)).check(matches(withText("lab27")));
//        onView(withId(R.id.lab28)).check(matches(withText("lab28")));
    }
    @Test
    public void TestRecycleViewIsDisplay() {
        onView(withId(R.id.recycler_view)).perform(scrollTo());
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
    }


}


