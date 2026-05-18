package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Courier;
import models.CourierCredentials;

import static io.restassured.RestAssured.given;
import static utils.Endpoints.*;

public class CourierClient {

    @Step("Создать курьера")
    public Response createCourier(Courier courier) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(COURIER);
    }

    @Step("Авторизовать курьера")
    public Response loginCourier(CourierCredentials credentials) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(credentials)
                .when()
                .post(COURIER_LOGIN);
    }

    @Step("Удалить курьера по id")
    public Response deleteCourier(int courierId) {
        return given()
                .baseUri(BASE_URL)
                .when()
                .delete(COURIER + "/" + courierId);
    }
}
