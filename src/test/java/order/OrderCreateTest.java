package order;

import client.OrderClient;
import base.BaseTest;
import org.junit.jupiter.api.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.apache.http.HttpStatus.*;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.notNullValue;

public class OrderCreateTest extends BaseTest  {

    private final OrderClient orderClient = new OrderClient();

    @ParameterizedTest
    @ValueSource(strings = {"BLACK", "GREY", "BOTH", "NONE"})
    @DisplayName("Создание заказа с разными вариантами цветов")
    void orderCanBeCreatedWithDifferentColors(String colorOption) {
        Order order;
        switch (colorOption) {
            case "BLACK":
                order = new Order("Yana", "Yanovna", "Moscow", "4", "+79999999999",
                        5, "2025-09-25", "test order", Collections.singletonList("BLACK"));
                break;
            case "GREY":
                order = new Order("Yana", "Yanovna", "Moscow", "4", "+79999999999",
                        5, "2025-09-25", "test order", Collections.singletonList("GREY"));
                break;
            case "BOTH":
                order = new Order("Yana", "Yanovna", "Moscow", "4", "+79999999999",
                        5, "2025-09-25", "test order", Arrays.asList("BLACK", "GREY"));
                break;
            default:
                order = new Order("Yana", "Yanovna", "Moscow", "4", "+79999999999",
                        5, "2025-09-25", "test order", null);
                break;
        }

        ValidatableResponse response = orderClient.create(order);
        response.statusCode(SC_CREATED)
                .body("track", notNullValue());
    }
}
