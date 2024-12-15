package org.todo.websocket;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import org.todo.TODOBaseTest;
import org.todo.api.todo.models.TODO;
import org.todo.utils.FakeTestData;
import org.todo.websocket.models.TODOEvent;

import java.util.List;
import java.util.stream.Stream;


@DisplayName("/ws multi connection")
@Feature("/ws multi connection")
@Tag("ws-todo-multi")
public class TODOWebsocketMultiConnectionTests extends TODOBaseTest {

    List<TODOWebsocketService> todoWebsocketServices;

    @BeforeEach
    void connect() {
        todoWebsocketServices = Stream.generate(() -> new TODOWebsocketService(todoAppConfig))
                .limit(2L)
                .toList();

        todoWebsocketServices.forEach(TODOWebsocketService::connect);
    }

    @AfterEach
    void closeConnection() {
        todoWebsocketServices.forEach(TODOWebsocketService::closeConnection);
    }

    @Test
    @DisplayName("Получение события с type = new_todo для 2 открытых соединений")
    void getNewTODOEventInTwoConnectionsTest() {
        TODO expectedTODO = FakeTestData.createTODO();
        todoApiService.createTODO(expectedTODO);

        List<List<TODOEvent>> todoEvents = todoWebsocketServices.stream()
                .map(TODOWebsocketService::waitMessages)
                .toList();

        todoAssertions.checkNewTODOEvenInSeveralConnections(todoEvents, expectedTODO);
    }
}
