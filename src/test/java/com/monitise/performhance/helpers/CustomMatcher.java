package com.monitise.performhance.helpers;


import com.monitise.performhance.exceptions.BaseException;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class CustomMatcher extends TypeSafeMatcher<BaseException> {

    public static CustomMatcher hasCode(int code) {
        return new CustomMatcher(code);
    }

    private int foundErrorCode;
    private final int expectedErrorCode;

    private CustomMatcher(int expectedErrorCode) {
        this.expectedErrorCode = expectedErrorCode;
    }

    @Override
    protected boolean matchesSafely(final BaseException exception) {
        foundErrorCode = exception.getCode();
        return foundErrorCode == expectedErrorCode;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expectedErrorCode)
                .appendText(" Found: ")
                .appendValue(foundErrorCode);
    }
}
