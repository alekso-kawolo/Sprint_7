package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Order;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static utils.Endpoints.*;

public class OrderClient {

    @Step("Создать заказ")
    public Response createOrder(Order order) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(ORDERS);
    }

    @Step("Получить список заказов")
    public Response getOrdersList() {
        return given()
                .baseUri(BASE_URL)
                .when()
                .get(ORDERS);
    }

    @Step("Отменить заказ")
    public Response cancelOrder(String track) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(Map.of("track", track))
                .when()
                .put(ORDERS_CANCEL);
    }
}
