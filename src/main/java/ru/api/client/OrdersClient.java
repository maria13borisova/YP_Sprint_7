package ru.api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.constants.Api;
import ru.constants.ContentType;
import ru.pojo.Order;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrdersClient {

    @Step("Получение списка заказов по Id курьера")
    public void getOrdersByCourierId(int courierId) {
        String fullPath = Api.MAKE_ORDER_ENDPOINT + "?courierId=" + courierId;
        given()
                .get(fullPath)
                .then().statusCode(200)
                .and().body("orders", hasItems());
    }

    @Step("Создать заказ")
    public Response createOrderResponse(Order order){
        Response response =
                given()
                        .header("Content-type", ContentType.CONTENT_TYPE)
                        .and()
                        .body(order)
                        .when()
                        .post(Api.MAKE_ORDER_ENDPOINT);
        return response;
    }

    @Step("Проверить, что заказ был создан")
    public void checkOrderIsCreated(Response response){
        response.then().statusCode(201)
                .and()
                .body("track", any(Integer.class))
                .and()
                .body("track", greaterThan(0));
    }
}