package ru.kkuzmichev.simpleappforespresso;

import androidx.test.espresso.idling.CountingIdlingResource;

public class EspressoIdlingResource {

    private static final String RESOURCE = "GLOBAL";

    private static final CountingIdlingResource countingIdlingResource =
            new CountingIdlingResource(RESOURCE);

    private EspressoIdlingResource() {
        // Utility class
    }

    public static void increment() {
        countingIdlingResource.increment();
    }

    public static void decrement() {
        if (!countingIdlingResource.isIdleNow()) {
            countingIdlingResource.decrement();
        }
    }

    public static CountingIdlingResource getIdlingResource() {
        return countingIdlingResource;
    }
}