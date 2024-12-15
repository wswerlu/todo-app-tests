package org.todo.api;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.todo.TODOBaseTest;
import org.todo.api.todo.models.TODO;
import org.todo.utils.FakeTestData;

import java.util.List;
import java.util.stream.Stream;

@DisplayName("GET /todos")
@Feature("GET /todos")
@Tag("get-todos")
public class GetTODOsTests extends TODOBaseTest {

    private List<TODO> todos;

    @BeforeEach
    public void createTODOs() {
        todos = FakeTestData.createTODOs(10);
        todos.forEach(todo -> todoApiService.createTODO(todo));
    }

    public static Stream<Arguments> getTODOsStream() {
        return Stream.of(
                Arguments.of(null, null, " без ограничений"),
                Arguments.of(null, 5, " с лимитом"),
                Arguments.of(3, null, " со сдвигом"),
                Arguments.of(3, 4, " со сдвигом и лимитом (лимит меньше количества элементов оставшихся после сдвига)"),
                Arguments.of(6, 100, " со сдвигом и лимитом (лимит больше количества элементов оставшихся после сдвига)")
        );
    }

    @ParameterizedTest(name = "{2}")
    @DisplayName("Получение списка TODO")
    @MethodSource("getTODOsStream")
    void getTODOsTest(Integer offset, Integer limit, String scenario) {
        int fromIndex = offset != null
                ? Math.min(offset, todos.size())
                : 0;
        int toIndex = limit != null
                ? Math.min(fromIndex + limit, todos.size())
                : todos.size();

        List<TODO> actualTODOs = todoApiService.getTODOs(offset, limit);
        List<TODO> expectedTODOs = todos.subList(fromIndex, toIndex);

        todoAssertions.checkTODOs(actualTODOs, expectedTODOs);
    }
}
