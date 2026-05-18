package tests;

import client.CourierClient;
import io.restassured.response.Response;
import models.Courier;
import models.CourierCredentials;
import org.junit.After;
import org.junit.Test;
import utils.CourierGenerator;

import static org.hamcrest.Matchers.equalTo;

public class CourierCreateTest {

    private final CourierClient courierClient = new CourierClient();
    private Integer courierId;

    @After
    public void tearDown() {
        if (courierId != null) {
            courierClient.deleteCourier(courierId);
        }
    }

    @Test
    public void shouldCreateCourier() {
        Courier courier = CourierGenerator.getRandomCourier();

        Response createResponse = courierClient.createCourier(courier);
        createResponse.then()
                .statusCode(201)
                .body("ok", equalTo(true));

        Response loginResponse = courierClient.loginCourier(CourierCredentials.from(courier));
        courierId = loginResponse.then().extract().path("id");
    }

    @Test
    public void shouldNotCreateDuplicateCourier() {
        Courier courier = CourierGenerator.getRandomCourier();

        courierClient.createCourier(courier);
        Response loginResponse = courierClient.loginCourier(CourierCredentials.from(courier));
        courierId = loginResponse.then().extract().path("id");

        Response secondCreateResponse = courierClient.createCourier(courier);
        secondCreateResponse.then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    public void shouldReturnErrorWhenLoginIsMissing() {
        Courier courier = new Courier(null, "1234", "Name");

        Response response = courierClient.createCourier(courier);
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void shouldReturnErrorWhenPasswordIsMissing() {
        Courier courier = new Courier("login_for_negative_test", null, "Name");

        Response response = courierClient.createCourier(courier);
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}