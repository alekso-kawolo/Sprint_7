package tests;

import client.OrderClient;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest {

    private final OrderClient orderClient = new OrderClient();

    @Test
    public void shouldReturnOrdersList() {
        Response response = orderClient.getOrdersList();
        response.then()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}