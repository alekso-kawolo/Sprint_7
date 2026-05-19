package tests;

import client.OrderClient;
import io.restassured.response.Response;
import org.junit.Test;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;

import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest {

    private final OrderClient orderClient = new OrderClient();

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка, что запрос списка заказов возвращает код 200, а тело ответа содержит массив orders.")
    public void shouldReturnOrdersList() {
        Response response = orderClient.getOrdersList();
        response.then()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}