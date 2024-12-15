package org.todo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "todo-app")
public class TODOAppConfig {

    private Api api;
    private Websocket websocket;

    @Data
    public static class Api {

        private String uri;
        private Credentials credentials;

        @Data
        public static class Credentials {

            private String username;
            private String password;
        }
    }

    @Data
    public static class Websocket {

        private String url;
    }
}
