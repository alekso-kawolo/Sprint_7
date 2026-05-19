package tests;

import client.OrderClient;
import io.restassured.response.Response;
import models.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;

import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    private final OrderClient orderClient = new OrderClient();
    private final List<String> colors;
    private String track;

    public OrderCreateTest(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "Цвет(а) заказа: {0}")
    public static Object[][] getColorData() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK", "GREY")},
                {null}
        };
    }

    @After
    public void tearDown() {
        if (track != null) {
            orderClient.cancelOrder(track);
            track = null;
        }
    }

    @Test
    @DisplayName("Создание заказа с разными вариантами цвета")
    @Description("Проверка, что заказ можно создать с одним цветом, двумя цветами или без указания цвета, а ответ содержит track.")
    public void shouldCreateOrderWithDifferentColors() {
        Order order = new Order(colors);

        Response response = orderClient.createOrder(order);
        track = response.then().extract().path("track").toString();

        response.then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}