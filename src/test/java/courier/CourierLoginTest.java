package courier;

import client.CourierClient;
import base.BaseTest;
import io.restassured.response.ValidatableResponse;
import model.Courier;
import model.CourierCredentials;
import org.junit.jupiter.api.*;

import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest extends BaseTest {

    private final CourierClient courierClient = new CourierClient();
    private int courierId;
    private String login;

    @BeforeEach
    void createCourier() {
        login = "Yanayay" + System.currentTimeMillis(); // уникальный логин для независимости тестов
        Courier courier = new Courier(login, "1234", "Yana");
        courierClient.create(courier);
        courierId = courierClient.login(new CourierCredentials(login, "1234"))
                .extract().path("id");
    }

    @Test
    @DisplayName("Курьер может авторизоваться")
    void courierCanLogin() {
        ValidatableResponse response = courierClient.login(new CourierCredentials(login, "1234"));
        response.statusCode(200)
                .body("id", notNullValue());
    }

    @AfterEach
    void tearDown() {
        if (courierId != 0) {
            courierClient.delete(courierId).statusCode(200);
        }
    }
}
