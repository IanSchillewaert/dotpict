package com.example.ian.pixelart;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.example.ian.pixelart.activities.LoadActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by ian.
 */
@RunWith(AndroidJUnit4.class)
public class ShowDialogWhenNoData {
    @Rule
    public final ActivityTestRule<LoadActivity> mActivityRule = new ActivityTestRule<>(LoadActivity.class);

    @Test
    public void testSample() throws Exception{
        if (getRVcount() == 0){
            onView(withText("no data found")).check(matches(isDisplayed()));
        }
    }

    private int getRVcount(){
        RecyclerView recyclerView = (RecyclerView) mActivityRule.getActivity().findViewById(R.id.recycler_view);
        if(recyclerView.getAdapter() != null)
            return recyclerView.getAdapter().getItemCount();
        else
            return 0;
    }
}
