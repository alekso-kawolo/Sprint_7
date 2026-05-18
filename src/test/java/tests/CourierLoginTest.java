package tests;

import client.CourierClient;
import io.restassured.response.Response;
import models.Courier;
import models.CourierCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.CourierGenerator;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class CourierLoginTest {

    private final CourierClient courierClient = new CourierClient();
    private Courier courier;
    private Integer courierId;

    @Before
    public void setUp() {
        courier = CourierGenerator.getRandomCourier();
        courierClient.createCourier(courier);
        Response loginResponse = courierClient.loginCourier(CourierCredentials.from(courier));
        courierId = loginResponse.then().extract().path("id");
    }

    @After
    public void tearDown() {
        if (courierId != null) {
            courierClient.deleteCourier(courierId);
        }
    }

    @Test
    public void shouldLoginCourier() {
        Response response = courierClient.loginCourier(CourierCredentials.from(courier));
        response.then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    public void shouldReturnErrorWhenLoginMissing() {
        CourierCredentials credentials = new CourierCredentials(null, courier.getPassword());

        Response response = courierClient.loginCourier(credentials);
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void shouldReturnErrorWhenPasswordMissing() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), null);

        Response response = courierClient.loginCourier(credentials);
        response.then()
                .statusCode(504)
                .body(equalTo("Service unavailable"));
    }

    @Test
    public void shouldReturnErrorWhenWrongLogin() {
        CourierCredentials credentials = new CourierCredentials("wrong_login", courier.getPassword());

        Response response = courierClient.loginCourier(credentials);
        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void shouldReturnErrorWhenWrongPassword() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), "wrong_password");

        Response response = courierClient.loginCourier(credentials);
        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void shouldReturnErrorForNonExistingCourier() {
        CourierCredentials credentials = new CourierCredentials("not_exist_user", "12345");

        Response response = courierClient.loginCourier(credentials);
        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}