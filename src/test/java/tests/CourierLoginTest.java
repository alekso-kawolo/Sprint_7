package tests;

import client.CourierClient;
import io.restassured.response.Response;
import models.Courier;
import models.CourierCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.CourierGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;

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
    @DisplayName("Успешная авторизация курьера")
    @Description("Проверка, что зарегистрированный курьер может авторизоваться, а ответ содержит id.")
    public void shouldLoginCourier() {
        Response response = courierClient.loginCourier(CourierCredentials.from(courier));
        response.then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Ошибка авторизации без логина")
    @Description("Проверка, что если не передать логин, API возвращает код 400 и сообщение о недостаточности данных для входа.")
    public void shouldReturnErrorWhenLoginMissing() {
        CourierCredentials credentials = new CourierCredentials(null, courier.getPassword());

        Response response = courierClient.loginCourier(credentials);
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка авторизации без пароля")
    @Description("Проверка, что если не передать пароль, API возвращает код 400 и сообщение о недостаточности данных для входа.")
    public void shouldReturnErrorWhenPasswordMissing() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), null);

        Response response = courierClient.loginCourier(credentials);
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка авторизации с неверным логином")
    @Description("Проверка, что при неверном логине API возвращает код 404 и сообщение о ненайденной учетной записи.")
    public void shouldReturnErrorWhenWrongLogin() {
        CourierCredentials credentials = new CourierCredentials("wrong_login", courier.getPassword());

        Response response = courierClient.loginCourier(credentials);
        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Ошибка авторизации с неверным паролем")
    @Description("Проверка, что при неверном пароле API возвращает код 404 и сообщение о ненайденной учетной записи.")
    public void shouldReturnErrorWhenWrongPassword() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), "wrong_password");

        Response response = courierClient.loginCourier(credentials);
        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Ошибка авторизации несуществующего курьера")
    @Description("Проверка, что несуществующий пользователь не может авторизоваться, а API возвращает код 404 и сообщение о ненайденной учетной записи.")
    public void shouldReturnErrorForNonExistingCourier() {
        CourierCredentials credentials = new CourierCredentials("not_exist_user", "12345");

        Response response = courierClient.loginCourier(credentials);
        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}