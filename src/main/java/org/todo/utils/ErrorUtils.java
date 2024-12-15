package org.todo.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.function.Executable;
import org.todo.exceptions.ErrorResponseException;
import org.todo.exceptions.TODOException;

@UtilityClass
public class ErrorUtils {

    @SneakyThrows
    public int getStatusCode(Executable executable) {
        try {
            executable.execute();
            throw new TODOException("Получен успешный ответ");
        } catch (ErrorResponseException e) {
            return Integer.parseInt(e.getMessage());
        }
    }
}
