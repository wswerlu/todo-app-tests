package org.todo.websocket.models;

import lombok.Data;
import org.todo.api.todo.models.TODO;
import org.todo.domain.enums.EventType;

@Data
public class TODOEvent {

    private TODO data;
    private EventType type;
}
