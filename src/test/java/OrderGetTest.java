import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.Order;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import clients.AuthClient;
import clients.OrderClient;
import generators.OrderGenerator;
import generators.UserGenerator;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

public class OrderGetTest {
    private OrderClient orderClient;
    private AuthClient authClient;
    private Order order;
    private List<Order> orderList;
    private User user;
    private String createdUserAuth;
    ValidatableResponse getOrderResponse;
    ValidatableResponse createUserResponse;

    @Before
    public void setUp() {
        authClient = new AuthClient();
        orderClient = new OrderClient();
        user = UserGenerator.getRandomUser();
        order = OrderGenerator.getOrderWithFirstAvailableIngredient(orderClient);
        createUserResponse = authClient.createUser(user);
        orderList = new ArrayList<Order>();
    }

    @After
    public void tearDown() {
        if (createUserResponse != null) {
            createdUserAuth = createUserResponse.extract().path("accessToken");
            if (createdUserAuth != null) {
                authClient.deleteUser(createdUserAuth);
            }
        }
    }

    @Test
    @DisplayName("Получение заказа с авторизацией")
    @Description("Проверяется возможность получения заказа с авторизацией")
    public void getOrdersOfAuthorizedUserSuccess() {
        createdUserAuth = createUserResponse.extract().path("accessToken");
        orderClient.createOrder(order,createdUserAuth);
        getOrderResponse = orderClient.getOrder(createdUserAuth);

        int statusCode = getOrderResponse.extract().statusCode();
        boolean responseSuccess = getOrderResponse.extract().path("success");
        orderList = getOrderResponse.extract().path("orders");

        assertThat(orderList.size(), greaterThan(0));
        assertEquals(200, statusCode);
        assertTrue(responseSuccess);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверяется невозможность получения заказа без авторизации")
    public void getOrdersOfUnauthorizedUserFail() {
        createdUserAuth = createUserResponse.extract().path("accessToken");
        orderClient.createOrder(order,createdUserAuth);
        getOrderResponse = orderClient.getOrder();

        int statusCode = getOrderResponse.extract().statusCode();
        boolean responseSuccess = getOrderResponse.extract().path("success");

        assertEquals(401, statusCode);
        assertFalse(responseSuccess);
    }
}