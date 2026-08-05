package ru.kkuzmichev.simpleappforespresso;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    /**
     * Домашнее задание 2.5.
     * Проверка текста на главном экране.
     */
    @Test
    public void checkHomeFragmentText() {
        onView(withId(R.id.text_home))
                .check(matches(withText("This is home fragment")));
    }

    /**
     * Домашнее задание 2.6.
     * Проверка Intent при выборе пункта Settings.
     */
    @Test
    public void checkSettingsIntent() {

        // Перехватываем внешний Intent, чтобы во время теста
        // реально не открывался браузер.
        intending(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData("https://google.com")
        )).respondWith(
                new Instrumentation.ActivityResult(
                        Activity.RESULT_OK,
                        null
                )
        );

        // Открываем правое меню Toolbar.
        openActionBarOverflowOrOptionsMenu(
                activityTestRule.getActivity()
        );

        // Проверяем, что пункт Settings отображается.
        onView(withText("Settings"))
                .check(matches(isDisplayed()));

        // Нажимаем Settings.
        onView(withText("Settings"))
                .perform(click());

        // Проверяем, что был отправлен правильный Intent:
        // ACTION_VIEW + https://google.com.
        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData("https://google.com")
        ));
    }
}