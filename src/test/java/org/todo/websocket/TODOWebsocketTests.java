package org.todo.websocket;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import org.todo.TODOBaseTest;
import org.todo.api.todo.models.TODO;
import org.todo.utils.FakeTestData;
import org.todo.websocket.models.TODOEvent;

import java.util.List;

@DisplayName("/ws")
@Feature("/ws")
@Tag("ws-todo")
public class TODOWebsocketTests extends TODOBaseTest {

    private TODO todo;

    private TODOWebsocketService todoWebsocketService;

    @BeforeEach
    void connect() {
        todoWebsocketService = new TODOWebsocketService(todoAppConfig);

        todoWebsocketService.connect();
    }

    @BeforeEach
    void createTODO() {
        todo = FakeTestData.createTODO();
        todoApiService.createTODO(todo);
    }

    @AfterEach
    void closeConnection() {
        todoWebsocketService.closeConnection();
    }

    @Test
    @DisplayName("Получение события с type = new_todo")
    void getNewTODOEventTest() {
        List<TODOEvent> todoEvents = todoWebsocketService.waitMessages();

        todoAssertions.checkNewTODOEvent(todoEvents, todo);
    }

    @Test
    @DisplayName("Проверка отсутствия получения события после закрытия соединения")
    void notGetTODOEventAfterClosingConnectionTest() {
        todoWebsocketService.closeConnection();

        todoApiService.createTODO(FakeTestData.createTODO());
        List<TODOEvent> todoEvents = todoWebsocketService.waitMessages(2);

        todoAssertions.checkNewTODOEvent(todoEvents, todo);
    }

    @Test
    @DisplayName("Отсутствие события при обновлении TODO")
    void notGetNewTODOEventWhenUpdateTest() {
        todoApiService.updateTODO(todo.getId(), FakeTestData.createTODO());
        List<TODOEvent> todoEvents = todoWebsocketService.waitMessages(2);

        todoAssertions.checkNewTODOEvent(todoEvents, todo);
    }

    @Test
    @DisplayName("Отсутствие события при удалении TODO")
    void notGetNewTODOEventWhenDeleteTest() {
        todoApiService.deleteTODO(todo.getId());
        List<TODOEvent> todoEvents = todoWebsocketService.waitMessages(2);

        todoAssertions.checkNewTODOEvent(todoEvents, todo);
    }
}
