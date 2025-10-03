package order;

import client.OrderClient;
import base.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.apache.http.HttpStatus.*;

import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest extends BaseTest {

    private final OrderClient orderClient = new OrderClient();

    @Test
    @DisplayName("Получение списка заказов")
    void ordersListShouldBeReturned() {
        orderClient.getOrders()
                .statusCode(SC_OK)
                .body("orders", notNullValue());
    }
}

