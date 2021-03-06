package com.example.taskmaster;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;



import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4ClassRunner.class)
public class ExampleInstrumentedTest {


    @Test
    public void testChangeUsername() {
        ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.setting)).perform(scrollTo());
        onView(withId(R.id.setting)).perform(click());
        String testText="hello’s tasks";
        onView(withId(R.id.username))
                .perform(typeText("hello"), closeSoftKeyboard());
        onView(withId(R.id.submitBtnId)).perform(click());
        onView(withId(R.id.usernameTitle)).check(matches(withText(testText)));

    }
    @Test
    public void testMyTaskText() {
        ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.myTaskText)).check(matches(withText("My Tasks")));
    }

    @Test
    public void TestRecycleViewIsDisplay() {
        ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.recycler_view)).perform(scrollTo());
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
    }
    @Test
    public void addTaskTest() {
        ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.addTask)).perform(scrollTo());
        onView(withId(R.id.addTask)).perform(click());

        String Task_name_entered="task 29";
        onView(withId(R.id.task_name))
                .perform(typeText(Task_name_entered), closeSoftKeyboard());
        onView(withId(R.id.describtion))
                .perform(typeText("database"), closeSoftKeyboard());
        onView(withId(R.id.addTaskBtn)).perform(click());
        pressBack();
        onView(ViewMatchers.withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));
        onView(withId(R.id.lab)).check(matches(withText("task 29")));

    }

    @Test
    public void CheckVisbility() {
        //check main layout
        ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.MainLayout)).check(matches(isDisplayed()));
        //check add task layout
        onView(withId(R.id.addTask)).perform(scrollTo());
        onView(withId(R.id.addTask)).perform(click());
        onView(withId(R.id.addTaskLayout)).check(matches(isDisplayed()));
        pressBack();
        //check all task layout
        onView(withId(R.id.allTask)).perform(scrollTo());
        onView(withId(R.id.allTask)).perform(click());
        onView(withId(R.id.allTaskLayout)).check(matches(isDisplayed()));
        pressBack();
        //check all task layout
        onView(withId(R.id.setting)).perform(scrollTo());
        onView(withId(R.id.setting)).perform(click());
        onView(withId(R.id.settingLayout)).check(matches(isDisplayed()));
        pressBack();


    }
    @Test
    public void CheckVisbilityForTeamSpinner() {
        ActivityScenario.launch(MainActivity.class);
        //check add task spinner
        onView(withId(R.id.addTask)).perform(scrollTo());
        onView(withId(R.id.addTask)).perform(click());
        onView(withId(R.id.spinner)).check(matches(isDisplayed()));
        pressBack();
        //check setting spinner
        onView(withId(R.id.setting)).perform(scrollTo());
        onView(withId(R.id.setting)).perform(click());
        onView(withId(R.id.spinner_setting)).check(matches(isDisplayed()));


    }
}


