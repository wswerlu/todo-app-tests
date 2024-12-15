package org.todo.api;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.todo.TODOBaseTest;
import org.todo.api.todo.models.TODO;
import org.todo.config.TODOAppConfig;
import org.todo.utils.ErrorUtils;
import org.todo.utils.FakeTestData;

import java.util.List;
import java.util.stream.Stream;

@DisplayName("DELETE /todos/:id")
@Feature("DELETE /todos/:id")
@Tag("delete-todo")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeleteTODOTests extends TODOBaseTest {

    private TODO todo;
    private static String username;
    private static String password;

    @BeforeAll
    void getTODOAppConfig(@Autowired TODOAppConfig todoAppConfig) {
        username = todoAppConfig.getApi().getCredentials().getUsername();
        password = todoAppConfig.getApi().getCredentials().getPassword();
    }

    @BeforeEach
    public void createTODO() {
        todo = FakeTestData.createTODO();
        todoApiService.createTODO(todo);
    }

    @Test
    @DisplayName("Удаление TODO")
    void deleteTODOTest() {
        todoApiService.deleteTODO(todo.getId());

        List<TODO> todos = todoApiService.getTODOs();
        todoAssertions.checkEmptyTODOs(todos);
    }

    @Test
    @DisplayName("Получение ошибки при передаче несуществующего id")
    void deleteTODOWithNonExistentIdTest() {
        int errorStatusCode = ErrorUtils.getStatusCode(() -> todoApiService.deleteTODO(todo.getId() + 1));
        commonAssertions.checkErrorStatusCode(errorStatusCode, 404);

        List<TODO> todos = todoApiService.getTODOs();
        todoAssertions.checkTODO(todos, todo);
    }

    @Test
    @DisplayName("Получение ошибки при удалении TODO без авторизации")
    void deleteTODOWithoutAuthTest() {
        int errorStatusCode = ErrorUtils.getStatusCode(() -> todoApiService.deleteTODO(todo.getId(), null, null));
        commonAssertions.checkErrorStatusCode(errorStatusCode, 401);

        List<TODO> todos = todoApiService.getTODOs();
        todoAssertions.checkTODO(todos, todo);
    }

    public static Stream<Arguments> deleteTODOWithInvalidAuthStream() {
        return Stream.of(
                Arguments.of("test-username", password,  " username"),
                Arguments.of(username, "test-password", " password")
        );
    }

    @ParameterizedTest(name = "{2}")
    @DisplayName("Получение ошибки при удалении TODO с невалидным")
    @MethodSource("deleteTODOWithInvalidAuthStream")
    void deleteTODOWithInvalidAuthTest(String username, String password, String scenario) {
        int errorStatusCode = ErrorUtils.getStatusCode(() -> todoApiService.deleteTODO(todo.getId(), username, password));
        commonAssertions.checkErrorStatusCode(errorStatusCode, 401);

        List<TODO> todos = todoApiService.getTODOs();
        todoAssertions.checkTODO(todos, todo);
    }
}
