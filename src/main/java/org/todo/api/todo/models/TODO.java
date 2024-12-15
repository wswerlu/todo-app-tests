package org.todo.api.todo.models;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TODO {

    int id;
    String text;
    Boolean completed;
}
