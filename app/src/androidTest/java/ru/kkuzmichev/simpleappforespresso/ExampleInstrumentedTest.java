package ru.kkuzmichev.simpleappforespresso;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
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

        // Инициализация Espresso Intents.
        Intents.init();

        // Регистрируем Idling Resource.
        IdlingRegistry.getInstance().register(
                EspressoIdlingResource.getIdlingResource()
        );
    }

    @After
    public void tearDown() {

        // Отключаем Idling Resource.
        IdlingRegistry.getInstance().unregister(
                EspressoIdlingResource.getIdlingResource()
        );

        // Освобождаем Espresso Intents.
        Intents.release();
    }

    /**
     * Домашнее задание 2.5.
     * Проверка текста на главном экране.
     */
    @Test
    public void checkHomeFragmentText() {

        onView(withId(R.id.text_home))
                .check(
                        matches(
                                withText("This is home fragment")
                        )
                );
    }

    /**
     * Домашнее задание 2.6.
     * Задание 1.
     *
     * Проверка Intent при выборе пункта Settings.
     */
    @Test
    public void checkSettingsIntent() {

        // Перехватываем внешний Intent,
        // чтобы браузер не открывался во время теста.
        intending(
                allOf(
                        hasAction(Intent.ACTION_VIEW),
                        hasData("https://google.com")
                )
        ).respondWith(
                new Instrumentation.ActivityResult(
                        Activity.RESULT_OK,
                        null
                )
        );

        // Открываем правое меню Toolbar.
        openActionBarOverflowOrOptionsMenu(
                activityTestRule.getActivity()
        );

        // Проверяем отображение пункта Settings.
        onView(withText("Settings"))
                .check(
                        matches(
                                isDisplayed()
                        )
                );

        // Нажимаем Settings.
        onView(withText("Settings"))
                .perform(
                        click()
                );

        // Проверяем Intent:
        // action = ACTION_VIEW
        // data = https://google.com
        intended(
                allOf(
                        hasAction(Intent.ACTION_VIEW),
                        hasData("https://google.com")
                )
        );
    }

    /**
     * Домашнее задание 2.6.
     * Задание 2.
     *
     * Проверка Gallery с использованием Idling Resources.
     *
     * Дополнительно:
     * 1. Проверка количества элементов списка.
     * 2. Проверка, что список является RecyclerView.
     */
    @Test
    public void checkGalleryWithIdlingResource() {

        // Открываем боковое меню.
        onView(withId(R.id.drawer_layout))
                .perform(
                        DrawerActions.open()
                );

        // Нажимаем Gallery.
        onView(withId(R.id.nav_gallery))
                .perform(
                        click()
                );

        /*
         * GalleryFragment вызывает increment()
         * перед началом fakeLoadData().
         *
         * Espresso ждёт завершения асинхронной операции.
         *
         * После 1500 мс GalleryFragment вызывает decrement(),
         * после чего Espresso продолжает выполнение теста.
         */

        // Проверяем отображение списка.
        onView(withId(R.id.recycle_view))
                .check(
                        matches(
                                isDisplayed()
                        )
                );

        // Дополнительное задание.
        // Проверяем количество элементов в списке.
        onView(withId(R.id.recycle_view))
                .check(
                        matches(
                                new RecyclerViewItemCountMatcher(10)
                        )
                );

        // Дополнительное задание.
        // Проверяем, что список является RecyclerView.
        onView(withId(R.id.recycle_view))
                .check(
                        new RecyclerViewAssertion()
                );

        // Прокручиваем RecyclerView до седьмого элемента.
        // Индексация начинается с 0, поэтому 7-й элемент = позиция 6.
        onView(withId(R.id.recycle_view))
                .perform(
                        RecyclerViewActions
                                .scrollToPosition(6)
                );

        // Проверяем отображение элемента с числом 7.
        onView(withText("7"))
                .check(
                        matches(
                                isDisplayed()
                        )
                );
    }
}