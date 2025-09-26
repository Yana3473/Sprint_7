package courier;

import base.BaseTest;
import client.CourierClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.restassured.response.ValidatableResponse;
import model.Courier;
import model.CourierCredentials;
import org.junit.jupiter.api.*;

import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CourierCreateTest extends BaseTest {

    private final CourierClient courierClient = new CourierClient();
    private int courierId;

    @Test
    @DisplayName("Создание курьера")
    void courierCanBeCreated() {
        Courier courier = new Courier("Yanayay", "1234", "Yana");
        ValidatableResponse response = courierClient.create(courier);

        response.statusCode(201)
                .body("ok", equalTo(true));

        courierId = courierClient.login(new CourierCredentials("Yanayay", "1234"))
                .extract().path("id");
    }

    @AfterEach
    void tearDown() {
        if (courierId != 0) {
            courierClient.delete(courierId).statusCode(200);
        }
    }
}


