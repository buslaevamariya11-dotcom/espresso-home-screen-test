package ru.kkuzmichev.simpleappforespresso;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class RecyclerViewItemCountMatcher extends TypeSafeMatcher<View> {

    private final int expectedCount;

    public RecyclerViewItemCountMatcher(int expectedCount) {
        this.expectedCount = expectedCount;
    }

    @Override
    protected boolean matchesSafely(View view) {

        if (!(view instanceof RecyclerView)) {
            return false;
        }

        RecyclerView recyclerView = (RecyclerView) view;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();

        return adapter != null
                && adapter.getItemCount() == expectedCount;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(
                "RecyclerView with item count: " + expectedCount
        );
    }
}