package com.tsongkha.spinnerdatepickerexample;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import org.hamcrest.Matcher;
import org.junit.experimental.theories.DataPoint;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.support.test.espresso.Espresso.onView;
import static junit.framework.Assert.assertEquals;

/**
 * Created by rawsond on 27/08/17.
 */

class NumberPickers {

    @Deprecated
    public static ViewAction setNumber(final int num) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                NumberPicker np = (NumberPicker) view;
                np.setValue(num);
                getOnValueChangeListener(np).onValueChange(np, num, num);
            }

            @Override
            public String getDescription() {
                return "Set the passed number into the NumberPicker";
            }

            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(NumberPicker.class);
            }
        };
    }

    public static ViewAction typeInNumberPicker(final String value) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                NumberPicker np = (NumberPicker) view;
                EditText et = com.tsongkha.spinnerdatepicker.NumberPickers.findEditText(np);
                ViewActions.typeText(value).perform(uiController, et);
                ViewActions.closeSoftKeyboard();
            }

            @Override
            public String getDescription() {
                return "Set the passed number into the NumberPicker";
            }

            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(NumberPicker.class);
            }
        };
    }

    public static ViewAction scroll(final int yOffsetInDp) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                NumberPicker np = (NumberPicker) view;
                int yOffsetInPx = (int) (yOffsetInDp * view.getResources().getDisplayMetrics().density);
                np.scrollBy(0, yOffsetInPx);
            }

            @Override
            public String getDescription() {
                return "Scroll up or down a given yOffset in dp";
            }

            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(NumberPicker.class);
            }
        };
    }

    public static ViewAssertion isDisplayed(final String expectedPick) {
        return new ViewAssertion() {
            @Override
            public void check(View view, NoMatchingViewException noViewFoundException) {
                NumberPicker numberPicker = (NumberPicker) view;
                String[] displayedValues = numberPicker.getDisplayedValues();
                String actualPicked = null;
                if (displayedValues == null) {
                    actualPicked = Integer.toString(numberPicker.getValue());
                } else {
                    actualPicked = numberPicker.getDisplayedValues()[numberPicker.getValue()];
                }
                assertEquals(expectedPick, actualPicked);
            }
        };
    }

    public static NumberPicker.OnValueChangeListener getOnValueChangeListener(NumberPicker numberPicker) {
        try {
            Field onValueChangedListener = NumberPicker.class.getDeclaredField(
                    "mOnValueChangeListener");
            onValueChangedListener.setAccessible(true);
            return (NumberPicker.OnValueChangeListener) onValueChangedListener.get(numberPicker);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}