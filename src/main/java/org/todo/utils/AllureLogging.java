package org.todo.utils;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.function.Consumer;

@UtilityClass
public class AllureLogging {

    private static final Logger LOG = LoggerFactory.getLogger(AllureLogging.class);

    public static void attachText(String text) {
        Allure.addAttachment("Вложение", "text/plain", text);
    }

    public static  <T> Matcher<T> logMatcherWithText(String text, Matcher<T> matcher) {
        return new HamcrestLogging<>(text, matcher);
    }

    private static void log(Status status, String message, String body) {
        LOG.error(message);
        String uuid = UUID.randomUUID().toString();
        StepResult result = new StepResult().setName(message);
        Allure.getLifecycle().startStep(uuid, result);

        try {
            if (body != null) {
                attachText(body);
            }

            Consumer<StepResult> update = stepResult -> stepResult.setStatus(status);
            Allure.getLifecycle().updateStep(uuid, update);
        } finally {
            Allure.getLifecycle().stopStep(uuid);
        }
    }

    @AllArgsConstructor
    private static class HamcrestLogging<T> extends BaseMatcher<T> {

        private String description;
        private Matcher<T> matcher;

        @Override
        public boolean matches(Object actual) {
            boolean isMatched = matcher.matches(actual);
            String body = actual != null
                    ? String.format("Checking \"%s\" and %s", actual, matcher.toString())
                    : String.format("Checking null and %s", matcher.toString());

            if (isMatched) {
                AllureLogging.log(Status.PASSED, description, body);
            } else {
                AllureLogging.log(Status.FAILED, description, body);
            }

            return isMatched;
        }

        @Override
        public void describeTo(Description description) {
            matcher.describeTo(description);
        }
    }
}
