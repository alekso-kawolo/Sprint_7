package tests;

import client.CourierClient;
import io.restassured.response.Response;
import models.Courier;
import models.CourierCredentials;
import org.junit.After;
import org.junit.Test;
import utils.CourierGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;

import static org.hamcrest.Matchers.equalTo;

public class CourierCreateTest {

    private final CourierClient courierClient = new CourierClient();
    private Courier courier;

    @After
    public void tearDown() {
        if (courier != null) {
            Response loginResponse = courierClient.loginCourier(CourierCredentials.from(courier));
            Integer courierId = loginResponse.then().extract().path("id");

            if (courierId != null) {
                courierClient.deleteCourier(courierId);
            }
        }
    }

    @Test
    @DisplayName("Успешное создание курьера")
    @Description("Проверка, что курьера можно создать с валидными данными, и API возвращает код 201 и ok=true.")
    public void shouldCreateCourier() {
        courier = CourierGenerator.getRandomCourier();

        Response createResponse = courierClient.createCourier(courier);
        createResponse.then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Нельзя создать дубликат курьера")
    @Description("Проверка, что повторное создание курьера с тем же логином возвращает код 409 и сообщение об ошибке.")
    public void shouldNotCreateDuplicateCourier() {
        courier = CourierGenerator.getRandomCourier();

        courierClient.createCourier(courier);

        Response secondCreateResponse = courierClient.createCourier(courier);
        secondCreateResponse.then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Ошибка при создании курьера без логина")
    @Description("Проверка, что если не передать логин, API возвращает код 400 и сообщение о недостаточности данных.")
    public void shouldReturnErrorWhenLoginIsMissing() {
        courier = new Courier(null, "1234", "Name");

        Response response = courierClient.createCourier(courier);
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));

        courier = null;
    }

    @Test
    @DisplayName("Ошибка при создании курьера без пароля")
    @Description("Проверка, что если не передать пароль, API возвращает код 400 и сообщение о недостаточности данных.")
    public void shouldReturnErrorWhenPasswordIsMissing() {
        courier = new Courier("login_for_negative_test", null, "Name");

        Response response = courierClient.createCourier(courier);
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));

        courier = null;
    }
}