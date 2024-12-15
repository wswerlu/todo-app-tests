package org.todo.api;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.todo.TODOBaseTest;
import org.todo.api.todo.models.TODO;
import org.todo.utils.ErrorUtils;
import org.todo.utils.FakeTestData;

import java.util.List;
import java.util.stream.Stream;

@DisplayName("PUT /todos/:id")
@Feature("PUT /todos/:id")
@Tag("update-todo")
public class UpdateTODOTests extends TODOBaseTest {

    private TODO todo;

    @BeforeEach
    public void createTODO() {
        todo = FakeTestData.createTODO();
        todoApiService.createTODO(todo);
    }

    @Test
    @DisplayName("Обновление TODO")
    void updateTODOTest() {
        TODO expectedTODO = FakeTestData.createTODO();
        todoApiService.updateTODO(todo.getId(), expectedTODO);

        List<TODO> todos = todoApiService.getTODOs();
        todoAssertions.checkTODO(todos, expectedTODO);
    }

    @Test
    @DisplayName("Получение ошибки при передаче несуществующего id")
    void updateTODOWithNonExistentIdTest() {
        TODO todoForTest = FakeTestData.createTODO();
        int errorStatusCode = ErrorUtils.getStatusCode(() -> todoApiService.updateTODO(todoForTest.getId(), todoForTest));
        commonAssertions.checkErrorStatusCode(errorStatusCode, 404);

        List<TODO> todos = todoApiService.getTODOs();
        todoAssertions.checkTODO(todos, todo);
    }

    @Test
    @DisplayName("Получение ошибки при передаче отрицательного id")
    void updateTODOWithNegativeIdTest() {
        int errorStatusCode = ErrorUtils.getStatusCode(
                () -> todoApiService.updateTODO(todo.getId(), FakeTestData.createTODO(-1)));
        commonAssertions.checkErrorStatusCode(errorStatusCode, 400);

        List<TODO> todos = todoApiService.getTODOs();
        todoAssertions.checkTODO(todos, todo);
    }

    public static Stream<Arguments> updateTODOWithoutFieldStream() {
        return Stream.of(
                Arguments.of(FakeTestData.createTODO().setText(null),  " text"),
                Arguments.of(FakeTestData.createTODO().setCompleted(null), " completed")
        );
    }

    @ParameterizedTest(name = "{1}")
    @DisplayName("Получение ошибки при обновлении TODO без поля")
    @MethodSource("updateTODOWithoutFieldStream")
    void updateTODOWithoutFieldTest(TODO invalidTODO, String scenario) {
        int errorStatusCode = ErrorUtils.getStatusCode(() -> todoApiService.updateTODO(todo.getId(), invalidTODO));
        commonAssertions.checkErrorStatusCode(errorStatusCode, 400);

        List<TODO> todos = todoApiService.getTODOs();
        todoAssertions.checkTODO(todos, todo);
    }
}
