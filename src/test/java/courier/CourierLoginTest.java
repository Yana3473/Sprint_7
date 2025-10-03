package courier;

import client.CourierClient;
import base.BaseTest;
import io.restassured.response.ValidatableResponse;
import model.Courier;
import model.CourierCredentials;
import org.junit.jupiter.api.*;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

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
        response.statusCode(SC_OK)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Ошибка при логине с неверным паролем")
    void loginWithWrongPassword() {
        courierClient.login(new CourierCredentials(login, "wrongPass"))
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Ошибка при логине с неверным логином")
    void loginWithWrongLogin() {
        courierClient.login(new CourierCredentials("wrongLogin", "1234"))
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Ошибка при логине без пароля")
    void loginWithoutPassword() {
        courierClient.login(new CourierCredentials(login, ""))
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка при логине без логина")
    void loginWithoutLogin() {
        courierClient.login(new CourierCredentials(null, "1234"))
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка при логине под несуществующим пользователем")
    void loginWithNonExistingUser() {
        courierClient.login(new CourierCredentials("nonExistent", "1234"))
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @AfterEach
    void tearDown() {
        if (courierId != 0) {
            courierClient.delete(courierId).statusCode(SC_OK);
        }
    }
}
