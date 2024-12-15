package org.todo.domain.filters;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.SneakyThrows;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.todo.exceptions.ErrorResponseException;

import java.util.List;

public class CustomExceptionFilter implements Filter {

    @Override
    @SneakyThrows
    public Response filter(FilterableRequestSpecification filterableRequestSpecification,
                           FilterableResponseSpecification filterableResponseSpecification,
                           FilterContext filterContext) {

        Response response = filterContext.next(filterableRequestSpecification, filterableResponseSpecification);
        String method = filterableRequestSpecification.getMethod();
        int statusCode = response.getStatusCode();

        if (List.of(HttpMethod.GET.name(), HttpMethod.PUT.name()).contains(method) && statusCode != HttpStatus.OK.value()) {
            throw new ErrorResponseException(statusCode);
        }

        if (method.equals(HttpMethod.POST.name()) && statusCode != HttpStatus.CREATED.value()) {
            throw new ErrorResponseException(statusCode);
        }

        if (method.equals(HttpMethod.DELETE.name()) && statusCode != HttpStatus.NO_CONTENT.value()) {
            throw new ErrorResponseException(statusCode);
        }

        return response;
    }
}
