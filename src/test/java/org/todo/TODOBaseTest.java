package org.todo;

import io.qameta.allure.Epic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.todo.api.todo.TODOApiService;
import org.todo.api.todo.models.TODO;
import org.todo.assertions.CommonAssertions;
import org.todo.assertions.TODOAssertions;
import org.todo.config.TODOAppConfig;
import org.todo.extensions.RestAssuredExtension;
import org.todo.websocket.TODOWebsocketService;

import java.util.List;

@SpringBootTest(classes = {
        /* services */
        TODOApiService.class,
        TODOWebsocketService.class,
        /* assertions */
        CommonAssertions.class,
        TODOAssertions.class
})
@EnableConfigurationProperties(TODOAppConfig.class)
@ExtendWith(RestAssuredExtension.class)
@Epic("todo")
@Tag("todo-app")
public class TODOBaseTest {

    /* services */
    @Autowired
    protected TODOApiService todoApiService;

    /* assertions */
    @Autowired
    protected CommonAssertions commonAssertions;

    @Autowired
    protected TODOAssertions todoAssertions;

    /* configs */
    @Autowired
    protected TODOAppConfig todoAppConfig;

    @AfterEach
    public void deleteTODOs() {
        List<TODO> todos = todoApiService.getTODOs();

        todos.forEach(todo -> todoApiService.deleteTODO(todo.getId()));
    }
}
