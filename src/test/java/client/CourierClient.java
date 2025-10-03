package client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.Courier;
import model.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient {

    private static final String COURIER_PATH = "/api/v1/courier";

    @Step("Создание курьера")
    public ValidatableResponse create(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    @Step("Логин курьера в системе")
    public ValidatableResponse login(CourierCredentials creds) {
        return given()
                .header("Content-type", "application/json")
                .body(creds)
                .when()
                .post(COURIER_PATH + "/login")
                .then();
    }

    @Step("Удаление курьера")
    public ValidatableResponse delete(int courierId) {
        return given()
                .when()
                .delete(COURIER_PATH + "/" + courierId)
                .then();
    }
}

