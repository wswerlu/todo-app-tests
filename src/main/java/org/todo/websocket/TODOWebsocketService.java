package org.todo.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.todo.config.TODOAppConfig;
import org.todo.websocket.models.TODOEvent;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

@Service
@EnableConfigurationProperties(TODOAppConfig.class)
public class TODOWebsocketService {

    private final WebsocketClient client;

    public TODOWebsocketService(TODOAppConfig todoAppConfig) {
        this.client = new WebsocketClient(URI.create(todoAppConfig.getWebsocket().getUrl()));
    }

    @SneakyThrows
    @Step("Ждем сообщения")
    public List<TODOEvent> waitMessages(int timeout, int count) {
        List<TODOEvent> todoEvents = new ArrayList<>();

        if (client.getCountDownLatch().getCount() == 0) {
            client.setCountDownLatch(count - 1);
        }

        boolean isMessageReceived = client.getCountDownLatch().await(timeout, TimeUnit.SECONDS);
        Queue<String> messages = client.getReceivedMessages();

        if (isMessageReceived || !messages.isEmpty()) {
            messages.forEach(message -> {
                Allure.addAttachment("Полученное сообщение", "application/json", message);
                try {
                    todoEvents.add(new ObjectMapper().readValue(message, TODOEvent.class));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            Allure.step("Не получили сообщение за " + timeout + " секунд");
        }

        return todoEvents;
    }

    public List<TODOEvent> waitMessages(int count) {
        return waitMessages(3, count);
    }

    public List<TODOEvent> waitMessages() {
        return waitMessages(3, 1);
    }

    @SneakyThrows
    @Step("Устанавливаем сооединение")
    public void connect() {
        try {
            client.connectBlocking();
        } catch (InterruptedException e) {
            throw new InterruptedException("Не удалось установить соединения с хостом");
        }
    }

    @SneakyThrows
    @Step("Закрываем сооединение")
    public void closeConnection() {
        client.closeBlocking();
    }
}
