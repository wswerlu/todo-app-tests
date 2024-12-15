package org.todo.exceptions;

public class ErrorResponseException extends RuntimeException {
    public ErrorResponseException(int statusCode) {
        super(String.valueOf(statusCode));
    }
}
