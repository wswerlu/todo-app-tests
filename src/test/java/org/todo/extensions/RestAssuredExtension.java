package org.todo.extensions;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.todo.domain.filters.CustomExceptionFilter;

public class RestAssuredExtension implements BeforeAllCallback {

    public void beforeAll(ExtensionContext extensionContext) {
        RestAssured.filters(
                new RequestLoggingFilter(),
                new ResponseLoggingFilter(),
                new CustomExceptionFilter(),
                new AllureRestAssured());
    }
}
