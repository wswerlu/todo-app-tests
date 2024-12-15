package org.todo.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum EventType {

    NEW_TODO;

    @JsonValue
    public String getValue() {
        return name().toLowerCase(Locale.ROOT);
    }
}
