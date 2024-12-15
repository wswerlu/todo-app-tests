package org.todo.assertions;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.springframework.stereotype.Component;
import org.todo.api.todo.models.TODO;
import org.todo.utils.AllureLogging;
import org.todo.websocket.models.TODOEvent;

import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.todo.domain.enums.EventType.NEW_TODO;

@Component
public class TODOAssertions {

    @Step("Проверка списка TODO")
    public void checkTODOs(List<TODO> actualTODOs, List<TODO> expectedTODOs) {
        assertThat(actualTODOs.size(),
                AllureLogging.logMatcherWithText("Количество полученных TODO = " + expectedTODOs.size(),
                        equalTo(expectedTODOs.size())));
        IntStream.range(0, actualTODOs.size())
                .forEach(i -> checkTODO(actualTODOs.get(i), expectedTODOs.get(i)));
    }

    @Step("Проверка пустого списка TODO")
    public void checkEmptyTODOs(List<TODO> todos) {
        assertThat(todos, AllureLogging.logMatcherWithText("Полученный список пустой", emptyIterable()));
    }

    @Step("Проверка созданного / обновленного TODO")
    public void checkTODO(List<TODO> todos, TODO expectedTODO) {
        assertThat(todos.size(), AllureLogging.logMatcherWithText("Количество полученных TODO = 1", equalTo(1)));
        checkTODO(todos.get(0), expectedTODO);
    }

    @Step("Проверка TODO c id = {actualTODO.id}")
    public void checkTODO(TODO actualTODO, TODO expectedTODO) {
        assertAll(
                () -> assertThat(actualTODO.getId(),
                        AllureLogging.logMatcherWithText("id = " + expectedTODO.getId(),
                                equalTo(expectedTODO.getId()))),
                () -> assertThat(actualTODO.getText(),
                        AllureLogging.logMatcherWithText("text = " + expectedTODO.getText(),
                                equalTo(expectedTODO.getText()))),
                () -> assertThat(actualTODO.getCompleted(),
                        AllureLogging.logMatcherWithText("completed = " + expectedTODO.getCompleted(),
                                equalTo(expectedTODO.getCompleted())))
        );
    }

    @Step("Проверка события c типом new_todo")
    public void checkNewTODOEvent(List<TODOEvent> todoEvents, TODO expectedTODO) {
        assertThat(todoEvents.size(),
                AllureLogging.logMatcherWithText("Количество полученных событий = 1", equalTo(1)));

        TODOEvent todoEvent = todoEvents.get(0);

        assertAll(
                () -> checkTODO(todoEvent.getData(), expectedTODO),
                () -> assertThat(todoEvent.getType(),
                        AllureLogging.logMatcherWithText("type = new_todo", equalTo(NEW_TODO)))
        );
    }

    @Step("Проверка события c типом new_todo в нескольких соединения")
    public void checkNewTODOEvenInSeveralConnections(List<List<TODOEvent>> todoEvents, TODO expectedTODO) {
        IntStream.range(1, todoEvents.size() + 1)
                .forEach(eventNumber -> Allure.step("Проверка " + eventNumber + " соединения",
                        () -> checkNewTODOEvent(todoEvents.get(eventNumber - 1), expectedTODO)));
    }
}
