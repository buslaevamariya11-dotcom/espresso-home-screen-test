package ru.kkuzmichev.simpleappforespresso;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;

import static org.junit.Assert.assertTrue;

public class RecyclerViewAssertion implements ViewAssertion {

    @Override
    public void check(
            View view,
            NoMatchingViewException noViewFoundException
    ) {

        if (noViewFoundException != null) {
            throw noViewFoundException;
        }

        assertTrue(
                "View is not an instance of RecyclerView",
                view instanceof RecyclerView
        );
    }
}