package org.todo.extensions;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.todo.websocket.TODOWebsocketService;

public class WebsocketExtension implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        TODOWebsocketService todoWebsocketService = getBean(context);
        todoWebsocketService.connect();
    }

    @Override
    public void afterEach(ExtensionContext context) {
        TODOWebsocketService todoWebsocketService = getBean(context);
        todoWebsocketService.closeConnection();
    }

    private TODOWebsocketService getBean(ExtensionContext context) {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);
        return applicationContext.getBean(TODOWebsocketService.class);
    }
}
