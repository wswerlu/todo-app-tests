package org.todo.api;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.todo.TODOBaseTest;
import org.todo.api.todo.models.TODO;
import org.todo.utils.ErrorUtils;
import org.todo.utils.FakeTestData;

import java.util.List;


@DisplayName("POST /todos")
@Feature("POST /todos")
@Tag("create-todo")
public class CreateTODOTests extends TODOBaseTest {

    @Test
    @DisplayName("Создание TODO")
    void createTODOTest() {
        TODO expectedTODO = FakeTestData.createTODO();
        todoApiService.createTODO(expectedTODO);

        List<TODO> todos = todoApiService.getTODOs();
        todoAssertions.checkTODO(todos, expectedTODO);
    }

    @Test
    @DisplayName("Успешное создание TODO с одинаковыми полями text и completed, но разными id")
    void createTODOWithCommonTextAndCompletedFieldsButDifferentIdsTest() {
        TODO todo= FakeTestData.createTODO();
        List<TODO> expectedTODOs = List.of(todo,
                new TODO()
                        .setId(todo.getId() + 1)
                        .setText(todo.getText())
                        .setCompleted(todo.getCompleted()));

        expectedTODOs.forEach(t -> todoApiService.createTODO(t));

        List<TODO> actualTODOs = todoApiService.getTODOs();
        todoAssertions.checkTODOs(actualTODOs, expectedTODOs);
    }

    @Test
    @DisplayName("Получение ошибки при создании TODO с существующим id")
    void createTODOWithExistingIdTest() {
        TODO expectedTODO = FakeTestData.createTODO();
        TODO todoForTest = FakeTestData.createTODO(expectedTODO.getId());

        todoApiService.createTODO(expectedTODO);
        int errorStatusCode = ErrorUtils.getStatusCode(() -> todoApiService.createTODO(todoForTest));
        commonAssertions.checkErrorStatusCode(errorStatusCode, 400);

        List<TODO> todos = todoApiService.getTODOs();
        todoAssertions.checkTODO(todos, expectedTODO);
    }

    @Test
    @DisplayName("Получение ошибки при создании TODO с отрицательным id")
    void createTODOWithNegativeIdTest() {
        TODO todo = FakeTestData.createTODO(-1);

        int errorStatusCode = ErrorUtils.getStatusCode(() -> todoApiService.createTODO(todo));
        commonAssertions.checkErrorStatusCode(errorStatusCode, 400);

        List<TODO> todos = todoApiService.getTODOs();
        todoAssertions.checkEmptyTODOs(todos);
    }

    @Test
    @DisplayName("Получение ошибки при создании TODO без поля text")
    void createTODOWithoutTExtFieldTest() {
        TODO todo = FakeTestData.createTODO().setText(null);

        int errorStatusCode = ErrorUtils.getStatusCode(() -> todoApiService.createTODO(todo));
        commonAssertions.checkErrorStatusCode(errorStatusCode, 400);

        List<TODO> todos = todoApiService.getTODOs();
        todoAssertions.checkEmptyTODOs(todos);
    }
}
