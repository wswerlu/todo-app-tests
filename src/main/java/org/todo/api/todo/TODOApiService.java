package org.todo.api.todo;

import io.qameta.allure.Step;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.todo.api.todo.models.TODO;
import org.todo.config.TODOAppConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

@Service
@EnableConfigurationProperties(TODOAppConfig.class)
public class TODOApiService {

    private final String baseUri;
    private final String username;
    private final String password;

    public TODOApiService(TODOAppConfig todoAppConfig) {
        this.baseUri = todoAppConfig.getApi().getUri();
        this.username = todoAppConfig.getApi().getCredentials().getUsername();
        this.password = todoAppConfig.getApi().getCredentials().getPassword();
    }

    @Step("[GET /todos] Получение списка TODO")
    public List<TODO> getTODOs(Integer offset, Integer limit) {
        Map<String, Integer> queryParams = new HashMap<>();

        if (offset != null) {
            queryParams.put("offset", offset);
        }

        if (limit != null) {
            queryParams.put("limit", limit);
        }

        return given()
                .baseUri(baseUri)
                .queryParams(queryParams)
                .get("/todos")
                .then()
                .extract().response().as(new TypeRef<>() {});
    }

    public List<TODO> getTODOs() {
        return getTODOs(null, null);
    }

    @Step("[POST /todos] Создание TODO")
    public void createTODO(TODO body) {
        given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .body(body)
                .post("/todos");
    }

    @Step("[PUT /todos/:id] Обновление TODO с id = {id}")
    public void updateTODO(int id, TODO body) {
        given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .body(body)
                .put("/todos/" + id);
    }

    @Step("[DELETE /todos/:id] Удаление TODO с id = {id}")
    public void deleteTODO(int id, String username, String password) {
        RequestSpecification request = given();

        if (username != null || password != null) {
            request.auth().preemptive()
                    .basic(username, password);
        }

        request
                .when()
                .baseUri(baseUri)
                .delete("/todos/" + id);
    }

    public void deleteTODO(int id) {
        deleteTODO(id, username, password);
    }
}
