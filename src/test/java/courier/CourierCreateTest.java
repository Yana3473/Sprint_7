package courier;

import base.BaseTest;
import client.CourierClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.restassured.response.ValidatableResponse;
import model.Courier;
import model.CourierCredentials;
import org.junit.jupiter.api.*;
import static org.apache.http.HttpStatus.*;
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

        response.statusCode(SC_CREATED)   // вместо 201
                .body("ok", equalTo(true));

        courierId = courierClient.login(new CourierCredentials("Yanayay", "1234"))
                .extract().path("id");
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    void cannotCreateTwoIdenticalCouriers() {
        Courier courier = new Courier("YanaDuplicate" + System.currentTimeMillis(), "1234", "Yana");
        courierClient.create(courier).statusCode(SC_CREATED);

        // вторая попытка
        courierClient.create(courier)
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Ошибка при создании курьера без логина")
    void cannotCreateCourierWithoutLogin() {
        Courier courier = new Courier(null, "1234", "Yana");
        courierClient.create(courier)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Ошибка при создании курьера без пароля")
    void cannotCreateCourierWithoutPassword() {
        Courier courier = new Courier("YanaBezParolya", null, "Yana");
        courierClient.create(courier)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Ошибка при создании курьера с существующим логином")
    void cannotCreateCourierWithDoubleLogin() {
        String login = "YanaDouble" + System.currentTimeMillis();
        Courier first = new Courier(login, "1234", "Yana");
        courierClient.create(first).statusCode(SC_CREATED);

        Courier second = new Courier(login, "5678", "Anna");
        courierClient.create(second).statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @AfterEach
    void tearDown() {
        if (courierId != 0) {
            courierClient.delete(courierId).statusCode(SC_OK);
        }
    }
}


