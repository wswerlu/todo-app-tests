package org.todo.assertions;

import io.qameta.allure.Step;
import org.springframework.stereotype.Component;
import org.todo.utils.AllureLogging;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Component
public class CommonAssertions {

    @Step("Проверка кода ошибки")
    public void checkErrorStatusCode(int actualErrorStatusCode, int expectedErrorStatusCode) {
        assertThat(actualErrorStatusCode,
                AllureLogging.logMatcherWithText("Код ошибки = " + expectedErrorStatusCode,
                        equalTo(expectedErrorStatusCode)));
    }
}
