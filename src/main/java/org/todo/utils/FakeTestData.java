package org.todo.utils;

import lombok.experimental.UtilityClass;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.todo.api.todo.models.TODO;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class FakeTestData {

    private final Faker FAKER = new Faker(Locale.ENGLISH);

    public TODO createTODO(Integer id) {
        return Instancio.of(TODO.class)
                .supply(Select.field(TODO::getId), () -> id != null ? id : FAKER.number().positive())
                .supply(Select.field(TODO::getText), () -> FAKER.food().dish())
                .supply(Select.field(TODO::getCompleted), () -> FAKER.bool().bool())
                .create();
    }

    public TODO createTODO() {
        return createTODO(null);
    }

    public List<TODO> createTODOs(int count) {
        return IntStream.range(0, count)
                .mapToObj(id -> createTODO(id + 1))
                .collect(Collectors.toList());
    }
}
