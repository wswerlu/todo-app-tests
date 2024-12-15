package org.todo.websocket;

import lombok.Getter;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

@Getter
public class WebsocketClient extends WebSocketClient {

    private CountDownLatch countDownLatch = new CountDownLatch(1);;
    private final Queue<String> receivedMessages = new ConcurrentLinkedQueue<>();

    public WebsocketClient(URI serverUri) {
        super(serverUri);
    }

    public void setCountDownLatch(int count) {
        this.countDownLatch = new CountDownLatch(count);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("Opened connection");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Message received: \n");
        System.out.println(message);
        this.receivedMessages.add(message);
        this.countDownLatch.countDown();
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Closed with exit code " + code + " additional info: " + reason + " remote = " + remote);
    }

    @Override
    public void onError(Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
}
